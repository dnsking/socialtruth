package com.app.socialtruth;

import android.app.Application;
import android.util.Log;

public class App extends Application {
    public static String Content = "Content";
    public static String Content2 = "Content2";
    public static String Url = "https://55fpgk1qu9.execute-api.us-east-1.amazonaws.com/SocialTruthStage";

    public static boolean IsDebug = true;
    private static final String TAG = "Social Truth App";
    public static void Log(String msg){
        if(App.IsDebug){
            int maxLogSize = 1000;
            if(msg.length()>maxLogSize)
                for(int i = 0; i <= msg.length() / maxLogSize; i++) {
                    int start = i * maxLogSize;
                    int end = (i+1) * maxLogSize;
                    end = end > msg.length() ? msg.length() : end;
                    Log.i(TAG, msg.substring(start, end));
                }
            else
                Log.i(TAG, msg);
        }
    }
}
