package com.imfondof.treeview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.imfondof.treeview.R;
import com.imfondof.treeview.utils.Node;
import com.imfondof.treeview.utils.TreeHlper;
import com.imfondof.treeview.utils.adapter.TreeViewListAdapter;

import java.util.List;

public class SimpleTreeListViewAdapter<T> extends TreeViewListAdapter<T> {
    public SimpleTreeListViewAdapter(ListView tree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        super(tree, context, datas, defaultExpandLevel);
    }

    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIcon = convertView.findViewById(R.id.id_item_icon);
            viewHolder.mText = convertView.findViewById(R.id.id_item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (node.getIcon() == -1) {
            viewHolder.mIcon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mIcon.setVisibility(View.VISIBLE);
            viewHolder.mIcon.setImageResource(node.getIcon());
        }
        viewHolder.mText.setText(node.getName());
        return convertView;
    }

    public void addExtraNode(int position, String string) {
        Node node = mVisibleNodes.get(position);
        int indexOf = mAllNodes.indexOf(node);
        Node extraNode = new Node(-1, node.getId(), string);
        extraNode.setParent(node);
        node.getChildren().add(extraNode);
        mAllNodes.add(indexOf + 1, extraNode);
        mVisibleNodes = TreeHlper.filterVisibleNodes(mAllNodes);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        ImageView mIcon;
        TextView mText;
    }

}
