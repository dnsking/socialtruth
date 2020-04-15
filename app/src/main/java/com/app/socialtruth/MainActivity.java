package com.app.socialtruth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.role.RoleManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.socialtruth.utils.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Calendar;

import io.github.cdimascio.essence.Essence;
import io.github.cdimascio.essence.EssenceResult;

import static android.app.role.RoleManager.ROLE_ASSISTANT;
import static android.app.role.RoleManager.ROLE_DIALER;

public class MainActivity extends AppCompatActivity {

    private View content,asker;
    private ExtendedFloatingActionButton makeDefault,addFalseReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_main);

        addFalseReport = findViewById(R.id.addFalseReport);
        makeDefault = findViewById(R.id.makeDefault);
        asker = findViewById(R.id.asker);
        content = findViewById(R.id.content);

        checkDefaultAssistant();
        makeDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerReplacingDefaultDialer();
            }
        });

        addFalseReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFalseReport();
            }
        });
       // test();

    }
    private void test(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    EssenceResult data = Essence.extract(Utils.UrlToHtml("https://www.factcheck.org/2020/03/gargling-water-with-salt-wont-eliminate-coronavirus/"));
                   App.Log("result "+data.getText());
                    App.Log("title "+data.getTitle());

                }
                catch (Exception ex){

                }
            }
        }).start();
    }
    private void addFalseReport(){
        Intent intent = new Intent(this,AddTruthActivity.class);
        startActivity(intent);

    }

    private void offerReplacingDefaultDialer() {
        startActivityForResult(new Intent(android.provider.Settings. ACTION_VOICE_INPUT_SETTINGS), 0);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkDefaultAssistant();
    }

    public ComponentName getCurrentAssist(Context context) {
        final String setting = Settings.Secure.getString(context.getContentResolver(), "assistant");

        App.Log("getCurrentAssist setting "+setting);
        if (setting != null) {
            return ComponentName.unflattenFromString(setting);
        }

        return null;
    }

    public ComponentName getCurrentAssistWithReflection(Context context) {
        try {
            Method myUserIdMethod = UserHandle.class.getDeclaredMethod("myUserId");
            myUserIdMethod.setAccessible(true);
            Integer userId = (Integer) myUserIdMethod.invoke(null);

            if (userId != null) {
                Constructor constructor = Class.forName("com.android.internal.app.AssistUtils").getConstructor(Context.class);
                Object assistUtils = constructor.newInstance(context);

                Method getAssistComponentForUserMethod = assistUtils.getClass().getDeclaredMethod("getAssistComponentForUser", int.class);
                getAssistComponentForUserMethod.setAccessible(true);
                return (ComponentName) getAssistComponentForUserMethod.invoke(assistUtils, userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    private void checkDefaultAssistant(){
        try{


            if (!getCurrentAssist(this).getPackageName().equals(getPackageName())
            ) {

                asker.setVisibility(View.VISIBLE);
                content.setVisibility(View.GONE);


            }
            else{

                asker.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);

            }
        }
        catch (Exception ex){

            asker.setVisibility(View.GONE);
            content.setVisibility(View.VISIBLE);
        }
    }
}
