package com.opencharge.opencharge.presentation.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.opencharge.opencharge.R;
import com.opencharge.opencharge.domain.Entities.Service;
import com.opencharge.opencharge.domain.helpers.DateConversion;
import com.opencharge.opencharge.domain.helpers.impl.DateConversionImpl;
import com.opencharge.opencharge.domain.use_cases.ServiceCreateUseCase;
import com.opencharge.opencharge.presentation.locators.UseCasesLocator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Oriol on 5/5/2017.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateServiceFragment extends Fragment {

    final Calendar calendar = Calendar.getInstance();
    int year, month, day, hour, min;
    private EditText date;
    private EditText dateEnd;
    private EditText inici;
    private EditText fi;
    private CheckBox mon;
    private CheckBox tue;
    private CheckBox wed;
    private CheckBox thu;
    private CheckBox fri;
    private CheckBox sat;
    private CheckBox sun;

    public CreateServiceFragment() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_service, container, false);

        date = (EditText) view.findViewById(R.id.date);
        date.setInputType(InputType.TYPE_NULL);
        date.setFocusable(false);
        showDate(year, month, day, date);

        dateEnd = (EditText) view.findViewById(R.id.dateEnd);
        dateEnd.setFocusable(false);
        dateEnd.setInputType(InputType.TYPE_NULL);

        inici = (EditText) view.findViewById(R.id.ini);
        inici.setFocusable(false);
        inici.setInputType(InputType.TYPE_NULL);
        
        fi = (EditText) view.findViewById(R.id.fi);
        fi.setFocusable(false);
        fi.setInputType(InputType.TYPE_NULL);

        mon = (CheckBox) view.findViewById(R.id.mon);
        tue = (CheckBox) view.findViewById(R.id.tue);
        wed = (CheckBox) view.findViewById(R.id.wed);
        thu = (CheckBox) view.findViewById(R.id.thu);
        fri = (CheckBox) view.findViewById(R.id.fri);
        sat = (CheckBox) view.findViewById(R.id.sat);
        sun = (CheckBox) view.findViewById(R.id.sun);

        Button save = (Button) view.findViewById(R.id.saveBtn);
        final Button cancel = (Button) view.findViewById(R.id.cancelBtn);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(),datePickerListener1, year, month, day);
                datePicker.setCancelable(false);
                datePicker.setTitle("Seleccionar data");
                datePicker.show();
            }
        });

        dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), datePickerListener2, year, month, day);
                datePicker.setCancelable(true);
                datePicker.setTitle("Seleccionar data");
                datePicker.show();
            }
        });

        inici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), timePickerListener1, hour+1, 0, true);
                createTimePicker(timePicker);
            }
        });

        fi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePicker = new TimePickerDialog(getActivity(), timePickerListener2, hour+2, 0, true);
                createTimePicker(timePicker);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideInput();
                cancel();
            }
        });

        return view;
    }

    private  DatePickerDialog.OnDateSetListener datePickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            showDate(i, i1, i2, date);
        }
    };

    private  DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            showDate(i, i1, i2, dateEnd);
        }
    };

    private void createTimePicker(TimePickerDialog timePicker) {
        timePicker.setCancelable(true);
        timePicker.setTitle("Seleccionar hora");
        timePicker.show();
    }

    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            showTime(i, i1, inici);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener2 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            showTime(i, i1, fi);
        }
    };

    private void showDate(int year, int month, int day, EditText text) {
        text.setText(day + "/" + (month+1) + "/" + year);
    }

    private void showTime(int h, int m, EditText text) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String time = simpleDateFormat.format(new Date(0, 0, 0, h, m));
        text.setText(time);
    }

    private void save() {

        String day = date.getText().toString();
        if (day.isEmpty()) {
            Toast.makeText(getActivity(), "Ha d'indicar el dia de la reserva!", Toast.LENGTH_SHORT).show();
            return;
        }
        String startHour = inici.getText().toString();
        if (startHour.isEmpty()) {
            Toast.makeText(getActivity(), "Ha d'indicar l'hora d'inici!", Toast.LENGTH_SHORT).show();
            return;
        }
        String endHour = fi.getText().toString();
        if (endHour.isEmpty()) {
            Toast.makeText(getActivity(), "Ha d'indicar l'hora de finalització!", Toast.LENGTH_SHORT).show();
            return;
        }

        DateConversion dateConversion = new DateConversionImpl();
        Date startDay = dateConversion.ConvertStringToDate(day);
        Date startTime = dateConversion.ConvertStringToTime(startHour);
        Date endTime = dateConversion.ConvertStringToTime(endHour);

        Service s = new Service(startDay, startTime, endTime);

        String endRepeat = dateEnd.getText().toString();
        if (!endRepeat.isEmpty()) {
            Date lastRepeat = dateConversion.ConvertStringToDate(endRepeat);
            s.setLastRepeat(lastRepeat);

            boolean selected = false;
            if (mon.isChecked()) {
                selected = true;
                s.setRepeatMonday();
            }
            if (tue.isChecked()) {
                selected = true;
                s.setRepeatTuesday();
            }
            if (wed.isChecked()) {
                selected = true;
                s.setRepeatWednesday();
            }
            if (thu.isChecked()) {
                selected = true;
                s.setRepeatThursday();
            }
            if (fri.isChecked()) {
                selected = true;
                s.setRepeatFriday();
            }
            if (sat.isChecked()) {
                selected = true;
                s.setRepeatSaturday();
            }
            if (sun.isChecked()) {
                selected = true;
                s.setRepeatSunday();
            }

            if (!selected) {
                Toast.makeText(getActivity(), "Seleccioni quins dies vol repetir", Toast.LENGTH_SHORT).show();
                return;
            }

        } else if (mon.isChecked() || tue.isChecked() || wed.isChecked() || thu.isChecked() || fri.isChecked() || sat.isChecked() || sun.isChecked()) {
            Toast.makeText(getActivity(), "Ha d'indicar data de finalització de repeticions!", Toast.LENGTH_SHORT).show();
            return;
        }

        UseCasesLocator useCasesLocator = UseCasesLocator.getInstance();
        ServiceCreateUseCase getServiceCreateUseCase = useCasesLocator.getServiceCreateUseCase(new ServiceCreateUseCase.Callback(){
            @Override
            public void onServiceCreated(String id) {
                android.app.FragmentManager fm = getFragmentManager();
                PointInfoFragment fragment = PointInfoFragment.newInstance(id);
                fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }

        });

    }

    private void cancel() {
        android.app.FragmentManager fm = getFragmentManager();
        MapsFragment mp = new MapsFragment();
        fm.beginTransaction().replace(R.id.content_frame, mp).commit();
    }

    private void hideInput() {
        InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
