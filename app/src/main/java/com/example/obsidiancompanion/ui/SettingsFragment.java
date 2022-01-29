package com.example.obsidiancompanion.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.obsidiancompanion.R;
import com.example.obsidiancompanion.classes.Processor;


import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends PreferenceFragmentCompat
{

    final int REQUEST_PERMISSIONS = 19;
    final int REQUEST_ALL_FILE_PERMISSION = 18;

    final int CHOOSE_APPEND_FILE = 20;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("TAG", "onCreate: page load - initing all prefs...");

        Processor.initPrefs(getContext());

    }

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

                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);



                chooseFile = Intent.createChooser(chooseFile, "Choose a file");

                 startActivityForResult(chooseFile, CHOOSE_APPEND_FILE);

                return true;
            }
        });






        //
        //set prepend postprocessing
        findPreference("prefQaPrepend").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {


                Processor.setPostProcessingStr(getContext(), newValue.toString(), Processor.PREPEND_PREF_CHOOSER);

                return true;

            }
        });


        //
        //set append postprocessing
        findPreference("prefQaAppend").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                Processor.setPostProcessingStr(getContext(), newValue.toString(), Processor.APPEND_PREF_CHOOSER);

                return true;
            }
        });



    }







    /*
    activity result
     */

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        switch(requestCode)
        {

            //
            //choose QuickAdd file
            case CHOOSE_APPEND_FILE:


        /*
        information on chosen URI

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

                //file pick
                super.onActivityResult(requestCode, resultCode, data);




                //URI:
                Uri contentUri = data.getData();
                Log.d("a", "uri - " + data.getData().toString());

                String[] strDirs = contentUri.getPath().split("/");
                String srcDir = strDirs[strDirs.length - 1];
                srcDir = srcDir.replace(".md", "");

                //save to prefs
                Processor.setQuickAddFileLocation(getContext(), contentUri.toString(), srcDir);


                Toast.makeText(getContext(), "File set!", Toast.LENGTH_SHORT).show();

              break;
              //end choose QuickAdd file







            //
            //ActivityResult: ALL_FILES_ACCESS_PERMISSION
            case REQUEST_ALL_FILE_PERMISSION:



                Log.d("TAG", "ActivityResult - All files permission");

                if(Environment.isExternalStorageManager())
                {
                    Toast.makeText(getContext(), "Accepted storage permission!", Toast.LENGTH_SHORT).show();
                    Log.d("permission", "storage permission accepted ");
                }
                else
                {
                    Toast.makeText(getContext(), "Declined storage permission!", Toast.LENGTH_SHORT).show();
                    Log.d("permission", "storage permission denied ");
                }


                String[] perms = {
                        "android.permission.WRITE_EXTERNAL_STORAGE",
                        "android.permission.MANAGE_EXTERNAL_STORAGE",
                        "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS",
                        "android.permission.ACTION_OPEN_DOCUMENT",
                        "android.permission.READ_USER_DICTIONARY",
                        "android.permission.WRITE_USER_DICTIONARY"
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
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.VIBRATE")));
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.READ_USER_DICTIONARY")));
            results.add(String.valueOf(ContextCompat.checkSelfPermission(getContext(), "android.permission.WRITE_USER_DICTIONARY")));

                if(results.size() > 0 && results.get(0) == String.valueOf(0))
                {
                    Toast.makeText(getContext(), "Accepted write permission!", Toast.LENGTH_SHORT).show();
                    Log.d("permission", "write permission accepted ");
                }
                else
                {
                    Toast.makeText(getContext(), "Declined write permission!", Toast.LENGTH_SHORT).show();
                    Log.d("permission", "write permission denied ");
                }

            results.add(String.valueOf(Environment.isExternalStorageManager()));





                String strPermissions = "";


                for (int i = 0; i < results.size(); i++)
                {
                    strPermissions +=  results.get(i).toString() + ", " ;
                }

                Log.d("TAG", "Permissions:");
                Log.d("TAG", strPermissions);






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