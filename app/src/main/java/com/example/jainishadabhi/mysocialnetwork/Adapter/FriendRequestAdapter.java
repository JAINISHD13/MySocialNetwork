package com.example.jainishadabhi.mysocialnetwork.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder>
{
    private static final String TAG ="FriendRequestRecyclerViewAdapter";

    private ArrayList<String> groupInvitations = new ArrayList<>();
    private Context mcontext;
    private RecyclerViewClickInterface mrecyclerViewClickInterface;
    private  String group_id;

    public void setOnItemClickListener(RecyclerViewClickInterface recyclerViewClickInterface)
    {
        mrecyclerViewClickInterface=recyclerViewClickInterface;
    }


    public FriendRequestAdapter(ArrayList<String> mgroupInvitations,Context mcontext)
    {
        this.groupInvitations = mgroupInvitations;
        this.mcontext = mcontext;
    }
    public FriendRequestAdapter(ArrayList<String> mgroupInvitations,RecyclerViewClickInterface recyclerViewClickInterface )
    {
        groupInvitations = new ArrayList<>(mgroupInvitations);
        this.mrecyclerViewClickInterface = recyclerViewClickInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendrequest_layout,parent,false);
        ViewHolder holder = new ViewHolder(view,mrecyclerViewClickInterface);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.fullname.setText(groupInvitations.get(position));
    }

    @Override
    public int getItemCount() {
        return groupInvitations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView fullname;
        CircleImageView acceptFriendRequest;
        CircleImageView rejectFriendRequest;


        public ViewHolder(View itemView,final RecyclerViewClickInterface listener)
        {
            super(itemView);
            fullname = itemView.findViewById(R.id.all_requested_users_profile_full_name);
            acceptFriendRequest = itemView.findViewById(R.id.accept_friend_request);
            rejectFriendRequest = itemView.findViewById(R.id.reject_friend_request);
/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });*/


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            rejectFriendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            acceptFriendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAcceptClick(position);
                        }
                    }
                }
            });
        }
    }
}
