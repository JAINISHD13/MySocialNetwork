
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
import com.example.jainishadabhi.mysocialnetwork.model.PostDetails;
import com.example.jainishadabhi.mysocialnetwork.model.TimeLineDetails;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>
{
    private static final String TAG ="PostRecyclerViewAdapter";

    private ArrayList<TimeLineDetails> mpostdec = new ArrayList<>();
    private Context mcontext;

    public PostAdapter(ArrayList<TimeLineDetails> mpostdec, Context mcontext)
    {
        this.mpostdec = mpostdec;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder:called");
        TimeLineDetails postDetails = mpostdec.get(position);
        holder.postDesc.setText(postDetails.getPost1());
        holder.postMode.setText(postDetails.getPrivacy());
        holder.dateTime.setText(postDetails.getTimestamp());
        holder.username.setText(postDetails.getEmail_id());
    }

    @Override
    public int getItemCount() {
        return mpostdec.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView postDesc,postMode,username,dateTime;

        public ViewHolder(View itemView) {
            super(itemView);
            postDesc = itemView.findViewById(R.id.post_description);
            username = itemView.findViewById(R.id.post_user_name);
            postMode =itemView.findViewById(R.id.post_Mode);
            dateTime =itemView.findViewById(R.id.post_DateTime);
        }
    }
}

