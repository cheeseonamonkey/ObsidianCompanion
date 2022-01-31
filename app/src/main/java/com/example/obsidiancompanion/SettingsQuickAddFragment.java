package com.example.obsidiancompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.obsidiancompanion.classes.Processor;

import java.io.File;
import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsQuickAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsQuickAddFragment extends PreferenceFragmentCompat
{


    final int RESULT_CHOOSE_APPEND_FILE = 20;


    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsQuickAddFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuickAddFragment.
     */
    // Rename and change types and number of parameters
    public static SettingsQuickAddFragment newInstance(String param1, String param2)
    {
        SettingsQuickAddFragment fragment = new SettingsQuickAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        Log.d("initPrefs", "QuickAdd tab frag load - checking initPrefs for all pref defaults");

        Processor.initPrefs(getContext());


    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences_quickadd, rootKey);




        //
        //set appending file
        findPreference("prefSetQuickAddFile").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);


                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
                //chooseFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                chooseFile.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                chooseFile.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                chooseFile.setType("text/markdown");

                chooseFile = Intent.createChooser(chooseFile, "Choose a file");

                startActivityForResult(chooseFile, RESULT_CHOOSE_APPEND_FILE);

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




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);



switch(requestCode)
{
    //
    //choose QuickAdd file
    case RESULT_CHOOSE_APPEND_FILE:


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


        if(data != null)
        {
            //URI:
            Uri contentUri = data.getData();


            //persistent uri access:
            final int takeFlags = data.getFlags();

            getContext().getContentResolver().takePersistableUriPermission(contentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //Log.d("TAG", "(persistable) flags: " + takeFlags);


            //extract data from uri:
            String[] strDirs = contentUri.getPath().split("/");
            String srcFileName = strDirs[strDirs.length - 1];
            srcFileName = srcFileName.replace(".md", "");

            //extract real path from uri:
            final String docId = DocumentsContract.getDocumentId(contentUri);
            final String[] split = docId.split(":");
            final String type = split[0];

            //path string:
            String pathA = System.getenv("EXTERNAL_STORAGE") + "/" + split[1];
            String pathB = Environment.getExternalStorageDirectory() + "/" + split[1];

            //set real path
            String scrFilePath = pathA;
            //String scrFilePath = pathB;


            log("picked file:\n_________________\n" +
                    "content uri - " + contentUri.toString() + "\n" + "filename - " + srcFileName + "\n" + "" +
                    "filePath - " + scrFilePath + "\n" +
                    "doc id - " + docId + "\n" +
                    "path a - " + pathA + "\n" +
                    "path b - " + pathB + "\n" +
                    "_________________");


            //save to prefs
            Processor.setQuickAddFileLocation(getContext(), contentUri.toString(), srcFileName, scrFilePath);


            Toast.makeText(getContext(), "File set!", Toast.LENGTH_SHORT).show();
        }
        break;
    //end choose QuickAdd file






    default: break;
}


    }

    public void log(String a)
    {
        Log.d("quicklog", a);
    }



}