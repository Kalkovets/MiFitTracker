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
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class CompletedExercitesData extends AppCompatActivity {

    FitnessOptions fitnessOptions;
    private static int ACTIVITY_RECOGNITION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_exercites_data);
        TextView textView;

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.TYPE_HEART_POINTS, FitnessOptions.ACCESS_READ)
//                .addDataType(DataType.AGGREGATE_HEART_RATE_SUMMARY, FitnessOptions.ACCESS_READ)
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
        } else {
            accessGoogleFit();
        }
    }



    public void authGoogleFitAPI2(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            String[] permissions = {Manifest.permission.ACTIVITY_RECOGNITION};
            ActivityCompat.requestPermissions(this,
                    permissions,
                    ACTIVITY_RECOGNITION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                accessGoogleFit();
            }
        }
    }

    private void accessGoogleFit() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("TIME", "Range Start: " + dateFormat.format(startTime));
        Log.i("TIME", "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        GoogleSignInAccount account = GoogleSignIn
                .getAccountForExtension(this, fitnessOptions);

        Fitness.getHistoryClient(this, account)
                .readData(readRequest)
                .addOnSuccessListener(response -> {
                    // Use response data here

                    for (Bucket b : response.getBuckets()) {
                        if (b != null) {
                            DataSet s = b.getDataSet(DataType.TYPE_STEP_COUNT_DELTA);
                            if (s != null) {
                                dumpDataSet(s);
                            }

                        }
                    }

                    Log.d("GETHISTORYCLIENT", "OnSuccess()");
                    Log.d("STATUS", response.getStatus().toString());
                })
                .addOnFailureListener(e -> {
                    Log.d("GETHISTORYCLIENT", "OnFailure()", e);
                });
//        Task<DataReadResponse> response = Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this).readData(readRequest);
//        List<DataSet> dataSets = response.getResult().getDataSets();
    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i("DUMPDATASET", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.i("DUMPDATASET", "Data point:");
            Log.i("DUMPDATASET", "\tType: " + dp.getDataType().getName());
            Log.i("DUMPDATASET", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i("DUMPDATASET", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i("DUMPDATASET", "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
        }
    }
}
