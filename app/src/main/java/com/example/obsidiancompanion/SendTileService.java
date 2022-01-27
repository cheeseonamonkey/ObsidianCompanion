package com.example.obsidiancompanion;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class SendTileService extends TileService
{



    @Override
    public void onTileAdded()
    {
        super.onTileAdded();

        /*
        SharedPreferences prefs = getSharedPreferences("settingsPrefs", MODE_PRIVATE);

        appendContentUri = prefs.getString("appendContentUri", null);
*/
        Log.d("TAG", "Tile Added");

    }


    @Override
    public void onClick()
    {

/*
        String a = runSh("cat > asdf3.txt");
        Log.d("", "onClick: " + a);

        a = runSh("cat > /sdcard/asdf4.txt");
        Log.d("", "onClick: " + a);

        a = runSh("cat > /storage/emulated/0/download/asdf5.txt");
        Log.d("", "onClick: " + a);

 */


        //makes dir:
        //runSh("mkdir /storage/emulated/0/Download/testingdir6");


/*
        runSh("system/bin/cat > /storage/emulated/0/Download/asdf13.txt");
        runSh("sh system/bin/cat > /storage/emulated/0/Download/asdf14.txt");
        runSh("cat > /storage/emulated/0/Download/asdf15.txt");

        runSh("/bin/sh cat > /storage/emulated/0/Download/asdf16.txt");
*/









        try {

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



            Toast.makeText(this,"Text appended",Toast.LENGTH_LONG).show();



        } catch (Exception exc) {
            //if caught
            Toast.makeText(this, "Text Could not be added",Toast.LENGTH_LONG).show();
            Log.d("TAG", "error" + exc);
        }


    }
}
