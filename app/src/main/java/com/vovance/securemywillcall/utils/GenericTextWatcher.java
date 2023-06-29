package com.vovance.securemywillcall.utils;

import static com.vovance.securemywillcall.activity.BaseActivity.hideSoftKeyboard;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.vovance.securemywillcall.R;

public class GenericTextWatcher implements TextWatcher {

    private final EditText[] editText;
    private View view;
    private Activity act;

    public GenericTextWatcher(Activity act, View view, EditText editText[]) {
        this.act = act;
        this.editText = editText;
        this.view = view;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (view.getId()) {

            case R.id.otpOne:
                if (text.length() == 1)
                    editText[1].requestFocus();

                break;
            case R.id.otpTwo:

                if (text.length() == 1)
                    editText[2].requestFocus();
                else if (text.length() == 0)
                    editText[0].requestFocus();
                break;
            case R.id.otpThree:
                if (text.length() == 1)
                    editText[3].requestFocus();
                else if (text.length() == 0)
                    editText[1].requestFocus();
                break;
            case R.id.otpFour:
                if (text.length() == 0)
                    editText[2].requestFocus();
                if (text.length() == 1) {
                    editText[3].clearFocus();
                    hideSoftKeyboard(act);
                }
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

}
