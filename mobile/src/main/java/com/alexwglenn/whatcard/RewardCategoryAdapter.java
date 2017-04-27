package com.alexwglenn.whatcard;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alexwglenn.whatcard.model.RewardCategory;
import com.alexwglenn.whatcard.viewholders.CategoryRewardViewHolder;

import java.util.ArrayList;

/**
 * Created by aglenn on 2/20/15.
 */
public class RewardCategoryAdapter extends RecyclerView.Adapter {

    private ArrayList<RewardCategory> rewardCategories;
    private Context mContext;
    private LayoutInflater mInflater;

    public RewardCategoryAdapter(ArrayList<RewardCategory> rewardCategories, Context mContext) {
        this.rewardCategories = rewardCategories;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }
//
//    @Override
//    public int getCount() {
//        return rewardCategories.size();
//    }
//
//    @Override
    public RewardCategory getItem(int position) {
        return rewardCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        CategoryRewardViewHolder viewHolder;
//
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.reward_category_grid_item, null);
//            viewHolder = new CategoryRewardViewHolder(convertView);
//            convertView.setTag(viewHolder);
//
//        } else {
//            viewHolder = (CategoryRewardViewHolder) convertView.getTag();
//        }
//
//        RewardCategory rCat = getItem(position);
//
//        viewHolder.setUp(rCat.bestCard, rCat.categoryName, rCat.bestRate);
//
//        return convertView;
//    }

    public void setRewardCategories(ArrayList<RewardCategory> rewardCategories) {
        this.rewardCategories = rewardCategories;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CategoryRewardViewHolder viewHolder;
        View v = mInflater.inflate(R.layout.reward_category_grid_item, parent, false);
//        v.setMinimumHeight(v.getMeasuredHeight());
//        v.setMinimumWidth(v.getMeasuredWidth());
        viewHolder = new CategoryRewardViewHolder(v);
        v.setTag(viewHolder);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryRewardViewHolder viewHolder = (CategoryRewardViewHolder)holder;
        RewardCategory rCat = getItem(position);
        viewHolder.setUp(rCat.bestCard, rCat.categoryName, rCat.bestRate);
    }

    @Override
    public int getItemCount() {
        return rewardCategories.size();
    }


}
