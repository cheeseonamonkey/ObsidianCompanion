package com.example.obsidiancompanion;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethod;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import com.example.obsidiancompanion.ui.InputDialogActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class SendTileService extends TileService
{

    final int QUICK_ADD_DIALOG = 31;


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

        try {

            //closes notif tray:
            /*
            Intent closeIntent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            closeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().sendBroadcast(closeIntent);

             */

            //dialog for entry:
            Intent dialogIntent = new Intent(getApplicationContext(), InputDialogActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(dialogIntent);

/*
            //writes - done in dialog activity now


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

*/

        } catch (Exception exc) {
            //if caught
            Toast.makeText(this, "Text Could not be added",Toast.LENGTH_LONG).show();
            Log.d("TAG", "error" + exc);
        }


    }
}
