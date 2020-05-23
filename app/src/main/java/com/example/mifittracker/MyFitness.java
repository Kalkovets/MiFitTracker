package com.example.mifittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.BleDevice;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.BleScanCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class MyFitness extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fitness);

//        System.out.println("name: " + getLocalBluetoothName());

        System.out.println("before something");
        do_something();
        System.out.println("after something");
    }

    BluetoothAdapter mBluetoothAdapter;

    public String getLocalBluetoothName(){
        if(mBluetoothAdapter == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        String name = mBluetoothAdapter.getName();
        if(name == null){
            System.out.println("Name is null!");
            name = mBluetoothAdapter.getAddress();
        }
        return name;
    }

    private void do_something() {
        BleScanCallback bleScanCallbacks = new BleScanCallback() {
            @Override
            public void onDeviceFound(BleDevice device) {
                System.out.println("onDeviceFound");
                // A device that provides the requested data types is available
            }
            @Override
            public void onScanStopped() {
                System.out.println("onScanStopped");
                // The scan timed out or was interrupted
            }
        };

        TextView hrBRM = (TextView)findViewById(R.id.hrBPM);
 //       hrBRM.setText();

        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE, FitnessOptions.ACCESS_READ)
                .build();

        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            System.out.println("TRY GET PERMISSIONS");
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    1, // e.g. 1
                    account,
                    fitnessOptions);
        } else {
            System.out.println("SUCCESS");
            //accessGoogleFit();
        }

        _response = Fitness.getBleClient(this, account)
                .startBleScan(Arrays.asList(DataType.TYPE_HEART_RATE_BPM),
                        1000, bleScanCallbacks);

        _response.addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> var1) {
                System.out.println("FUCKING SHIT");
                System.out.println("EXCEPTION: " + var1.getException());
                System.out.println("Status " + var1.isSuccessful());
            }
        });
    }

    private Task<Void> _response;

}
