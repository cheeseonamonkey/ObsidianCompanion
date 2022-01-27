package com.example.obsidiancompanion.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.obsidiancompanion.R;
import com.example.obsidiancompanion.databinding.ActivityMainBinding;
import com.example.obsidiancompanion.databinding.InputDialogActivityBinding;

public class InputDialogActivity extends AppCompatActivity
{

    private InputDialogActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        binding = InputDialogActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setContentView(R.layout.input_dialog_activity);

    }
}