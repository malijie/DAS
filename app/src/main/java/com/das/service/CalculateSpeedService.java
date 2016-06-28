package com.das.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import java.util.List;

public class CalculateSpeedService extends Service {

    private LocationManager locationManager;
    private String provider;
    private Location location;
    private boolean quit;
    private String GPSStatus;
    float Speed;
    float formerspeed;
    float xx ;
    float yy ;


    public CalculateSpeedService() {
    }

    private MyBinder binder = new MyBinder();

    public class MyBinder extends Binder{

        public float getSpeed(){
            return Speed;
        }

        public String getGPSStatus() {
            return GPSStatus;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
       // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        System.out.println("Service is Binded");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service is Created");

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (!quit) {
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    List<String> providerList = locationManager.getProviders(true);
                    if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                        provider = LocationManager.GPS_PROVIDER;
                    } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                        provider = LocationManager.NETWORK_PROVIDER;
                    } else {
                        GPSStatus = "NULL";
                        //    statusTextView.setText("NULL");
                        //    Toast.makeText(getActivity(), "No Location Provider", Toast.LENGTH_LONG).show();
                    }

                    location = locationManager.getLastKnownLocation(provider);

                    if (location != null) {
                        showLocation(location);
                    } else if (location == null) {
                        //   statusTextView.setText("null");
                        location = locationManager.getLastKnownLocation(provider);
                    }

               //     locationManager.requestLocationUpdates(provider, 1000, 0, locationLinstener);

                    LocationListener locationLinstener = new LocationListener() {
                        @Override
                        public void onLocationChanged(android.location.Location location) {
                            showLocation(location);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                            switch (status) {
                                case LocationProvider.AVAILABLE:
                                    GPSStatus = "GPS状态：可见";
                                    //    statusTextView.setText("GPS状态：可见");
                                    break;
                                case LocationProvider.OUT_OF_SERVICE:
                                    GPSStatus = "GPS状态：服务区外";
                                    //    statusTextView.setText("GPS状态：服务区外");
                                    break;
                                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                                    GPSStatus = "GPS状态：暂停服务";
                                    //    statusTextView.setText("GPS状态：暂停服务");
                                    break;
                            }

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    };
                    locationManager.requestLocationUpdates(provider, 1000, 0, locationLinstener);

                }
            }
            }).start();
        }





    private void showLocation(Location location){

        float x =(float) location.getLatitude();
        float y =(float) location.getLongitude();
        double Xspeed = 111110 * Math.cos(x) * (x - xx);
        double Yspeed = 111110 * (y - yy);
        float speed =(float) Math.sqrt(Xspeed*Xspeed + Yspeed*Yspeed);

        if(Math.abs(speed - formerspeed)>50){
            speed = formerspeed;
        }

        Speed = speed;

    //    String currentPosition = "lat:" +x + " " + xx + " "+ (x-xx) + "\n" +
    //            "lon:" + y +" "+ yy +" "+(y-yy) + "\n" + "速度" + 3.6*Speed;
    //    positionTextView.setText(currentPosition);

    /*  Myapp myapp=  ((Myapp)getActivity().getApplicationContext());
        myapp.setMyspeed(Speed);

        long sysTime = System.currentTimeMillis();
        CharSequence sysTimeStr = DateFormat.format(" yyyy-MM-dd    HH:mm:ss ", sysTime);

        SharedPreferences preferences = getActivity().getSharedPreferences("speedrecord", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat((String)sysTimeStr,Speed);
        editor.commit();*/

        formerspeed = speed;
        xx = x;   yy = y;
      //  return speed;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        System.out.println("Service is Unbinded");
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        this.quit = true;
        System.out.println("Service is Destroyed");
    }


}
