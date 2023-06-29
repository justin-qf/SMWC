package com.vovance.securemywillcall.common;

import android.content.Context;

import java.util.Observable;

public class AppObserver extends Observable {
    private int nStatusType;
    private String userName;
    private String data;

    public AppObserver(Context context) {
    }

    public int getValue() {
        HELPER.print("GET_VALUE:::::", "YES");
        return nStatusType;
    }

    public void setValue(int nStatusTyp) {
        HELPER.print("SET_VALUE::::::",String.valueOf(nStatusTyp));
        this.nStatusType = nStatusTyp;
        setChanged();
        notifyObservers(userName);
    }

    public String getData() {
        return data;
    }

    public void setValue(int nStatusTyp, String data) {
        this.nStatusType = nStatusTyp;
        this.data = data;
        setChanged();
        notifyObservers(userName);
    }
}
