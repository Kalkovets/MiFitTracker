package com.example.mifittracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.DateFormat;
import java.util.Calendar;

public class CollectingUserData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting_user_data);

        EditText dateBirthday = (EditText)findViewById(R.id.dateBirthday);
        dateBirthday.setRawInputType(0x00000000);

        ImageView dateButton = (ImageView)findViewById(R.id.dateButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        String dateBirthdayString = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        EditText dateBirthday = (EditText)findViewById(R.id.dateBirthday);
        dateBirthday.setText(dateBirthdayString);
    }
}
