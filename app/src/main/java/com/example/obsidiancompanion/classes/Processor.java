package com.example.obsidiancompanion.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Processor
{

    static String getSavedFileLocation(Context context)
    {
        try
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String strData = prefs.getString("obsCom_appendContentUri", null);//If there is no key found null will be the default value.

            if (strData == null)
                throw new Exception("Error - File not yet set");

            return strData;



        }catch(Exception exc)
        {
            Log.d("error", "Error in Processor: " + exc.getMessage());
            return null;

        }



    }


    static String[] getSavedPostProcessingStrs(Context context)
    {
        try
        {


            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String ppPrepend = prefs.getString("obsCom_ppPrepend", null);//If there is no key found null will be the default value.
            String ppAppend = prefs.getString("obsCom_ppAppend", null);//If there is no key found null will be the default value.

            if (ppPrepend == null)
                throw new Exception("Error - No PostProcessing data set ");
            if (ppAppend == null)
                throw new Exception("Error - No PostProcessing data set ");

            return new String[] { ppPrepend, ppAppend };


        }catch(Exception exc)
        {
            Log.d("error", "Error in Processor: " + exc.getMessage());
            return null;

        }

    }


    static String replaceVariables(String strIn, Context context)
    {
        //todo: format these
        String strOut =
                strIn.replace("[!date]", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE))
        .replace("[!time]", LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_TIME));

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
