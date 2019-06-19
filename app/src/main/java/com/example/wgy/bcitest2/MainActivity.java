package com.example.wgy.bcitest2;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.CircularArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.asus.robotframework.API.MotionControl;
import com.asus.robotframework.API.RobotAPI;
import com.choosemuse.libmuse.Accelerometer;
import com.choosemuse.libmuse.Battery;
import com.choosemuse.libmuse.DrlRef;
import com.choosemuse.libmuse.Eeg;
import com.choosemuse.libmuse.Gyro;
import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.choosemuse.libmuse.MuseListener;
import com.choosemuse.libmuse.MuseManagerAndroid;

import java.util.ArrayList;
import java.util.List;

import NetworkConnection.ClientConnection;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MA = "MainActivity";

    //muse BCI
    public ConnectMuse connectMuse;
    public Handler handler;     //receive data
    //zenbo Robot
    public ControlRobot controlRobot;
    //data Processing
    public DataProcessing dataProcessing;

    //
    Context thisActivity = this;

    //button
    private Button buttonStart;

    private ClientConnection clientConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init
        controlRobot = new ControlRobot(this);
        dataProcessing = new DataProcessing(controlRobot);
        handler = new MyHandler(this, dataProcessing);
        connectMuse = new ConnectMuse(this, handler );

        //button
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(this);
        //
        clientConnection = new ClientConnection("192.168.0.108", 33000);
        clientConnection.connect();
    }

    private static class MyHandler extends Handler{
        Context context;
        DataProcessing dataPr;

        MyHandler(Context ct, DataProcessing dataP){
            context = ct;
            dataPr = dataP;
        }
        @Override
        public void handleMessage(Message msg) {
            double[] data;
            data = msg.getData().getDoubleArray("EEG");
            Toast.makeText(context , "  "+ data[0]+ "  " + data[1] + " "+data[2] + "  " + data[3] , Toast.LENGTH_SHORT ).show();
            dataPr.putEEG(data);
            Log.d(MA, " " +data[0]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_start:

                connectMuse.GetMuse0();
                connectMuse.StartReceiveEEGdata();
                //clientConnection.connect();
                break;


//                double[] eeg = {1, 2, 3, 4};
//                double[][] eeg1 = new double[4][220];
//                eeg1[0][0] = 1;
//                eeg1[0][1] = 2;
//                eeg1[1][0] = 3;
//                clientConnection.sendEEGBuffer(eeg1);
        }
    }

}
