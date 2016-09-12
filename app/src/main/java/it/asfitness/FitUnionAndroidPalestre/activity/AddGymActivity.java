package it.asfitness.FitUnionAndroidPalestre.activity;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.servercode.annotation.Async;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;

import it.asfitness.FitUnionAndroidPalestre.R;
import it.asfitness.FitUnionAndroidPalestre.data.Clienti;
import it.asfitness.FitUnionAndroidPalestre.utils.Utils;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class AddGymActivity extends AppCompatActivity{
    MaterialEditText nome;
    MaterialEditText cognome;
    MaterialEditText email;
    MaterialEditText password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        setContentView(R.layout.activity_add_gym);
        setupActionBar();
        nome = (MaterialEditText) findViewById(R.id.et_nome);
        cognome = (MaterialEditText) findViewById(R.id.et_cognome);
        email = (MaterialEditText) findViewById(R.id.et_mail);





    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_gym, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.action_save) {
                Date date = new Date();
            registerUserAccount(email.getText().toString(), cognome.getText().toString(),nome.getText().toString());

        }

        return super.onOptionsItemSelected(item);
    }

   public void addUser(final String nome, final String cognome, final String email,BackendlessUser user) {
        Clienti mClienti = new Clienti();
        mClienti.setNome(nome);
        mClienti.setCognome(cognome);
        mClienti.setEmail(email);
       mClienti.setIngressi_disponibili(10);
        mClienti.setUserId(user.getObjectId());

       Backendless.Persistence.save(mClienti, new AsyncCallback<Clienti>() {
           @Override
           public void handleResponse(Clienti response) {
               Toast.makeText(getApplicationContext(), "Cliente registrato correttamente.", Toast.LENGTH_LONG).show();
               finish();
           }

           @Override
           public void handleFault(BackendlessFault fault) {
               Toast.makeText(getApplicationContext(), "Errore " + fault.getMessage(), Toast.LENGTH_LONG).show();
           }
       });

    }

    public void registerUserAccount(final String mail,  final String cognome, final String nomeUtente){
        BackendlessUser user = new BackendlessUser();
        user.setProperty("email", mail);
        user.setProperty("name", nomeUtente);
        user.setProperty("userStatus", true);
        user.setProperty("user_level", 3);
        user.setPassword("fitunion");

        Backendless.UserService.register(user, new AsyncCallback<BackendlessUser>() {
            public void handleResponse(BackendlessUser registeredUser) {
                addUser(nomeUtente,cognome,mail,registeredUser);
            }

            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getApplicationContext(), "Errore nella registrazione, riprovare. " + fault.getCode(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
