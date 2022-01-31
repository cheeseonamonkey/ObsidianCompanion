package com.example.obsidiancompanion;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.obsidiancompanion.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_general, R.id.navigation_quickadd)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        //int tabNum = 0;

        //navigateToTab(0);


    }


    public void navigateToTab(int tabNum)
    {
        try
        {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            switch(tabNum)
            {
                case 0:
                    //navController.navigate();

                    Log.d("TAG", "Error changing tabs!");


                    break;

                case 1:

                     //navController.navigate();


                    break;


                case 2:

                    //navController.navigate();

                    break;


                default:

                    break;

        }


        } catch (Exception exc)
        {
            Log.d("TAG", "Error changing tabs!");
        }

    }




}