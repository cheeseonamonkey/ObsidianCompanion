package com.example.obsidiancompanion.classes;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Processor
{

    public final static int PREPEND_PREF_CHOOSER = 11;
    public final static int APPEND_PREF_CHOOSER = 12;

    public final static int DEFAULT_PREF_WRITE_MODE = 21;
    public final static int LOCAL_PREF_WRITE_MODE = 22;

    //preference (persistent storage) keys:
    public static final String APPEND_PREF_KEY = "obscom_ppAppend";
    public static final String PREPEND_PREF_KEY = "obscom_ppPrepend";
    public static final String FILE_URI_PREF_KEY = "obscom_QuickAddFileUri";
    public static final String FIRST_RUN_PREF_KEY = "obscom_QuickAddFileUri";
    public static final String FILE_NAME_PREF_KEY = "obscom_QuickAddFileName";
    public static final String FILE_PATH_PREF_KEY = "obscom_QuickAddFilePath";

    public static final String VAULT_FOLDER_PATH_PREF_KEY = "obscom_VaultFolderPath";

    public boolean firstRun = false;



    //===============================
    //nested classes
    public static class PrefWriter
    {
        //read method is public, write method is private

        public static boolean isPrefSet(Context context, String prefKey)
        {
            boolean boolOut = false;

            try
            {
                String str = readPref(context, prefKey);

                //not set
                if(str == null || str.contains("NOTSET:"))
                    return false;
                else //is set
                    return true;



            }catch(Exception exc)
            {
                Log.d("TAG", "Processor error 518: (caught and handled in logic) in isPref():  " + exc.getMessage() );
                return false;
            }

        }


        public static String readPref(Context context, String prefKey)
        {

            try
            {
                        //default prefs only
                        //if using local prefs, refactor like writePref()

                        //get the pref
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                        String strPref = prefs.getString(prefKey, null);//If there is no YOURKEY found null will be the default value.

                        //if pref is set to something
                        if(strPref != null)
                        {
                            Log.d("TAG", "Completed pref load:\n\t - key: " + prefKey + "\t - data (read back): " + strPref);
                        }else
                        {
                            //pref not set
                            Log.d("TAG", "Error 516 Pref load not set. Returning a NOTSET: value\n\t - key: " + prefKey + "\t - data (read back): " + strPref);
                            return "NOTSET:" + prefKey;


                        }
                        //

                       return strPref;
                

            }catch(Exception exc)
            {

                Log.d("error", "Error in Processor: " + exc.getMessage());
                return "ERR" + "NOTSET:" + prefKey ;
            }

        }

        private static void writePref(Context context, String prefKey, String prefData)
        {
            writePref(context, prefKey, prefData, DEFAULT_PREF_WRITE_MODE);
        }
        private static void writePref(Context context, String prefKey, String prefData, Integer PREF_WRITE_MODE )
        {
            try
            {


                //default/public preferences
                if(PREF_WRITE_MODE == DEFAULT_PREF_WRITE_MODE)
                    PREF_WRITE_MODE = DEFAULT_PREF_WRITE_MODE;
                //regular preferences
                else if(PREF_WRITE_MODE == LOCAL_PREF_WRITE_MODE)
                    PREF_WRITE_MODE = LOCAL_PREF_WRITE_MODE;


                switch(PREF_WRITE_MODE)
                {
                    case DEFAULT_PREF_WRITE_MODE:


                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

                        //save preference
                        editor.putString(prefKey, prefData);
                        editor.commit();


                        //debug logging to make sure pref got saved:
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

                        //reads back the data to be sure it was saved
                        String debugCallbackData = readPref(context, prefKey);

                        if(debugCallbackData == null)
                            throw new Exception("Error 101 in Processor - detected that the preference was not set on write");


                        
                        Log.d("TAG", "Successfully saved pref:\n\t - key: " + prefKey + "\t - data (read back): " + debugCallbackData);
                        //

                        break;

                        //no need for this yet, but is refactored here just in case:
                    case LOCAL_PREF_WRITE_MODE:

                        throw new Exception("Error 203 in Processor -- Local preference writing not yet implemented.");


                    default:
                        throw new Exception("Error 201 in Processor. ");

                }//end pref write mode switch


            }catch(Exception exc)
            {

                
                Log.d("error", "Error in Processor: " + exc.getMessage());
            }

        }

    }

    public static class QuickAdder
    {
        public static boolean quickAdd(Context context, String strData)
        {
            try
            {

                Log.d("quickadd", "QuickAdding now!\ncontent:\n" + strData);

                //if path set
                if( ! Processor.PrefWriter.isPrefSet(context, Processor.FILE_PATH_PREF_KEY))
                {
                    Toast.makeText(context, "Error - file not yet chosen!", Toast.LENGTH_SHORT).show();
                    throw new Exception("File not yet chosen.");
                }

                //load path from prefs
                String filePath = Processor.PrefWriter.readPref(context, Processor.FILE_PATH_PREF_KEY);
                Log.d("quickadd", "QuickAdd target path: " + filePath);

                //get data string ready:
                String strWrite = strData;

                if(strWrite == "")
                {
                    Toast.makeText(context, "Error - Content empty!", Toast.LENGTH_SHORT).show();
                    throw new Exception("Error 701 - Content box was empty!");
                }
                else
                {

                    //post-process data string
                    strWrite = Processor.process(strWrite, context);

                    //attempt quickadd:
                    Log.d("quickadd", "Attempting QuickAdd now\ndata to write:\n" + strWrite + "\nfile path:\n" + filePath);

                    if
                        //write to file
                    (appendToFile(context,filePath, strWrite))
                    {
                        //write success
                        Log.d("quickadd", "QuickAdd success!");

                    }else
                    {
                        //write fail
                        Log.d("quickadd", "quickadd failed - file write was not completed.");
                    }

                    //user success notification
                    Toast.makeText(context, "QuickAdded", Toast.LENGTH_SHORT).show();
                    Processor.vibrateSmol(context);

                    return true;

                }
                //end if data string empty




            }catch(Exception exc)
            {
                Log.d("error", "ERROR 421 in input dialog - " + exc.getMessage());
                return false;
            }
        }


        private static boolean appendToFile(Context context, String filePath, String strWrite)
        {
            Log.d("io", "Writing to file: " + filePath + "\ncontent:\n" + strWrite);

            try
            {
                //file from path
                File file = new File(filePath);

                FileOutputStream outputStream = new FileOutputStream(file, true);

                //file writer
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

                streamWriter.write(strWrite);

                streamWriter.flush();
                streamWriter.close();

                //return true for write success
                return true;


            }catch(Exception exc)
            {
                Log.d("TAG", "Error 141 writing to file - " + exc.getMessage());
                return false;
            }


        }

    }

    //end nested classes
    //===============================


    @RequiresApi(api = Build.VERSION_CODES.R)
    public static boolean[] checkBasicPermissions(Context context)
    {
        /*
        return format:
         { fileManage, write }
        */

        boolean fileManage = Environment.isExternalStorageManager();
        boolean write;
        
        
        int intWrite = ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE");
        if(intWrite == 0) //permission accepted
            write = true;
        else //permission not accepted
            write = false;

        Log.d("permission", "Permissions: \n" + "manage Environment files: " + fileManage + "\nwrite file: " + write);

        return new boolean[] { fileManage, write };

    }
    
    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Map<String, String> logAllPermissions(Context context)
    {
        boolean booFileManage = Environment.isExternalStorageManager();
        String fileManage;
        
        if(booFileManage) //permission accepted
            fileManage = "true";
        else
            fileManage = "false";
            
        boolean write;
        
        Map<String, String> perms = new Hashtable<>();


        perms.put(
                "external file manager\t",
        fileManage
        );

        perms.put(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                String.valueOf(ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE" )));
        perms.put(
                "android.permission.MANAGE_EXTERNAL_STORAGE",
                String.valueOf(ContextCompat.checkSelfPermission(context, "android.permission.MANAGE_EXTERNAL_STORAGE" )));
        perms.put(
                "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS",
                String.valueOf(ContextCompat.checkSelfPermission(context, "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS" )));
        perms.put(
                "android.permission.VIBRATE",
                String.valueOf(ContextCompat.checkSelfPermission(context, "android.permission.VIBRATE" )));
        perms.put(
                "android.permission.READ_USER_DICTIONARY",
                String.valueOf(ContextCompat.checkSelfPermission(context, "android.permission.READ_USER_DICTIONARY" )));
        perms.put(
                "android.permission.WRITE_USER_DICTIONARY",
                String.valueOf(ContextCompat.checkSelfPermission(context, "android.permission.WRITE_USER_DICTIONARY" )));

       String pstr = "\n\nPermissions:\n___________\n";



        for(String key : perms.keySet())
        {
            pstr += key + "\t-\t-\t-  " + perms.get(key) + "\n";
        }

        pstr += "\n___________\n";

        Log.d("permission", pstr);

        return perms;

        
    }


    public static String getClipBoard(Context context)
    {
        try
        {


            // Declaring the clipboard manager
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);

            boolean hasClip = clipboardManager.hasPrimaryClip();

            Log.d("TAG", "has clipboard contents: " + hasClip);

            if(hasClip)
            {
                ClipData clipData = clipboardManager.getPrimaryClip();


                ClipData.Item clipItem = clipData.getItemAt(0);

                String strClip = clipItem.getText().toString();

                Log.d("TAG", "got from clipboard: " + strClip);
                return strClip;

            }else
            {
                throw new Exception("Error 704 getclipboard - ");
            }

        }catch(Exception exc)
        {
            Log.d("TAG", "Error 703 - " + exc.getMessage());
            Toast.makeText(context, "Couldn't get clipboard.", Toast.LENGTH_SHORT).show();
            return null;
        }

    }



    public static boolean initPrefs(Context context)
    {
        //if prefs aren't yet set, sets the defaults

        boolean isSet_APPEND_PREF_KEY = PrefWriter.isPrefSet(context, APPEND_PREF_KEY);
        boolean isSet_PREPEND_PREF_KEY = PrefWriter.isPrefSet(context, PREPEND_PREF_KEY);
        boolean isSet_FILE_URI_PREF_KEY = PrefWriter.isPrefSet(context, FILE_URI_PREF_KEY);
        boolean isSet_FILE_NAME_PREF_KEY = PrefWriter.isPrefSet(context, FILE_NAME_PREF_KEY);

        if( ! isSet_APPEND_PREF_KEY)
        {
            Log.d("TAG", String.valueOf(APPEND_PREF_KEY) + " pref not saved - setting default.");

            setPostProcessingStr(context, "\\n\\n\\n---\\n\\n" , APPEND_PREF_CHOOSER);

        }
        if( ! isSet_PREPEND_PREF_KEY)
        {
            Log.d("TAG", String.valueOf(PREPEND_PREF_KEY) + " pref not saved - setting default values!");

            setPostProcessingStr(context, "\\n[!date] [!time]\\n\\n", PREPEND_PREF_CHOOSER);
        }
        if( ! isSet_FILE_URI_PREF_KEY)
        {
            //don't set a default
            Log.d("TAG", String.valueOf(FILE_URI_PREF_KEY) + " pref not saved - user must choose the file!");


        }
        if( ! isSet_FILE_NAME_PREF_KEY )
        {
            Log.d("TAG", String.valueOf(FILE_URI_PREF_KEY) + " pref not saved - setting default filename");
            Processor.PrefWriter.writePref(context, FILE_NAME_PREF_KEY, "NOTSET:" + FILE_NAME_PREF_KEY, DEFAULT_PREF_WRITE_MODE);
        }



        String APPEND_PR = PrefWriter.readPref(context, APPEND_PREF_KEY);
        String PREPEND_P = PrefWriter.readPref(context, PREPEND_PREF_KEY);
        String FILE_URI_ = PrefWriter.readPref(context, FILE_URI_PREF_KEY);
        String FIRST_RUN = PrefWriter.readPref(context, FIRST_RUN_PREF_KEY);

        return true;
    }

    public static void vibrateSmol(Context context)
    {
        try
        {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            // Vibrate for 50 milliseconds:
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else
            {
                //deprecated in API 26
                v.vibrate(50);
            }
        }catch(Exception exc)
        {
            Log.d("error", "VibrateSmol error - " + exc.getMessage());
        }
    }


    public static boolean setQuickAddFileLocation(Context context, String uriData, String fileName, String filePath)
    {
        PrefWriter.writePref(context, FILE_URI_PREF_KEY, uriData);
        PrefWriter.writePref(context, FILE_NAME_PREF_KEY, fileName);



        PrefWriter.writePref(context, FILE_PATH_PREF_KEY, filePath);

        return true;
    }

    static String getQuickAddFileLocation(Context context)
    {
        try
        {




            //load from default prefs:
            String strPrefValue = Processor.PrefWriter.readPref(context, Processor.FILE_URI_PREF_KEY);


            if(PrefWriter.isPrefSet(context, FILE_URI_PREF_KEY) == false)
            {
                throw new Exception("Error - File not yet set");
            }
            return strPrefValue;







        }catch(Exception exc)
        {

            
            Log.d("error", "Error in Processor: " + exc.getMessage());
            return null;

        }



    }




    public static boolean setPostProcessingStr(Context context, String strSaveData, int PREF_CHOOSER)
    {







        switch(PREF_CHOOSER)
        {


            case PREPEND_PREF_CHOOSER:

                PrefWriter.writePref(context, PREPEND_PREF_KEY, strSaveData, DEFAULT_PREF_WRITE_MODE);
                return true;


            case APPEND_PREF_CHOOSER:

                PrefWriter.writePref(context, APPEND_PREF_KEY, strSaveData, DEFAULT_PREF_WRITE_MODE);
                return true;




            default:

                
                Log.d("error", "Wacky error 311 in set pref in Processor");
                return false;


        }//end pref chooser switch



    }

    static String[] getSavedPostProcessingStrs(Context context)
    {
        try
        {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String ppPrepend = prefs.getString(PREPEND_PREF_KEY, null);//If there is no key found null will be the default value.
            String ppAppend = prefs.getString(APPEND_PREF_KEY, null);//If there is no key found null will be the default value.

            if (ppPrepend == null)
                throw new Exception("Error - No PostProcessing data set ");
            if (ppAppend == null)
                throw new Exception("Error - No PostProcessing data set ");

            return new String[] { ppPrepend, ppAppend };


        }catch(Exception exc)
        {

            
            Log.d("error", "Error 103 in Processor: " + exc.getMessage());
            return null;
        }

    }


    static String replaceVariables(String strIn, Context context)
    {

        String strOut =
                strIn.replace("[!date]", LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                        .replace("[!time]", LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mma")))
                        .replace("[!empty]", "");


        strOut = strOut.replace("\\n","\n");

        strOut = strOut.replace("NOTSET:", "n0ts3t");




        return strOut;
    }

    public static String process(String strIn, Context context)
    {

        String strOut = strIn;

        //postprocessing:

        //load the saved preference strings for postprocessing
        String[] postProcessingStrs = getSavedPostProcessingStrs(context);

        String strPrepend = postProcessingStrs[0];
        String strAppend = postProcessingStrs[1];

        //append and prepending  - (do first to capture the rest of the formatting)
        strOut = strPrepend + strOut + strAppend;

        strOut = replaceVariables(strOut, context);


        return strOut;
    }

}
