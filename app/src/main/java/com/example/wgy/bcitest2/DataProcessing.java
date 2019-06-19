package com.example.wgy.bcitest2;

import android.app.Application;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import NetworkConnection.ClientConnection;

public class DataProcessing {
    //debugging
    public static final String TAG = "DataProcessing";

    private double[][] circleBuf;
    private final int nTimes = 12;
    private int dataPointer = 0;
//    private ClientConnection clientConnection;

    private ControlRobot controlRobot;


    DataProcessing(ControlRobot control){
//        init
        circleBuf = new double[4][nTimes];
        dataPointer = 0;
        controlRobot = control;

    }

/*    public int getDataPointer() {
        return dataPointer;
    }*/

    //    add 4 EEG data in

    public void putEEG( double[] newData){

        //demean
        for(int i = 0; i < 4; i++){
            circleBuf[i][dataPointer] = newData[i];
        }
        dataPointer++;
        if(dataPointer == nTimes){
            dataPointer= 0;
        }

        if(dataPointer == 0){
            if(circleBuf[0][0] == circleBuf[0][10]){
                controlRobot.setLightBrightnessUp(); //not calm   turn up the light
            }else {
                controlRobot.setLightBrightnessDown(); //clam      light down
            }
        }




    }

    public double[][] getCircleBuf(){
        return circleBuf;
    }

}
