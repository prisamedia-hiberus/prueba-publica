package com.prisadigital.realmedia.adlib.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

public class StorageProvider {
    private static final long      TIME_TO_EXPIRE = 1 * 24 * 60 * 60 * 1000; // 24 hours

    // Instance
    private static StorageProvider instance;

    // Path
    private File                   storagePath;

    private StorageProvider(Context context) {
        initialize(context);
    }

    private void initialize(Context context) {
        // Get Package
        String appPackage = context.getPackageName();

        // Get Environment
        String storageState = Environment.getExternalStorageState();

        // Check SD
        if (storageState != null && storageState.equals(android.os.Environment.MEDIA_MOUNTED) && isSdPermission(context)) {
            // Get SD Path
            storagePath = new File(Environment.getExternalStorageDirectory(), "Android/data/"
                    + appPackage + "/files/");
        } else {
            // We don't cache in internal memory
            storagePath = context.getFilesDir();
        }

        // Execute cleanup
        cleanStorage();
    }
    
    private boolean isSdPermission(Context context) {
        return context.getPackageManager().checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    public File getFile(String filename) {
        // Create File
        File file = new File(storagePath, AdLibUtils.md5(filename.toLowerCase()));

        return file;
    }
    
    public boolean saveFile(InputStream is, String filename) {
        int nBytes;
        
        try {
            // Create File
            File file = new File(storagePath, AdLibUtils.md5(filename.toLowerCase()));
    
            // Check if the storage exits
            if (!storagePath.exists())
                storagePath.mkdirs();
    
            // Check if exists
            if (!file.exists())
                file.createNewFile();
            
            // Create output stream
            FileOutputStream fos = new FileOutputStream(file);
            
            // Create buffer to read
            byte buffer[] = new byte[512];

            // Iterate Stream to save it on the file
            while((nBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, nBytes);
            }
            
            // Close Streams
            fos.close();
            is.close();
            
            return true;
        } catch(IOException ioe) {
            return false;
        }
    }

    public boolean saveFile(byte data[], String filename) {
        // Create File
        File file = new File(storagePath, filename);

        try {
            // Check if the storage exits
            if (!storagePath.exists())
                storagePath.mkdirs();

            // Check if exists
            if (!file.exists())
                file.createNewFile();

            // Create output stream
            FileOutputStream fos = new FileOutputStream(file);
            // Write stream
            fos.write(data);
            // Close file
            fos.close();

            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public static StorageProvider getStorage(Context context) {
        if (instance == null)
            instance = new StorageProvider(context);

        return instance;
    }

    private void cleanStorage() {
        try {
            // Check if the storage exits
            if (storagePath.exists() && storagePath.isDirectory()) {
                // Get all files in directory
                String paths[] = storagePath.list();

                // Iterate paths
                for (String path : paths) {
                    // Open file
                    File file = new File(storagePath, path);

                    // Check if exists
                    if (file.exists()) {
                        // Check if the file expire
                        if (file.lastModified() < (System.currentTimeMillis() - TIME_TO_EXPIRE)) {
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
