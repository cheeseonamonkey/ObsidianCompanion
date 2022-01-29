package com.example.obsidiancompanion;

import android.content.Intent;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import com.example.obsidiancompanion.ui.inputDialog.InputDialogActivity;

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
            // - QuickAdding is accomplished in this activity
            Intent dialogIntent = new Intent(getApplicationContext(), InputDialogActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //start inputDialogActivity for QuickAdding
            getBaseContext().startActivity(dialogIntent);




            Toast.makeText(this,"Text appended",Toast.LENGTH_LONG).show();



        } catch (Exception exc) {
            //if caught
            Toast.makeText(this, "Text Could not be added",Toast.LENGTH_LONG).show();




        Log.d("TAG", "error" + exc);
        }


    }
}
