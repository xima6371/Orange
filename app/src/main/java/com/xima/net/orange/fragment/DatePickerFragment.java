package com.xima.net.orange.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.xima.net.orange.utils.DateUtils;

import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog mDialog;
    private OnDateSetListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int[] date = DateUtils.getYearMonthDay(new Date());
        int year = date[0];
        int month = date[1];
        int day = date[2];

        mDialog = new DatePickerDialog(getActivity(), this, year, month, day);

        return mDialog;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mListener != null)
            mListener.onDateSet(year, month, dayOfMonth);
    }

    public void setListener(OnDateSetListener mListener) {
        this.mListener = mListener;
    }

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int dayOfMonth);
    }
}
