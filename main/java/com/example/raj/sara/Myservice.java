package com.example.raj.sara;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by raj on 5/6/2017.
 */

public class Myservice extends Service {

    LocationManager mlocationmanager = null;
    private static final int LOCATION_INTERVAL = 0;
    private static final float LOCATION_DISTANCE = 10000;


    private class LocationListener implements android.location.LocationListener {

        Location mylocation;

        public LocationListener(String proivder) {
            Log.e("salam", "LocationListerner" + proivder);
            mylocation = new Location(proivder);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e("salam", "Location Changed" + location);
            mylocation.set(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e("salam", "Onstatus Changed" + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e("salam", "Onproviderenabled" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e("salam", "On provider disabled" + provider);
        }
    }

    LocationListener[] mlocationlistener = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("salam", "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("salam", "onCreate");
        initializeLocationManager();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(Myservice.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Myservice.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mlocationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mlocationlistener[1]);
        mlocationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mlocationlistener[0]);



        }





    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("salam","Ondestroy");
        if (mlocationmanager != null) {
            for (int i = 0; i < mlocationlistener.length; i++) {
                try {
                    mlocationmanager.removeUpdates(mlocationlistener[i]);
                } catch (Exception ex) {
                    Log.i("salam", "fail to remove location listners, ignore", ex);
                }
            }
        }
    }


    private void initializeLocationManager() {
        Log.e("salam", "initializeLocationManager");
        if (mlocationmanager == null) {
            mlocationmanager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
