package com.example.android.theguardian;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment fragment = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPrefs.edit().putString(getResources().getString(R.string.settings_country), getDeviceCountry());

        fragment = new MainFragment();
        setTitle(R.string.main);
        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.commit();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.main) {
            fragment = new MainFragment();
            setTitle(R.string.main);
        } else if (id == R.id.country) {
            fragment = new CountryFragment();
            setTitle(getString(R.string.home_country) + " - " + getDeviceCountry());
        } else if (id == R.id.politics) {
            fragment = new PoliticsFragment();
            setTitle(R.string.politics);
        } else if (id == R.id.economy) {
            fragment = new EconomyFragment();
            setTitle(R.string.economy);
        } else if (id == R.id.education) {
            fragment = new EducationFragment();
            setTitle(R.string.education);
        } else if (id == R.id.technology) {
            fragment = new TechnologyFragment();
            setTitle(R.string.technology);
        }else if (id == R.id.sport) {
            fragment = new SportFragment();
            setTitle(R.string.sport);
        }else if (id == R.id.film) {
            fragment = new FilmFragment();
            setTitle(R.string.film);
        }else if (id == R.id.sttings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame_layout, fragment);
            ft.commit();
        }
        
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String getDeviceCountry(){
        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCodeValue = tm.getNetworkCountryIso();
        Locale loc = new Locale("",countryCodeValue);
        return loc.getDisplayCountry();
    }
}
