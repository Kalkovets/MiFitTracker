package com.example.mifittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialogForCompletedExercises extends DialogFragment {

    private String _exercise_name;
    private String _pulse_bpm;

    public CustomDialogForCompletedExercises(String exercise_name, String pulse_bpm) {
        super();
        _exercise_name = exercise_name;
        _pulse_bpm = pulse_bpm;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater11 = getActivity().getLayoutInflater();

        View myView1 = inflater11.inflate(R.layout.custom_dialog_for_completed_exercises, null);

        TextView textCompletedExerciseName = myView1.findViewById(R.id.name_exercises_dialog_for_completed_exercises);
        TextView textPulseBPM = myView1.findViewById(R.id.pulse_bpm);

        builder1.setView(myView1);

        textCompletedExerciseName.setText(_exercise_name);
        textPulseBPM.setText(_pulse_bpm+" ударов/мин");

        return builder1.create();
    }
}
