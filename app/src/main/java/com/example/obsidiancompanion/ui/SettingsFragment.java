package com.example.obsidiancompanion.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.obsidiancompanion.MainActivity;
import com.example.obsidiancompanion.R;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;




import java.io.File;


public class SettingsFragment extends PreferenceFragmentCompat
{

    final int REQUEST_PERMISSIONS = 19;

    final int CHOOSE_APPEND_FILE = 20;

    final int SET_PREPEND_DATA = 21;
    final int SET_APPEND_DATA = 22;





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

                String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.MANAGE_EXTERNAL_STORAGE", "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS"};


                requestPermissions(perms, REQUEST_PERMISSIONS);

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
                chooseFile.setType("text/plain");


                chooseFile.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                chooseFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                chooseFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                 startActivityForResult(chooseFile, CHOOSE_APPEND_FILE);

                return true;
            }
        });

/*
        findPreference("prefQaPrepend").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                return false;
            }
        });


        findPreference("prefQaAppend").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                return false;
            }
        });
*/
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


default: break;




        }//ends switch of activityResult requestCode

















    } //end activity result





            /*
        permission result
         */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch(requestCode)
        {
            case REQUEST_PERMISSIONS:


            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            int result = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
            //int result1 = ContextCompat.checkSelfPermission(getContext(), MANAGE_EXTERNAL_STORAGE);

            if(result == 0)
            {
                Toast.makeText(getContext(), "permission accepted", Toast.LENGTH_SHORT).show();
                Log.d("", "permission accepted ");
            }
            else if(result == -1)
            {
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_SHORT).show();
                Log.d("", "permission denied ");
            }

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