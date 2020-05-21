package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jainishadabhi.mysocialnetwork.Adapter.PostAdapter;
import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.PostDetails;
import com.example.jainishadabhi.mysocialnetwork.model.TimeLineDetails;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageButton addPost;
    private TextView navUsername;
   // String group_key_Final;

    //RecyclerView.Adapter myAdapter;
    private RecyclerView postList;
    ArrayList<TimeLineDetails> mPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addPost = (ImageButton) findViewById(R.id.add_post);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");

        navUsername= (TextView)findViewById(R.id.nav_user_full_name);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawable_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*postList = (RecyclerView)findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.setReverseLayout(true);
        postList.setLayoutManager(linearLayoutManager);*/

        PrefsManager obj = new PrefsManager(getApplicationContext());


        //navUsername.setText(obj.getStringData("email_id"));



        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        //navUsername.setText(obj.getStringData("email_id"));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return false;
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SendUserToPostActivity();
            }
        });

        //      initPostDec();
        DisplayAllPosts();
    }
/*    private  void initPostDec()
    {
        mPost.add("First Post");
        mPost.add("Second Post");
        mPost.add("Third Post");
        mPost.add("Fourth Post");
        mPost.add("Fifth Post");
        mPost.add("sixth Post");
        mPost.add("Seven Post");

        initRecyclerView();
    }*/

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.all_users_post_list);
        PostAdapter adapter = new PostAdapter(mPost, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void DisplayAllPosts()
    {

        PrefsManager obj = new PrefsManager(getApplicationContext());
        if (CommonUtils.checkInternetConnection(MainActivity.this)) {
            PostViewActivityAPI groupKEY = new PostViewActivityAPI(getApplicationContext());
            groupKEY.execute();
        }
        else {
            Toast.makeText(getApplicationContext(), "Something wrong with Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void SendUserToPostActivity() {
        Intent intent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(intent);
    }
/*
    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser==null)
        {
            SendUserToLoginActivity();
        }
    }
*/

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_post:
                SendUserToPostActivity();
                break;
/*
            case R.id.nav_Create_Group:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.nav_friends_request:
                SendUserToFriendRequestActivity();
                break;

            case R.id.nav_group_requests:
                SendUserToGroupRequestActivity();
                break;

            case R.id.nav_find_friends:
                SendUserToFindFriendsActivity();
                break;

            case R.id.nav_Create_Group:
                SendUserToGroupActivity();
                break;

            case R.id.nav_find_groups:
                SendUserToFinGroupsActivity();
                break;

            case R.id.nav_Friends:
                SendUserToMyFriendActivity();
                break;

            case R.id.nav_Mygroups:
                SendUserToMyGroupsActivity();
                break;
/*

            case R.id.nav_settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
*/

            case R.id.nav_logout:
                SendUserToLoginActivity();
                break;

        }
    }

    private void SendUserToMyGroupsActivity()
    {
        Intent intent = new Intent(MainActivity.this, MyGroupsActivity.class);
        startActivity(intent);
    }

    private void SendUserToGroupRequestActivity()
    {
        Intent intent = new Intent(MainActivity.this, GroupRequestActivity.class);
        startActivity(intent);
    }

    private void SendUserToFinGroupsActivity()
    {
        Intent intent = new Intent(MainActivity.this, FindGroupsActivity.class);
        startActivity(intent);

    }

    private void SendUserToGroupActivity() {
        Intent intent = new Intent(MainActivity.this, GroupActivity.class);
        startActivity(intent);
    }

    private void SendUserToMyFriendActivity() {
        Intent intent = new Intent(MainActivity.this, MyFriendsActivity.class);
        startActivity(intent);
    }

    private void SendUserToFriendRequestActivity() {
        Intent intent = new Intent(MainActivity.this, FriendRequestActivity.class);
        startActivity(intent);
    }

    private void SendUserToFindFriendsActivity() {
        Intent findFriend = new Intent(MainActivity.this, FindFriendsActivity.class);
        findFriend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(findFriend);
        finish();
    }
/*
    private class GetGroupKeyAPI extends AsyncTask<Void, Void, String>
    {
        Context mcontext;

        public GetGroupKeyAPI(Context applicationContext, String email_id)
        {
            mcontext =applicationContext;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            group_key_Final = s;
            System.out.println("Group key final in on post:"+group_key_Final);
            PostViewActivityAPI finalPost = new PostViewActivityAPI(getApplicationContext());
            finalPost.execute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String to_email_id;
            PrefsManager obj = new PrefsManager(getApplicationContext());
            to_email_id = obj.getStringData("email_id");

            System.out.println("to_email_id:" + to_email_id);
            Call<String> getGroupKey = Api.getClient().getGroupKeyData(to_email_id);
            try {
                Response<String> response = getGroupKey.execute();
                System.out.println("issuccessful:" + response.isSuccessful());
                System.out.println("response body:" + response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }*/

    private class PostViewActivityAPI extends AsyncTask<Void, Void, List<TimeLineDetails>>
    {
        Context context;

        public PostViewActivityAPI(Context applicationContext) {
            context = applicationContext;
        }

        @Override
        protected void onPostExecute(List<TimeLineDetails> postDetails) {

            PrefsManager obj = new PrefsManager(getApplicationContext());
            if(postDetails!=null && postDetails.size()>0)
            {
                for(TimeLineDetails postDetails1 : postDetails)
                {

                    if ((postDetails1.getPrivacy()).equals("Public"))
                    {
                        mPost.add(postDetails1);
                    }
                    else if((postDetails1.getPrivacy().equals("Friends")) || (postDetails1.getPrivacy().equals("Group")))
                    {
                        String to_email_id = obj.getStringData("email_id");
                        //get private key
                        String privateKeyAlias = to_email_id + "public_private";

                        KeyStore keyStore = null;
                        try {
                            keyStore = KeyStore.getInstance("AndroidKeyStore");
                            keyStore.load(null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PrivateKey privateKey = null;
                        try {
                            privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("Group_key_Final:" + postDetails1.getGroup_key1());

                        //Group key converted to bytes
                        byte[] GroupKeyBytes = Base64.decode(postDetails1.getGroup_key1(), Base64.DEFAULT);

                        //Decrypt Group key
                        byte[] DecryptGrpKey = obj.decryptAsymmetric(GroupKeyBytes, privateKey);

                        //Process Decrypt Session key using group key

                        //Convert SessionKey into bytes
                        byte[] SessionKeyInBytes = Base64.decode(postDetails1.getSession_key(), Base64.DEFAULT);


                        //Convert Decrypted group key to Secret key
                        SecretKeySpec keyspec = new SecretKeySpec(DecryptGrpKey, 0, DecryptGrpKey.length, "AES");

                        //Decryption of Session key
                        byte[] DecryptSessionKey = null;
                        try {
                            DecryptSessionKey = obj.decrypt(SessionKeyInBytes, keyspec);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //Convert Decrypted sessionkey key to Secret key
                        SecretKeySpec keyspec1 = new SecretKeySpec(DecryptSessionKey, 0, DecryptSessionKey.length, "AES");

                        //Decrypt Post
                        String Finally_Post = "";
                        byte[] decryptPostBytes = null;
                        System.out.println("Post before final operation:" + postDetails1.getPost1());
                        try {
                            decryptPostBytes = obj.decrypt(Base64.decode(postDetails1.getPost1(), Base64.DEFAULT), keyspec1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Finally_Post = new String(decryptPostBytes);
                            System.out.println("Finally you got the post::" + Finally_Post);
                            postDetails1.setPost1(Finally_Post);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mPost.add(postDetails1);
                    }else if((postDetails1.getPrivacy()).equals("Private"))
                    {
                        String email = obj.getStringData("email_id");
                       String privateSecretKey = obj.getStringData("privateSecretKey"+email);
                       byte[] privateSecretKeyBytes = Base64.decode(privateSecretKey,Base64.DEFAULT);
                       byte[] sessionKeyBytes = Base64.decode(postDetails1.getSession_key(),Base64.DEFAULT);

                       SecretKeySpec secretKeySpec = new SecretKeySpec(privateSecretKeyBytes,0,privateSecretKeyBytes.length,"AES");
                        try {
                            byte[] sessionDecrypt = obj.decrypt(sessionKeyBytes,secretKeySpec);
                            SecretKeySpec secretKeySpec1 = new SecretKeySpec(sessionDecrypt,0,sessionDecrypt.length,"AES");
                            byte[] encryptedPost = Base64.decode(postDetails1.getPost1(),Base64.DEFAULT);
                            byte[] postInBytes = obj.decrypt(encryptedPost,secretKeySpec1);
                            String finalDecryptedPost = new String(postInBytes,Base64.DEFAULT);
                            System.out.println("Final Post--------------------------:::"+finalDecryptedPost);
                            postDetails1.setPost1(finalDecryptedPost);

                        } catch (Exception e)
                        {

                        }

                        mPost.add(postDetails1);
                    }
                    initRecyclerView();
                    /*String to_email_id = obj.getStringData("email_id");
                    //get private key
                    String privateKeyAlias = to_email_id + "public_private";

                    KeyStore keyStore = null;
                    try {
                        keyStore = KeyStore.getInstance("AndroidKeyStore");
                        keyStore.load(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    PrivateKey privateKey = null;
                    try {
                        privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("Group_key_Final:" + group_key_Final);

                    //Group key converted to bytes
                    byte[] GroupKeyBytes = Base64.decode(group_key_Final, Base64.DEFAULT);

                    //Decrypt Group key
                    byte[] DecryptGrpKey = obj.decryptAsymmetric(GroupKeyBytes, privateKey);

                    //Process Decrypt Session key using group key

                    //Convert SessionKey into bytes
                     byte[] SessionKeyInBytes = Base64.decode(postDetails1.getSession_key(), Base64.DEFAULT);


                    //Convert Decrypted group key to Secret key
                    SecretKeySpec keyspec = new SecretKeySpec(DecryptGrpKey, 0, DecryptGrpKey.length, "AES");

                    //Decryption of Session key
                    byte[] DecryptSessionKey = null;
                    try {
                        DecryptSessionKey = obj.decrypt(SessionKeyInBytes, keyspec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Convert Decrypted sessionkey key to Secret key
                    SecretKeySpec keyspec1 = new SecretKeySpec(DecryptSessionKey, 0, DecryptSessionKey.length, "AES");

                    //Decrypt Post
                    String Finally_Post = "";
                    byte[] decryptPostBytes = null;
                    System.out.println("Post before final operation:" + postDetails1.getPost1());
                    try {
                        decryptPostBytes = obj.decrypt(Base64.decode(postDetails1.getPost1(), Base64.DEFAULT), keyspec1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Finally_Post = new String(decryptPostBytes);
                        System.out.println("Finally you got the post::" + Finally_Post);
                        postDetails1.setPost1(Finally_Post);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mPost.add(postDetails1);*/
                }

                //initRecyclerView();
            }

        }

        @Override
        protected List<TimeLineDetails> doInBackground(Void... voids) {
            System.out.println("In Doing");
            PrefsManager obj = new PrefsManager(getApplicationContext());
            obj.getStringData("email_id");
            try
            {
                Call<List<TimeLineDetails>> callAuth = Api.getClient().getPost(obj.getStringData("email_id"));
                Response<List<TimeLineDetails>> response = callAuth.execute();
                System.out.println("-----------Final Response-------:"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}