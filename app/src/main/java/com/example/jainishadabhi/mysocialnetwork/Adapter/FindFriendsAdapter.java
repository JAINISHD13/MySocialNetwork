
package com.example.jainishadabhi.mysocialnetwork.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.ViewHolder> implements Filterable
{
    private static String Tag ="In Find Friends";

    private ArrayList<String> username = new ArrayList<>();
    private ArrayList<String> allUsername = new ArrayList<>();
    private Context mcontext;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public FindFriendsAdapter(ArrayList<String> mUsername, Context mcontext)
    {
        this.username = mUsername;
        this.mcontext = mcontext;
    }
    public FindFriendsAdapter(ArrayList<String> mUser,RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.username = mUser;
        allUsername = new ArrayList<>(username);
        this.recyclerViewClickInterface =recyclerViewClickInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdisplay_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder:called");
        holder.fullname.setText(username.get(position));
    }

    @Override
    public int getItemCount() {
        return username.size();
    }

    @Override
    public Filter getFilter() {
        return usernameFilter;
    }

    private Filter usernameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(allUsername);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (String item : allUsername) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            username.clear();
            username.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView fullname;

        public ViewHolder(View itemView) {
            super(itemView);
            fullname = itemView.findViewById(R.id.all_users_profile_full_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
