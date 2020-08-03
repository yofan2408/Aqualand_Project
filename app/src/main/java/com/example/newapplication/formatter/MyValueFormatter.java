package com.example.newapplication.formatter;

import com.example.newapplication.SuhuAir;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class MyValueFormatter extends ValueFormatter {

    @Override
    public String getPointLabel(Entry entry) {
        String result = entry.getY() + SuhuAir.degreePattern;
        return result;
    }
}
