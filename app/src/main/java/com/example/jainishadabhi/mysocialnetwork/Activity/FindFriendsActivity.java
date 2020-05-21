    package com.example.jainishadabhi.mysocialnetwork.Activity;

    import android.content.Context;
    import android.content.Intent;
    import android.os.AsyncTask;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.support.v7.widget.Toolbar;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.inputmethod.EditorInfo;
    import android.support.v7.widget.SearchView;
    import android.widget.Toast;
    import com.example.jainishadabhi.mysocialnetwork.Adapter.FindFriendsAdapter;
    import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
    import com.example.jainishadabhi.mysocialnetwork.R;
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
    import retrofit2.http.Query;

    public class FindFriendsActivity extends AppCompatActivity implements RecyclerViewClickInterface
    {

        private Toolbar mToolbar;
        private SearchView searchView;
        private RecyclerViewClickInterface recyclerViewClickInterface;
        private RecyclerView userList;
        FindFriendsAdapter adapter;
        ArrayList<String> mUser = new ArrayList<>();
        String to_email,from_email;

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_find_friends);

            mToolbar = (Toolbar) findViewById(R.id.find_friends_appbar_layout);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Find Friends");

            PrefsManager obj = new PrefsManager(getApplicationContext());

            System.out.println("*************************************Email ID:"+obj.getStringData("email_id"));
            from_email = obj.getStringData("email_id");
            DisplayAllFriends();
        }

        public void DisplayAllFriends()
        {
            if (CommonUtils.checkInternetConnection(getApplicationContext()))
            {
                UserDetailsAPI authenticate = new UserDetailsAPI(getApplicationContext());
                authenticate.execute();
            }
            else
            {
                Toast.makeText(this, "Check ur Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

        public boolean onCreateOptionsMenu(Menu menu)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
            return true;
        }


        private void initRecyclerView()
        {
            RecyclerView recyclerView = findViewById(R.id.search_result_list);
            recyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            adapter = new FindFriendsAdapter(mUser, (RecyclerViewClickInterface) this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
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
            Intent intent = new Intent(FindFriendsActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onItemClick(int position)
        {
            GroupInvitation groupInvitation = new GroupInvitation();
            to_email = mUser.get(position);
            Toast.makeText(this, "Item:"+mUser.get(position), Toast.LENGTH_SHORT).show();

            groupInvitation.setTo_email_id(to_email);
            groupInvitation.setFrom_email_id(from_email);

            if(CommonUtils.checkInternetConnection(getApplicationContext()))
            {
                FriendRequestAPI requestFriend = new FriendRequestAPI(getApplicationContext(),from_email,to_email);
                requestFriend.execute();
            }
            removeItem(position);
        }
        public void removeItem(int position) {
            mUser.remove(position);
            adapter.notifyItemRemoved(position);
        }

        @Override
        public void onDeleteClick(int position) {

        }

        @Override
        public void onAcceptClick(int position) {

        }

        private class UserDetailsAPI extends AsyncTask<Void,Void, List<UserDetails>>
        {
            Context appContext;
            List<UserDetails> userDetails = new ArrayList<>();

            public UserDetailsAPI(Context applicationContext)
            {
                appContext=applicationContext;
            }


            @Override
            protected void onPreExecute()
            {/*
                loadingBar.setTitle("Creating New Account");
                loadingBar.setMessage("Please wait until the process finished.");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);*/

            }

            @Override
            protected  void onPostExecute(List<UserDetails> userDetails)
            {
                for (UserDetails userDetails1: userDetails)
                {
                    mUser.add(userDetails1.getEmail_id());
                }
                initRecyclerView();
            }

            @Override
            protected List<UserDetails> doInBackground(Void... voids)
            {
                PrefsManager obj = new PrefsManager(getApplicationContext());
                try
                {
                    Call<List<UserDetails>> callUser = Api.getClient().getUserDetails(obj.getStringData("email_id"));
                    Response<List<UserDetails>> response = callUser.execute();
                    return response.body();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                return null;
            }
        }

        private class FriendRequestAPI extends AsyncTask<Void,Void,String>
        {
            Context mcontext;
            String from_email,to_email;
            public FriendRequestAPI(Context applicationContext, String from_email, String to_email)
            {
                mcontext = applicationContext;
                this.from_email = from_email;
                this.to_email = to_email;
            }

            @Override
            protected  void onPostExecute(String status)
            {
                System.out.println("Status"+status);
                if(status=="success")
                    Toast.makeText(mcontext, "Request Sent", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mcontext, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected String doInBackground(Void... voids)
            {
                Call<String> sendFriendReq = Api.getClient().sendFriendRequest(from_email,to_email);
                try {
                    Response<String> response = sendFriendReq.execute();
                    if (response.isSuccessful())
                        return "success";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
