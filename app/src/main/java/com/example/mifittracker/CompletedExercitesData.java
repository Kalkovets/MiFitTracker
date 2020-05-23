package com.example.mifittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class CompletedExercitesData extends AppCompatActivity {

    FitnessOptions fitnessOptions;
    private static int ACTIVITY_RECOGNITION_CODE = 1;
    private static DataType DATA_TYPE = DataType.TYPE_HEART_RATE_BPM;
    private static DataType AGGREGATE_DATA_TYPE = DataType.AGGREGATE_HEART_RATE_SUMMARY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_exercites_data);

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DATA_TYPE, FitnessOptions.ACCESS_READ)
                .addDataType(AGGREGATE_DATA_TYPE, FitnessOptions.ACCESS_READ)
                .build();

        authGoogleFitAPI();
        authGoogleFitAPI2();
        accessGoogleFit();
    }

    public void authGoogleFitAPI(){
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    1, // e.g. 1
                    account,
                    fitnessOptions);
        }
    }

    public void authGoogleFitAPI2(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED)
        {
            String[] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
            ActivityCompat.requestPermissions(this,
                    permissions,
                    ACTIVITY_RECOGNITION_CODE);
        }
    }

    private void accessGoogleFit() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = calendar.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("TIME", "Range Start: " + dateFormat.format(startTime));
        Log.i("TIME", "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DATA_TYPE, AGGREGATE_DATA_TYPE)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.HOURS)
                .build();

        GoogleSignInAccount account = GoogleSignIn
                .getAccountForExtension(this, fitnessOptions);

        Fitness.getHistoryClient(this, account)
                .readData(readRequest)
                .addOnSuccessListener(response -> {

                    Log.d("Response status", response.getStatus().toString());

                    for (Bucket currentBucket : response.getBuckets()) {
                        if (currentBucket != null) {
                            DataSet dataSet = currentBucket.getDataSet(AGGREGATE_DATA_TYPE);
                            if (dataSet != null) {
                                dumpDataSet(dataSet);
                            } else {
                                Log.w("Unexpected", "Got null DataSet");
                            }
                        } else {
                            Log.w("Unexpected", "Got null Bucket");
                        }
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("Request", "Unexpected error while the request was processing", e);
                });

    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i("DUMP_DATA_SET", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i("DUMP_DATA_SET", "Data point:");
            Log.i("DUMP_DATA_SET", "\tType:  " + dp.getDataType().getName());
            Log.i("DUMP_DATA_SET", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i("DUMP_DATA_SET", "\tEnd:   " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i("DUMP_DATA_SET", "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
        }
    }
}
