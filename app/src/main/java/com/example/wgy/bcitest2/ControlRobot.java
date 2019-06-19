package com.example.wgy.bcitest2;

import android.content.Context;
import android.util.Log;

import com.asus.robotframework.API.MotionControl;
import com.asus.robotframework.API.RobotAPI;
import com.asus.robotframework.API.RobotFace;
import com.asus.robotframework.API.WheelLights;

public class ControlRobot {
    private RobotAPI mRobotAPI;
    private int LightBrightness;

    ControlRobot(Context context){
        //init
        mRobotAPI  = new RobotAPI(context);
        LightBrightness = 25;
    }

    public void moveForward(){
        mRobotAPI.motion.remoteControlBody(MotionControl.Direction.Body.FORWARD);
    }

    public void moveBackward(){
        mRobotAPI.motion.remoteControlBody(MotionControl.Direction.Body.BACKWARD);
    }

    public void setLightBrightnessUp(){
        if(LightBrightness < 55)
            LightBrightness += 5;
        Log.d("RobotControl", "Brightness = " + LightBrightness );
        mRobotAPI.wheelLights.setBrightness(WheelLights.Lights.SYNC_BOTH, 0xff, LightBrightness, true);
        expressionLevel();
    }

    public void setLightBrightnessDown(){
        if(LightBrightness > 0)
            LightBrightness-= 5;

        Log.d("RobotControl", "Brightness = " + LightBrightness );
        mRobotAPI.wheelLights.setBrightness(WheelLights.Lights.SYNC_BOTH, 0xff, LightBrightness, true);
        expressionLevel();
    }

    private void expressionLevel(){
        switch (LightBrightness){
            case 0: mRobotAPI.robot.setExpression(RobotFace.LAZY);break;
            case 5:mRobotAPI.robot.setExpression(RobotFace.HELPLESS);break;
            case 15:mRobotAPI.robot.setExpression(RobotFace.TIRED);break;
            case 25:mRobotAPI.robot.setExpression(RobotFace.DEFAULT);break;
            case 35:mRobotAPI.robot.setExpression(RobotFace.ACTIVE);break;
            case 55: mRobotAPI.robot.setExpression(RobotFace.SINGING);break;
        }
    }

}
