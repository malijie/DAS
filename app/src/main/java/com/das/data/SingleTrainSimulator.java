package com.das.data;

import android.widget.Toast;

import com.das.Myapp;
import com.das.db.DBConfig;
import com.das.db.DBManager;
import com.das.util.Logger;

/**
 * Java port of the Single Train Simulator.
 * Matlab version by Stuart Hillmansen.
 * Java port by ZhangCheng.
 */
public class SingleTrainSimulator {
   private static final Object sObject = new Object();
   private static SingleTrainSimulator sSingleTrainSimulator = null;
   private double[][] mGradient;
   private double[][] mVel_profile;
   private double[] mStation_info;

   private SingleTrainSimulator(){
      mGradient =  DBManager.getInstance().read2DArrayFromTable(DBConfig.TABLE_GRADIENTS);
      mVel_profile = DBManager.getInstance().read2DArrayFromTable(DBConfig.TABLE_VELOCITY);
      mStation_info = DBManager.getInstance().read1DArrayFromTable(DBConfig.TABLE_STATION_INFO);
   }

   public static SingleTrainSimulator getInstance() {
      if(sSingleTrainSimulator == null){
         synchronized (sObject){
            if(sSingleTrainSimulator == null){
               sSingleTrainSimulator = new SingleTrainSimulator();
            }
         }
      }
      return sSingleTrainSimulator;
   }

   public void updateTrainStatus() {

      /*************************************** Vehicle ******************************************/
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

      /* train control
          1 = fast as possible
          2 = proportional
          3 = proportional and notched
       */
      int train_control=1;

      /****************************************** Route ********************************************/
      //route KingsX_NewC_KingsX  读取数据文件，分别为坡度数据、限速数据、车站信息、终点站信息
//      double[][] gradient = read2DArrayFile("data/STS/Route_KingsX_NewC_KingsX_gradient.csv");
//      double[][] mVel_profile = read2DArrayFile("data/STS/Route_KingsX_NewC_KingsX_mVel_profile.csv");
//      double[] station_info = read1DArrayFile("data/STS/Route_KingsX_NewC_KingsX_station_info.csv");
      //double[] terminal = read1DArrayFile("data/STS/Route_KingsX_NewC_KingsX_terminal.csv");


      /*
       * Compute traction curves - for display purposes only?  v[i] 表示以m/s为单位的速度表
       */

      /*
       *       v=1:1:max_speed;
       *       v=v./3.6;
       */
      double[] v = new double[(int)max_speed];
      for(int i=1; i<=max_speed; i++) {
         v[i-1] = i / 3.6;
      }

      /*
       *     trac=Power./(v)/(Mass*1000);  计算牵引加速度表trac[i]
       */
      double[] trac = new double[v.length];
      for(int i=1; i<=v.length; i++) {
         trac[i-1] = Power/v[i-1]/(Mass*1000);
      }

      /*
       *     Res=zeros(1,length(v));    计算制动加速度表Res[i]
       */
      double[] Res = new double[v.length];
      for(int i=1; i<=Res.length; i++) {
         Res[i-1] = 0;
      }

      /*
       *       for i=1:length(v)
       *          if trac(1,i)>max_accel(1)
       *             trac(1,i)=max_accel(1);
       *          END
       *          Res(1,i)=Davis(1)+ v(1, i)*Davis(2) + (v(1, i))^2*Davis(3);
       *       end
       */
      for(int i=1; i<=v.length; i++) {
         if(trac[i-1] > max_accel) {
            trac[i-1] = max_accel;
         }
         Res[i-1] = Davis[0] + v[i-1] * Davis[1] + ((v[i-1] * v[i-1]) * Davis[2]);
      }

      /*
       *       Res=Res./Mass;
       */
      for(int i=1; i<=v.length; i++) {
         Res[i-1] /= Mass;
      }

      /*
       *      accel=trac-Res;     总加速度表accel[i]
       */
      double[] accel = new double[v.length];
      for(int i=1; i<=v.length; i++) {
         accel[i-1] = trac[i-1] - Res[i-1];
      }



      // Students to modify code below     惰行速度
      double Coasting = 0;
      double Coasting_vel = 80*1.6/3.6;

      int S_min = 0;

      /*
       * S_max=max(mVel_profile(:,1))*1000;   最大里程S_max，单位m
       */
      double S_max = Double.MIN_VALUE;
      for(int i = 0; i< mVel_profile.length; i++) {
         S_max = Math.max(S_max, mVel_profile[i][0]);
      }
      S_max *= 1000;

      int del_S = 10;

      /*
       * s=S_min:del_S:S_max;   里程表s[i]， 10m一个单位
       */
      int[] s = new int[(int)(Math.floor(S_max) / del_S) + 1];
      for(int i=0; i<s.length; i++) {
         s[i] = del_S * i;
      }

      int size = s.length;

      /*
       * %DEFINE ARRAYS
       * vel_limit=zeros(1, Size);
       * vel_error=zeros(1, Size);
       * grad_prof=zeros(1, Size);
       * TractionF=zeros(1, Size);
       * TractionB=zeros(1, Size);
       * EnergyF=zeros(1, Size);
       * EnergyB=zeros(1, Size);
       * Notch=zeros(1, Size);
       * acceleration=zeros(1, Size);
       * acceler=zeros(1, Size);
       * accF=zeros(1, Size);
       * accB=zeros(1, Size);
       * T=zeros(1, Size);
       * vel=zeros(1, Size);
       * del_T=zeros(1, Size);
       */
      double[] vel_limit = new double[size]; //更新后限速表，在中间车站处限速为1m/s,终点站处限速为1/30m/s(这个值随便取一个接近于0的值就可以)
      double[] vel_error = new double[size]; //
      double[] grad_prof = new double[size]; //坡度加速度表
      double[] TractionF = new double[size]; //牵引功率表
      double[] TractionB = new double[size]; //制动功率表
      double[] EnergyF = new double[size];   //每步长（10m）牵引能耗
      double[] EnergyB = new double[size];   //每步长（10m）制动能耗
      double[] Notch = new double[size];     //
      double[] acceleration = new double[size]; //
      double[] acceler = new double[size];  // 实时加速度
      double[] accF = new double[size];  // 实时牵引加速度
      double[] accB = new double[size];  // 实时制动加速度
      double[] T = new double[size];     //当前时刻
      double[] vel = new double[size];   //实时速度
      double[] del_T = new double[size]; //每一个步长（10m）的时间

      /*
       * %DETERMINE VEL LIMIT FROM INPUT FILE  读取限速文件
       * pos=2;
       * for i=1:Size-1
       *     vel_limit(1,i)=mVel_profile(pos-1,2)/3.6;
       *     if vel_limit(1,i)>=max_speed/3.6
       *         vel_limit(1,i)=max_speed/3.6;
       *         max_speed/3.6;                               //???
       *     end
       *     if i*del_S>=fix(mVel_profile(pos,1)*1000)
       *         pos=pos+1;
       *     end
       * end
       */
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

      /*
       * %DETERMINE STATION STOPS FROM INPUT FILE  更新车站处限速
       * pos=1;
       * for i=1:fix(1000/del_S*max(station_info(:,1)))
       *     if i*del_S>=fix(station_info(pos,1)*1000)
       *         vel_limit(1,i)=3/dwell*del_S;
       *         vel_limit(1,i+1)=3/dwell*del_S;
       *         pos=pos+1;
       *     end
       * end
       */
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

      /*
       * %DETERMINE Terminal STATION STOPS FROM INPUT FILE  更新终点站处限速
       * pos=1;
       * for i=1:fix(1000/del_S*max(terminal(:,1)))
       *     if i*del_S>=fix(terminal(pos,1)*1000)
       *         vel_limit(1,i)=3/(Terminal_time)*del_S;
       *         vel_limit(1,i+1)=3/(Terminal_time)*del_S;
       *         pos=pos+1;
       *     end
       * end
       */
      pos = 1;
//      double maxTerminalInfo = Integer.MIN_VALUE;
//      for(int i=0; i<terminal.length; i++) {
//         maxTerminalInfo = Math.max(maxTerminalInfo, terminal[i]);
//      }
//      for(int i=1; i<=Math.floor(1000.0/del_S*maxTerminalInfo); i++) {
//         if(i*del_S>=Math.floor(terminal[pos-1]*1000)) {
//            vel_limit[i-1]=3.0/(Terminal_time)*del_S;
//            vel_limit[i] = 3.0/(Terminal_time)*del_S;
//            pos++;
//         }
//      }

      /*
       * %DETERMINE GRADIENT PROFILE FROM INPUT FILE    计算坡度加速度表
       * pos=2;
       * for i=1:fix(1000/del_S*max(gradient(:,1)))
       *    grad_prof(1,i)=-gradient(pos-1,2)/1000*9.8/(1+lambda);
       *     if i*del_S>=fix(gradient(pos,1)*1000)
       *         pos=pos+1;
       *     end
       * end
       */
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

      /*
       * %GRADIENT SMOOTHING - not necessary
       * for i=5:fix(1000/del_S*max(gradient(:,1)))-4
       *     grad_prof(1,i)=mean(grad_prof(1,i-3:i+3));
       * end
       */
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

      /*
       * %FORWARD VELOCITY CALCULATION     计算列车速度
       * velF=zeros(1,Size)+0.01;
         for i=1:(Size-1)
         Resistance=(Davis(1)+Davis(2)*velF(1,i)+Davis(3)*(velF(1,i))^2)/inertial_mass;
         %train control (DEFINED in vehicle.m)
         %1 = fast as possible
         %2 = proportional
         %3 = proportional and notched
         if train_control==1
             vel_error(1,i)=1;
         end
         if train_control==2
              prop
         end
         if train_control==3
              prop_notched
         end

       */
      double[] velF = new double[size];
      for(int i=0; i<size-1; i++) {
         velF[i] = 0.01;
      }
      for(int i=0; i<size-1; i++) {
         double Resistance = (Davis[0] + (Davis[1]*velF[i]) + (Davis[2]* Math.pow(velF[i],2))) / inertial_mass;  //  列车阻力（空气阻力、摩擦力等）加速度
         if(train_control==1) {
            vel_error[i]=1;
         } else if(train_control==2) {
            // PROP
            /*
             *         vel_error(1,i)=kd*(vel_limit(1,i) - velF(1,i));
                         if vel_error(1,i)>1
                             vel_error(1,i)=1;
                         end
                         if vel_error(1,i)<=0
                             vel_error(1,i)=0.1;
                         end
             */
            vel_error[i] = kd*(vel_limit[i]-velF[i]);
            if(vel_error[i]>1) {
               vel_error[i]=1;
            }
            if(vel_error[i]<=0) {
               vel_error[i]=0.1;
            }
         } else if(train_control==3) {
            // PROP Notched - Not done, doesn't work in Matlab STS.
            /*
                  %proportional and notched
                  %approximate time calculation
                      del_T_F(1,i+1)=2*del_S/(velF(1,i)+velF(1,i+1));
                      if velF(1,i)==0
                          T_F(1,i+1)=T_F(1,i)+1;
                      else
                          T_F(1,i+1)=T_F(1,i)+del_T_F(1,i+1);
                      end

                      if T_F(1,i)>=counter
                          vel_error(1,i)=kd*(vel_limit(1,i) - velF(1,i));
                          if velF(1,i)>30
                          counter=counter+30;
                          else
                          counter=counter+10;
                          end
                      else
                          vel_error(1,i)=vel_error(1,i-1);
                      end

                      if vel_error(1,i)>1
                          vel_error(1,i)=1;
                      end
                      if vel_error(1,i)<=0
                          vel_error(1,i)=0.1;
                      end
                      vel_error(1,i)=round(vel_error(1,i)*notch_num)/notch_num;
             */
         }

         /*
          *
             if velF(1,i)<vel_limit(1,i)
                 Traction=Power*vel_error(1,i)/(velF(1,i))/(inertial_mass*1000);
                 if Traction>max_accel(1)
                         Traction=max_accel(1);
                 end
                 accF(1,i+1)=Traction - Resistance + grad_prof(1,i);
                 velF(1,i+1)=(velF(1,i).^2+2*accF(1,i+1)*(del_S)).^0.5;
             else
                 Traction=Power*vel_error(1,i)/(velF(1,i))/(inertial_mass*1000);
                 if Traction>max_accel(1)
                         Traction=max_accel(1);
                 end
                 accF(1,i+1)=Traction - Resistance + grad_prof(1,i);
                 %This if condition is to allow for deceleration at full power
                 %while assending a gradient
                 if (accF(1,i+1)<0);
                     velF(1,i+1) = (velF(1,i)^2+2*(accF(1,i+1))*(del_S))^0.5;
                     if velF(1,i+1)>vel_limit(1,i)
                         velF(1,i+1)=vel_limit(1,i);
                     end
                 else
                     accF(1,i+1)=0;
                     velF(1,i+1) = vel_limit(1,i);
                 end
             end
         end
          */
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

//      DEBUG
//      for(int i=0; i<accF.length; i++) {
//         System.out.println("I=" + i + ", accF =" + accF[i]);
//      }
//      for(int i=0; i<velF.length; i++) {
//         System.out.println("I=" + i + ", velF =" + velF[i]);
//      }

      /*
       * %BACKWARD VELOCITY CALCULATION
         velB=zeros(1,Size)+0.01;
         for i=(fix(S_max/del_S)):-1:1
             if velB(1,i+1)<vel_limit(1,i+1)
             Resistance=(Davis(1)+Davis(2)*velB(1,i+1)+Davis(3)*(velB(1,i+1))^2)/inertial_mass;
             accB(1,i)=Resistance + brake(velB(1,i+1), max_accel, Power, inertial_mass, Coasting, Coasting_vel)- grad_prof(1,i+1);
             velB(1,i)=(velB(1,i+1)^2+2*accB(1,i)*(del_S))^0.5;
             else
             velB(1,i) = vel_limit(1,i+1);
             end
         end
       */

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

//      // DEBUG
//      for(int i=0; i<accB.length; i++) {
//         System.out.println("I=" + i + ", accB =" + accB[i]);
//      }
//      for(int i=0; i<velB.length; i++) {
//         System.out.println("I=" + i + ", velB =" + velB[i]);
//      }

      /*
       * %ACTUAL VELOCITY PROFILE
         for i=1:Size
             if velF(1,i)<=velB(1,i)
                 acceler(1,i)=accF(1,i);
                 vel(1,i)=velF(1,i);
             else
                 acceler(1,i)=-accB(1,i);
                 vel(1,i)=velB(1,i);
             end
         end
       */

      for(int i=0; i<size; i++) {   // 牵引速度和制动速度取小的一个值作为vel[i]优化后 速度表
         if(velF[i] <= velB[i]) {   //代表牵引
            acceler[i]=accF[i];
            vel[i] = velF[i];
         } else {				//代表制动
            acceler[i]=-accB[i];
            vel[i]=velB[i];
         }
      }

      /*
       * %TIME CALCULATION
         for i=1:Size-1
             del_T(1,i+1)=2*del_S/(vel(1,i)+vel(1,i+1));
             if vel(1,i)==0
                 T(1,i+1)=T(1,i)+1;
             else
                 T(1,i+1)=T(1,i)+del_T(1,i+1);
             end
         end
       */

      for(int i=0; i<size-1; i++) {     //列车运行时间表
         del_T[i+1] = 2*del_S/(vel[i]+vel[i+1]);
         if(vel[i]==0) {
            T[i+1] = T[i]+1;
         } else {
            T[i+1]=T[i] + del_T[i+1];
         }
      }

      /*
       * %TRACTION (F-forward B-backward) CALCULATION
         for i=2:Size-2
             Resistance=(Davis(1)+Davis(2)*vel(1,i)+Davis(3)*(vel(1,i))^2)/inertial_mass;
             Notch(1,i)=((acceler(1,i-1)+acceler(1,i-1))/2 ...
             + Resistance - grad_prof(1,i))...
             *vel(1,i)*inertial_mass*1000;
             if Notch(1,i)>0
                 TractionB(1,i)=0;
             else
                 TractionB(1,i)=-Notch(1,i);
             end
             Notch(1,i)=((acceler(1,i+1)+acceler(1,i+1))/2 ...
             + Resistance - grad_prof(1,i))...
             *vel(1,i)*inertial_mass*1000;
             if Notch(1,i)>0
                 TractionF(1,i)=Notch(1,i);
             else
                 TractionF(1,i)=0;
             end
             del_T(1,i)=2*del_S/(vel(1,i)+vel(1,i+1));
         end    判断列车牵引、制动、惰行的拐点
       */
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

      /*
       *
         %Energy (F-forward B-backward) CALCULATION     每步长（10m）制动能耗表
         for i=2:Size-2
                 EnergyF(1,i)=TractionF(1,i)/vel(1,i)*del_S;
                 if TractionB(1,i)> Power
                     EnergyB(1,i)=Power/vel(1,i)*del_S;
                 else
                     EnergyB(1,i)=TractionB(1,i)/vel(1,i)*del_S;
                 end
         end
       */
      for(int i=1; i<size-3; i++) {
         EnergyF[i]=TractionF[i]/vel[i]*del_S;
         if(TractionB[i]>Power) {
            EnergyB[i]=Power/vel[i]*del_S;
         } else {
            EnergyB[i]=TractionB[i]/vel[i]*del_S;
         }
      }

      /*
       * Height function
       * grad_1=zeros(1, Size);
         pos=2;
         for i=1:fix(1000/del_S*max(gradient(:,1)))
            grad_1(1,i)=-gradient(pos-1,2);
             if i*del_S>=fix(gradient(pos,1)*1000)
                 pos=pos+1;
             end
         end      以步长（10m）为单位更新后的坡度表
       */
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

//      // DEBUG
//      for(int i=0; i<grad_1.length; i++) {
//         System.out.println((i+1) + " = " + grad_1[i]);
//      }

      /*
       * altitude = -cumtrapz(s(1,:), grad_1(1,:))/1000;     坡度曲线
       */
      double alt =0;
      double[] altitude = new double[size];
      for(int i=1; i<size; i++) {
         double altChange = ((grad_1[i] + grad_1[i-1]) * (s[i] - s[i-1]) / 2.0) / 1000.0;
         if(altChange != Double.NaN) {
            alt -= altChange;
         }
         altitude[i] = alt;
      }

      // DEBUG
//      for(int i=0; i<altitude.length; i++) {
//         System.out.println((i+1) + " = " + altitude[i]);
//      }
//
      /*
       * kWh_Mech_F=sum(EnergyF)/3.6/1000000;  总牵引能耗
       */

      double kWh_Mech_F = 0;
      for(int i=0; i<EnergyF.length; i++) {
         kWh_Mech_F += EnergyF[i];
      }
      kWh_Mech_F /= (3.6*1000000);

      /*
       * kWh_Mech_B=sum(EnergyB)/3.6/1000000;    总制动能耗
       */
      double kWh_Mech_B = 0;
      for(int i=0; i<EnergyB.length; i++) {
         kWh_Mech_B += EnergyB[i];
      }
      kWh_Mech_B /= (3.6*1000000);

      /*
       * journey_time=max(T);   列车总运行时间
       */
      double journey_time = 0;
      for(int i=0; i<T.length; i++) {
         journey_time= Math.max(journey_time, T[i]);
      }

      /*
       * energy_consumed=(cumsum(EnergyF)-cumsum(EnergyB))/3.6/1000000;  相应里程处的能耗表
       */
      double[] energy_consumed = new double[size];
      double e=0;
      for(int i=0; i<size; i++) {
         e+= EnergyF[i] / 3.6 / 1000000;
         e-= EnergyB[i] / 3.6 / 1000000;
         energy_consumed[i] = e;
      }

      /*
       * kinetic_energy=1/2*inertial_mass.*vel.*vel*1000/3.6/1000000;   相应里程处的动能表
       */
      double[] kinetic_energy = new double[size];     //TODO Not working
      for(int i=0; i<size; i++) {
         kinetic_energy[i] = 1.0 / 2.0 * inertial_mass * vel[i] * vel[i]  * 1000 / 3.6 / 1000000;
      }

      /*
       * potential_energy=altitude*9.81*Mass/3.6/1000;    相应里程处的势能表
       */
      double[] potential_energy = new double[size];
      for(int i=0; i<size; i++) {
         potential_energy[i] = altitude[i]*9.81*Mass/3.6/1000;
      }

      /*
       * resistance_energy= cumsum((Davis(1)+Davis(2).*vel+Davis(3).*(vel.^2)).*del_S)*1000/3.6/1000000;    相应里程处的能耗表
       */
      double[] resistance_energy = new double[size];
      double re = 0;
      for(int i=0; i<size; i++) {
         re += ((Davis[0] + (Davis[1] * vel[i]) + (Davis[2] * vel[i] * vel[i]))) * del_S * 1000/3.6/1000000;
         resistance_energy[i] = re;
      }

       for(int i=0;i<vel.length;i++){
//           Logger.d("MLJ","vel[i" + i + "]" + vel[i]);
       }
       Toast.makeText(Myapp.sContext,"acceler.length=" + TractionF.length,Toast.LENGTH_LONG).show();

//      new STSGraphs(altitude, vel, vel_limit, T, s, v, trac, Res, accel, Mass, acceler, TractionF, TractionB, energy_consumed, resistance_energy, kinetic_energy, potential_energy);


      System.out.println("END");
   }


   public double brake(double vel, double max_accel, double Power, double Mass, double Coasting, double Coasting_vel) {
      /*
       * brake function
       * function [b] = brake(vel, max_accel, Power, Mass, Coasting, Coasting_vel)
         if vel>1
             b=Power/Mass/1000/vel;
             if b>max_accel;
                 b=max_accel;
             end
         else
             b=max_accel;
         end
         if Coasting == 1
         if vel > Coasting_vel
             b=0.001;
         end
         end
       */

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
