package com.das.manager;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.das.Myapp;
import com.das.util.Logger;

import java.util.List;

/**
 * Created by malijie on 2016/7/4.
 */
public class BaiduLocationManager {
    private static final String TAG = BaiduLocationManager.class.getSimpleName();
    private static final Object sObject = new Object();
    private BDLocation mLocation;
    private double mCurrentLatitude;
    private double mCurrentLongitude;
    private float mCurrentSpeed;

    private static BaiduLocationManager mLocationManager = null;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private BaiduLocationManager(){
        initLocationConfig();
    }

    public static BaiduLocationManager getInstance(){
        if (mLocationManager == null){
            synchronized (sObject){
                if(mLocationManager == null){
                    mLocationManager = new BaiduLocationManager();
                }
            }
        }
        return mLocationManager;
    }

    public void initLocationConfig(){
        mLocationClient = new LocationClient(Myapp.sContext);     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        initLocation();
    }

    public void start(){
        if(mLocationManager != null){
            mLocationClient.start();
        }
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setEnableSimulateGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setPriority(LocationClientOption.GpsFirst);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            mLocation = location;
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

                mCurrentLatitude = location.getLatitude();
                mCurrentLongitude = location.getLongitude();
                mCurrentSpeed = location.getSpeed();
                if(mCurrentSpeed <0){
                    mCurrentSpeed = 0;
                }

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
                mLocationClient.start();
                mCurrentLatitude = location.getLatitude();
                mCurrentLongitude = location.getLongitude();
                mCurrentSpeed = location.getSpeed();


            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                mLocationClient.start();
                mLocationClient.requestLocation();

//                mCurrentSpeed = getLastKnownSpeed();
//                mCurrentLongitude = getLastKnownLongitude();
//                mCurrentLatitude = getLastKnownLatitude();



            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }


            Log.i("BaiduLocationApiDem", sb.toString() );
            Logger.w(TAG,"getLocType()=" + location.getLocType()
                    + ",speed=" +  mCurrentSpeed
                    + ",lat" + mCurrentLatitude
                    + ",long" + mCurrentLongitude);

        }
    }

    public double getCurrentLatitude(){
        return mCurrentLatitude;
    }

    public double getCurrentLongitude(){
        return  mCurrentLongitude;
    }

    public float getCurrentSpeed(){
        return mCurrentSpeed;
    }

    private float getLastKnownSpeed(){
        if(mLocationClient == null){
            return 0;
        }
        return mLocationClient.getLastKnownLocation().getSpeed();
    }

    private double getLastKnownLatitude(){
        if(mLocationClient == null){
            return 0;
        }
        return mLocationClient.getLastKnownLocation().getLatitude();
    }

    private double getLastKnownLongitude(){
        if(mLocationClient == null){
            return 0;
        }
        return mLocationClient.getLastKnownLocation().getLongitude();
    }

}
