package anviinfotechs.hartikandharparivar;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public  class TimerActivity extends Service {
    private final static String TAG = "BroadcastService";

    public static final String COUNTDOWN_BR = "com.example.loginapplication";
    Intent bi = new Intent(COUNTDOWN_BR);

    CountDownTimer cdt = null;

    @Override
    public void onCreate() {
        super.onCreate();


        cdt = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                bi.putExtra("countdown", millisUntilFinished);
                sendBroadcast(bi);
            }

            @Override
            public void onFinish() {
            }
        };

        cdt.start();
    }

    @Override
    public void onDestroy() {

        cdt.cancel();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}