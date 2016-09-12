package it.asfitness.FitUnionAndroidPalestre.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import java.util.List;

import it.asfitness.FitUnionAndroidPalestre.data.Clienti;

/**
 * Created by a.digiacomo on 13/01/2016.
 */
public class Utils {
    private static Utils singleton;

    public static Object user;

    public static Context context;
    public static Utils getInstance(Context ctx) {
        if (singleton == null) {
            singleton = new Utils(ctx);
        }
        return singleton;
    }

    public Utils(Context ctx) {
        this.context = ctx;
    }

    public static void setUser(Object user) {
        Utils.user = user;
    }

    public static Object getUser() {
        return user;
    }

    public static String getClassificazione(int level) {
        if (level == 1) {
            return "Regular";

        } else if (level == 2) {

            return "Premium";
        } else {
            return "Top Class";

        }

    }




    public static void scalaIngresso(Clienti mClienti) {
       int ingressiDisponibili = mClienti.getIngressi_disponibili().intValue();
       if (ingressiDisponibili > 0) {
           Integer newIngressiDisponibili = new Integer(ingressiDisponibili - 1);
           mClienti.setIngressi_disponibili(newIngressiDisponibili);
           Backendless.Persistence.save(mClienti, new AsyncCallback<Clienti>() {
               @Override
               public void handleResponse(Clienti response) {
                   Toast.makeText(context, "Ingresso scalato correttamente", Toast.LENGTH_LONG).show();
               }

               @Override
               public void handleFault(BackendlessFault fault) {

               }
           });
       } else {
           Toast.makeText(context, "Ingressi terminati.", Toast.LENGTH_LONG).show();
       }
}
             /*  ParseQuery<ParseObject> query = ParseQuery.getQuery("Utenti");
        query.whereEqualTo("objectId", idUser);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        int ingressi = (int) object.get("ingressi_disponibili");
                        ingressi= ingressi-1;
                        Log.e("ADG","Nuovi ingressi disponibili "+ ingressi);
                        object.put("ingressi_disponibili",ingressi);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(context, "Ingresso scalato correttamente", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, "Impossibile trovare l'utente specificato", Toast.LENGTH_LONG).show();
                }
            }
        });
        */

}