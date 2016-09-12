package it.asfitness.FitUnionAndroidPalestre.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import it.asfitness.FitUnionAndroidPalestre.R;
import it.asfitness.FitUnionAndroidPalestre.utils.Utils;

public class DetailGymActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView txName;
    TextView txClassificazione;
    TextView txIscrizione;
    ProgressDialog progressDialog;
    MapFragment map;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gym);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        txName = (TextView) findViewById(R.id.tx_name);
        txClassificazione = (TextView) findViewById(R.id.tx_level);
        txIscrizione = (TextView) findViewById(R.id.tx_iscrizione);
        map = (MapFragment) getFragmentManager().findFragmentById(R.id.map_detail);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        map.getMapAsync(this);
    }


    public void getSelectedItem() {
        progressDialog = ProgressDialog.show(this, "Attendere", "Download dei dati", true);

        Intent i = getIntent();
        Bundle b = i.getExtras();

        if (b != null) {
            getDataForItem((String) b.get("objectId"));
        }

    }

    public void getDataForItem(String objectId) {
       /* ParseQuery<ParseObject> query = ParseQuery.getQuery("Palestre");
        query.whereEqualTo("objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        txName.setText((String) object.get("nome"));
                        txClassificazione.setText(Utils.getClassificazione((int) object.get("level")));
                        boolean isActive;
                        if ((Boolean) object.get("attivo")) {
                            txIscrizione.setText("Attivo");
                        } else {
                            txIscrizione.setText("Non attivo");
                        }
                        double latitude = (double) object.get("latitude");
                        double longitude = (double) object.get("longitude");
                        LatLng latLng = new LatLng(latitude, longitude);
                        String address = (String) object.get("via");

                        map.getMap().addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(address)
                                .snippet(""));

                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 11.0f);
                        map.getMap().animateCamera(yourLocation);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Impossibile trovare l'elemento", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
*/
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.e("ADG", "LOCATION ENABLED");
            map.getMap().setMyLocationEnabled(true);
            getSelectedItem();

        } else {

        }
    }



}
