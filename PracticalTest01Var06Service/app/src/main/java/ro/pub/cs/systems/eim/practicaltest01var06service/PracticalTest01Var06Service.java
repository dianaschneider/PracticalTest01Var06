package ro.pub.cs.systems.eim.practicaltest01var06service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class PracticalTest01Var06Service extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String scor = intent.getStringExtra(Constants.SERVICE_DATA);
            Intent action = new Intent();
            action.setAction(Constants.SERVICE_ACTION);
            action.putExtra(Constants.SERVICE_DATA, "Victory " + scor);
            getApplicationContext().sendBroadcast(action);
        }
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}