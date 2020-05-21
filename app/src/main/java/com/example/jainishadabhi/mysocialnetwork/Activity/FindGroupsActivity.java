package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jainishadabhi.mysocialnetwork.Adapter.FindFriendsAdapter;
import com.example.jainishadabhi.mysocialnetwork.Adapter.FindGroupAdapter;
import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.GroupDetails;
import com.example.jainishadabhi.mysocialnetwork.model.GroupInvitation;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FindGroupsActivity extends AppCompatActivity implements RecyclerViewClickInterface
{

    private Toolbar mToolbar;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private RecyclerView userList;
    FindGroupAdapter adapter;
    ArrayList<GroupDetails> mgroup = new ArrayList<>();
    String from_email_id,to_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_groups);

        mToolbar = (Toolbar) findViewById(R.id.find_groups_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Find Groups");

        PrefsManager obj = new PrefsManager(getApplicationContext());

        System.out.println("*************************************Email ID:"+obj.getStringData("email_id"));
        from_email_id = obj.getStringData("email_id");
        DisplayAllGroups();
    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.search_result_group_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new FindGroupAdapter(mgroup, (RecyclerViewClickInterface)this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position)
    {

        Toast.makeText(this, "Group_ID:" + mgroup.get(position).getGroup_id(), Toast.LENGTH_SHORT).show();
        PrefsManager obj = new PrefsManager(getApplicationContext());
        from_email_id=obj.getStringData("email_id");
        int group_id = mgroup.get(position).getGroup_id();
        String group_name =mgroup.get(position).getGroup_name();
        if(CommonUtils.checkInternetConnection(getApplicationContext()))
        {
            GroupRequestAPI requestFriend = new GroupRequestAPI(getApplicationContext(),from_email_id,group_id,group_name);
            requestFriend.execute();
        }

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onAcceptClick(int position) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            sendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendUserToMainActivity()
    {
        Intent intent = new Intent(FindGroupsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void DisplayAllGroups()
    {
        if (CommonUtils.checkInternetConnection(getApplicationContext()))
        {
            GroupsDetailsAPI authenticate = new GroupsDetailsAPI(getApplicationContext());
            authenticate.execute();
        }
        else
        {
            Toast.makeText(this, "Check ur Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class GroupsDetailsAPI extends AsyncTask<Void,Void,List<GroupDetails>>
    {
        Context mcontext;
        public GroupsDetailsAPI(Context applicationContext)
        {
            mcontext = applicationContext;
        }

        @Override
        protected  void onPostExecute(List<GroupDetails> groupDetails)
        {
            for (GroupDetails groupDetail1: groupDetails)
            {
                mgroup.add(groupDetail1);
            }
            initRecyclerView();
        }


        @Override
        protected List<GroupDetails> doInBackground(Void... voids)
        {
            PrefsManager obj = new PrefsManager(getApplicationContext());
            try
            {
                Call<List<GroupDetails>> callgroup = Api.getClient().getGroupDetailsForRequest(obj.getStringData("email_id"));
                Response<List<GroupDetails>> response = callgroup.execute();
                return response.body();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GroupRequestAPI extends AsyncTask<Void,Void,String>
    {
        Context mcontext;
        String from_email_id,group_name;
        int group_id;

        public GroupRequestAPI(Context applicationContext, String from_email_id, int group_id, String group_name)
        {
            mcontext=applicationContext;
            this.from_email_id=from_email_id;
            this.group_id=group_id;
            this.group_name=group_name;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("Success"))
            {
                Toast.makeText(mcontext, "Request Sent", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(mcontext, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            Call<String> sendFriendReq = Api.getClient().sendGroupRequest(from_email_id,group_id,group_name);
            try {
                Response<String> response = sendFriendReq.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
