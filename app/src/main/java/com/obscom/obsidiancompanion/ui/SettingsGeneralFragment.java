package com.obscom.obsidiancompanion.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.obscom.obsidiancompanion.R;
import com.obscom.obsidiancompanion.classes.Processor;


public class SettingsGeneralFragment extends PreferenceFragmentCompat
{

    final int RESULT_REQUEST_PERMISSIONS = 19;
    final int RESULT_REQUEST_ALL_FILE_PERMISSION = 18;
    final int RESULT_SET_VAULT_FOLDER = 20;


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void updateDyanamicPrefs()
    {
        //get the saved permissions to write permission statuses to the permission button:
        boolean[] basicPermissions = Processor.checkBasicPermissions(getContext());
        boolean allFilesManagePerm = basicPermissions[0];
        boolean fileWritePerm = basicPermissions[1];

        findPreference("prefRequestPermissions").setSummary("\nCurrent permissions:\n" +
                " - Manage all files: " + allFilesManagePerm + "\n" +
                " - Write files: " + fileWritePerm);



        findPreference("prefSetVaultFolder").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);

                //dont think we need content permissions, just choosing the path
                chooseFile.putExtra(DocumentsContract.EXTRA_PROMPT, "Allow Write Permission");

                //to set starting dir - intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uriToLoad);

                //is this even how to do this for a folder?
                //chooseFile.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);

                startActivityForResult(chooseFile, RESULT_SET_VAULT_FOLDER);

                return true;
            }
        });



        findPreference("prefGitHubButton").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent githubIntent = new Intent(Intent.ACTION_VIEW);
                githubIntent.setData(Uri.parse("https://github.com/cheeseonamonkey/ObsidianCompanion"));
                startActivity(githubIntent);

                return true;
            }
        });

        findPreference("prefRequestPermissions").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {

                boolean[] basicPermissions = Processor.checkBasicPermissions(getContext());
                boolean allFilesManagePerm = basicPermissions[0];
                boolean fileWritePerm = basicPermissions[1];

                if (!allFilesManagePerm) //all files manager permission NOT granted
                {

                    Intent filePermissionIntent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(filePermissionIntent, RESULT_REQUEST_ALL_FILE_PERMISSION);

                    Toast.makeText(getContext(), "File manage permission already granted!", Toast.LENGTH_SHORT).show();

                } else //file write permission is NOT granted
                {

                    //request these permissions either way

                    String[] perms = {
                            "android.permission.WRITE_EXTERNAL_STORAGE",
                            "android.permission.MANAGE_EXTERNAL_STORAGE",
                            "android.permission.BROADCAST_CLOSE_SYSTEM_DIALOGS",
                            "android.permission.ACTION_OPEN_DOCUMENT",
                            "android.permission.READ_USER_DICTIONARY",
                            "android.permission.WRITE_USER_DICTIONARY"
                    };
                    requestPermissions(perms, RESULT_REQUEST_PERMISSIONS);

                }

                //other permissions requested after the result

                return true;
            }
        });

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        Log.d("initPrefs", "General tab frag load - checking initPrefs for all pref defaults");

        Processor.initPrefs(getContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences_general, rootKey);


        updateDyanamicPrefs();



        //
        //request permissions

        /*  ____________________  */
        /*  permission listener:  */
        //listener is set in the updatePermissionButton() method now
        //result of it displaying data and remaining clickable
        //findPreference("prefRequestPermissions").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        //




        /*
        //debug test storage
        findPreference("prefTestStorage").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                File dir = getContext().getExternalFilesDir("\\");


                log("absolutepath-" + dir.getAbsolutePath().toString());
                log("path-" + dir.getPath().toString());
                log("canwrite-" +String.valueOf(dir.canWrite()));
                log("exists-" + String.valueOf(dir.exists()));
                log("parents parent-" + String.valueOf(dir.getParentFile().getParentFile()));

                for(String s : dir.list())
                {
                    log("\nlst- " + s.toString());
                }

                return true;
            }
        });
        */

        /*
        findPreference("prefTestFile").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                //testing file path stuff

                try
                {

                    /*

                    File file = new File("/sdcard/Documents/testo.md");
                    log("test write to: " + file.getPath());

                    FileOutputStream outputStream = new FileOutputStream(file, true);

                    //file writer
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

                    streamWriter.write("asdfasdf\n12345\nHello!\n12345\nasdfasdf - ");

                    streamWriter.flush();
                    streamWriter.close();

                    Toast.makeText(getContext(), "wrote some nonsense to test file", Toast.LENGTH_SHORT).show();



                } catch (Exception exc)
                {
                    log("error 901 - " + exc.getMessage());
                }


                return true;
            }
        });
        /*




    public void log(String a)
    {
        Log.d("quicklog", a);
    }



        */

    }//end on create prefs






        /*                     */
        /*  Activity results:  */

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode)
        {


            //
            //ActivityResult: ALL_FILES_ACCESS_PERMISSION
            case RESULT_REQUEST_ALL_FILE_PERMISSION:



                Log.d("TAG", "ActivityResult - File Manager permission");

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

                requestPermissions(perms, RESULT_REQUEST_PERMISSIONS);


                updateDyanamicPrefs();

                break;
                //end all file permission result




            /*  ---------------------------------  */
            /*  Activity result: Set vault folder  */
            case RESULT_SET_VAULT_FOLDER:

                if(data != null)
                {
                    Uri contentUri = data.getData();

                    //extract real path from uri:
                    String dirPath = contentUri.getPath();
                    String strPath = "/" + dirPath.split(":")[1];

                    //Log.d("TAG", "path - " + strPath);
                    //Log.d("TAG", "name - " + strVaultName);

                    Processor.setVaultFolderPath(getContext(), strPath);

                    Log.d("a", "Vault Folder set");
                    Toast.makeText(getContext(), "Vault folder set", Toast.LENGTH_SHORT).show();
                }
                break;
                //end vault folder result


    default: break;




        }//ends switch of result code

















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
            case RESULT_REQUEST_PERMISSIONS:

                Log.d("tag", "ActivityResult - general permissions");

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);


                Processor.logAllPermissions(getContext());

                boolean writePerm = Processor.checkBasicPermissions(getContext())[1];

                if(writePerm)
                {
                    Toast.makeText(getContext(), "Accepted write permission!", Toast.LENGTH_SHORT).show();
                    Log.d("permission", "write permission accepted ");
                }
                else
                {
                    Toast.makeText(getContext(), "Declined write permission!", Toast.LENGTH_SHORT).show();
                    Log.d("permission", "write permission denied ");
                }


                updateDyanamicPrefs();


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