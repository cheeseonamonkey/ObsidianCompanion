package com.example.obsidiancompanion.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Processor
{

    public final static int PREPEND_PREF_CHOOSER = 11;
    public final static int APPEND_PREF_CHOOSER = 12;

    public final static int DEFAULT_PREF_WRITE_MODE = 21;
    public final static int LOCAL_PREF_WRITE_MODE = 22;

    public static class PrefWriter
    {
        public static final String APPEND_PREF_KEY = "obscom_ppAppend";
        public static final String PREPEND_PREF_KEY = "obscom_ppPrepend";
        public static final String FILE_URI_PREF_KEY = "obscom_QuickAddFileUri";

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
                        String debugCallbackData = prefs.getString(prefKey, null);//If there is no YOURKEY found null will be the default value.

                        if(debugCallbackData == null)
                            throw new Exception("Error 101 in Processor. ");

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


    static String getQuickAddFileLocation(Context context)
    {
        try
        {
            //load from default prefs:
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String strData = prefs.getString("obsCom_QuickAddFileUri", null);//If there is no key found null will be the default value.

            if (strData == null)
                throw new Exception("Error - File not yet set");

            return strData;



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

                PrefWriter.writePref(context, PrefWriter.PREPEND_PREF_KEY, strSaveData, DEFAULT_PREF_WRITE_MODE);
                return true;


            case APPEND_PREF_CHOOSER:

                PrefWriter.writePref(context, PrefWriter.APPEND_PREF_KEY, strSaveData, DEFAULT_PREF_WRITE_MODE);
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
            String ppPrepend = prefs.getString(PrefWriter.PREPEND_PREF_KEY, null);//If there is no key found null will be the default value.
            String ppAppend = prefs.getString(PrefWriter.APPEND_PREF_KEY, null);//If there is no key found null will be the default value.

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
        //todo: format these
        String strOut =
                strIn.replace("[!date]", LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")))
                        .replace("[!time]", LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mma")));

        return strOut;
    }

    public static String process(String strIn, Context context)
    {

        //postprocessing:
        String strOut = strIn;

        //read the saved preference strings to postprocess
        String[] postProcessingStrs = getSavedPostProcessingStrs(context);

        String strPrepend = postProcessingStrs[0];
        String strAppend = postProcessingStrs[1];

        strOut = strPrepend + strOut + strAppend;

        strOut = replaceVariables(strOut, context);


        return strOut;
    }

}
