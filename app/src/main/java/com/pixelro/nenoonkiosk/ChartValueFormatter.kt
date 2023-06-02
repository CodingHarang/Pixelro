package com.pixelro.nenoonkiosk

import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter

class ChartValueFormatter : ValueFormatter() {

    @Override
    public override fun getFormattedValue(value: Float): String {
        return if(value > 0) {
            String.format("%.0fcm", value)
        } else {
            "";
        }
    }
}