package info.androidhive.navigationdrawer.other;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by Ganesh on 18/9/17.
 */

public class DelayedAutoCompleteTextView extends android.support.v7.widget.AppCompatAutoCompleteTextView {
    /* Custom View of AutocompleteTextView which helps load the textview after a few keystrokes,
    to prevent fewer network calles & avoid invalid data - */

    private static final int MESSAGE_TEXT_CHANGED = 0;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 1;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            DelayedAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);


        }
    };
    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private ProgressBar mLoadingIndicator;


    public DelayedAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
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


}
