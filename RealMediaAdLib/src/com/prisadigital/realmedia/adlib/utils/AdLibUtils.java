package com.prisadigital.realmedia.adlib.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;



public class AdLibUtils {
    @SuppressWarnings("unused")
    private static final String TAG                = "AdLibUtils";
    public static final String  URL_ADSERVER       = "http://pubads.g.doubleclick.net/gampad/adx?iu=%s&sz=%s&mob=js&c=%s&t=mobcon%%3D%s%%26apid%%3d%s&m=text/xml&ppid=%s&tile=%s";
    public static final String  SIZE_BANNER        = "320x50";
    public static final String  SIZE_INTERSTIAL    = "320x480";
    public static final String  SIZE_PREROLL       = "640x380";

    // Density
    private static float        density            = -1;
    // Random
    private static Random       random             = null;

    public static final int     TYPE_WIFI          = 0;
    public static final int     TYPE_WIRELESS_DATA = 1;
    public static final int     TYPE_UNKNOWN       = -1;

    private static String       uniqueID           = null;
    // òltimo adUnitKey solicitado
    private static String       mCurrentAdUnitKey;
    // Cuenta de las veces que el œltimo adUnitKey ha sido solicitado
    private static int          mTileCount;
    // Nœmero aleatorio actual. Debe ser el mismo para todas las peticiones de la misma secci—n
    private static int          mCurrentRandom;
    
    private static final String PREF_UNIQUE_ID     = "PREF_UNIQUE_ID";

    public static double getDensity(Activity activity) {
        if (density == -1) {
            // Create Metrics
            DisplayMetrics displayMetrics = new DisplayMetrics();
            // Get Window Manager
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            // Save Density
            density = displayMetrics.density;
        }

        return density;
    }

    public static String getUrl(Context context, String adUnitKey, String adSize) {
        String sDataTypeConn = "UNKNOWN";
        int dataTypeConn = AdLibUtils.getDataTypeConnection(context);

        if (dataTypeConn == AdLibUtils.TYPE_WIFI) {
            sDataTypeConn = "WIFI";
        } else if (dataTypeConn == AdLibUtils.TYPE_WIRELESS_DATA) {
            sDataTypeConn = "3G";
        }
        // Se debe llamar primero a getTile porque es la funci—n que controla el cambio de secci—n y genera tile, adunit y currentRandom
        int tile = getTile(adUnitKey);
        
        
        String url = String.format(URL_ADSERVER, adUnitKey, adSize, mCurrentRandom, sDataTypeConn, getIdentifier(context), getIdentifier(context), tile);
        Log.d(TAG, "Ad request: "+url);
        return url;
    }
    
    /**
     * Devuelve un valor autoincrementado por cada petici—n seguida a un mismo adUnitKey
     * Ej:
     * - Petici—n a AdUnitKey_A -> return 1
     * - Petici—n a AdUnitKey_A -> return 2
     * - Petici—n a AdUnitKey_B -> return 1
     * - Petici—n a AdUnitKey_A -> return 1
     */
    private static int getTile(String adUnitKey) {
        mTileCount++;
        if (!adUnitKey.equals(mCurrentAdUnitKey)) {
            // Nuevo adUnitKey, se guarda y resetea contador
            mCurrentAdUnitKey = adUnitKey;
            mTileCount = 1;
            mCurrentRandom = AdLibUtils.getRandomInt();
        } 
        return mTileCount;
    }

    public static String getIdentifier(Context context){
    	return md5(getAndroidAppId(context)+getAndroidDeviceId(context));
    }
    
    
    public static String getAndroidAppId(Context context){
    	return context.getPackageName();
    }
    
    public static String getAndroidDeviceId(Context context) {
    	
        if (uniqueID == null) {
            SharedPreferences sharedPrefs = context.getSharedPreferences(
                    PREF_UNIQUE_ID, Context.MODE_PRIVATE);
            uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString();
                SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
                String format = s.format(new Date());
                uniqueID = uniqueID.concat(format);
                Editor editor = sharedPrefs.edit();
                editor.putString(PREF_UNIQUE_ID, uniqueID);
                editor.commit();
            }
        }
        return uniqueID;
        
    }

    public static int getRandomInt() {
        if (random == null)
            random = new Random(System.currentTimeMillis());

        return Math.abs(random.nextInt());
    }

    public static boolean isInternetConnection(Context context) {
        if (context == null)
            return false;

        // Get Manager
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get Network Info
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

        // Return state
        return networkInfo != null && networkInfo.isConnected();
    }

    public static int getDataTypeConnection(Context context) {
        if (context == null)
            return TYPE_UNKNOWN;

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get Network Info
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            return TYPE_UNKNOWN;
        } else {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            } else {
                return TYPE_WIRELESS_DATA;
            }
        }
    }

    public static byte[] getFileContents(InputStream is) throws IOException {
        // Read File
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[512];
        int nBytes;

        while ((nBytes = is.read(buffer)) != -1) {
            baos.write(buffer, 0, nBytes);
        }

        // Get File Data
        byte data[] = baos.toByteArray();
        baos.close();

        return data;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            return s;
        }
    }
}
