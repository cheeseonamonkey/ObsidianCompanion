package com.obscom.obsidiancompanion.classes;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Processor
{

    public final static int PREPEND_PREF_CHOOSER = 11;
    public final static int APPEND_PREF_CHOOSER = 12;

    public final static int DEFAULT_PREF_WRITE_MODE = 21;
    public final static int LOCAL_PREF_WRITE_MODE = 22;

    //preference keys:
    //default prefs are readable by all running android apps and services
    public static final String APPEND_PREF_KEY = "obscom_ppAppend";
    public static final String PREPEND_PREF_KEY = "obscom_ppPrepend";
    public static final String FILE_URI_PREF_KEY = "obscom_QuickAddFileUri";
    public static final String FIRST_RUN_PREF_KEY = "obscom_QuickAddFileUri";
    public static final String FILE_NAME_PREF_KEY = "obscom_QuickAddFileName";
    public static final String FILE_PATH_PREF_KEY = "obscom_QuickAddFilePath";
    public static final String DEFAULT_TAB_PREF_KEY = "obscom_DefaultTab";

    public static final String VAULT_FOLDER_PATH_PREF_KEY = "obscom_VaultFolderPath";
    public static final String VAULT_PARENT_FOLDER_PATH_PREF_KEY = "obscom_VaultParentFolderPath";
    public static final String VAULT_NAME_PREF_KEY = "obscom_VaultName";
    public static final String VAULT_SIZE_PREF_KEY = "obscom_VaultSize";

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
                if(str == null || str.contains("NOT SET") || str.contains("NOTSET"))
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

    public static class Zipper
    {

        // zip a directory, including sub files and sub directories
        private static boolean zipFolder(File source, File destination, Stack<File> stackOfFilesToBeZipped, ZipOutputStream zipOutputStream) throws IOException
        {
            Log.d("zipFolder", "zipFolder: attempting to zip folder:\n__________\n" +
                    "source: " + source.toString() + "\n" +
                    "source: " + stackOfFilesToBeZipped.toString() + "\n" +
                    "source: " + zipOutputStream.toString()
            );

            try
            {
                //todo: if a file zip fails, notify user and keep going - individual error handling
                while (!stackOfFilesToBeZipped.isEmpty())
                {
                    //if completed zipping
                    if (stackOfFilesToBeZipped.empty())
                    {
                        zipOutputStream.close();
                        return true;
                    }

                    // zip file
                    zipFile(stackOfFilesToBeZipped.pop(), source, stackOfFilesToBeZipped, zipOutputStream);
                    return true;
                }
            }catch(Exception exc)
            {
                Log.d("zipWrite", "zipWrite 324 error - " + exc.getMessage().toString());
                return false;
            }

                return false;

        }

        private static boolean zipFile(File file, File source, Stack<File> stackOfFilesToBeZipped, ZipOutputStream zipOutputStream ) throws IOException
        {
            try
            {
                int BUFFER_SIZE = 1024 * 8;
                int EOF = -1;
                int OFFSET_START = 0;
                int SUBSTRING_OFFSET = 1;

                String relativePath = file.getAbsolutePath().replace(source.getAbsolutePath(), "");

                if (relativePath.isEmpty())
                {
                    // relativePath will be empty for a single file
                    // so to fix that we must add only that file's filename
                    relativePath = source.getName();
                } else
                {
                    // we do not want the forward slash /
                    // so for all other path we have to trim out the first character i.e. /
                    relativePath = relativePath.substring(SUBSTRING_OFFSET);
                }

                ZipEntry zipEntry = new ZipEntry(relativePath);
                zipOutputStream.putNextEntry(zipEntry);

                // write the data to output file
                final FileInputStream fileInputStream = new FileInputStream(file);
                final byte[] buffer = new byte[BUFFER_SIZE];
                int readBytes;

                while (true)
                {
                    readBytes = fileInputStream.read(buffer);
                    if (readBytes == EOF) break;
                    zipOutputStream.write(buffer, OFFSET_START, readBytes);
                }

                fileInputStream.close();
                zipOutputStream.closeEntry();

                return true;

            }catch(Exception exc)
            {
                Log.d("zipWrite", "zipWrite 316 error - " + exc.getMessage().toString());
                return false;
            }


        }

        public static boolean zipWrite(File source, File destination)
        {
            try
            {
                final ZipOutputStream zipOutputStream;

                final Stack<File> stackOfFilesToBeZipped;


                // the source file and the destination file
                // can be a single file or a directory

                final FileOutputStream fileOutputStream = new FileOutputStream(destination);
                zipOutputStream = new ZipOutputStream(fileOutputStream);

                // the level of compression can be either of 2; ZipEntry.STORED or ZipEntry.DEFLATED
                zipOutputStream.setLevel(ZipEntry.STORED);

                stackOfFilesToBeZipped = new Stack<>();

                // count total files to be zipped
                recursivelyAddFilesToStack(source, stackOfFilesToBeZipped);

                Log.d("zipWrite", "there are " + stackOfFilesToBeZipped.size() + " files to be zipped.");

                zipFolder(source, destination, stackOfFilesToBeZipped,zipOutputStream);


                return true;


            }catch(Exception exc)
            {
                Log.d("zipWrite", "zipWrite 317 error - " + exc.getMessage().toString());
                return false;
            }

        }
    }

    private static void recursivelyAddFilesToStack(File file, Stack<File> stackOfFilesToBeZipped) {

        if (file.isFile())
        {
            stackOfFilesToBeZipped.add(file);
            return;
        }

        File[] files = file.listFiles();
        if (files == null)
        {
            // directory is empty
            return;
        }

        for (File item : files)
        {
            recursivelyAddFilesToStack(item, stackOfFilesToBeZipped);
        }

    }




    public static void setVaultFolderPath(Context context, String strPath)
    {
        String strVaultNamesTemp[] = strPath.split("/");
        String strVaultName = strVaultNamesTemp[strVaultNamesTemp.length - 1];



        //todo: file size

        PrefWriter.writePref(context, VAULT_FOLDER_PATH_PREF_KEY, strPath);
        PrefWriter.writePref(context, VAULT_NAME_PREF_KEY, strVaultName);
        //VAULT_SIZE_PREF_KEY

    }



    public static void setDefaultTab(Context context, int defaultTabValue)
    {


        PrefWriter.writePref(context, DEFAULT_TAB_PREF_KEY, String.valueOf(defaultTabValue));
    }

    public static boolean initPrefs(Context context)
    {
        //if prefs aren't yet set, sets the defaults
        Map<String, Boolean> prefsSet = new Hashtable<>();

        prefsSet.put("APPEND_PREF_KEY", PrefWriter.isPrefSet(context, APPEND_PREF_KEY));
        prefsSet.put("PREPEND_PREF_KEY", PrefWriter.isPrefSet(context, PREPEND_PREF_KEY));
        prefsSet.put("FILE_URI_PREF_KEY", PrefWriter.isPrefSet(context, FILE_URI_PREF_KEY));
        prefsSet.put("FILE_PATH_PREF_KEY", PrefWriter.isPrefSet(context, FILE_PATH_PREF_KEY));
        prefsSet.put("FILE_NAME_PREF_KEY", PrefWriter.isPrefSet(context, FILE_NAME_PREF_KEY));
        prefsSet.put("DEFAULT_TAB_PREF_KEY", PrefWriter.isPrefSet(context, DEFAULT_TAB_PREF_KEY));
        prefsSet.put("VAULT_FOLDER_PATH_PREF_KEY", PrefWriter.isPrefSet(context, VAULT_FOLDER_PATH_PREF_KEY));
        prefsSet.put("VAULT_NAME_PREF_KEY", PrefWriter.isPrefSet(context, VAULT_NAME_PREF_KEY));





        for(String key : prefsSet.keySet())
        {

            //if a pref isn't set
            if(prefsSet.get(key) != null && prefsSet.get(key) == false)
            {


                switch (key)
                {

                    case "APPEND_PREF_KEY":
                        Log.d("initPref", String.valueOf(APPEND_PREF_KEY) + " pref not set - setting default: " + "\\n\\n\\n---\\n\\n");
                        Processor.PrefWriter.writePref(context, "\\n\\n\\n---\\n\\n" , APPEND_PREF_KEY);
                        break;

                    case "PREPEND_PREF_KEY":
                        Log.d("initPref", String.valueOf(PREPEND_PREF_KEY) + " pref not set - setting default: " + "\\n[!date] [!time]\\n\\n");
                        Processor.PrefWriter.writePref(context, "\\n[!date] [!time]\\n\\n", PREPEND_PREF_KEY);
                        break;



/*
            I think higher-level error handling is already doing all this, with user notification

                case "FILE_URI_PREF_KEY":
                    Log.d("initPref", String.valueOf(FILE_URI_PREF_KEY) + " pref not set - setting default: " + "" );
                    setPostProcessingStr(context, "" , APPEND_PREF_CHOOSER);
                    break;


                case "FILE_PATH_PREF_KEY":
                    Log.d("initPref", String.valueOf(FILE_PATH_PREF_KEY) + " pref not set - setting default: " + "\\n[!date] [!time]\\n\\n" );
                    setPostProcessingStr(context, "" , APPEND_PREF_CHOOSER);
                    break;
*/

                    case "FILE_NAME_PREF_KEY":
                        Log.d("initPref", String.valueOf(FILE_NAME_PREF_KEY) + " pref not set - setting default: " + "FILE NOT SET");
                        Processor.PrefWriter.writePref(context, Processor.FILE_NAME_PREF_KEY, "FILE NOT SET");
                        break;

                    case "DEFAULT_TAB_PREF_KEY":
                        Log.d("initPref", String.valueOf(DEFAULT_TAB_PREF_KEY) + " pref not set - setting default: " + "0");
                        Processor.PrefWriter.writePref(context, Processor.DEFAULT_TAB_PREF_KEY, "0");
                        break;

                    case "VAULT_FOLDER_PATH_PREF_KEY":
                        Log.d("initPref", String.valueOf(VAULT_FOLDER_PATH_PREF_KEY) + " pref not set - setting default: " + "\\");
                        Processor.PrefWriter.writePref(context, Processor.VAULT_FOLDER_PATH_PREF_KEY, "/");
                        break;

                    case "VAULT_NAME_PREF_KEY":
                        Log.d("initPref", String.valueOf(VAULT_NAME_PREF_KEY) + " pref not set - setting default: " + "\\");
                        Processor.PrefWriter.writePref(context, Processor.VAULT_NAME_PREF_KEY, "VAULT NOT SET");
                        break;


                    default:
                        break;
                }//end switch


            }else if (prefsSet.get(key) != null)   //the pref is already set
            {
                Log.d("initPrefs", "initPref - no default, pref already set");

                //wacky xml error does not like this
                //prefsSet.remove(key);

            }
        }


        return true;
    }

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
