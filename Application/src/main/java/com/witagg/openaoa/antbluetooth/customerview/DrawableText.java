
//
//  OpenAoA Project
//
//  Copyright  2022 WiTagg, Inc
//
//  SPDX-License-Identifier: AGPL-3.0-only
//
package com.witagg.openaoa.antbluetooth.customerview;


import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.graphics.Canvas;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
public class DrawableText extends AppCompatTextView{

    private CircularProgressDrawable mProgressDrawable;
    private Rect bounds;
    public DrawableText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DrawableText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawableText(Context context) {
        super(context);
        init();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        final int offsetX = (int) calcOffset();
        mProgressDrawable.setBounds(offsetX, bounds.top, bounds.right + offsetX, bounds.bottom);
        //Calculates the distance the canvas is translated to the left
        final int tranX = (bounds.width() + getCompoundDrawablePadding()) / 2;
        canvas.translate(-tranX, 0);

        super.onDraw(canvas);
    }

    private void init(){
        mProgressDrawable = new CircularProgressDrawable(getContext());
        mProgressDrawable.setColorSchemeColors(getTextColors().getDefaultColor());
        mProgressDrawable.setBounds(0, 0, 80, 80);
        //save Bounds first
        bounds = mProgressDrawable.copyBounds();
        setCompoundDrawables(mProgressDrawable, null, null, null);
        mProgressDrawable.setStrokeWidth(10);
    }
    //Calculates need offset
    private float calcOffset() {
        //getCompoundPaddingStart()  = paddingStart + drawableWidth + drawablePadding
        return (getWidth() - (getCompoundPaddingStart() + 20  + getTextWidth())) / 2;

    }

    //Calculate the length of text
    private float getTextWidth() {
        return  getPaint().measureText(getText().toString());
    }

    public void start(){
        mProgressDrawable.start();
    }

    public void stop(){
        mProgressDrawable.stop();
    }
}
