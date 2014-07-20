package com.nguyenmp.reader.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class DisplayHelper {
    public static void showKeyboard(EditText target) {
        target.requestFocus();
        InputMethodManager imm = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

        // Either show it once
        imm.showSoftInput(target, InputMethodManager.SHOW_FORCED);

        // Or toggle it twice with forced and hide with not force
        imm.toggleSoftInputFromWindow(target.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
        imm.toggleSoftInputFromWindow(target.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
