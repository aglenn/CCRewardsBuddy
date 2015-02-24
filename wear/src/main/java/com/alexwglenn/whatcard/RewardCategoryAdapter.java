package com.alexwglenn.whatcard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.wearable.view.GridPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexwglenn.whatcard.model.RewardCategory;
import com.alexwglenn.whatcard.viewholders.CategoryRewardViewHolder;

import java.util.ArrayList;

/**
 * Created by aglenn on 2/20/15.
 */
public class RewardCategoryAdapter extends GridPagerAdapter {

    private ArrayList<RewardCategory> rewardCategories;
    private Context mContext;
    private LayoutInflater mInflater;

    public RewardCategoryAdapter(ArrayList<RewardCategory> rewardCategories, Context mContext) {
        this.rewardCategories = rewardCategories;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        Log.d("Adapter", "Returning col count for row: " + i + " ("+ rewardCategories.size() + ")");
        return rewardCategories.size();
    }

    @Override
    protected Object instantiateItem(ViewGroup viewGroup, int row, int col) {
        CategoryRewardViewHolder viewHolder;

        Log.d("Adapter", "Inflating view for (" + row + ", " + col + ")");

        View v = mInflater.inflate(R.layout.category_reward, null);
        viewHolder = new CategoryRewardViewHolder(v);
        v.setTag(viewHolder);

        RewardCategory rCat = getItem(col);

        viewHolder.categoryName.setText(rCat.categoryName);
        viewHolder.bestCardName.setText(rCat.bestCard.name);
        viewHolder.bestCardRate.setText(rCat.bestRate + "%");
        viewHolder.cardBG.setBackground(getBGDrawable(rCat.bestCard.color));
        v.setTag(col);

        viewGroup.addView(v);

        return v;
    }

    @Override
    protected void destroyItem(ViewGroup viewGroup, int i, int i2, Object view) {
        viewGroup.removeView((View)view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public RewardCategory getItem(int position) {
        return rewardCategories.get(position);
    }

    public static GradientDrawable getBGDrawable(int color) {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(color);
        gd.setCornerRadius(10);
        float[] colorHSV = new float[3];
        Color.colorToHSV(color, colorHSV);
        colorHSV[2] = (colorHSV[2] * 7) /10;
        gd.setStroke(2, Color.HSVToColor(colorHSV));
        return gd;
    }

    public void setRewardCategories(ArrayList<RewardCategory> rewardCategories) {
        this.rewardCategories = rewardCategories;
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
