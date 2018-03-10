package fr.iut_orsay.frinme;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.iut_orsay.frinme.view.Contact;
import fr.iut_orsay.frinme.view.Event;
import fr.iut_orsay.frinme.view.EventAdmin;
import fr.iut_orsay.frinme.view.EventList;
import fr.iut_orsay.frinme.view.Map;
import fr.iut_orsay.frinme.view.dialog.JoinFrag;
import fr.iut_orsay.frinme.view.dialog.QuitFrag;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        JoinFrag.OnFragmentInteractionListener, QuitFrag.OnFragmentInteractionListener{

    public enum Status {
        EXTERNE,
        INTERNE,
        CREATEUR
    }

    public static Status userStatus = Status.EXTERNE;
    private View parentLayout;
    private boolean isFabOpen = false;
    private FloatingActionButton fabContact;
    private FloatingActionButton fabEvent;
    private FloatingActionButton fab;
    private final FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        parentLayout = findViewById(android.R.id.content);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Map mapFrag = new Map();
        fragmentTransaction.add(R.id.fragment_container, mapFrag, "init");
        fragmentTransaction.commit();

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);

        //Mise en place du menu flottant
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabContact = (FloatingActionButton) findViewById(R.id.fabContact);
        fabEvent = (FloatingActionButton) findViewById(R.id.fabEvent);

        fab.setOnClickListener(v -> {
            if(!isFabOpen) showFabMenu();
            else closeFabMenu();
        });
        fabContact.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new Contact())
                    .addToBackStack(null)
                    .commit();
            closeFabMenu();
        });
        fabEvent.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new Event())
                    .addToBackStack(null)
                    .commit();
            closeFabMenu();
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, t, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contacts) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new Contact())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_evts) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new EventList())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_map) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new Map())
                    .addToBackStack(null)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int res){
        if (res == 1) {
            userStatus = Status.INTERNE;
        } else if (res == 2) {
            userStatus = Status.EXTERNE;
        }
        Log.e("STATUS", userStatus.toString());
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new Map())
                    .commit();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    public void setEventFrag(){
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, new Event())
                .addToBackStack(null)
                .commit();
    }

    private void showFabMenu(){
        isFabOpen=true;
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabContact.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing));
        fabEvent.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing)*2);
    }

    private void closeFabMenu(){
        isFabOpen=false;
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fabContact.animate().translationY(0);
        fabEvent.animate().translationY(0);
    }
}
