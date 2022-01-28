package com.example.obsidiancompanion.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.obsidiancompanion.R;
import com.example.obsidiancompanion.databinding.ActivityMainBinding;
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

        //btnSend
        binding.btnSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("TAG", "onClick: ");
                try
                {


                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    String appendContentUri = prefs.getString("obsCom_appendContentUri", null);//If there is no key found null will be the default value.


                    Toast.makeText(getApplicationContext(), "Appending to " + appendContentUri + "...", Toast.LENGTH_SHORT).show();


                    Uri uri = Uri.parse(appendContentUri);
                    //Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADownload%2Fsample3.txt");


                    getContentResolver().openOutputStream(uri, "wa");

                    OutputStreamWriter osw = new OutputStreamWriter(getContentResolver().openOutputStream(uri, "wa"));

                    osw.write("daaaataaaa\n");
                    osw.flush();
                    osw.close();


                    Toast.makeText(getApplicationContext(), "Text appended", Toast.LENGTH_LONG).show();

                }
                catch(Exception exc)
                {
                    Log.d("TAG", "ERROR - " + exc.toString());
                }


            }
        });










        setContentView(R.layout.input_dialog_activity);



    }
}