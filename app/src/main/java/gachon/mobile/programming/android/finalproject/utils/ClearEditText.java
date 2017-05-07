package gachon.mobile.programming.android.finalproject.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import gachon.mobile.programming.android.finalproject.R;

/**
 * Created by jungwh on 2017-05-07.
 */

public class ClearEditText extends AppCompatEditText implements TextWatcher, View.OnTouchListener, View.OnFocusChangeListener {
    private Drawable clearDrawable;
    private OnFocusChangeListener onFocusChangeListener;
    private OnTouchListener onTouchListener;

    public ClearEditText(final Context context) {
        super(context);
        init();
    }

    public ClearEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    private void init() {
        final Drawable tempDrawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_clear_black_24dp);
        clearDrawable = DrawableCompat.wrap(tempDrawable);
        DrawableCompat.setTintList(clearDrawable, getHintTextColors());
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());

        setClearIconVisible(false);

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void onFocusChange(final View view, final boolean isFocus) {
        if (isFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }

        if (onFocusChangeListener != null) {
            onFocusChangeListener.onFocusChange(view, isFocus);
        }
    }

    @Override
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int x = (int) motionEvent.getX();
        if (clearDrawable.isVisible() && x > getWidth() - getPaddingRight() - clearDrawable.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setError(null);
                setText(null);
            }
            return true;
        }

        /*if (onTouchListener != null) {
            return onTouchListener.onTouch(view, motionEvent);
        } else {
            return false;
        }*/
        return onTouchListener != null && onTouchListener.onTouch(view, motionEvent);
    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int start, final int before, final int count) {
        if (isFocused()) {
            setClearIconVisible(charSequence.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void setClearIconVisible(boolean isVisible) {
        clearDrawable.setVisible(isVisible, false);
        setCompoundDrawables(null, null, isVisible ? clearDrawable : null, null);
    }
}
