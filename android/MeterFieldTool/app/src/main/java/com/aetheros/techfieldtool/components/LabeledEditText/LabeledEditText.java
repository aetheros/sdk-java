/*
 *                   AETHEROS, INC. CONFIDENTIAL
 *
 * The source code contained or described herein and all documents related
 * to the source code ("Material") are owned by Aetheros, Inc. or its
 * suppliers or licensors. label to the Material remains with Aetheros or its
 * suppliers and licensors. The Material contains trade secrets and proprietary
 * and confidential information of Aetheros or its suppliers and licensors. The
 * Material is protected by worldwide copyright and trade secret laws and treaty
 * provisions. No part of the Material may be used, copied, reproduced, modified,
 * published, uploaded, posted, transmitted, distributed, or disclosed in any way
 * without the prior express written permission of Aetheros, Inc.
 *
 * No license under any patent, copyright, trade secret or other intellectual
 * property right is granted to or conferred upon you by disclosure or delivery
 * of the Material, either expressly, by implication, inducement, estoppel or
 * otherwise. Any license under such intellectual property rights must be
 * express and approved by Aetheros, Inc. in writing.
 *
 *       Copyright (c) 2020 Aetheros, Inc.  All Rights Reserved.
 *
 *
 */

package com.aetheros.techfieldtool.components.LabeledEditText;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aetheros.techfieldtool.R;

public class LabeledEditText extends LinearLayout {
    String mLabel;
    String mNote;
    String mHint;

    TextView mLabelTv;
    TextView mNoteTv;

    EditText mEditText;

    public LabeledEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Inflate the layout xml and create references to its child views.
        inflate(context, R.layout.component_labeled_edittext, this);

        mLabelTv = findViewById(R.id.label);
        mNoteTv = findViewById(R.id.note);
        mEditText = findViewById(R.id.input);

        // Extract the attributes set in the layout instantiating this view component.
        // This is a shared resource - do not forget to recycle it when you are done.
        TypedArray arr = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ComponentLabeledEditText, 0, 0
        );

        try {
            // If a label was set via an xml attribute in a layout, set it.
            mLabel = arr.getString(R.styleable.ComponentLabeledEditText_label);
            mNote = arr.getString(R.styleable.ComponentLabeledEditText_note);
            mHint = arr.getString(R.styleable.ComponentLabeledEditText_hint);

            if (mLabel != null) {
                this.setLabel(mLabel);
            }

            if (mNote != null) {
                this.setNote(mNote);
            } else {
                mNoteTv.setVisibility(INVISIBLE);
            }

            if(mHint != null) {
                this.setHint(mHint);
            }
        } finally {
            // Clean up.
            arr.recycle();
        }

    }

    /**
     * Set the label text in the toolbar.
     *
     * @param label The text to display.
     */
    public void setLabel (String label){
        mLabel = label;
        mLabelTv.setText(mLabel);
        invalidate();
        requestLayout();
    }

    public String getLabel () {
        return mLabel;
    }

    public void setNote(String note){
        mNote = note;
        mNoteTv.setText(mNote);
        invalidate();
        requestLayout();
    }

    public String getNote() {
        return mNote;
    }

    public void setHint(String note){
        mHint = note;
        mEditText.setHint(mHint);
        invalidate();
        requestLayout();
    }

    public String getHint() {
        return mHint;
    }

    public void setText(String text) {
        mEditText.setText(text);
    }

    public String getText() {
        return this.mEditText.getText().toString();
    }
}
