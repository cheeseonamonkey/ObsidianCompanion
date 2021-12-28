package com.example.obsidiancompanion.ui.homeTab;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.obsidiancompanion.R;
import com.example.obsidiancompanion.databinding.SettingsTabFragmentBinding;

public class SettingsTabFragment extends Fragment
{

    private SettingsTabViewModel mViewModel;
    SettingsTabFragmentBinding binding;

    public static SettingsTabFragment newInstance()
    {
        return new SettingsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        //Inflate the layout for this fragment:
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//private Binding binding;

        binding = SettingsTabFragmentBinding.inflate(getLayoutInflater(), container,false);

        View view = binding.getRoot();

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//=========
//INIT:


//=========
//LISTENERS:

        binding.btnSet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences.Editor prefEditor =  getContext().getSharedPreferences("testPrefs", Context.MODE_PRIVATE).edit();

                prefEditor.putString("test", "this is a saved pref"  );

                prefEditor.commit();

            }
        });

        binding.btnGetPrefs.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SharedPreferences prefs = getContext().getSharedPreferences("testPrefs", Context.MODE_PRIVATE);

                String testStr = prefs.getString("test", "error :(");

                binding.txtFill.setText(testStr);
            }
        });

//=========



return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingsTabViewModel.class);
        // TODO: Use the ViewModel
    }

}