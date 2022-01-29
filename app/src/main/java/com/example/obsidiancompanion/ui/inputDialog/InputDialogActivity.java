package com.example.obsidiancompanion.ui.inputDialog;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.obsidiancompanion.classes.Processor;
import com.example.obsidiancompanion.databinding.InputDialogActivityBinding;

import java.io.OutputStreamWriter;

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





        binding.btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {

                    Log.d("TAG", "QuickAdding now!");







                    //load the URI from the saved file location
                    String uriStr = Processor.PrefWriter.readPref(getApplicationContext(), Processor.FILE_URI_PREF_KEY);

                    Log.d("TAG", "uri string loaded: " + uriStr);

                    if( ! Processor.PrefWriter.isPrefSet(getApplicationContext(), Processor.FILE_URI_PREF_KEY))
                    {
                        Toast.makeText(getApplicationContext(), "Error - file not yet chosen!", Toast.LENGTH_SHORT).show();
                        throw new Exception("File not yet chosen.");
                    }


                    Uri uri = Uri.parse(Processor.PrefWriter.readPref(getApplicationContext(), Processor.FILE_URI_PREF_KEY));

                    //log the gotten URI
                    Log.d("TAG", "Selected file's content uri:\n\t\t - " + uri.toString());


                    //use ContentResolver to work with content URI:
                    ContentResolver contentResolver = getApplicationContext().getContentResolver();

                    contentResolver.openOutputStream(uri, "wa");

                    OutputStreamWriter osw = new OutputStreamWriter(contentResolver.openOutputStream(uri, "wa"));

                    //get data string ready:
                    String strWrite = binding.txtSendData.getText().toString();

                    strWrite = Processor.process(strWrite, getApplicationContext());

                    //write to file
                    Log.d("TAG", "Attempting to QuickAdd:\n\t\t -data to write: " + strWrite + "\t\t -file's content uri: " + uri.toString());
                    osw.write(strWrite);
                    osw.flush();
                    osw.close();

                    //user success notification
                    Toast.makeText(getApplicationContext(), "QuickAdded", Toast.LENGTH_SHORT).show();


                }catch(Exception exc)
                {

                    Log.d("error", "ERROR 421 in input dialog - " + exc.getMessage());
                }


            }
        });
















    }


}