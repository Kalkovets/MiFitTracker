package com.example.mifittracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Training_Page extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training__page);
        FirebaseApp.initializeApp(this);

        listview = (ListView) findViewById(R.id.listView);

        FirebaseFirestore databaseFirebase = FirebaseFirestore.getInstance();

        databaseFirebase.collection("Exercises").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.get("Name_Exercise").toString());
                        //list.add(document.get("Count").toString());
//                        System.out.println("YCPEWHO! " + document.get("Name_Exercise").toString());
//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
//                        listview.setAdapter(arrayAdapter);
                    }
                    List<String> list2 = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list2.add(document.get("Count").toString());
                        //list.add(document.get("Count").toString());
//                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
//                        listview.setAdapter(arrayAdapter);
                    }
                    MyAdapter adapter = new MyAdapter(getApplicationContext(), list, list2);
                    listview.setAdapter(adapter);
//                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                        }
//                    });
                    Log.d("EXERCISES", list.toString());
                } else {
                    Log.d("EXERCISES", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        List<String> rTitle;
        List<String> rDescription;

        MyAdapter (Context c, List<String> title, List<String> description){
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
