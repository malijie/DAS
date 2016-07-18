package com.das.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.das.constants.MsgConstant;
import com.das.control.TrainConstants;
import com.das.control.TrainControl;
import com.das.db.DBConfig;
import com.das.db.DBManager;
import com.das.constants.IntentConstants;
import com.das.manager.IntentManager;
import com.das.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by malijie on 2016/7/16.
 */
public class SimulatorService extends Service{
    private static final String TAG = SimulatorService.class.getSimpleName();
    private double mTotalMileage = 0;

    private double[][] mGradient;
    private double[][] mVel_profile;
    private double[] mStation_info;

    private ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private TrainControl mTrainControl = null;

    // Default vehicle values for CLASS 14x DMU   定义机车信息，相当于我们的读取车辆数据过程
    double Mass=49.5;
    double lambda=0.08;
    double inertial_mass=Mass*(1+lambda);
    double Davis[] = new double[] {1.35379143, 0.006369827, 0.004215268, 0};
    double Power=334*0.7*1000;
    double max_speed=75*1.6;
    int seats=121;
    // MAXIMUM TRACTION FORCE
    double co_fric=0.1;
    double gravity=9.81;
    double driving_wheel_Po=Mass*0.5;
    double max_traction=Mass * 1000 * 9.81 * co_fric * (driving_wheel_Po/Mass)/1000;
    // MAXIMUM ACCELERATION
    double max_accel=max_traction/Mass;
    // STATION DWELL TIME (Seconds)
    int dwell=30;
    // STATION TERMINAL TIME (Seconds)
    int Terminal_time=15*60;
    // proportional k
    double kd=0.5;
    // notch numbers
    int notch_num=5;

    //---------------------simulator data-------------------
    private double[] vel_limit;
    private double[] vel_error;
    private double[] grad_prof;
    private double[] TractionF;
    private double[] TractionB;
    private double[] EnergyF;
    private double[] EnergyB;
    private double[] Notch;
    private double[] acceleration;
    private double[] acceler;
    private double[] accF;
    private double[] accB;
    private double[] T;
    private double[] vel;
    private double[] del_T;



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d(TAG,"SimulatorService onCreate");

        mGradient =  DBManager.getInstance().read2DArrayFromTable(DBConfig.TABLE_GRADIENTS);
        mVel_profile = DBManager.getInstance().read2DArrayFromTable(DBConfig.TABLE_VELOCITY);
        mStation_info = DBManager.getInstance().read1DArrayFromTable(DBConfig.TABLE_STATION_INFO);

        mTrainControl = TrainControl.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Logger.d(TAG,"SimulatorService action=" + action);
        if(action.equals(IntentConstants.ACTION_START_SIMULATE)){
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    int train_control=1;
                     //v[i] 表示以m/s为单位的速度表
                    double[] v = new double[(int)max_speed];
                    for(int i=1; i<=max_speed; i++) {
                        v[i-1] = i / 3.6;
                    }

                    //trac=Power./(v)/(Mass*1000);  计算牵引加速度表trac[i]
                    double[] trac = new double[v.length];
                    for(int i=1; i<=v.length; i++) {
                        trac[i-1] = Power/v[i-1]/(Mass*1000);
                    }

                    //Res=zeros(1,length(v));    计算制动加速度表Res[i]
                    double[] Res = new double[v.length];
                    for(int i=1; i<=Res.length; i++) {
                        Res[i-1] = 0;
                    }

                    for(int i=1; i<=v.length; i++) {
                        if(trac[i-1] > max_accel) {
                            trac[i-1] = max_accel;
                        }
                        Res[i-1] = Davis[0] + v[i-1] * Davis[1] + ((v[i-1] * v[i-1]) * Davis[2]);
                    }

                    //Res=Res./Mass;
                    for(int i=1; i<=v.length; i++) {
                        Res[i-1] /= Mass;
                    }

                    //accel=trac-Res;     总加速度表accel[i]
                    double[] accel = new double[v.length];
                    for(int i=1; i<=v.length; i++) {
                        accel[i-1] = trac[i-1] - Res[i-1];
                    }

                    // Students to modify code below     惰行速度
                    double Coasting = 0;
                    double Coasting_vel = 80*1.6/3.6;

                    int S_min = 0;

                    //S_max=max(mVel_profile(:,1))*1000;   最大里程S_max，单位m
                    double S_max = Double.MIN_VALUE;
                    for(int i = 0; i< mVel_profile.length; i++) {
                        S_max = Math.max(S_max, mVel_profile[i][0]);
                    }
                    S_max *= 1000;

                    int del_S = 10;

                    //s=S_min:del_S:S_max;   里程表s[i]， 10m一个单位
                    int[] s = new int[(int)(Math.floor(S_max) / del_S) + 1];
                    for(int i=0; i<s.length; i++) {
                        s[i] = del_S * i;
                    }

                    int size = s.length;

                    initData(s.length);

                    //%DETERMINE VEL LIMIT FROM INPUT FILE  读取限速文件
                    int pos = 2;
                    for(int i=1; i<size; i++) {
                        vel_limit[i-1] = mVel_profile[pos-2][2-1]/3.6;
                        if(vel_limit[i-1]>=max_speed/3.6) {
                            vel_limit[i-1]=max_speed/3.6;
                        }
                        if(i*del_S >= Math.floor(mVel_profile[pos-1][0]*1000)) {
                            pos = pos + 1;
                        }
                    }

                    //%DETERMINE STATION STOPS FROM INPUT FILE  更新车站处限速
                    pos = 1;
                    double maxStationInfo = Integer.MIN_VALUE;
                    for(int i=0; i<mStation_info.length; i++) {
                        maxStationInfo = Math.max(maxStationInfo, mStation_info[i]);
                    }
                    for(int i = 1; i<= Math.floor(1000.0/del_S*maxStationInfo); i++) {
                        if(i*del_S>= Math.floor(mStation_info[pos-1]*1000)) {
                            vel_limit[i-1]=3.0/dwell*del_S;
                            vel_limit[i] = 3.0/dwell*del_S;
                            pos++;
                        }
                    }

                    //更新终点站处限速
                    pos = 1;

                    //计算坡度加速度表
                    pos=2;
                    double maxGradient = Integer.MIN_VALUE;
                    for(int i=0; i<mGradient.length; i++) {
                        maxGradient = Math.max(maxGradient, mGradient[i][0]);
                    }
                    for(int i = 1; i< Math.floor(1000.0/del_S*maxGradient); i++) {
                        grad_prof[i-1]=-mGradient[pos-2][1] / 1000.0 * 9.8 / (1+lambda);
                        if(i*del_S>= Math.floor(mGradient[pos-1][0]*1000)) {
                            pos++;
                        }
                    }

                    for(int i = 5; i< Math.floor(1000.0/del_S*maxGradient) - 4; i++) {
                        double gp = grad_prof[i-3];      // Bug because taking average of already overwritten values.
                        gp += grad_prof[i-2];
                        gp += grad_prof[i-1];
                        gp += grad_prof[i];
                        gp += grad_prof[i+1];
                        gp += grad_prof[i+2];
                        gp += grad_prof[i+3];
                        gp /= 7;
                        grad_prof[i] = gp;
                    }

                     //计算列车速度
                    double[] velF = new double[size];
                    for(int i=0; i<size-1; i++) {
                        velF[i] = 0.01;
                    }
                    for(int i=0; i<size-1; i++) {
                        double Resistance = (Davis[0] + (Davis[1]*velF[i]) + (Davis[2]* Math.pow(velF[i],2))) / inertial_mass;  //  列车阻力（空气阻力、摩擦力等）加速度
                        if(train_control==1) {
                            vel_error[i]=1;
                        } else if(train_control==2) {
                            vel_error[i] = kd*(vel_limit[i]-velF[i]);
                            if(vel_error[i]>1) {
                                vel_error[i]=1;
                            }
                            if(vel_error[i]<=0) {
                                vel_error[i]=0.1;
                            }
                        } else if(train_control==3) {
                        }

                        if(velF[i] < vel_limit[i]) {     //当前速度小于限速时
                            double Traction = Power*vel_error[i]/velF[i]/(inertial_mass*1000);  //  牵引加速度计算
                            if(Traction > max_accel) {
                                Traction = max_accel;
                            }
                            accF[i+1] = Traction-Resistance + grad_prof[i];  // 总牵引加速度
                            velF[i+1] = Math.pow(
                                    Math.pow(velF[i],2) + (2*accF[i+1]*del_S),
                                    0.5);                                       //牵引速度
                        } else {        //当前速度大于等于限速时
                            double Traction = Power*vel_error[i]/velF[i]/(inertial_mass*1000);
                            if(Traction > max_accel) {
                                Traction = max_accel;
                            }
                            accF[i+1] = Traction - Resistance + grad_prof[i];    // 总牵引加速度
                            if(accF[i+1]<0) {
                                velF[i+1] = Math.pow(
                                        Math.pow(velF[i],2) + (2*accF[i+1]*del_S),
                                        0.5);  //牵引速度
                                if(velF[i+1] > vel_limit[i]) {
                                    velF[i+1]=vel_limit[i];
                                }
                            } else {
                                accF[i+1]=0;
                                velF[i+1] = vel_limit[i];
                            }
                        }
                    }

                    double[] velB = new double[size];
                    for(int i=0; i<size-1; i++) {
                        velB[i] = 0.01;
                    }
                    for(int i = (int) Math.floor(S_max / del_S)-1; i>=0; --i) {
                        if(velB[i+1] < vel_limit[i+1]) {
                            double Resistance = (Davis[0] + (Davis[1]*velB[i+1]) + (Davis[2]* Math.pow(velB[i+1],2))) / inertial_mass;  //  列车阻力（空气阻力、摩擦力等）加速度
                            accB[i]=Resistance + brake(velB[i+1], max_accel, Power, inertial_mass, Coasting, Coasting_vel)- grad_prof[i+1];  //列车制动加速度（阻力加速度+制动加速度brake-坡度加速度）
                            velB[i] = Math.pow(
                                    Math.pow(velB[i+1],2) + (2*accB[i]*del_S),
                                    0.5);
                        } else {
                            velB[i] = vel_limit[i+1];
                        }
                    }

                    for(int i=0; i<size; i++) {   // 牵引速度和制动速度取小的一个值作为vel[i]优化后 速度表
                        if(velF[i] <= velB[i]) {   //代表牵引
                            acceler[i]=accF[i];
                            vel[i] = velF[i];
                        } else {				//代表制动
                            acceler[i]=-accB[i];
                            vel[i]=velB[i];
                        }
                    }

                    for(int i=0; i<size-1; i++) {     //列车运行时间表
                        del_T[i+1] = 2*del_S/(vel[i]+vel[i+1]);
                        if(vel[i]==0) {
                            T[i+1] = T[i]+1;
                        } else {
                            T[i+1]=T[i] + del_T[i+1];
                        }
                    }

                    //判断列车牵引、制动、惰行的拐点
                    for(int i=1; i<size-3; i++) {
                        double Resistance = (Davis[0] + Davis[1]*vel[i] + Davis[2]* Math.pow(vel[i],2)) / inertial_mass;
                        Notch[i] = ((acceler[i-1] + acceler[i-1]) / 2 + Resistance - grad_prof[i]) * vel[i]*inertial_mass *1000;
                        if(Notch[i]>0) {
                            TractionB[i]=0;
                        } else {
                            TractionB[i]=-Notch[i];
                        }
                        Notch[i] = ((acceler[i+1] + acceler[i+1]) / 2 + Resistance - grad_prof[i]) * vel[i]*inertial_mass *1000;
                        if(Notch[i]>0) {
                            TractionF[i] = Notch[i];
                        } else {
                            TractionF[i]=0;
                        }
                        del_T[i]=2*del_S/(vel[i]+vel[i+1]);
                    }

                    //每步长（10m）制动能耗表
                    for(int i=1; i<size-3; i++) {
                        EnergyF[i]=TractionF[i]/vel[i]*del_S;
                        if(TractionB[i]>Power) {
                            EnergyB[i]=Power/vel[i]*del_S;
                        } else {
                            EnergyB[i]=TractionB[i]/vel[i]*del_S;
                        }
                    }

                    //以步长（10m）为单位更新后的坡度表
                    double[] grad_1 = new double[size];
                    int apos = 1;
                    double maxGrad = 0;
                    for(int i=0; i<mGradient.length; i++) {
                        maxGrad = Math.max(maxGrad, mGradient[i][0]);
                    }
                    for(int i = 0; i< Math.floor(1000/del_S*maxGrad); i++) {
                        grad_1[i] = -mGradient[apos-1][1];
                        if((i+1)*del_S>= Math.floor(mGradient[apos][0] * 1000)) {
                            apos++;
                        }
                    }

                    //坡度曲线
                    double alt =0;
                    double[] altitude = new double[size];
                    for(int i=1; i<size; i++) {
                        double altChange = ((grad_1[i] + grad_1[i-1]) * (s[i] - s[i-1]) / 2.0) / 1000.0;
                        if(altChange != Double.NaN) {
                            alt -= altChange;
                        }
                        altitude[i] = alt;
                    }

                    //总牵引能耗
                    double kWh_Mech_F = 0;
                    for(int i=0; i<EnergyF.length; i++) {
                        kWh_Mech_F += EnergyF[i];
                    }
                    kWh_Mech_F /= (3.6*1000000);

                    //总制动能耗
                    double kWh_Mech_B = 0;
                    for(int i=0; i<EnergyB.length; i++) {
                        kWh_Mech_B += EnergyB[i];
                    }
                    kWh_Mech_B /= (3.6*1000000);

                    //列车总运行时间
                    double journey_time = 0;
                    for(int i=0; i<T.length; i++) {
                        journey_time= Math.max(journey_time, T[i]);
                    }

                    //相应里程处的能耗表
                    double[] energy_consumed = new double[size];
                    double e=0;
                    for(int i=0; i<size; i++) {
                        e+= EnergyF[i] / 3.6 / 1000000;
                        e-= EnergyB[i] / 3.6 / 1000000;
                        energy_consumed[i] = e;
                    }

                    //相应里程处的动能表
                    double[] kinetic_energy = new double[size];     //TODO Not working
                    for(int i=0; i<size; i++) {
                        kinetic_energy[i] = 1.0 / 2.0 * inertial_mass * vel[i] * vel[i]  * 1000 / 3.6 / 1000000;
                    }

                    //相应里程处的势能表
                    double[] potential_energy = new double[size];
                    for(int i=0; i<size; i++) {
                        potential_energy[i] = altitude[i]*9.81*Mass/3.6/1000;
                    }

                    //resistance_energy= cumsum((Davis(1)+Davis(2).*vel+Davis(3).*(vel.^2)).*del_S)*1000/3.6/1000000;    相应里程处的能耗表
                    double[] resistance_energy = new double[size];
                    double re = 0;
                    for(int i=0; i<size; i++) {
                        re += ((Davis[0] + (Davis[1] * vel[i]) + (Davis[2] * vel[i] * vel[i]))) * del_S * 1000/3.6/1000000;
                        resistance_energy[i] = re;
                    }

                    for(int i=0;i<vel.length;i++){
                        Logger.d(TAG,"vel[i" + i + "]" + vel[i]);
                    }
                    //      new STSGraphs(altitude, vel, vel_limit, T, s, v, trac, Res, accel, Mass, acceler, TractionF, TractionB, energy_consumed, resistance_energy, kinetic_energy, potential_energy);


                }

            });

        }else if(action.equals(IntentConstants.ACTION_CALCULATE_TRAIN_MILEAGE)){
            //计算当前运行总里程
            mSimulateHandler.sendEmptyMessageDelayed(MsgConstant.MSG_CALCULATE_MILEAGE,1000);

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initData(int size){
        vel_limit = new double[size]; //更新后限速表，在中间车站处限速为1m/s,终点站处限速为1/30m/s(这个值随便取一个接近于0的值就可以)
        vel_error = new double[size]; //
        grad_prof = new double[size]; //坡度加速度表
        TractionF = new double[size]; //牵引功率表
        TractionB = new double[size]; //制动功率表
        EnergyF = new double[size];   //每步长（10m）牵引能耗
        EnergyB = new double[size];   //每步长（10m）制动能耗
        Notch = new double[size];     //
        acceleration = new double[size]; //
        acceler = new double[size];  // 实时加速度
        accF = new double[size];  // 实时牵引加速度
        accB = new double[size];  // 实时制动加速度
        T = new double[size];     //当前时刻
        vel = new double[size];   //实时速度
        del_T = new double[size]; //每一个步长（10m）的时间
    }

    private Handler mSimulateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MsgConstant.MSG_CALCULATE_MILEAGE:
                    //计算当前列车里程, 速度单位是km/h
                    mTotalMileage = mTotalMileage + mTrainControl.getCurrentSpeed() * TrainConstants.KM_PER_HOUR_2_M_PER_SECONDS * 1;
                    int velocityIndex = (int)(mTotalMileage / 10);
                    double suggestVelocity = vel[velocityIndex];
                    IntentManager.sendBroadcastMsg(IntentConstants.ACTION_UPDATE_TRAIN_SUGGEST_SPEED,
                            "suggest_velocity",suggestVelocity);
                    break;
            }

        }
    };


    public double brake(double vel, double max_accel, double Power, double Mass, double Coasting, double Coasting_vel) {
        double b;
        if(vel>1) {
            b = Power/Mass/1000/vel;
            if(b>max_accel) {
                b = max_accel;
            }
        } else {
            b = max_accel;
        }
        if(Coasting == 1) {
            if(vel>Coasting_vel) {
                b = 0.001;
            }
        }
        return b;
    }

}
