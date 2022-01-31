package com.example.obsidiancompanion.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obsidiancompanion.R;
import com.example.obsidiancompanion.classes.Processor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipperFragment extends PreferenceFragmentCompat
{


    private void updateDynamicPrefs()
    {
        String vName = Processor.PrefWriter.readPref(getContext(), Processor.VAULT_NAME_PREF_KEY);
        findPreference("prefZippingCategory").setSummary("Vault: " + vName);

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

        findPreference("prefCompressTest").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {

                String vaultPath = Processor.PrefWriter.readPref(getContext(), Processor.VAULT_FOLDER_PATH_PREF_KEY);

                Path pathTargetDir = Paths.get(vaultPath);

                try
                {
                    Processor.Zipper.zipFolder(pathTargetDir);

                }catch(Exception exc)
                {
                    Log.d("T", "error - " + exc.getMessage() );
                    return false;
                }

                Log.d("TAG", "zip success");
                return true;

            }
        });

    }

}