package fr.iut_orsay.frinme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import fr.iut_orsay.frinme.model.SessionManagerPreferences;
import fr.iut_orsay.frinme.view.Contact;
import fr.iut_orsay.frinme.view.Event;
import fr.iut_orsay.frinme.view.EventAdd;
import fr.iut_orsay.frinme.view.EventList;
import fr.iut_orsay.frinme.view.ListeContact;
import fr.iut_orsay.frinme.view.Map;
import fr.iut_orsay.frinme.view.dialog.JoinFrag;
import fr.iut_orsay.frinme.view.dialog.QuitFrag;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        JoinFrag.OnFragmentInteractionListener, QuitFrag.OnFragmentInteractionListener {

    public enum Status {
        EXTERNE,
        INTERNE,
        CREATEUR
    }

    public static Status userStatus = Status.EXTERNE;

    private boolean isFabOpen = false;
    private FloatingActionButton fabContact;
    private FloatingActionButton fabEvent;
    private FloatingActionButton fab;
    private final FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Affichage du fragment par défaut
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Map mapFrag = new Map();
        fragmentTransaction.add(R.id.fragment_container, mapFrag, "init");
        fragmentTransaction.commit();

        // Mise en place de l'AppBar
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);

        // Mise en place du menu flottant
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabContact = (FloatingActionButton) findViewById(R.id.fabContact);
        fabEvent = (FloatingActionButton) findViewById(R.id.fabEvent);

        fab.setOnClickListener(v -> {
            if (!isFabOpen) showFabMenu();
            else closeFabMenu();
        });
        fabContact.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, new Contact())
                    .addToBackStack(null)
                    .commit();
            closeFabMenu();
        });
        fabEvent.setOnClickListener(v -> {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, new EventAdd())
                    .addToBackStack(null)
                    .commit();
            closeFabMenu();
        });

        // Mise en place du drawer
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
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Deconnexion");
                alertDialog.setMessage("Voulez-vous vraiment vous déconnecter?");
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SessionManagerPreferences.getSettings(getApplicationContext()).logout();
                        startActivity(new Intent(MainActivity.this, ConnexionActivity.class));
                    }
                });
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Annulé
                    }
                });
                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment currentFrag = getFragmentManager().findFragmentById(R.id.fragment_container);

        // Gère le changement de fragment dans le drawer
        int id = item.getItemId();

        if (id == R.id.nav_contacts && !(currentFrag instanceof ListeContact)) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, new ListeContact())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_evts && !(currentFrag instanceof EventList)) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, new EventList())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (id == R.id.nav_map && !(currentFrag instanceof Map)) {
            fragmentManager.beginTransaction()
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                            android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, new Map())
                    .addToBackStack(null)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(int res) {
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
                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragment_container, new Map())
                    .commit();
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    /**
     * Déroule le menu flottant
     */
    private void showFabMenu() {
        isFabOpen = true;
        fab.setImageResource(R.drawable.ic_close_black_24dp);
        fabContact.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing));
        fabEvent.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing) * 2);
    }

    /**
     * Enroule le menu flottant
     */
    private void closeFabMenu() {
        isFabOpen = false;
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fabContact.animate().translationY(0);
        fabEvent.animate().translationY(0);
    }

}
