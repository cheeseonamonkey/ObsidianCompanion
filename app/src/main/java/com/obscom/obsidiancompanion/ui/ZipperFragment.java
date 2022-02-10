package com.obscom.obsidiancompanion.ui;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.os.Environment;
import android.util.Log;

import com.obscom.obsidiancompanion.R;
import com.obscom.obsidiancompanion.classes.Processor;

import java.io.File;

public class ZipperFragment extends PreferenceFragmentCompat
{


    private void updateDynamicPrefs()
    {
        String vName = Processor.PrefWriter.readPref(getContext(), Processor.VAULT_NAME_PREF_KEY);
        findPreference("prefZippingCategory").setSummary("Vault: " + vName);
        findPreference("prefExtractingCategory").setSummary("Vault: " + vName);


    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d("initPrefs", "Zipper tab frag load - checking initPrefs for all pref defaults");

        Processor.initPrefs(getContext());


    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences_zipper, rootKey);

        updateDynamicPrefs();

        findPreference("prefCompressTest").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {


                //okay
                //maybe lets just request access to parent folder
                //immediately after the folder choosing, open new chooser starting at the parent folder and have the user select it

                String vaultPath = Processor.PrefWriter.readPref(getContext(), Processor.VAULT_FOLDER_PATH_PREF_KEY);

                String pathRead = Environment.getRootDirectory().toPath().toString();
                String externalUri=  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toURI().toString();

                Log.d("", "pathread - " + pathRead);
                Log.d("", "externalUri - " + externalUri);

                try
                {
                    Processor.Zipper.zipWrite(new File(vaultPath), new File(vaultPath).getParentFile());

                }catch(Exception exc)
                {
                    Log.d("T", "zip error 325 - " + exc.getMessage() );
                    return false;
                }

                Log.d("TAG", "zip success - path: not set" );
                return true;

            }
        });

    }

}