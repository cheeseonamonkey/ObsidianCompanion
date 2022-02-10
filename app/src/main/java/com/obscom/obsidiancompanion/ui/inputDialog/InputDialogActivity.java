package com.obscom.obsidiancompanion.ui.inputDialog;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.obscom.obsidiancompanion.classes.Processor;
import com.obscom.obsidiancompanion.databinding.InputDialogActivityBinding;

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


        binding.lblQuickAddFile.setText( Processor.PrefWriter.readPref(getApplicationContext(), Processor.FILE_NAME_PREF_KEY).toString());

        binding.btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Processor.QuickAdder.quickAdd(getApplicationContext(), binding.txtSendData.getText().toString());

                finish();

            }
        });





    binding.btnPasteToTxt.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {


            binding.txtSendData.setText(Processor.getClipBoard(getApplicationContext()));



        }
    });











    }





}