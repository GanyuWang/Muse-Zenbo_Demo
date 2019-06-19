package com.example.wgy.bcitest2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.choosemuse.libmuse.*;

import java.util.List;

public class ConnectMuse {
//    tag
    private String TagConnect = "TagConnect";

//    Variables
    private MuseManagerAndroid museManager;
    private List<Muse> museList;
    private Muse muse;
    //data
    private MyMuseDataListener museDataListener;

    //renew
    private Handler connectHandler;

    ConnectMuse(Context context, Handler handler){
        connectHandler = handler;

        museManager = MuseManagerAndroid.getInstance();
        museManager.setContext(context);
        museManager.setMuseListener(new MyMuseListener());

        museManager.stopListening();
        museManager.startListening();

        museDataListener = new MyMuseDataListener();

    }

    //
    public void GetMuse0(){
        muse = museManager.getMuses().get(0);
        Log.d(TagConnect, muse.getName());
    }
    //
    public void StartReceiveEEGdata(){

        muse.unregisterAllListeners();

        muse.registerDataListener(museDataListener, MuseDataPacketType.ALPHA_ABSOLUTE);

        muse.runAsynchronously();
    }
    public void Refresh(){
        museManager.stopListening();
        museManager.startListening();
    }

    public void stop(){
        museManager.stopListening();
    }


    //realization
    private class MyMuseListener extends MuseListener{
        @Override
        public void museListChanged() {
            museList = museManager.getMuses();
            muse = museList.get(0);
            Log.d(TagConnect, "get" + muse.getName());
        }
    }

    private class MyMuseDataListener extends MuseDataListener{

        private double[] data;

        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket, Muse muse) {
            Bundle bundle = new Bundle();
            Message msg= new Message();

            //get data from channels
            data = new double[4];
            getEegChannelValues(data, museDataPacket);
            if(data != null) {
                //set message to send
                bundle.putDoubleArray("EEG", data);
                msg.setData(bundle);
                //send message
                connectHandler.sendMessage(msg);
            }
        }
        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket, Muse muse) {
            //nothing to do
        }
        // Updates newData array based on incoming EEG channel values
        private void getEegChannelValues(double[] newData, MuseDataPacket p) {
            newData[0] = p.getEegChannelValue(Eeg.EEG1);
            newData[1] = p.getEegChannelValue(Eeg.EEG2);
            newData[2] = p.getEegChannelValue(Eeg.EEG3);
            newData[3] = p.getEegChannelValue(Eeg.EEG4);
        }
    }
}
