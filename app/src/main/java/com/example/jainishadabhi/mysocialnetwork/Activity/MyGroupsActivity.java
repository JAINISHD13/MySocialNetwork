package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.content.Context;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jainishadabhi.mysocialnetwork.Adapter.FindGroupAdapter;
import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.GroupDetails;
import com.example.jainishadabhi.mysocialnetwork.model.GroupKeyDetails;
import com.example.jainishadabhi.mysocialnetwork.model.PostDetails;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Response;

public class MyGroupsActivity extends AppCompatActivity implements RecyclerViewClickInterface{

    private Toolbar mToolbar;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    private RecyclerView userList;
    FindGroupAdapter adapter;
    ArrayList<GroupDetails> mgroup = new ArrayList<>();
    String from_email_id,to_email;
    String group_name;
    EditText groupPost;
    int group_id;
    String groupPostString;
    PostDetails postDetails = new PostDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_groups);

        mToolbar = (Toolbar) findViewById(R.id.find_mygroup_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Friend Requests");


        GroupDetailsFetch fetchdetails = new GroupDetailsFetch(getApplicationContext());
        fetchdetails.execute();

        groupPost = (EditText) findViewById(R.id.post_for_groups);

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
        Intent intent = new Intent(MyGroupsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onItemClick(int position)
    {
        groupPostString = groupPost.getText().toString();
        Toast.makeText(this, "Group_ID:" + mgroup.get(position).getGroup_id(), Toast.LENGTH_SHORT).show();
        PrefsManager obj = new PrefsManager(getApplicationContext());
        from_email_id=obj.getStringData("email_id");
        group_id = mgroup.get(position).getGroup_id();
        group_name =mgroup.get(position).getGroup_name();
        String SessionKeyString = obj.getStringData("session_key_string");

        GetGroupKeyAPI objKey = new GetGroupKeyAPI(getApplicationContext(),group_id);
        objKey.execute();
/*        postDetails.setGroup_id(group_id);
        postDetails.setPost1(groupPostString);
        postDetails.setOriginal_post_id(1);
        postDetails.setSession_key();*/

    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.search_mygroupresult_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new FindGroupAdapter(mgroup, (RecyclerViewClickInterface)this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onAcceptClick(int position) {

    }

    private class GroupDetailsFetch  extends AsyncTask<Void,Void,List<GroupDetails>> {
        Context mcontext;
        public GroupDetailsFetch(Context applicationContext)
        {
            mcontext = applicationContext;
        }

        @Override
        protected void onPostExecute(List<GroupDetails> groupDetails) {
            for (GroupDetails groupDetail1: groupDetails)
            {
                mgroup.add(groupDetail1);
            }
            initRecyclerView();
        }

        @Override
        protected List<GroupDetails> doInBackground(Void... voids) {
            PrefsManager obj = new PrefsManager(getApplicationContext());
            obj.getStringData("email_id");
            System.out.println("email_id in do in:"+obj.getStringData("email_id"));
            Call<List<GroupDetails>> objforgroup = Api.getClient().getGroups(obj.getStringData("email_id"));
            try {
                Response<List<GroupDetails>> response = objforgroup.execute();
                System.out.println("Groups:"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GroupPostAPI extends AsyncTask<Void,Void,String>
    {
        Context mcontext;
        PostDetails postDetails;
        public GroupPostAPI(Context applicationContext, PostDetails postDetails)
        {
            this.postDetails= postDetails;
            mcontext =applicationContext;
        }

        @Override
        protected void onPostExecute(String s) {

            if (s.equals("Success"))
            {
                Toast.makeText(mcontext, "Posted Successfully in the group", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(mcontext, "Problem Occured", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            Call<String> callFinalPost = Api.getClient().groupPostData(postDetails);
            try {
                Response<String> response = callFinalPost.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetGroupKeyAPI extends AsyncTask<Void,Void,List<GroupKeyDetails>> {
        int group_id;
        Context mcontext;

        public GetGroupKeyAPI(Context applicationContext, int group_id)
        {
            mcontext = applicationContext;
            this.group_id = group_id;
        }

        @Override
        protected void onPostExecute(List<GroupKeyDetails> s)
        {

            PrefsManager obj = new PrefsManager(getApplicationContext());
            String group_key = s.get(0).getGroup_key1();

            byte[] group_key_decrypt_bytes = Base64.decode(group_key,Base64.DEFAULT);
            String privateKeyAlias = obj.getStringData("email_id")+"public_private";

            byte[] decrypt_group_key=null;
            try {
                KeyStore keyStore = null;
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias, null);
                decrypt_group_key = obj.decryptAsymmetric(group_key_decrypt_bytes,privateKey);

            }catch (Exception e) {
            }
                byte[] sessionkeyBytes = Base64.decode(obj.getStringData("session_key_string"), Base64.DEFAULT);
            SecretKeySpec secretKeySpec = new SecretKeySpec(sessionkeyBytes,0,sessionkeyBytes.length,"AES");
            try {
                byte[] encryptedPost = obj.encrypt(groupPostString.getBytes(),secretKeySpec);
                String encryptedPostString = Base64.encodeToString(encryptedPost,Base64.DEFAULT);

                SecretKeySpec secretKeySpec1 = new SecretKeySpec(decrypt_group_key,0,decrypt_group_key.length,"AES");

                byte[] sessionKeyEncrypt = obj.encrypt(sessionkeyBytes,secretKeySpec1);

                String encryptedSession = Base64.encodeToString(sessionKeyEncrypt,Base64.DEFAULT);

                postDetails.setSession_key(encryptedSession);
                postDetails.setPost1(encryptedPostString);
                postDetails.setGroup_id(group_id);
                postDetails.setDigital_signature("test");
                postDetails.setPrivacy("Group");
                postDetails.setOwner_email_id(obj.getStringData("email_id"));
                postDetails.setOriginal_post_id(1);

                GroupPostAPI post = new GroupPostAPI(getApplicationContext(),postDetails);
                post.execute();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected List<GroupKeyDetails> doInBackground(Void... voids)
        {
            System.out.println("Final do IN:------------------------");
            PrefsManager obj = new PrefsManager(getApplicationContext());
            String email_id = obj.getStringData("email_id");
            Call<List<GroupKeyDetails>> groupKey = Api.getClient().getGroupKey(group_id,email_id);
            try {
                Response<List<GroupKeyDetails>> response = groupKey.execute();
                System.out.println("Final do IN:------------------------"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
