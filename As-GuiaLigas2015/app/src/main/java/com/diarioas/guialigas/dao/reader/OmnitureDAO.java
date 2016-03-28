package com.diarioas.guialigas.dao.reader;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;

import com.diarioas.guialigas.utils.Defines;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by luismiguel on 8/3/16.
 */
public class OmnitureDAO {

    private static Properties properties;
    private static OmnitureDAO sInstance;
    private static Context mContext;



    public static OmnitureDAO getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new OmnitureDAO(ctx);
            mContext = ctx;
            sInstance.init();
        }
        return sInstance;
    }

    public OmnitureDAO(Context ctx) {
        mContext = ctx;
        PackageInfo pInfo = null;
        try {
            pInfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void init(){
        properties = new Properties();
        getProperties(Defines.Omniture.OMNITURE_PROPERTIES_FILE_NAME);
    }

    public Properties getProperties(String FileName) {
        try {
            AssetManager assetManager = mContext.getAssets();
            InputStream inputStream = assetManager.open(FileName);
            properties.load(inputStream);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("AssetsPropertyReader",e.toString());
        }
        return properties;
    }

    public static String getProperty(String key){
        if(properties != null){
            return (String)properties.get(key);
        }else{
            return "";
        }
    }

}
