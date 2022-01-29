package com.example.obsidiancompanion.ui.inputDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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

                    //load the saved content URI:
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String appendContentUri = prefs.getString("obsCom_appendContentUri", null);//If there is no key found null will be the default value.

                    if(appendContentUri == null)
                        throw new Exception("File not yet chosen.");

                    Log.d("TAG", "Content uri:  " + appendContentUri + "...");

                    //get the URI from the saved file location
                    Uri uri = Uri.parse(appendContentUri);
                    //Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADownload%2Fsample3.txt");

                    //use ContentResolver to work with content URI:
                    ContentResolver contentResolver = getApplicationContext().getContentResolver();

                    contentResolver.openOutputStream(uri, "wa");

                    OutputStreamWriter osw = new OutputStreamWriter(contentResolver.openOutputStream(uri, "wa"));

                    //get data string ready:
                    String strWrite = binding.txtSendData.getText().toString();

                    strWrite = Processor.process(strWrite, getApplicationContext());

                    //write to file
                    Log.d("TAG", "\tAttempting to write data:  \n" + strWrite);
                    osw.write(strWrite);
                    osw.flush();
                    osw.close();

                    //user success notification
                    Toast.makeText(getApplicationContext(), "QuickAdded", Toast.LENGTH_SHORT).show();


                }catch(Exception exc)
                {
                    Log.d("error", "ERROR - " + exc.getMessage());
                }


            }
        });
















    }


}