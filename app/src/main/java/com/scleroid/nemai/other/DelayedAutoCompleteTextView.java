package com.scleroid.nemai.other;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.ProgressBar;

/**
 * Created by Ganesh on 18/9/17.
 */


public class DelayedAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    /* Custom View of AutocompleteTextView which helps load the textview after a few keystrokes,
    to prevent fewer network calls & avoid invalid data - */

    private static final int MESSAGE_TEXT_CHANGED = 0;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 0;
    private Handler mHandler;
    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar mLoadingIndicator;


    public DelayedAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                DelayedAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);


            }
        };
    }
    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }

    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }


    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        //removed static handler
        //andler= new Handler();
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text));
    }

    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }


        super.onFilterComplete(count);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        final InputConnection ic = super.onCreateInputConnection(outAttrs);
        if (ic != null && outAttrs.hintText == null) {
            // If we don't have a hint and our parent is a TextInputLayout, use it's hint for the
            // EditorInfo. This allows us to display a hint in 'extract mode'.
            final ViewParent parent = getParent();
            if (parent instanceof TextInputLayout) {
                outAttrs.hintText = ((TextInputLayout) parent).getHint();
            }
        }
        return ic;
    }


}
