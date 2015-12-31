package com.kinvey.sample.vicinity.configuration;

import android.app.Instrumentation;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.sample.vicinity.util.Utils;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.File;

/**
 * Created by Tanay Agrawal on 10/10/2015.
 */
public class BootstrapApplication extends MultiDexApplication {

//    //public static final String ACCOUNT_TYPE = ;
//    public static final String LOGIN_TYPE_KEY = "loginType";
//
//    private static Client service;
    private static BootstrapApplication instance;
    public static String size = "";

    private static Storage storage = null;

    public BootstrapApplication() {

    }

    public BootstrapApplication(final Context context) {
        this();
        attachBaseContext(context);
    }

    public BootstrapApplication(final Instrumentation instrumentation) {
        this();
        attachBaseContext(instrumentation.getTargetContext());
    }


    //get instance for BootstrapApplication
    public static BootstrapApplication getInstance() {

        if (instance == null) {
            instance = new BootstrapApplication();
            return instance;

        } else {
            return instance;
        }
    }

//    public static Client getKinveyService() {
//        return service;
//    }

    //get size value
    public static String getSize() {
        return size;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initializeStorage(this.getApplicationContext());

    }

//    private void initializeClient() {
//        // Enter your app credentials here
//        service = new Client.Builder(this.getApplicationContext()).build();
//        service.user().keepOfflineStorageOnLogout();
//        service.enableDebugLogging();
//        com.kinvey.java.Logger.configBuilder().all();
//    }

    public static void initializeStorage(Context context) {
        try {
            if (SimpleStorage.isExternalStorageWritable()) {
                storage = SimpleStorage.getExternalStorage();
            } else {
                storage = SimpleStorage.getInternalStorage(context);
            }

            if (!storage.isDirectoryExists("Vicinity")) {
                storage.createDirectory("Vicinity");
            }
        } catch (Exception ex) {

        }
    }


    //get current used storage
    public static Storage getStorage(Context context) {
        if (storage != null) {
            return storage;
        } else {
            initializeStorage(context);
            return storage;
        }
    }


    //store data in Vicinity folder
    public static void storeData(String id, String data) {
        try {
            if (storage.isFileExist("Vicinity", id)) {
                storage.deleteFile("Vicinity", id);
                // create new file
                storage.createFile("Vicinity", id, data);
            } else {
                // create new file
                storage.createFile("Vicinity", id, data);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
    }

    //read file present in Vicinity folder
    public static String readFile(String id) {
        try {
            if (storage.isFileExist("Vicinity", id)) {
                return storage.readTextFile("Vicinity", id);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
        return "";
    }


    //get raw file from Vicinity
    public static File getFile(String id) {
        try {
            if (storage.isFileExist("Vicinity", id)) {
                return storage.getFile("Vicinity", id);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
        return null;
    }


    //create sub directory
    public static void createSubDir(String sub) {
        try {
            if (!storage.isDirectoryExists("Vicinity/" + sub)) {
                // create directory
                storage.createDirectory("Vicinity/" + sub);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
    }

    //store data into a sub directory
    public static void storeDataSubDirectory(String id, String data, String sub) {

        try {
            if (storage.isFileExist("Vicinity/" + sub, id)) {
                storage.deleteFile("Vicinity/" + sub, id);
                // create new file
                storage.createFile("Vicinity/" + sub, id, data);
            } else {
                // create new file
                storage.createFile("Vicinity/" + sub, id, data);
            }
        } catch (Exception ex) {
            Log.d("error saving", ex.toString());
        }
    }

    //read file from a sub directory
    public static String readFileSubDirectory(String id, String sub) {
        try {
            if (storage.isFileExist("Vicinity/" + sub, id)) {
                return storage.readTextFile("Vicinity/" + sub, id);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
        return "";
    }

    //delete file from a sub folder inside Vicinity
    public static void deleteFile(String id, String sub) {
        try {
            if (storage.isFileExist("Vicinity/" + sub, id)) {
                storage.deleteFile("Vicinity/" + sub, id);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
    }


    //delete file directory
    public static void deleteFileDir(String id, String dir) {
        try {
            if (storage.isFileExist(dir, id)) {
                storage.deleteFile(dir, id);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
    }

    //check if file Exists
    public static boolean isFileExists(String id, String dir) {
        try {

            return storage.isFileExist(dir, id);
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
        return false;
    }

    //get file from a sub directory
    public static File getFile(String id, String sub) {
        try {
            if (storage.isFileExist("Vicinity/" + sub, id)) {
                return storage.getFile("Vicinity/" + sub, id);
            }
        } catch (Exception ex) {
            Log.d("error sub", ex.toString());
        }
        return null;
    }


    //append data into a file
    public static void appendData(String id, String data) {
        try {
            if (storage.isFileExist("Vicinity", id)) {
                // append data file
                storage.appendFile("Vicinity", id, data);
            } else {
                // create new file
                storage.createFile("Vicinity", id, data);
            }
        } catch (Exception ex) {
            Log.d("monitor file append", ex.toString());
        }
    }
}
