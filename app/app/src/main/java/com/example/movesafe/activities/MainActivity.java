package com.example.movesafe.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.movesafe.R;
import com.example.movesafe.fragments.dashboard.MainActivityDashboardFragment;
import com.example.movesafe.fragments.feedback.MainActivityFeedbackFragment;
import com.example.movesafe.fragments.profile.MainActivityProfileFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.dashboardViewDrawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.dashboardViewNavigationView);
        navigationView.setNavigationItemSelectedListener(this::handleSelectDrawerItem);
        handleSelectDrawerItem(navigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.three_dot_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.dashboardViewMenuAddDevice:
                startActivity(new Intent(this, AddDeviceActivity.class));
                break;
            case R.id.dashboardViewMenuLogout:
                new AlertDialog.Builder(this)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            SharedPreferences sharedPreferences = getSharedPreferences("movesafe", MODE_PRIVATE);
                            sharedPreferences.edit().remove("user").apply();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want to exit the app?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            finishAffinity();
            System.exit(0);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void clearNavigationDrawerItems() {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    public boolean handleSelectDrawerItem(MenuItem menuItem) {
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.navigationViewProvideFeedback:
                fragmentClass = MainActivityFeedbackFragment.class;
                break;
            case R.id.navigationViewProfile:
                fragmentClass = MainActivityProfileFragment.class;
                break;
            default:
                fragmentClass = MainActivityDashboardFragment.class;
                break;
        }

        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.mainViewParentFrameLayout, fragment).commit();

            clearNavigationDrawerItems();
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
            drawerLayout.closeDrawers();
        }

        return true;
    }
}