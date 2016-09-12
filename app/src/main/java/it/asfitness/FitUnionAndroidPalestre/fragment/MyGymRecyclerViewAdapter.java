package it.asfitness.FitUnionAndroidPalestre.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import it.asfitness.FitUnionAndroidPalestre.R;
import it.asfitness.FitUnionAndroidPalestre.data.Clienti;

import java.util.List;

public class MyGymRecyclerViewAdapter extends RecyclerView.Adapter<MyGymRecyclerViewAdapter.ViewHolder> {

    private final List<Clienti> mValues;

    public MyGymRecyclerViewAdapter(List<Clienti> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_gym, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String nomeCognome= (String)mValues.get(position).getNome()+" "+mValues.get(position).getCognome();
        holder.mName.setText(nomeCognome);
        holder.mAddress.setText(mValues.get(position).getEmail());
        /*if ((Boolean)mValues.get(position).get("attivo")==true){
            holder.mImageState.setImageResource(R.drawable.ic_active_true);
        }else{
            holder.mImageState.setImageResource(R.drawable.ic_active_false);
        }*/
      /*  holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
             //       mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        */
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mAddress;

        public Clienti mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.gym_name);
            mAddress = (TextView) view.findViewById(R.id.gym_address);

        }

    }
}
