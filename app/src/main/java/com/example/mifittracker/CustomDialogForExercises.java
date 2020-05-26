package com.example.mifittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getDateTimeInstance;
import static java.text.DateFormat.getTimeInstance;

public class CustomDialogForExercises extends DialogFragment {

    private String _exercise_name;
    private String _description_exercise;
    public double currentTime;
    public double endTime;
    public String currentDate;
    public String currentClock;

    public CustomDialogForExercises(String exercise_name, String description_exercise) {
        super();
        _exercise_name = exercise_name;
        _description_exercise = description_exercise;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View myView = inflater.inflate(R.layout.custom_dialog_for_exercises, null);

        TextView textExerciseName = myView.findViewById(R.id.name_exercises_dialog_for_exercises);
        TextView textDescription = myView.findViewById(R.id.description_exercises_dialog_for_exercises);

        builder.setView(myView);

        textExerciseName.setText(_exercise_name);
        textDescription.setText(_description_exercise);

        FirebaseFirestore databaseFirebase = FirebaseFirestore.getInstance();

        Button startButton = myView.findViewById(R.id.StartButton);
        Button endButton = myView.findViewById(R.id.EndButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                currentTime = calendar.getTimeInMillis(); //Время в миллисекундах

                java.text.DateFormat dateFormat = getDateInstance();
                currentDate = dateFormat.format(currentTime); //Достаем дату
                startButton.setVisibility(myView.GONE);
                endButton.setVisibility(myView.VISIBLE);
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                endTime = calendar.getTimeInMillis(); //Время в миллисекундах

                java.text.DateFormat timeFormat = getTimeInstance();
                currentClock = timeFormat.format(endTime);

                Map<String, Object> completed_exercises = new HashMap<>();
                completed_exercises.put("DateExercise", currentDate.toString());
                completed_exercises.put("EndTimeInMillis", endTime);
                completed_exercises.put("NameExercise", _exercise_name.toString());
                completed_exercises.put("StartTimeInMillis", currentTime);
                completed_exercises.put("TimeExercise", currentClock.toString());

                databaseFirebase.collection("Completed_Exercises")
                .add(completed_exercises)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("COMPLERED_EXERCISES", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("COMPLETED_EXERCISES", "Error adding document", e);
                            }
                        });
            }
        });

        return builder.create();
    }
}
