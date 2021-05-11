package com.example.oldphonedisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION = "com.example.oldphonedisplay";
    private static Handler handler;
    final String LOG_TAG = "==FullscreenActivity==";
    TextView textView2;
    TextView textView4;
    TextView mTimer;
    RadioButton Level0, Level1, Level2, Level3, Level4, Level5;
    ImageButton imageButton2, imageButton3, imageButton4, imageButton7;

    private SensorManager sensorManager;
    private Sensor lightSensor;

    int counter = 0;
    String box;
    String L5;
    String L4;
    String L3;
    String L2;
    String L1;
    String L0;

    public static void sendfromConnectionWorker(Intent intent) {
        String massagefromConnectionWorker = intent.getStringExtra("msg");
        Message massg = new Message();
        massg.obj = massagefromConnectionWorker;
        handler.sendMessage(massg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);



        textView2 = (TextView) findViewById(R.id.textView2);
        textView4 = (TextView) findViewById(R.id.textView4);
        mTimer = (TextView) findViewById(R.id.textView5);

        Level5 = (RadioButton) findViewById(R.id.radioButton5);
        Level4 = (RadioButton) findViewById(R.id.radioButton4);
        Level3 = (RadioButton) findViewById(R.id.radioButton3);
        Level2 = (RadioButton) findViewById(R.id.radioButton2);
        Level1 = (RadioButton) findViewById(R.id.radioButton1);
        Level0 = (RadioButton) findViewById(R.id.radioButton0);
        //   imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
        //   imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
        //   imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
        //   imageButton7 = (ImageButton) findViewById(R.id.imageButton7);



        // For receive massage from sendfromConnectionWorker through Handler
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String text = (String) msg.obj;
                GetData(text);


                PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG:");
                wakeLock.acquire(60000);

                KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG:");
                keyguardLock.disableKeyguard();

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                //     BrightnessControl(50);
                mytimer1();

                //   wakeUp();                            //===========================================================
            }
        };

        new Server();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        //delayedHide(5000);

    }



    void GetData(String content) {
        Log.d(LOG_TAG, "content  = " + content);
        if (content.length() > 2){

//        in1=22.3&in2=22.3&in3=22.3&in4=22.3&in5=22.3&in6=22.3&in7=22.3&in8=22.3&lev0=1&lev1=1&lev2=1&lev3=1&lev4=1&lev5=1&open=1&movement=1&guard=1&poweron=1

            //   webView.loadData(content, "text/html; charset=utf-8", "utf-8");
//            Toast.makeText(getActivity(), "Данные загружены", Toast.LENGTH_SHORT).show();

            int index1, index2;
            int index_tin_1 = content.indexOf("in7") + 4;
            int index_tin_2 = content.indexOf("&in8");
            int index_out_1 = content.indexOf("in1") + 4;
            int index_out_2 = content.indexOf("&in2");

            String tin;
            String tout;

            if(index_tin_1>0 && index_tin_2>0) {
                tin = content.substring(index_tin_1, index_tin_2);
            }else{
               tin = "no data";
            }
            if(index_out_1>0 && index_out_2>0) {
                tout = content.substring(index_out_1, index_out_2);
            }else{
                tout = "no data";
            }
            //int indexbox = content.indexOf("heatingbox=") + 13;
            int indexL5 = content.indexOf("lev5") + 5;
            int indexL4 = content.indexOf("lev4") + 5;
            int indexL3 = content.indexOf("lev3") + 5;
            int indexL2 = content.indexOf("lev2") + 5;
            int indexL1 = content.indexOf("lev1") + 5;
            int indexL0 = content.indexOf("lev0") + 5;

            //box = content.substring(indexbox, (indexbox + 1));
            L5 = content.substring(indexL5, (indexL5 + 1));
            L4 = content.substring(indexL4, (indexL4 + 1));
            L3 = content.substring(indexL3, (indexL3 + 1));
            L2 = content.substring(indexL2, (indexL2 + 1));
            L1 = content.substring(indexL1, (indexL1 + 1));
            L0 = content.substring(indexL0, (indexL0 + 4));

            //    Log.d(LOG_TAG, "L5  = " + L5);
            //   Log.d(LOG_TAG, "L4  = " + L4);
     /*
        int indextout = content.indexOf("Т наружн") + 12;
        int indextout2 = content.indexOf("<br>H наружн") - 2;
        tout = content.substring(indextout, indextout2);
        int indextheat = content.indexOf("Т отопления") + 14;
        theat = content.substring(indextheat, (indextheat + 4));
        int indexL5 = content.indexOf("Level5") + 9;
        int indexL4 = content.indexOf("Level4") + 9;
        int indexL3 = content.indexOf("Level3") + 9;
        int indexL2 = content.indexOf("Level2") + 9;
        int indexL1 = content.indexOf("Level1") + 9;
        int indexL0 = content.indexOf("Level0") + 9;
        int indexguardstatus = content.indexOf("Guard=") + 8;
        int indexguarddata = content.indexOf("Guard=") + 15;
        int indexpumpe = content.indexOf("Pumpe=") + 8;
        int indexwatering = content.indexOf("Watering=") + 11;
        int indexwateringtime0 = content.indexOf("полив сегодня");
        int indexwateringtime1 = content.indexOf(":", indexwateringtime0) + 1;
        int indexwateringtime = content.indexOf(":", indexwateringtime1) + 2;
        int indexwateringtime2 = content.indexOf("<", indexwateringtime);

        int indexledlight = content.indexOf("ledlight=") + 11;
        int indexbox = content.indexOf("heatingbox=") + 13;
        int indexmodeli = content.indexOf("modeli=") + 9;
        int indexmodehi = content.indexOf("modehi=") + 9;
        int indexdoor = content.indexOf("door=") + 7;

        box = content.substring(indexbox, (indexbox + 1));
        L5 = content.substring(indexL5, (indexL5 + 1));
        L4 = content.substring(indexL4, (indexL4 + 1));
        L3 = content.substring(indexL3, (indexL3 + 1));
        L2 = content.substring(indexL2, (indexL2 + 1));
        L1 = content.substring(indexL1, (indexL1 + 1));
        L0 = content.substring(indexL0, (indexL0 + 4));
        guardstatus = content.substring(indexguardstatus, (indexguardstatus + 1));
        if (guardstatus.charAt(0) == '2') {
            guarddata = content.substring(indexguarddata, (indexguarddata + 20));
        } else {
            guarddata = "";
        }
        char firstChar = L5.charAt(0);
        if (firstChar == '0') {

        }
        pumpe = content.substring(indexpumpe, (indexpumpe + 1));
        watering = content.substring(indexwatering, (indexwatering + 1));
        wateringtime = content.substring(indexwateringtime, indexwateringtime2);
        ledlight = content.substring(indexledlight, (indexledlight + 1));
        modeli = content.substring(indexmodeli, (indexmodeli + 1));
        modehi = content.substring(indexmodehi, (indexmodehi + 1));
        modehi = content.substring(indexmodehi, (indexmodehi + 1));
        heatingbox = content.substring(indexbox, (indexbox + 1));
        door = content.substring(indexdoor, (indexdoor + 1));

        //Log.d(LOG_TAG, "GetData answer all= "+content);
        //Log.d(LOG_TAG, "wateringtime="+wateringtime);
        //Log.d(LOG_TAG, "watering="+watering);
        //Log.d(LOG_TAG, "GetData answer: heatingbox=" + heatingbox);

*/
            textView2.setText(tin);
            textView4.setText(tout);

            //    if (box.charAt(0) == '1') {
            //        imageButton4.setImageResource(R.drawable.draw_imagebutton_freezing2);
            //    } else {
            //       imageButton4.setImageResource(R.drawable.draw_imagebutton_freezing0);
            //    }
            if (L5.charAt(0) == '1') {
                Level5.setVisibility(View.VISIBLE);
            } else {
                Level5.setVisibility(View.INVISIBLE);
            }
            if (L4.charAt(0) == '1') {
                Level4.setVisibility(View.VISIBLE);
            } else {
                Level4.setVisibility(View.INVISIBLE);
            }
            if (L3.charAt(0) == '1') {
                Level3.setVisibility(View.VISIBLE);
            } else {
                Level3.setVisibility(View.INVISIBLE);
            }
            if (L2.charAt(0) == '1') {
                Level2.setVisibility(View.VISIBLE);
            } else {
                Level2.setVisibility(View.INVISIBLE);
            }
            if (L1.charAt(0) == '1') {
                Level1.setVisibility(View.VISIBLE);
            } else {
                Level1.setVisibility(View.INVISIBLE);
            }
            if (L0.charAt(0) == '1') {
                Level0.setVisibility(View.VISIBLE);
            } else {
                Level0.setVisibility(View.INVISIBLE);
            }

            //    if (Integer.parseInt(L0.trim()) < 1000) {
            //        Level0.setVisibility(View.VISIBLE);
            //        imageButton5.setImageResource(R.drawable.leakage80);

            //    } else {
            //        Level0.setVisibility(View.INVISIBLE);
            //        imageButton5.setImageResource(R.drawable.leakage80_no);
            //    }

        }

    }

    void mytimer1(){
        counter++;

        //Создаем таймер обратного отсчета на 20 секунд с шагом отсчета
        //в 1 секунду (задаем значения в миллисекундах):
        new CountDownTimer(20000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                //mTimer.setText("Осталось: " + millisUntilFinished / 1000);
            }
            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {
                counter--;
                if(counter == 0) {

                    PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
                    PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG:");
                    //wakeLock.acquire();
                    //      wakeLock.acquire(10000);

                    //mTimer.setText("Бабах!");
                    //  BrightnessControl(0);       //=====================================================

                }
            }
        }
                .start();
    }




}