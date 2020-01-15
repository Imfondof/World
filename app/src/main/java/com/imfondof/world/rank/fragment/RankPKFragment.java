package com.imfondof.world.rank.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.imfondof.world.R;
import com.imfondof.world.base.BaseFragment;
import com.imfondof.world.rank.bean.Rank;
import com.imfondof.world.rank.utils.SQLiteDBUtil;

/**
 * Imfondof on 2019/12/18 10:08
 * description:
 */
public class RankPKFragment extends BaseFragment implements OnClickListener {
    private View rootview;
    private Button btn_left, btn_right, btn_rank_jump;
    private TextView text_left, text_right, id_left, id_right;
    int random_left, random_right;
    List<Rank> ranks;

    private SharedPreferences pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_rank_pk, container, false);
        return rootview;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        btn_left = rootview.findViewById(R.id.btn_rank_left);
        btn_right = rootview.findViewById(R.id.btn_rank_right);
        btn_rank_jump = rootview.findViewById(R.id.btn_rank_jump);
        text_left = rootview.findViewById(R.id.tv_rank_left);
        text_right = rootview.findViewById(R.id.tv_rank_right);

        id_left = rootview.findViewById(R.id.tv_rank_left_value);
        id_right = rootview.findViewById(R.id.tv_rank_right_value);

        if (isShow()) {
            updateAll();
        }
    }

    @Override
    protected void initEvents() {
        super.initEvents();

        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_rank_jump.setOnClickListener(this);
    }

    private void updateAll() {
        ranks = new ArrayList<>();
        String currentType = readCurrentType();
        SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
        SQLiteDatabase sd = db.getReadableDatabase();
        String sql = "select * from rankTable order by score desc";// 查询的结果按照分数排序
        Cursor cursor = sd.rawQuery(sql, null);
        int i = 1;
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String type = cursor.getString(1);
            String name = cursor.getString(2);
            float score = cursor.getFloat(3);
            int vCount = cursor.getInt(4);
            int dCount = cursor.getInt(5);
            int allCount = cursor.getInt(6);

            if (!"".equals(name) && type.equals(currentType)) {
                Rank rank = new Rank(id, type, name, score, vCount, dCount, allCount);
                i++;
                ranks.add(rank);
            }
        }
        cursor.close();
        db.close();
        i--;

        random_left = (int) (Math.random() * i);
        random_right = random_left;
        while (random_right == random_left) {
            random_right = (int) (Math.random() * i);
        }

        text_left.setText(getname(ranks, random_left));
        text_right.setText(getname(ranks, random_right));

        id_left.setText(getid(ranks, random_left) + "");
        id_right.setText(getid(ranks, random_right) + "");
    }

    private void jisuan(int i) {// 左边按钮 就是0 右边按钮就是1
        int id_left = getid(ranks, random_left);
        int id_right = getid(ranks, random_right);
        float score_left = getscore(ranks, random_left);
        float score_right = getscore(ranks, random_right);

        if (i == 0) {
            score_left = (float) (score_left + K(score_left) * (S(1) - E(score_left, score_right)));
            score_right = (float) (score_right + K(score_right) * (S(0) - E(score_right, score_left)));
            SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
            SQLiteDatabase sd = db.getWritableDatabase();

            // 胜利+1
            String sql = "update ranktable set score= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{score_left + 1, id_left});
            sql = "update ranktable set vcount= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{getvcount(id_left) + 1, id_left});

            // 失败+1
            sql = "update ranktable set score= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{score_right - 1, id_right});
            sql = "update ranktable set dcount= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{getdcount(id_right) + 1, id_right});
            db.close();
        }

        if (i == 1) {
            score_left = (float) (score_left + K(score_left) * (S(0) - E(score_left, score_right)));
            score_right = (float) (score_right + K(score_right) * (S(1) - E(score_right, score_left)));
            SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
            SQLiteDatabase sd = db.getWritableDatabase();

            // 失败+1
            String sql = "update ranktable set score= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{score_left, id_left});
            sql = "update ranktable set dcount= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{getdcount(id_left) + 1, id_left});

            // 胜利+1
            sql = "update ranktable set score= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{score_right, id_right});
            sql = "update ranktable set vcount= ?" + " where id= ?";
            sd.execSQL(sql, new Object[]{getvcount(id_right) + 1, id_right});
            db.close();
        }
        setAllcount(id_left, id_right);
    }

    private String getname(List<Rank> ranks, int i) {
        return ranks.get(i).getName();
    }

    private int getid(List<Rank> ranks, int i) {
        return ranks.get(i).getId();
    }

    private int getvcount(int id) {
        int getv = 0;
        SQLiteDBUtil db = new SQLiteDBUtil(getActivity()                .getApplicationContext());
        SQLiteDatabase sd = db.getReadableDatabase();
        String sql = "select * from rankTable where id=" + id;// 查询的结果按照分数排序
        Cursor cursor = sd.rawQuery(sql, null);
        int i = 1;
        while (cursor.moveToNext()) {
            getv = cursor.getInt(4);
        }
        cursor.close();
        db.close();
        return getv;
    }

    private int getdcount(int id) {
        int getd = 0;
        SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
        SQLiteDatabase sd = db.getReadableDatabase();
        String sql = "select * from rankTable where id=" + id;// 查询的结果按照分数排序
        Cursor cursor = sd.rawQuery(sql, null);
        int i = 1;
        while (cursor.moveToNext()) {
            getd = cursor.getInt(5);
        }
        cursor.close();
        db.close();
        return getd;
    }

    private int getAllcount(int id) {// 获取判断的总次数
        int getd = 0;
        SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
        SQLiteDatabase sd = db.getReadableDatabase();
        String sql = "select * from rankTable where id=" + id;// 查询的结果按照分数排序
        Cursor cursor = sd.rawQuery(sql, null);
        int i = 1;
        while (cursor.moveToNext()) {
            getd = cursor.getInt(6);
        }
        cursor.close();
        db.close();
        return getd;
    }

    private void setAllcount(int idleft, int idriget) {
        int leftCount = getAllcount(idleft);
        int rightCount = getAllcount(idriget);
        SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
        SQLiteDatabase sd = db.getWritableDatabase();
        String sql = "update ranktable set allcount= ?" + " where id= ?";
        sd.execSQL(sql, new Object[]{leftCount + 1, idleft});
        sd.execSQL(sql, new Object[]{rightCount + 1, idriget});
        db.close();
    }

    private float getscore(List<Rank> ranks, int i) {
        return ranks.get(i).getScore();
    }

    public boolean isShow() {
        SQLiteDBUtil db = new SQLiteDBUtil(getActivity().getApplicationContext());
        SQLiteDatabase sd = db.getReadableDatabase();
        String sql = "select * from rankTable order by score desc";// 查询的结果按照分数排序
        Cursor cursor = sd.rawQuery(sql, null);
        int i = 1;
        while (cursor.moveToNext()) {
            String type = cursor.getString(1);
            String name = cursor.getString(2);

            if (!"".equals(name) && type.equals(readCurrentType())) {
                i++;
            }
        }
        cursor.close();
        db.close();
        if (i > 2) {
            return true;
        }
        return false;
    }

    private String readCurrentType() {
        pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        String type = pref.getString("type", "type");
        return type;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rank_left:
                jisuan(0);// 左边按钮 就是0
                updateAll();
                break;
            case R.id.btn_rank_right:
                jisuan(1);// 右边按钮就是1
                updateAll();
                break;
            case R.id.btn_rank_jump:
                updateAll();
                break;
            default:
                break;
        }
    }

    //*************************** 公式 ***************************//

    // 返回胜利结果，胜利为1，失败为0
    private static double S(double x) {
        // 如果参数为1，那么自己赢了；如果参数为0，那么自己输了
        if (x == 1)
            return 1;
        return 0;
    }

    // 计算期望值
    private static double E(double Ra, double Rb) {
        double fenmu = 1 + Math.pow(10, (Rb - Ra) / 400.0);
        return Math.pow(fenmu, -1);
    }

    // 判断实力值
    private static double K(double d) {
        if (d >= 2400)
            return (double) 16.0;
        if (d >= 2100 && d <= 2399)
            return 24.0;
        if (d >= 0 && d <= 2100)
            return 32;
        return 32;
    }
}
