package com.example.obsidiancompanion;

import android.service.quicksettings.TileService;
import android.widget.Toast;

public class SendTileService extends TileService
{
    @Override
    public void onTileAdded()
    {
        super.onTileAdded();


    }

    @Override
    public void onClick()
    {
        Toast.makeText(getApplicationContext(), "Sending to Obsidian...", Toast.LENGTH_SHORT).show();
    }
}
