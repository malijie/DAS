package com.das.db;

/**
 * Created by Administrator on 2016/7/1.
 */
public class SQLContainer {

    public static final String getCreatePathTableSQL(){
        return "CREATE TABLE " + DBConfig.TABLE_PATH + "(" +
                "name VARCHAR(20)," +
                "start_node VARCHAR(20)," +
                "end_node VARCHAR(20)," +
                "length VARCHAR(10)," +
                "speed_limit_up VARCHAR(10)," +
                "speed_limit_down VARCHAR(10)," +
                "gradient VARCHAR(15)," +
                "tunnel VARCHAR(10)," +
                "radius VARCHAR(10)," +
                "note VARCHAR(20))";
    }

    public static final String getCreateNodeTableSQL(){
        return "CREATE TABLE " + DBConfig.TABLE_NODE + "(" +
                "name VARCHAR(20)," +
                "location_x VARCHAR(20)," +
                "location_y VARCHAR(20)," +
                "id VARCHAR(10)," +
                "km VARCHAR(10)," +
                "km_change VARCHAR(10)," +
                "type VARCHAR(10)," +
                "device VARCHAR(10)," +
                "device_direction VARCHAR(20)," +
                "track_id VARCHAR(10))";
    }

    public static String getDropNodeTableSQL(){
        return "DROP TABLE IF EXISTS" + DBConfig.TABLE_NODE;
    }

    public static String getDropPathTableSQL(){
        return "DROP TABLE IF EXISTS" + DBConfig.TABLE_PATH;
    }

    public static String getSelectAllSQL(){
        return "SELECT * FROM ";
    }


}
