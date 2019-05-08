package com.xima.net.orange.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.xima.net.orange.listener.OnDateSetListener;
import com.xima.net.orange.utils.DateUtils;

import java.util.Date;
import java.util.Objects;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSetListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int[] date = DateUtils.getDate(new Date());
        int year = date[0];
        int month = date[1];
        int day = date[2];

        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (mListener != null)
            mListener.onDateSet(year, month, dayOfMonth);
    }

    public void setListener(OnDateSetListener mListener) {
        this.mListener = mListener;
    }


}
