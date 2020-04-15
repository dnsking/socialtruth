package com.app.socialtruth.voice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.service.voice.VoiceInteractionSession;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.socialtruth.App;
import com.app.socialtruth.TruthActivity;
import com.app.socialtruth.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

public class InteractionSession extends VoiceInteractionSession {
    public InteractionSession(Context context) {
        super(context);
    }

    public InteractionSession(Context context, Handler handler) {
        super(context, handler);
    }


    @Override
    public void onHandleScreenshot(@Nullable Bitmap screenshot) {
        //super.onHandleScreenshot(screenshot);
        if(screenshot!=null){
            File dir = Utils.FetchScreenShotsDir(getContext());
            File file = new File(dir, new Date().getTime()+".png");
            File file2 = new File(dir, new Date().getTime()+"_.png");
            try {
                Bitmap newBitmap = Utils.Resize(screenshot,600,600);
                newBitmap.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file));
                screenshot.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(file2));

                Intent intent = new Intent(getContext(), TruthActivity.class);
                intent.putExtra(App.Content,file.getAbsolutePath());
                intent.putExtra(App.Content2,file2.getAbsolutePath());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                hide();
                getContext().startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        hide();
    }
}
