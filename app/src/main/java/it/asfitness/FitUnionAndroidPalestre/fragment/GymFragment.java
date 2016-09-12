package it.asfitness.FitUnionAndroidPalestre.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;

import it.asfitness.FitUnionAndroidPalestre.data.Clienti;
import it.asfitness.FitUnionAndroidPalestre.data.Palestre;
import it.asfitness.FitUnionAndroidPalestre.utils.Utils;
import it.asfitness.FitUnionAndroidPalestre.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
@SuppressLint("ValidFragment")
public class GymFragment extends Fragment {
    MyGymRecyclerViewAdapter adapter;
    ProgressDialog progress;
    List<Clienti> list = new ArrayList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GymFragment newInstance() {
        GymFragment fragment = new GymFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progress = ProgressDialog.show(getActivity(), "Attendere", "Download dei dati", true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gym_list, container, false);
        BackendlessUser user = Backendless.UserService.CurrentUser();

        String whereClause = "ownerId = '"+user.getUserId()+"'";
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        dataQuery.setWhereClause( whereClause );
        Backendless.Persistence.of( Clienti.class ).find(dataQuery, new AsyncCallback<BackendlessCollection<Clienti>>() {
            @Override
            public void handleResponse(BackendlessCollection<Clienti> response) {
                adapter = new MyGymRecyclerViewAdapter(response.getData());
                if (view instanceof RecyclerView) {
                    Context context = view.getContext();
                    RecyclerView recyclerView = (RecyclerView) view;
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getActivity(), "Errore nel download dei dati. Provare più tardi. " + fault.getCode() + " msg " + fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        progress.dismiss();
        // Set the adapter

        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
