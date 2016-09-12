package it.asfitness.FitUnionAndroidPalestre.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;


import it.asfitness.FitUnionAndroidPalestre.data.Clienti;
import it.asfitness.FitUnionAndroidPalestre.fragment.GymFragment;
import it.asfitness.FitUnionAndroidPalestre.fragment.QrReaderScalaIngressi;
import it.asfitness.FitUnionAndroidPalestre.utils.Utils;
import it.asfitness.FitUnionAndroidPalestre.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, QRCodeReaderView.OnQRCodeReadListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    TextView email;
    Boolean scalaIngressiAttivo=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddGymActivity();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        getPermission();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        BackendlessUser user = Backendless.UserService.CurrentUser();

        email= (TextView) navigationView.getHeaderView(0).findViewById(R.id.email_user);
        email.setText(user.getEmail());

        displayView(R.id.nav_gym_list);
    }



    public void showAddGymActivity(){
        Intent addGymIntent = new Intent(MainActivity.this, AddGymActivity.class);
        MainActivity.this.startActivity(addGymIntent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displayView(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayView(int id) {
        String title = getString(R.string.app_name);
        Fragment fragment = null;
        if (id == R.id.nav_gym_list) {
            fragment = new GymFragment().newInstance();
        }else if(id == R.id.nav_qr){
            fragment = new QrReaderScalaIngressi(this).newInstance("","",this);
        }
        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }



    ////////////////////////QR CODE/////////////////
    @Override
    public void onQRCodeRead(final String text, PointF[] points) {

        if (scalaIngressiAttivo){
            scalaIngressiAttivo=false;
            final ProgressDialog progress = ProgressDialog.show(this, "Attendere",
                    "Acquisizione Codice", true,true);
            String whereClause = "objectId = '"+text+"'";
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            dataQuery.setWhereClause(whereClause);
            Backendless.Persistence.of(Clienti.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Clienti>>() {
                @Override
                public void handleResponse(BackendlessCollection<Clienti> response) {
                    if (response.getData().size() > 0) {
                        final Clienti mClienti = response.getData().get(0);
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setTitle("QR-Reader");
                        builder1.setMessage(mClienti.getNome() + " " + mClienti.getCognome() + "\nIngressi disponibili : " + mClienti.getIngressi_disponibili() + "\n\nScalare ingresso?");
                        builder1.setCancelable(true);
                        builder1.setNeutralButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Utils.getInstance(MainActivity.this).scalaIngresso(mClienti);
                                        dialog.dismiss();
                                    }
                                });
                        builder1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                scalaIngressiAttivo = true;
                            }
                        });
                        AlertDialog alert11 = builder1.create();
                        progress.dismiss();
                        alert11.show();

                    } else {
                        progress.dismiss();
                        scalaIngressiAttivo = true;
                        Toast.makeText(getApplicationContext(), "Impossibile trovare l'utente specificato", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    progress.dismiss();
                    scalaIngressiAttivo = true;
                    Toast.makeText(getApplicationContext(), "Errore " + fault.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else{

        }


    }

    @Override
    public void cameraNotFound() {

    }

    @Override
    public void QRCodeNotFoundOnCamImage() {

    }

    public void getPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                    } else {

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayView(R.id.nav_gym_list);
    }



}
