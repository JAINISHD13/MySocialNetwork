package com.example.jainishadabhi.mysocialnetwork.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jainishadabhi.mysocialnetwork.R;

import java.util.ArrayList;

public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.ViewHolder>
{
    private static final String TAG ="FriendRequestRecyclerViewAdapter";

    private ArrayList<String> username = new ArrayList<>();
    private Context mcontext;

    public MyFriendsAdapter(ArrayList<String> mUsername, Context mcontext)
    {
        this.username = mUsername;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfriends,parent,false);
        MyFriendsAdapter.ViewHolder holder = new MyFriendsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.fullname.setText(username.get(position));
    }

    @Override
    public int getItemCount() {
        return username.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView fullname;

        public ViewHolder(View itemView)
        {
            super(itemView);
            fullname = itemView.findViewById(R.id.myfriends_name);
        }
    }
}
