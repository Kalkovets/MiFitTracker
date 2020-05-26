package com.example.mifittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomDialogForExercises extends DialogFragment {

    private String _exercise_name;
    private String _description_exercise;

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

        return builder.create();
    }
}
