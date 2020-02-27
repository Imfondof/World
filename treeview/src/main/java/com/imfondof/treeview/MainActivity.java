package com.imfondof.treeview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.imfondof.treeview.adapter.SimpleTreeListViewAdapter;
import com.imfondof.treeview.bean.FileBean;
import com.imfondof.treeview.bean.OrgBean;
import com.imfondof.treeview.utils.Node;
import com.imfondof.treeview.utils.adapter.TreeViewListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 原理：listview'item +paddingleft(level) +expand include
     * 将系统中的 bean转化为 node（反射+注解）【注解可以被命名规范所替代】
     * 1、List<T>--->  List<Node>
     * 2、设置节点间的关联关系
     * 3、排序
     * 4、过滤出需要显示的数据
     * <p>
     * 若node里有很多属性，则node里设置一个Object属性
     */
    private ListView mTree;
    private SimpleTreeListViewAdapter<FileBean> mAdapter;
    private List<FileBean> mDatas;

    private List<OrgBean> mDatas2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTree = findViewById(R.id.id_listview);

        initDatas();
        try {
            mAdapter = new SimpleTreeListViewAdapter<>(mTree, this, mDatas, 0);
            mTree.setAdapter(mAdapter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        initEvent();
    }

    private void initEvent() {
        mAdapter.setOnTreeNodeClickListener(new TreeViewListAdapter.OnTreeNodeClickListener() {
            @Override
            public void onClick(Node node, int position) {
                if (node.isLeaf()) {
                    Toast.makeText(MainActivity.this, node.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mTree.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText et = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this, 0).setTitle("Add Node")
                        .setView(et)
                        .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (TextUtils.isEmpty(et.getText().toString())) return;
                                mAdapter.addExtraNode(position, et.getText().toString());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
                return true;
            }
        });
    }

    private void initDatas() {
        mDatas = new ArrayList<FileBean>();
        FileBean bean = new FileBean(1, 0, "根目录1");
        mDatas.add(bean);
        bean = new FileBean(2, 0, "根目录2");
        mDatas.add(bean);
        bean = new FileBean(3, 0, "根目录3");
        mDatas.add(bean);
        bean = new FileBean(4, 1, "根目录1-1");
        mDatas.add(bean);
        bean = new FileBean(5, 1, "根目录1-2");
        mDatas.add(bean);
        bean = new FileBean(6, 5, "根目录1-2-1");
        mDatas.add(bean);
        bean = new FileBean(7, 3, "根目录3-1");
        mDatas.add(bean);
        bean = new FileBean(8, 3, "根目录3-2");
        mDatas.add(bean);


        /**
         * 这里添加第二种类型的数据，只需要把adapter里对应的数据类型改一下
         */
//        mDatas2 = new ArrayList<OrgBean>();
//        OrgBean bean2 = new OrgBean(1, 0, "根目录1");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(2, 0, "根目录2");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(3, 0, "根目录3");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(4, 1, "根目录1-1");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(5, 1, "根目录1-2");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(6, 5, "根目录1-2-1");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(7, 3, "根目录3-1");
//        mDatas2.add(bean2);
//        bean2 = new OrgBean(8, 3, "根目录3-2");
//        mDatas2.add(bean2);
    }
}
