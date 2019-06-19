package NetworkConnection;

import android.util.Log;

import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection {
//debug
    private static final String TAG = "ClientConnection";
//socket and input output stream
    private Socket socket;
//remote IP addr and port
    private String remoteAddr;
    private int remotePort;

    public ClientConnection(String addr, int port){
        remoteAddr = addr;
        remotePort = port;
    }

    public void connect(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                socket = null;
                try{
                    Log.d(TAG, "connecting");
                    socket = new Socket(remoteAddr, remotePort);
                    Log.d(TAG, "have connected to computer");
                }catch (IOException e1){
                    e1.printStackTrace();
                }
            }
        });
        t.start();
    }

//    send Json
    public void sendJSONObject(JSONObject jsonObject){
        OutputStream outputStream;
        try{
            outputStream = socket.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }



//    send EEG
    public void sendEEG(double[] EEG){
        try {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i< 4; i++) {
                jsonObject.put("EEG" + i, EEG[i]);
            }
            jsonObject.put("Command", "EEG");
            sendJSONObject(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void sendEEGBuffer(double[][] EEGBuffer){
        try {
            JSONObject jsonObject = new JSONObject();
            for(int i = 0; i < 4; i++) {
                JSONArray jsonArray = new JSONArray();
                for(int j = 0; j<220; j++) {
                    jsonArray.put(j, EEGBuffer[i][j]);
                }
                jsonObject.put("EEG" + i, jsonArray);
            }
            jsonObject.put("Command", "EEG");
            sendJSONObject(jsonObject);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    //    receive Json
    public JSONObject receiveJSONObject(){


        InputStream inputStream;
        JSONObject jsonObject = null;
        String data;
        try{
            inputStream = socket.getInputStream();

            byte[] buffer = new byte[2048];
            int n = inputStream.read(buffer);
            data = new String(buffer,0,n);
            Log.d(TAG, "1 "  + data);
            try{
                jsonObject = new JSONObject(data);
                Log.d(TAG, jsonObject.toString());
            }catch (JSONException e){
                e.printStackTrace();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String receiveCommand(){
        String cmd = null;
        JSONObject jsonObject = receiveJSONObject();
        try{
            cmd = jsonObject.getString("Command");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return cmd;
    }

    public static void main(String [ ] args)
    {
//        String data = new String("\{\"Command\"\: \"yes\"}");
//        JSONObject jsonObject = new JSONObject().getJSONObject()

    }


}
