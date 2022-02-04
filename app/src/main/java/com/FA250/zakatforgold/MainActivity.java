package com.FA250.zakatforgold;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //input
    private EditText goldWeight;
    private RadioButton useWear;
    private RadioButton useKeep;
    private EditText goldValue;

    //output
    private TextView goldValueOut;
    private TextView urufOut;
    private TextView payableOut;
    private TextView totalOut;

    //var
    double goldValueCalc;
    double weight;
    double price;
    double uruf;
    double zakatPayable;
    double totalZakat;
    int pick = 0;
    String empty = "";

    Button b;

    private static final String tag = "MyActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findTv();
        calcValue();

        b = findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(MainActivity.this,AboutMe.class);
                startActivity(launch);
            }
        });
    }

    public void findTv()
    {
        SharedPreferences prefs = this.getSharedPreferences("Data", Context.MODE_PRIVATE);

        goldWeight = (EditText) findViewById(R.id.inputWeight);
        goldValue = (EditText) findViewById(R.id.inputValue);

        goldValueOut = (TextView) findViewById(R.id.out1);
        urufOut = (TextView) findViewById(R.id.out2);
        payableOut = (TextView) findViewById(R.id.out3);
        totalOut = (TextView) findViewById(R.id.out4);

        useWear = (RadioButton) findViewById(R.id.wearButton);
        useKeep = (RadioButton) findViewById(R.id.keepButton);

        boolean wearA = prefs.getBoolean("WearButton",false);
        boolean useA = prefs.getBoolean("KeepButton",false);

        //Log.i("Wear",String.valueOf(wearA) );
        //Log.i("Use",String.valueOf(useA) );




        float a = prefs.getFloat("GoldValue",0);
        float b = prefs.getFloat("GoldWeight",0);

        goldValue.setText(String.valueOf(a));
        goldWeight.setText(String.valueOf(b));

        if(wearA)
        {
            useWear.setChecked(true);
            calcGold(200);
            pick = 1;
        }
        else if(useA)
        {
            useKeep.setChecked(true);
            calcGold(85);
            pick = 2;
        }
    }


    public void calcValue()
    {
        SharedPreferences prefs = this.getSharedPreferences("Data", Context.MODE_PRIVATE);

        goldWeight = (EditText) findViewById(R.id.inputWeight);
        goldValue = (EditText) findViewById(R.id.inputValue);

        goldWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ApplySharedPref")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(goldWeight.getText().toString().trim().equals(""))
                {
                    notificationDialog("Missing gold weight" , "Please input your gold weight");
                    int ze = 0;
                    goldValueOut.setText(String.valueOf(ze));
                    urufOut.setText(String.valueOf(ze));
                    payableOut.setText(String.valueOf(ze));
                    totalOut.setText(String.valueOf(ze));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putFloat("GoldWeight", 0.0f);
                    editor.apply();
                }
                else if(!goldWeight.getText().toString().trim().equals("") && !goldValue.getText().toString().trim().equals(""))
                {
                    if (pick == 1)
                    {
                        calcGold(200);
                    }
                    if (pick == 2)
                    {
                        calcGold(85);
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putFloat("GoldWeight", (float) Double.parseDouble(goldWeight.getText().toString()));
                    editor.apply();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim().length()==0)
                {
                    //notificationDialog("Missing char2" , "miss");
                }
            }
        });

        goldValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ApplySharedPref")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(goldValue.getText().toString().trim().equals(""))
                {
                    notificationDialog("Missing gold value" , "Please input your gold value");
                    int ze = 0;
                    goldValueOut.setText(String.valueOf(ze));
                    urufOut.setText(String.valueOf(ze));
                    payableOut.setText(String.valueOf(ze));
                    totalOut.setText(String.valueOf(ze));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putFloat("GoldValue", 0.0f);
                    editor.apply();
                }
                else if(!goldWeight.getText().toString().trim().equals("") && !goldValue.getText().toString().trim().equals(""))
                {
                    if(pick == 1)
                    {
                        calcGold(200);
                    }
                    else if(pick == 2)
                    {
                        calcGold(85);
                    }
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putFloat("GoldValue", (float) Double.parseDouble(goldValue.getText().toString()));
                    editor.apply();
                }


            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if(editable.toString().trim().length()==0)
                {
                    //notificationDialog("Missing char1" , "miss");
                }
            }

        });

    }

    public void onRadioButtonClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        String testWeight = goldWeight.getText().toString();
        String testValue = goldValue.getText().toString();



        //calculation
        switch (view.getId())
        {
            case R.id.wearButton:
            {
                if(checked)
                {
                    if(testWeight.matches("") && testValue.matches(""))
                    {
                        //notificationDialog("Both values are missing" , "Please input your gold weight and value!");
                    }
                    else if(testWeight.matches(""))
                    {
                        //notificationDialog("Gold weight are missing" , "Please input your gold weight");
                    }
                    else if(testValue.matches(""))
                    {
                        //notificationDialog("Gold Value are missing" , "Please input your gold value");
                    }
                    else
                    {
                        pick = 1;
                        calcGold(200);

                        SharedPreferences prefs = this.getSharedPreferences("Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("WearButton", true);
                        editor.putBoolean("KeepButton", false);
                        editor.apply();
                    }
                    break;
                }
            }
            case R.id.keepButton:
            {
                if(checked)
                {
                    if(testWeight.matches("") && testValue.matches(""))
                    {
                        //notificationDialog("Both values are missing" , "Please input your gold weight and value!");
                    }
                    else if(testWeight.matches(""))
                    {
                        //notificationDialog("Gold weight are missing" , "Please input your gold weight");
                    }
                    else if(testValue.matches(""))
                    {
                        //notificationDialog("Gold Value are missing" , "Please input your gold value");
                    }
                    else
                    {
                        pick = 1;
                        calcGold(85);

                        SharedPreferences prefs = this.getSharedPreferences("Data", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("WearButton", false);
                        editor.putBoolean("KeepButton", true);
                        editor.apply();
                    }
                    break;
                }
            }
        }


    }

    public void calcGold(double con)
    {
        //gold value
        weight = Double.parseDouble(goldWeight.getText().toString());
        price = Double.parseDouble(goldValue.getText().toString());
        goldValueCalc = (weight * price);

        //Uruf
        uruf = weight - con;

        //zakat payable
        if(uruf > 0)
        {
            zakatPayable = (uruf * price);
        }
        else
        {
            zakatPayable = 0;
        }

        //total zakat
        totalZakat = zakatPayable * 0.025;

        if(weight <= 0 && price <= 0)
        {
            goldValueCalc = 0;
            uruf = 0;
            zakatPayable = 0;
            totalZakat = 0;
        }

        PrintGold();
    }

    public void PrintGold()
    {
        //gold value

        String test1 = Double.toString(goldValueCalc);
        goldValueOut.setText(test1);
        String test2 = Double.toString(uruf);
        urufOut.setText(test2);
        String test3 = Double.toString(zakatPayable);
        payableOut.setText(test3);
        String test4 = Double.toString(totalZakat);
        totalOut.setText(test4);

    }

    //Notification
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "FA_250";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel
                    = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content)
                .setContentInfo("Information");
        notificationManager.notify(1, notificationBuilder.build());
    }

    private void AboutMeLink(View v)
    {
        Intent in = new Intent(MainActivity.this, AboutMe.class);
        startActivity(in);
    }

}