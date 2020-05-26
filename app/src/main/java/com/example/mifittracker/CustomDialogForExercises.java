package com.example.mifittracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class CustomDialogForExercises extends DialogFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        System.out.println("CREATING ONE MORE FUCKING SHIT");
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        System.out.println("CREATING FUCKING SHIT");
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater_dialog_exercises = getActivity().getLayoutInflater();

        builder.setView(inflater_dialog_exercises.inflate(R.layout.activity_custom_dialog_for_exercises, null));

        return builder.create();
        //return super.onCreateDialog(savedInstanceState);
    }
}
