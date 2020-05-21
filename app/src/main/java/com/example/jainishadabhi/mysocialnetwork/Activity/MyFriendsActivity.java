package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.jainishadabhi.mysocialnetwork.Adapter.FindFriendsAdapter;
import com.example.jainishadabhi.mysocialnetwork.Adapter.MyFriendsAdapter;
import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyFriendsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView userList;
    MyFriendsAdapter adapter;
    ArrayList<String> mUser = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        mToolbar = (Toolbar) findViewById(R.id.friendshow_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("My Friends");

        ShowAllFriends();

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
        Intent intent = new Intent(MyFriendsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.friendshow_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new MyFriendsAdapter(mUser,getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void ShowAllFriends()
    {
        PrefsManager obj = new PrefsManager(getApplicationContext());
        ShowFriendAPI showFriend = new ShowFriendAPI(getApplicationContext(),obj.getStringData("email_id"));
        showFriend.execute();
    }

    private class ShowFriendAPI extends AsyncTask<Void,Void,List<UserDetails>>
    {
        Context context;
        String to_email_id;
        public ShowFriendAPI(Context applicationContext, String email_id)
        {
            context =applicationContext;
            to_email_id=email_id;
        }

        @Override
        protected  void onPostExecute(List<UserDetails> userDetails)
        {
            if(userDetails == null || userDetails.size() == 0){
                System.out.println("userdetails: null object or size 0");
            }
            for (int i=0; i<userDetails.size(); i++)
            {
                System.out.println("userdetails:"+userDetails.get(i).getEmail_id());
                mUser.add(userDetails.get(i).getEmail_id());
            }
            initRecyclerView();
        }


        @Override
        protected List<UserDetails> doInBackground(Void... voids)
        {
            Call<List<UserDetails>> getFriends = Api.getClient().getMyFriends(to_email_id);
            try {
                Response<List<UserDetails>> response = getFriends.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
