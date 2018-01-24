package com.phduo.hiit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.NumberPicker;

/**
 * Created by phduo on 12/10/2017.
 */

public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context) {
        super(context);
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        processAttributeSet(attrs);
        this.setFormatter((value) -> {
            return (value < 10) ? "0" + value : "" + value;
        });
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        processAttributeSet(attrs);
        this.setFormatter((value) -> {
            return (value < 10) ? "0" + value : "" + value;
        });
    }

    private void processAttributeSet(AttributeSet attrs) {
        this.setMinValue(attrs.getAttributeIntValue(null, "min", 0));
        this.setMaxValue(attrs.getAttributeIntValue(null, "max", 59));
    }
}
