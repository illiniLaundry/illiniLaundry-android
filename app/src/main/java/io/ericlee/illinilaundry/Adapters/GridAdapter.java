package io.ericlee.illinilaundry.Adapters;

/**
 * Created by Eric on 11/10/2015.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.ericlee.illinilaundry.Activities.DormActivity;
import io.ericlee.illinilaundry.Activities.MainActivity;
import io.ericlee.illinilaundry.Model.Dorm;
import io.ericlee.illinilaundry.R;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private ArrayList<Dorm> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView txtTitle;
        public TextView txtWash;
        public TextView txtDry;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            txtWash = (TextView) v.findViewById(R.id.txtWasher);
            txtDry = (TextView) v.findViewById(R.id.txtDrier);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GridAdapter(ArrayList<Dorm> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_dorms, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getContext(), DormActivity.class);
                intent.putExtra("Name", mDataset.get(position).getName());

                // TODO: Do we need index?
                intent.putExtra("Index", position);

                intent.putExtra("Wash", mDataset.get(position).getWash());
                intent.putExtra("Dry", mDataset.get(position).getDry());
                intent.putExtra("InWash", mDataset.get(position).getInWash());
                intent.putExtra("InDry", mDataset.get(position).getInDry());
                MainActivity.getContext().startActivity(intent);
            }
        });

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.txtTitle.setText(mDataset.get(position).getName());
        holder.txtWash.setText(mDataset.get(position).getWash() + "");
        holder.txtDry.setText(mDataset.get(position).getDry() + "");
        holder.image.setImageResource(R.drawable.isr);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}