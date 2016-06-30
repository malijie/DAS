package data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malijiea on 2016/6/30.
 */
public class DataManager {

    private static final Object sObject = new Object();
    private static DataManager sDataManager = null;

    public static DataManager getInstance(){
        if (sDataManager == null){
            synchronized (sObject){
                if(sDataManager == null){
                    sDataManager = new DataManager();
                }
            }
        }
        return sDataManager;
    }

    public List<Integer> getSafeSpeedList(){
        List<Integer> safeSpeedList = new ArrayList<>();
        for(int i=0;i<DataConstants.ROUTE_DISTANCE;i++){
            //前10公里140码
            if(i<10){
                safeSpeedList.add(140);
            }
            if(i>10 && i<25){
                safeSpeedList.add(160);
            }

            if(i>25 && i<30){
                safeSpeedList.add(5);
            }
        }
        return safeSpeedList;
    }

    public List<Integer> getSuggestSpeedList(){
        List<Integer> suggestSpeedList = new ArrayList<>();
        for(int i=0;i<DataConstants.ROUTE_DISTANCE;i++){
            //前10公里140码
            if(i<10){
                suggestSpeedList.add(110);
            }
            if(i>10 && i<25){
                suggestSpeedList.add(140);
            }

            if(i>25 && i<30){
                suggestSpeedList.add(10);
            }
        }
        return suggestSpeedList;
    }


}
