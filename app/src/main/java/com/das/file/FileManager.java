package com.das.file;

import com.das.Myapp;
import com.das.util.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/6/26.
 */
public class FileManager {
    private static final String TAG = FileManager.class.getSimpleName();
    private static final Object sObject = new Object();
    private static FileManager sFileManager = null;

    public static FileManager getInstance(){
        if(sFileManager == null){
            synchronized (sObject){
                if(sFileManager == null){
                    sFileManager = new FileManager();
                }
            }
        }
        return sFileManager;
    }

    public void copyDBToRootDirectory(){
        File dbFile = new File(getDBAbsolutePath());
        InputStream is = null;
        FileOutputStream os = null;
        if(!dbFile.exists()){//判断文件夹是否存在，不存在就新建一个
            Logger.d(TAG,"copy db to phone");
            dbFile.getParentFile().mkdirs();
            try{
                os = new FileOutputStream(getDBAbsolutePath());//得到数据库文件的写入流
                is = Myapp.sContext.getResources().getAssets().open(getDBName());//得到数据库文件的数据流
                byte[] buffer = new byte[1024];
                int count = 0;
                while((count=is.read(buffer))>0){
                    os.write(buffer, 0, count);
                    os.flush();
                }
                Logger.d(TAG,"finish copy");
            }catch(FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isDBExist(){
        File dbFile = new File(getDBAbsolutePath());
        if(!dbFile.exists()){
            dbFile.getParentFile().mkdirs();
        }
        return true;
    }

    private String getDBAbsolutePath(){
        return FileConstants.PHONE_DATABASE_DIRECTORY + FileConstants.DATABASE_NAME;
    }

    private String getDBName(){
        return FileConstants.DATABASE_NAME;
    }

}
