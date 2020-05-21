package com.example.jainishadabhi.mysocialnetwork.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.GroupDetails;

import java.util.ArrayList;


public class FindGroupAdapter extends RecyclerView.Adapter<FindGroupAdapter.ViewHolder>
{
    private static String Tag ="In Find Groups";

    private ArrayList<GroupDetails> groupDetails = new ArrayList<>();
    private Context mcontext;
    private RecyclerViewClickInterface recyclerViewClickInterface;

    public FindGroupAdapter(ArrayList<GroupDetails> mgroup, Context mcontext)
    {
        groupDetails = mgroup;
        this.mcontext = mcontext;
    }
    public FindGroupAdapter(ArrayList<GroupDetails> mgroup,RecyclerViewClickInterface recyclerViewClickInterface)
    {
        this.groupDetails = mgroup;
        this.recyclerViewClickInterface =recyclerViewClickInterface;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.groupdisplay,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        GroupDetails groupDetails1 = groupDetails.get(position);
        holder.groupname.setText(groupDetails1.getGroup_name());
    }

    @Override
    public int getItemCount() {
        return groupDetails.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView groupname;

        public ViewHolder(View itemView) {
            super(itemView);
            groupname = itemView.findViewById(R.id.all_group_profile_full_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recyclerViewClickInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
