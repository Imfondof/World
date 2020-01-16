package com.imfondof.world.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import static android.widget.LinearLayout.VERTICAL;

/**
 * Imfondof on 2020/1/16 11:05
 * <p>
 * 支持recyclerview的GridLayoutManager的分割线
 * （https://github.com/Jude95/EasyRecyclerView）
 */
public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private int space;
    private int headerCount = -1;
    private int footerCount = Integer.MAX_VALUE;
    private boolean mPaddingEdgeSide = true;
    private boolean mPaddingStart = true;
    private boolean mPaddingEnd = true;
    private boolean mPaddingHeaderFooter = false;
    private int mBorderWidth;
    private boolean mIsPaddingEdgeSide = true; //是否设置了左右padding,默认是设置的,仅在构造中可以置为false


    /**
     * recyclerview分割线，边线和中间线宽度相同时可用
     *
     * @param space 分割线的宽度
     */
    public SpaceDecoration(int space) {
        this.space = space;
    }

    /**
     * recyclerview分割线，边线和中间线宽度不同时可用
     *
     * @param space       分割线的宽度
     * @param borderWidth 左右的padding（只对左右有效）
     */
    public SpaceDecoration(int space, int borderWidth) {
        this.space = space;
        this.mBorderWidth = borderWidth;
        //如果要设置左右padding则该属性应给false,代表不用系统赋值，而是手动用mBorderWidth的值
        mPaddingEdgeSide = false;
        mIsPaddingEdgeSide = false;
    }

    /**
     * 设置左右padding
     *
     * @param mPaddingEdgeSide 注意：调用过SpaceDecoration(int space,int borderWidth)构造后，该方法无效
     */
    public void setPaddingEdgeSide(boolean mPaddingEdgeSide) {
        if (!mIsPaddingEdgeSide) {
            return;
        }
        this.mPaddingEdgeSide = mPaddingEdgeSide;
    }

    public void setPaddingStart(boolean mPaddingStart) {
        this.mPaddingStart = mPaddingStart;
    }

    public void setPaddingEnd(boolean mPaddingEnd) {
        this.mPaddingEnd = mPaddingEnd;
    }

    public void setPaddingHeaderFooter(boolean mPaddingHeaderFooter) {
        this.mPaddingHeaderFooter = mPaddingHeaderFooter;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int spanCount = 0;
        int orientation = 0;
        int spanIndex = 0;
        int headerCount = 0, footerCount = 0;
        if (parent.getAdapter() instanceof BaseQuickAdapter) {
            headerCount = ((BaseQuickAdapter) parent.getAdapter()).getHeaderLayoutCount();
            footerCount = ((BaseQuickAdapter) parent.getAdapter()).getFooterLayoutCount();
        }

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            spanCount = 1;
            spanIndex = 0;
        }

        /**
         * 普通Item的尺寸
         */
        if ((position >= headerCount && position < parent.getAdapter().getItemCount() - footerCount)) {

            if (orientation == VERTICAL) {
                /**
                 * 期望的每个item尺寸
                 * mPaddingEdgeSide true 左右两边有padding的情况 默认的padding就是设置的分割线的宽度，即space
                 * 这种情况：期望的每个item尺寸 = （屏幕宽度 - （列数+1）* space - 0）/列数
                 * mPaddingEdgeSide false 左右两边没有padding的情况 这时候可以设置左右的padding，如果不设置就是0，设置后就是设置的值
                 * 这种情况：期望的每个item尺寸 = （屏幕宽度 - （列数-1）* space - mBorderWidth * 2）/列数
                 * 设置方法可以通过构造传入：SpaceDecoration(int space,int borderWidth)
                 */
                float expectedWidth = (float) (parent.getWidth() - space * (spanCount + (mPaddingEdgeSide ? 1 : -1)) - (mPaddingEdgeSide ? 0 : mBorderWidth * 2)) / spanCount;
                float originWidth = (float) parent.getWidth() / spanCount;
                float expectedX = (mPaddingEdgeSide ? space : mBorderWidth) + (expectedWidth + space) * spanIndex;
                float originX = originWidth * spanIndex;
                outRect.left = (int) (expectedX - originX);
                outRect.right = (int) (originWidth - outRect.left - expectedWidth);
                if (position - headerCount < spanCount && mPaddingStart) {
                    outRect.top = space;
                }
                if (mPaddingEnd)
                    outRect.bottom = space;
                return;
            } else {
                float expectedHeight = (float) (parent.getHeight() - space * (spanCount + (mPaddingEdgeSide ? 1 : -1)) - (mPaddingEdgeSide ? 0 : mBorderWidth * 2)) / spanCount;
                float originHeight = (float) parent.getHeight() / spanCount;
                float expectedY = (mPaddingEdgeSide ? space : mBorderWidth) + (expectedHeight + space) * spanIndex;
                float originY = originHeight * spanIndex;
                outRect.bottom = (int) (expectedY - originY);
                outRect.top = (int) (originHeight - outRect.bottom - expectedHeight);
                if (position - headerCount < spanCount && mPaddingStart) {
                    outRect.left = space;
                }
                if (mPaddingEnd)
                    outRect.right = space;
                return;
            }
        } else if (mPaddingHeaderFooter) {
            if (orientation == VERTICAL) {
                outRect.right = outRect.left = mPaddingEdgeSide ? space : mBorderWidth;
                outRect.top = mPaddingStart ? space : 0;
            } else {
                outRect.top = outRect.bottom = mPaddingEdgeSide ? space : mBorderWidth;
                outRect.left = mPaddingStart ? space : 0;
            }
        }
    }
}
