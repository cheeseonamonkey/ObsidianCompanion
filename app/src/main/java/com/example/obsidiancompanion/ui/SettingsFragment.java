package com.example.obsidiancompanion.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.obsidiancompanion.MainActivity;
import com.example.obsidiancompanion.R;






import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class SettingsFragment extends PreferenceFragmentCompat
{

    final int REQUEST_PERMISSIONS = 19;
    final int REQUEST_ALL_FILE_PERMISSION = 18;

    final int CHOOSE_APPEND_FILE = 20;

    final int SET_PREPEND_PP = 21;
    final int SET_APPEND_PP = 22;

    //todo: refactor preference saving/loading into a (static?) util class



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey);


        //
        //request permissions
        findPreference("prefRequestPermissions").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {

                Intent filePermissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(filePermissionIntent, REQUEST_ALL_FILE_PERMISSION);


                //other permissions requested after the result



                return true;
            }
        });




        //
        //set appending file
        findPreference("prefSetDiscreetFile").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                chooseFile.setType("text/markdown");



                //chooseFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                chooseFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                chooseFile.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                //chooseFile.addFlags(Intent.flag);


                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                 startActivityForResult(chooseFile, CHOOSE_APPEND_FILE);

                return true;
            }
        });



        //
        //set prepend postprocessing
        findPreference("prefQaPrepend").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                return true;
            }
        });



        //
        //set append postprocessing
        findPreference("prefQaAppend").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                return true;
            }
        });



    }







    /*
    activity result
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch(requestCode)
        {

            //
            //choose QuickAdd file
            case CHOOSE_APPEND_FILE:


        /*
        information on chosen URI that might be useful later:

        String src = uri.getPath();
        Log.d("a", "GETPATH" + src);

        // Get uri related document id.
        String documentId = DocumentsContract.getDocumentId(uri);
        Log.d("a", "doc id - " + documentId);

        // Get uri authority.
        String uriAuthority = uri.getAuthority();
        Log.d("a", "uriauthority" + uriAuthority);

        File f = Environment.getExternalStorageDirectory();
        Log.d("t", "external storage dir1: " + f.getAbsolutePath());
        Log.d("t", "external storage dir2: " + f.getPath());

        File f2 = new File(uri.toString());
        Log.d("asdf", "file 2 output - " + f2.getAbsolutePath());

        */

                //file pick:
                super.onActivityResult(requestCode, resultCode, data);


                Uri contentUri = data.getData();
                Log.d("a", "GETDATA " + data.getData().toString());

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();

                editor.putString("obsCom_appendContentUri", contentUri.toString());
                editor.commit();

                //debug to make sure pref got saved
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                String name = prefs.getString("obsCom_appendContentUri", null);//If there is no YOURKEY found null will be the default value.
                Log.d("TAG", "saved appendContentUri to default settingPrefs: " + name);
                //


              break;
              //end choose QuickAdd file


            //
            //prepend post processing changed
            case SET_PREPEND_PP:

                //you are here - just set these PP vars up











                break;
            //end prepend pp case



            //
            //append post processing changed
            case SET_APPEND_PP:

                break;
            //end append pp case









            //
            //ActivityResult: ALL_FILES_ACCESS_PERMISSION
            case REQUEST_ALL_FILE_PERMISSION:

                Log.d("TAG", "ActivityResult - All file permission");



                String[] perms = {
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.MANAGE_EXTERNAL_STORAGE",
                        "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS",
                        "android.permission.ACTION_OPEN_DOCUMENT",
                        "android.permission.MANAGE_MEDIA"
                                };

                requestPermissions(perms, REQUEST_PERMISSIONS);



                break;
                //end all file permission











default: break;



        }//ends switch of activityResult requestCode

















    } //end activity result





            /*
        permission result
         */

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case REQUEST_PERMISSIONS:


            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            List<String> results = new ArrayList<>();

            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_EXTERNAL_STORAGE")));
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.MANAGE_EXTERNAL_STORAGE")));
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS")));
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.ACTION_OPEN_DOCUMENT" )));
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.MANAGE_MEDIA" )));


            results.add(String.valueOf(Environment.isExternalStorageManager()));





String strPermissions = "";

                Log.d("TAG", "Permissions:");
                for (int i = 0; i < results.size(); i++)
                {
                    strPermissions +=  results.get(i).toString() + ", " ;
                }

                Log.d("TAG", strPermissions);


/*
todo: make this toast when permissions are all figured out

            if(results.get(0) == 0)
            {
                Toast.makeText(getContext(), "permission accepted", Toast.LENGTH_SHORT).show();
                Log.d("", "permission accepted ");
            }
            else if(results.get(0) == -1)
            {
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_SHORT).show();
                Log.d("", "permission denied ");
            }
*/

            break;

            default: break;
        }//end request code switch


    }//end permission result




    @Nullable
    @Override
    public <T extends Preference> T findPreference(@NonNull CharSequence key)
    {
        return super.findPreference(key);
    }
}