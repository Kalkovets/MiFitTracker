package com.example.mifittracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
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

    ListView listView1;
    String TimeExercises;
    public static double HeartRateBRM;

    FitnessOptions fitnessOptions;
    private static int ACTIVITY_RECOGNITION_CODE = 1;
    private static DataType DATA_TYPE = DataType.TYPE_HEART_RATE_BPM;
    private static DataType AGGREGATE_DATA_TYPE = DataType.AGGREGATE_HEART_RATE_SUMMARY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_exercites_data);

        Button mainMenuButton = (Button)findViewById(R.id.button3);
        mainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backtoMainMenu = new Intent(getApplicationContext(), Main_Menu.class);
                startActivity(backtoMainMenu);
            }
        });

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DATA_TYPE, FitnessOptions.ACCESS_READ)
                .addDataType(AGGREGATE_DATA_TYPE, FitnessOptions.ACCESS_READ)
                .build();

        authGoogleFitAPI();
        authGoogleFitAPI2();

        listView1 = (ListView) findViewById(R.id.listView2);

        FirebaseFirestore databaseFirebase = FirebaseFirestore.getInstance();

        databaseFirebase.collection("Completed_Exercises").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                if (task1.isSuccessful()) {
                    List<String> list3 = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task1.getResult()) {
                        list3.add(document.get("NameExercise").toString());
                    }
                    List<String> list4 = new ArrayList<>();
                    List<String> listTimeExercises = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task1.getResult()) {
                        list4.add(document.get("DateExercise").toString()+" "+document.get("TimeExercise").toString());
                        listTimeExercises.add(document.get("TimeExercise").toString());
                        TimeExercises = document.get("TimeExercise").toString();
                        System.out.println(list3+" "+list4);
                    }
                    List<Double> list5Start = new ArrayList<>();
                    List<Double> list5End = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task1.getResult()) {
                        list5Start.add(Double.parseDouble(document.get("StartTimeInMillis").toString()));
                        listTimeExercises.add(document.get("TimeExercise").toString());
                        list5End.add(Double.parseDouble(document.get("EndTimeInMillis").toString()));
                        TimeExercises = document.get("TimeExercise").toString();

                        System.out.println("LONG "+list5Start);
                    }
                    CompletedExercitesData.MyAdapter1 adapter = new CompletedExercitesData.MyAdapter1(getApplicationContext(), list3, list4);
                    listView1.setAdapter(adapter);

                    listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                            String exercise_name = list3.get(position); // Назва вправи

                            databaseFirebase.collection("Completed_Exercises").whereEqualTo("TimeExercise", listTimeExercises.get(position))
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d("GETTIME", document.getId() + " => " + document.getData());

                                            double startExercise = (list5Start.get(position));
                                            long startExerciseLong = (long)(startExercise);

                                            double endExercise = (list5End.get(position));
                                            long endExerciseLong = (long)(endExercise);

                                            System.out.println("FUCK CANT "+endExerciseLong);

                                            String pulse_bpm = "90 ударов/мин";

                                            accessGoogleFit(startExerciseLong, endExerciseLong);

                                            CustomDialogForCompletedExercises dialog = new CustomDialogForCompletedExercises(exercise_name, pulse_bpm);
                                            dialog.show(getSupportFragmentManager(), "SHOW DIALOG FOR EXERCISES");
                                        }
                                    } else {
                                        Log.d("GETTIME", "Error getting documents: ", task.getException());
                                    }
                                }
                            });

                        }
                    });
//                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        }
//                    });
                    Log.d("EXERCISES", list3.toString());
                } else {
                    Log.d("EXERCISES", "Error getting documents: ", task1.getException());
                }
            }
        });


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

    private void accessGoogleFit(long _startTime, long _endTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long endTime = _endTime; //calendar.getTimeInMillis();
        //calendar.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = _startTime; //calendar.getTimeInMillis();

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
            //    if (field.getName()=="average") {
                    //this.HeartRateBRM = dp.getValue(field);
                    Log.i("DUMP_DATA_SET", "\tField: " + field.getName() + " Value: " + dp.getValue(field));
                    System.out.println("SUUUCK " + field.getName());
             //   }
            }
        }
    }

    class MyAdapter1 extends ArrayAdapter<String> {
        Context context;
        List<String> rTitle;
        List<String> rDescription;

        MyAdapter1 (Context c, List<String> title, List<String> description){
            super(c, R.layout.row, R.id.textView111, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            TextView nameTraining = row.findViewById(R.id.textView111);
            TextView countTraining = row.findViewById(R.id.textView222);

            nameTraining.setText(rTitle.get(position).toString());
            countTraining.setText(" "+rDescription.get(position).toString());

            return row;
        }
    }
}
