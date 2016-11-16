package com.alexwglenn.whatcard.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.alexwglenn.whatcard.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by aglenn on 2/19/15.
 */
public class AddCategoryRateViewHolder {

    @InjectView(R.id.categoryName)
    public EditText categoryName;
    @InjectView(R.id.categoryRateTitle)
    public TextView categoryRateTitle;
    @InjectView(R.id.categoryRateSeek)
    public SeekBar categoryRateSeek;
    @InjectView(R.id.startDate)
    public EditText startDate;
    @InjectView(R.id.endDate)
    public EditText endDate;
    @InjectView(R.id.isStore)
    public AppCompatCheckBox isStore;

    private float categoryRate;

    public AddCategoryRateViewHolder(View v) {
        ButterKnife.inject(this, v);

        categoryRate = (float)categoryRateSeek.getProgress() / 4;

        categoryRateTitle.setText("Category Reward Rate: " + categoryRate + "%");

        categoryRateSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                categoryRate = (float)i / 4;
                categoryRateTitle.setText("Category Reward Rate " + categoryRate + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setCategoryRate(float categoryRate) {
        this.categoryRate = categoryRate;
        categoryRateSeek.setProgress(Math.round(categoryRate * 4));
        categoryRateTitle.setText("Category Reward Rate: " + categoryRate + "%");
    }

    public float getCategoryRate() {
        return categoryRate;
    }
}
