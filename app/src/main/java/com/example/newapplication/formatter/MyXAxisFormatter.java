package com.example.newapplication.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyXAxisFormatter extends ValueFormatter {

    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());

        return sdf.format(new Date());
    }
}
