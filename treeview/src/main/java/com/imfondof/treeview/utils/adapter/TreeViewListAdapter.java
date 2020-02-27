package com.imfondof.treeview.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.imfondof.treeview.utils.Node;
import com.imfondof.treeview.utils.TreeHlper;

import java.util.List;

public abstract class TreeViewListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<Node> mAllNodes;
    protected List<Node> mVisibleNodes;

    protected LayoutInflater mInflater;
    protected ListView mTree;

    protected OnTreeNodeClickListener mListener;

    /**
     * 设置 node的点击事件
     *
     * @param mListener
     */
    public void setOnTreeNodeClickListener(OnTreeNodeClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnTreeNodeClickListener {
        void onClick(Node node, int position);
    }

    public TreeViewListAdapter(ListView tree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        mContext = context;
        mTree = tree;
        mInflater = LayoutInflater.from(mContext);
        mAllNodes = TreeHlper.getSortedNodes(datas, defaultExpandLevel);
        mVisibleNodes = TreeHlper.filterVisibleNodes(mAllNodes);

        mTree.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                expandOrCollapse(position);
                if (mListener != null) {
                    mListener.onClick(mVisibleNodes.get(position), position);
                }
            }
        });

    }

    /**
     * 点击 收缩或者展开
     *
     * @param position
     */
    private void expandOrCollapse(int position) {
        Node n = mVisibleNodes.get(position);
        if (n != null) {
            if (n.isLeaf()) return;
            n.setExpand(!n.isExpand());
            mVisibleNodes = TreeHlper.filterVisibleNodes(mAllNodes);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mVisibleNodes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVisibleNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;//或者返回node的id
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Node node = mVisibleNodes.get(position);
        convertView = getConvertView(node, position, convertView, parent);
        //设置内边距
        convertView.setPadding(node.getLevel() * 30, 3, 3, 3);
        return convertView;
    }

    public abstract View getConvertView(Node node, int position, View convertView, ViewGroup parent);
}
