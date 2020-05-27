package com.example.mifittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CollectingUserData extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting_user_data);

        String ID_User = getIntent().getStringExtra("ID_User");
        FirebaseApp.initializeApp(this);

        FirebaseFirestore databaseFirebase = FirebaseFirestore.getInstance();
        databaseFirebase.collection("User_Data")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println("ID_USER_ID_USER "+ID_User+" getID "+document.getId());
                                if (document.getId().equals(ID_User)){
                                    Intent intentToMainMenu = new Intent(getApplicationContext(), Main_Menu.class);
                                    startActivity(intentToMainMenu);
                                }
                                Log.d("Checking data ", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("Checking data ", "Error getting documents: ", task.getException());
                        }
                    }
                });

        EditText dateBirthday = (EditText)findViewById(R.id.dateBirthday);
        dateBirthday.setRawInputType(0x00000000);
        EditText height = (EditText)findViewById(R.id.Height);
        EditText weight = (EditText)findViewById(R.id.Weight);
        Spinner sex = (Spinner)findViewById(R.id.Sex);
        Spinner purpose_training = (Spinner)findViewById(R.id.Purpose_Training);
        Spinner place_training = (Spinner)findViewById(R.id.Place_Training);

        Button nextButton = (Button)findViewById(R.id.button);

        ImageView dateButton = (ImageView)findViewById(R.id.dateButton);



        //Написано на ленивых и нада удалить

//        Map<String, Object> exercises = new HashMap<>();
//        exercises.put("Classification", "");
//        exercises.put("Count", "");
//        exercises.put("Description", "");
//        exercises.put("For_Max_Age", "");
//        exercises.put("For_Max_Height", "");
//        exercises.put("For_Max_Weight", "");
//        exercises.put("For_Min_Age", "");
//        exercises.put("For_Min_Height", "");
//        exercises.put("For_Min_Weight", "");
//        exercises.put("Name_Exercise", "");
//        exercises.put("Place", "");
//        exercises.put("Purpose", "");
//        exercises.put("Sex", "");
//        exercises.put("TimerOrApproah", "");
//
//        databaseFirebase.collection("Exercises")
//                .document("Exercise8")
//                .set(exercises)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        System.out.println("YCPEWHO!");
//                    }
//                });

         //Конец кода для ленивых

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Map<String, Object> user_data = new HashMap<>();
                user_data.put("Height", Integer.parseInt(height.getText().toString()));
                user_data.put("Weight", Integer.parseInt(weight.getText().toString()));
                user_data.put("Born_Date", dateBirthday.getText().toString());
                user_data.put("Sex", sex.getSelectedItem().toString());
                user_data.put("Purpose_Training", purpose_training.getSelectedItem().toString());
                user_data.put("Place_Training", place_training.getSelectedItem().toString());

                databaseFirebase.collection("User_Data")
                        .document(ID_User)
                        .set(user_data)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(CollectingUserData.this, CompletedExercitesData.class);
                                startActivity(intent);
                            }
                        });
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

/* Чтобы узнать, какой пол выбрали при нажатии кнопки "Далее"
Spinner spinner = findViewById(R.id.spinner);
String selected = spinner.getSelectedItem().toString();
Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
*/