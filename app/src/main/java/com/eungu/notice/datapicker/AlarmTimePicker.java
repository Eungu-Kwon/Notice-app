package com.eungu.notice.datapicker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class AlarmTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @NonNull
    @Override
    @SuppressWarnings("deprecation")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //TODO make TimePicker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int miniute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar , this, hour, miniute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {

    }
}
