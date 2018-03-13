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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fr.iut_orsay.frinme.view.Event;
import fr.iut_orsay.frinme.view.EventAdmin;
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
        Event evtFrag = new Event();
        fragmentTransaction.add(R.id.fragment_container, evtFrag, "init");
        fragmentTransaction.commit();

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);

        //Mise en place du menu flottant
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabContact = (FloatingActionButton) findViewById(R.id.fabContact);
        fabEvent = (FloatingActionButton) findViewById(R.id.fabEvent);

        fab.setOnClickListener(v -> {
            if(!isFabOpen) showFabMenu(); else closeFabMenu(); });
        fabContact.setOnClickListener(v ->
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new Contact()).commit()
        );
        fabEvent.setOnClickListener(v ->
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new Event()).commit()
        );

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
            Event evtFrag = new Event();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, evtFrag);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_evts) {
            EventAdmin evtAdminFrag = new EventAdmin();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, evtAdminFrag);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_settings) {
            SettingsActivity settings = new SettingsActivity();
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_map) {
            Map MapFrag = new Map();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, MapFrag);
            fragmentTransaction.commit();
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
