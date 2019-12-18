package com.imfondof.world.base.adapter;

import android.view.View;

/**
 * zhaishuo on 2019/12/18 10:00
 * description:
 */
public interface OnClickCallback {
    abstract void onClick(View v, int position, BaseViewHolder viewHolder);
    boolean onLongClick(View v, int position, BaseViewHolder viewHolder);
}
