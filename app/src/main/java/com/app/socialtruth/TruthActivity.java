package com.app.socialtruth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.app.socialtruth.actions.AddTruthAction;
import com.app.socialtruth.helpers.SocialTruthNetworkHelper;

import java.io.File;

public class TruthActivity extends AppCompatActivity {

    private String imgPath,imgPath2;

    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        imgPath = getIntent().getStringExtra(App.Content);
        imgPath2= getIntent().getStringExtra(App.Content2);

        setContentView(R.layout.activity_truth);



        imgView =  findViewById(R.id.imgView);

        Bitmap bm = BitmapFactory.decodeFile((imgPath2));
       imgView.setImageBitmap( cropBitmap1( bm,getNavigationBarHeight()));

        searchTruth();
    }

    private Bitmap cropBitmap1( Bitmap bmp2,int height)
    {

       return Bitmap.createBitmap(
                bmp2,
                0,0,
                bmp2.getWidth(),
                bmp2.getHeight()-height
        );
    }
    private void searchTruth(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                  final  AddTruthAction addTruthAction  = SocialTruthNetworkHelper.SearchTruth(imgPath);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult(addTruthAction);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showResult( new AddTruthAction());
                        }
                    });
                }
            }
        }).start();
    }
    private void showResult(AddTruthAction addTruthAction){

        ResultFragment fragment =
                ResultFragment.newInstance(addTruthAction);
        fragment.show(getSupportFragmentManager(),
                "add_photo_dialog_fragment");
        /*fragment.onDismiss(new DialogInterface() {
            @Override
            public void cancel() {
                finish();
            }

            @Override
            public void dismiss() {
                finish();

            }
        });*/
       // ViewAnimationUtils.createCircularReveal()
    }

    public int getNavigationBarHeight()
    {
        boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0 && !hasMenuKey)
        {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

}
