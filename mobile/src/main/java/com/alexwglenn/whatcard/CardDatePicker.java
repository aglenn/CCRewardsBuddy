package com.alexwglenn.whatcard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by aglenn on 9/26/16.
 */

public class CardDatePicker implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private Context context;
    private EditText targetText;
    private Calendar calendar;

    public CardDatePicker(Context context, EditText targetText) {
        this.targetText = targetText;
        this.calendar = Calendar.getInstance();
        this.context = context;

        String text = targetText.getText().toString();

        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

        try {
            Date date = format.parse(text);

            if (date != null) {
                calendar.setTime(date);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Log.d("DatePicker", "Should show");
        new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.US);

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        targetText.setText(format.format(calendar.getTime()));
    }
}
