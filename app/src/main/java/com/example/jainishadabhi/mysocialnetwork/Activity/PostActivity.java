package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.bluetooth.BluetoothAssignedNumbers;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.GroupDetails;
import com.example.jainishadabhi.mysocialnetwork.model.GroupKeyDetails;
import com.example.jainishadabhi.mysocialnetwork.model.PostDetails;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Response;


public class PostActivity extends AppCompatActivity
{

    private Toolbar mToolbar;
    private Button addPost;
    private Spinner postSpinner,groupSpinner;
    private String postMode;
    private EditText postDescription;
    public String groupkey;
    String post;
    public int group_id_fetched;
    public int groupVersion;
    PostDetails postDetails = new PostDetails();
    String interesting,interesting2;
    List<String> groupNames =new ArrayList<>();
    List<GroupDetails> groupDetailsFinal = new ArrayList<>();
    String group_name_spinner;
    int group_id_final;
    String encryptedSessionKeString,postEncryptString;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mToolbar = (Toolbar) findViewById(R.id.update_post_page_toolbar);
        addPost =(Button)findViewById(R.id.add_post_final);
        postDescription=(EditText)findViewById(R.id.post_written);
        postSpinner = (Spinner)findViewById(R.id.checkPostSpinner);


        System.out.println("Post Mode"+postMode);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Update Post");



        postSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                interesting = postSpinner.getSelectedItem().toString();

                System.out.println("Interesting item:"+interesting);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,groupNames);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);

/*        groupSpinner.setAdapter(spinnerArrayAdapter);
        spinnerArrayAdapter.notifyDataSetChanged();
        groupSpinner.setSelection(1);*/
/*
        GroupDetailsFetch fetchdetails = new GroupDetailsFetch(getApplicationContext());
        fetchdetails.execute();*/



        for (GroupDetails groupDetails:groupDetailsFinal)
        {
            if ((groupDetails.getGroup_name().equals(group_name_spinner)))
            {
                group_id_final=groupDetails.getGroup_id();
            }
        }

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interesting.equals("Public"))
                {
                    Toast.makeText(PostActivity.this, "Hello selected:"+interesting, Toast.LENGTH_SHORT).show();
                    addPostToDatabasePublic();

                }else  if(interesting.equals("Friends"))
                {
                    addpostToDatabaseFriends();
                    Toast.makeText(PostActivity.this, "Hello selected:"+interesting, Toast.LENGTH_SHORT).show();

                }else if(interesting.equals("Private"))
                {

                    Toast.makeText(PostActivity.this, "Hello selected:"+interesting, Toast.LENGTH_SHORT).show();
                    try {
                        addPostToDatabasePrivate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }/*else if(interesting.equals("Group"))
                {
                    Toast.makeText(PostActivity.this, "Hello selected:"+interesting, Toast.LENGTH_SHORT).show();

                }
*/
            }
        });

        System.out.println("Interesting item:"+interesting);

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
        Intent intent = new Intent(PostActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void addPostToDatabasePrivate() throws Exception {
        post = postDescription.getText().toString();

        System.out.println("Post:"+post);
        PrefsManager obj = new PrefsManager(getApplicationContext());
        String email = obj.getStringData("email_id");
        String sessionkey = obj.getStringData("session_key_string");

        byte[] sessionKeyBytes = Base64.decode(sessionkey,Base64.DEFAULT);
        byte[] sessionEncrypt=null;

        SecretKeySpec secretKeySpec = new SecretKeySpec(sessionKeyBytes,0,sessionKeyBytes.length,"AES");
        System.out.println(""+sessionEncrypt);

        byte[] postEncrypt = obj.encrypt(post.getBytes(),secretKeySpec);
        postEncryptString = Base64.encodeToString(postEncrypt,Base64.DEFAULT);
        try
        {/*
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            String alias = email+"Secret";
            System.out.println("SecretKey or not:"+keyStore.containsAlias(alias));*/
           /* SecretKey secretKey = (SecretKey) keyStore.getKey(alias,null);
           */

           String privateSecretKey  = obj.getStringData("privateSecretKey"+email);
           byte[] privateKeyBytes = Base64.decode(privateSecretKey,Base64.DEFAULT);
           SecretKeySpec secretKeySpec1 = new SecretKeySpec(privateKeyBytes,0,privateKeyBytes.length,"AES");
           byte[] encryptedSessionKey = obj.encrypt(sessionKeyBytes,secretKeySpec1);
           encryptedSessionKeString = Base64.encodeToString(encryptedSessionKey,Base64.DEFAULT);

           PostPrivateAPI callPrivate = new PostPrivateAPI(getApplicationContext());
           callPrivate.execute();

            //yte[] decryptPost = obj.decrypt(encryptPost,secretKey);
           // System.out.println("Check:"+));
          //  System.out.println("Final:"+(new String(decryptPost)));
         //   sessionEncrypt = obj.encrypt(sessionKeyBytes,secretKey);
           // EncryptedSessionKey = Base64.encodeToString(sessionEncrypt,Base64.DEFAULT);
/*

            byte[] decodeSessionKey =  Base64.decode(EncryptedSessionKey,Base64.DEFAULT);
            byte[] decryptSession = obj.decrypt(decodeSessionKey,secretKey);
*/

           // SecretKeySpec secretKeySpec1 = new SecretKeySpec(decryptSession,0,decryptSession.length,"AES");

            //byte[] decryptPost = obj.decrypt(postEncrypt,secretKeySpec1);
            //System.out.println("Decrypt post:"+Base64.decode(decryptPost,Base64.DEFAULT));

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void addpostToDatabaseFriends()
    {
        post = postDescription.getText().toString();

        PrefsManager obj = new PrefsManager(getApplicationContext());

        //Get email id
        String email_id = obj.getStringData("email_id");

        GetGroupIdAPI getGrpId = new GetGroupIdAPI(getApplicationContext(),postMode,email_id);
        getGrpId.execute();
    }

    private void addPostToDatabasePublic()
    {
        PrefsManager obj = new PrefsManager(getApplicationContext());
        post =postDescription.getText().toString();
        System.out.println("Post For Group:"+post);
        PostDetails postDetails = new PostDetails();

        postDetails.setOwner_email_id(obj.getStringData("email_id"));
        postDetails.setGroup_version(1);
        postDetails.setDigital_signature("test");
        postDetails.setPrivacy("Public");
        postDetails.setSession_key("No Session key");
        postDetails.setPost1(post);
        postDetails.setGroup_id(1);
        postDetails.setOriginal_post_id(1);
        AddPostDataGroupAPI addpost = new AddPostDataGroupAPI(getApplicationContext(),postDetails);
        addpost.execute();
    }

    private class GroupDetailsFetch extends AsyncTask<Void,Void,List<GroupDetails>>
    {
        Context mcontext;
        public GroupDetailsFetch(Context applicationContext)
        {
            mcontext = applicationContext;
        }

        @Override
        protected void onPostExecute(List<GroupDetails> groupDetails) {
            super.onPostExecute(groupDetails);
            for(GroupDetails groupDetails1:groupDetails)
            {
                groupNames.add(groupDetails1.getGroup_name());
                groupDetailsFinal.add(groupDetails1);
            }
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
    private class AddPostDataGroupAPI  extends  AsyncTask<Void,Void,String>
    {
        Context context;
        PostDetails postDetails;
        public AddPostDataGroupAPI(Context applicationContext, PostDetails postDetails)
        {
            context = applicationContext;
            this.postDetails = postDetails;
        }

        @Override
        protected void onPostExecute(String s)
        {
            //super.onPostExecute(s);
            if (s.equals("Success"))
            {
                Toast.makeText(context, "Post successfully Added!!!!!!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Problem", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            System.out.println("In Do in:"+postDetails.getPost1());

            Call<String> callPost = Api.getClient().addPostPublic(postDetails);
            try {
                Response<String> response = callPost.execute();
                System.out.println("In response:"+response.body());
                return  response.body();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetGroupIdAPI extends AsyncTask<Void,Void,List<GroupKeyDetails>>
    {
        Context context;
        String postMode,email_id;
        public GetGroupIdAPI(Context applicationContext, String postMode, String email_id) {
            this.postMode = postMode;
            this.email_id = email_id;
        }

        @Override
        protected  void onPostExecute(List<GroupKeyDetails> grpDetails)
        {

            group_id_fetched = grpDetails.get(0).getGroup_id();
            groupVersion = grpDetails.get(0).getGroup_version();
            groupkey = grpDetails.get(0).getGroup_key1();


            KeyGenerator keyGenerator2 = null;
            try {
                keyGenerator2 = KeyGenerator.getInstance("AES");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            keyGenerator2.init(128);
            SecretKey sessionKey = keyGenerator2.generateKey();

            PrefsManager obj = new PrefsManager(getApplicationContext());

            //Post has been encrypted by session key
            byte[] encryptedPostBytes = new byte[0];
            try {
                System.out.println("Post _______________:"+post);
                encryptedPostBytes = obj.encrypt(post.getBytes(),sessionKey);
                System.out.println("encrypted Post Bytes in post:"+encryptedPostBytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //convert encrypted post to string
            String encryptedPostString = Base64.encodeToString(encryptedPostBytes,Base64.DEFAULT);
            System.out.println("Hello:"+encryptedPostString);

            //get group key
            byte[] groupkeyBytes = Base64.decode(groupkey,Base64.DEFAULT);
            System.out.println("Groupkey in pst"+groupkey);

            //Get private key
            String privateKeyAlias = obj.getStringData("email_id")+"public_private";
            KeyStore keyStore = null;
            try
            {
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            PrivateKey privateKey = null;
            try {
                privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias,null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] originalgrpkey = obj.decryptAsymmetric(groupkeyBytes,privateKey);

            SecretKeySpec secretKeySpec = new SecretKeySpec(originalgrpkey,0,originalgrpkey.length,"AES");

            byte[] encyptSessionkeybytes=null;
            try {
                encyptSessionkeybytes = obj.encrypt(sessionKey.getEncoded(),secretKeySpec);

            } catch (Exception e) {
                e.printStackTrace();
            }
            String encryptedSessionKey = Base64.encodeToString(encyptSessionkeybytes,Base64.DEFAULT);

/*            //generate digital Singature
            Signature sign = null;
            try {
                sign = Signature.getInstance("SHA256withDSA");
                sign.initSign(privateKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sign.update(post.getBytes());
            } catch (SignatureException e) {
                e.printStackTrace();
            }
            byte[] signature = new byte[0];
            try {
                signature = sign.sign();
            } catch (SignatureException e) {
                e.printStackTrace();
            }*/

            //convert digital signature to string
            //String signatureString = Base64.encodeToString(signature,Base64.DEFAULT);
            System.out.println("encrypted:"+encryptedPostString);
            postDetails.setPost1(encryptedPostString);
            postDetails.setSession_key(encryptedSessionKey);
            postDetails.setDigital_signature("test");
            postDetails.setGroup_id(group_id_fetched);
            postDetails.setGroup_version(groupVersion);
            postDetails.setPrivacy(interesting);
            postDetails.setOwner_email_id(email_id);

            System.out.println(postDetails.getPost1());
            System.out.println(postDetails.getSession_key());
            System.out.println(postDetails.getDigital_signature());
            System.out.println(postDetails.getGroup_id());
            System.out.println(postDetails.getGroup_version());
            System.out.println(postDetails.getPrivacy());
            System.out.println(postDetails.getOwner_email_id());

            if(CommonUtils.checkInternetConnection(getApplicationContext()))
            {
                AddPostDataAPI addpost = new AddPostDataAPI(getApplicationContext(),postDetails);
                addpost.execute();
            }

        }

        @Override
        protected List<GroupKeyDetails> doInBackground(Void... voids) {
            System.out.println("In do in back:post"+email_id+""+interesting);
            System.out.println("postmode"+postMode+"   email:"+email_id);
            Call<List<GroupKeyDetails>> getgroupDetail = Api.getClient().getGroupDetailsForPost(interesting,email_id);
            try {
                Response<List<GroupKeyDetails>> response = getgroupDetail.execute();
                System.out.println("Getting group Details:"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class AddPostDataAPI extends AsyncTask<Void,Void,String>
    {
        Context context;
        PostDetails postDetails;
        public AddPostDataAPI(Context applicationContext, PostDetails postDetails) {
            context = applicationContext;
            this.postDetails = postDetails;
        }

        @Override
        protected  void onPostExecute(String statusForPost)
        {
            if(statusForPost.equals("Success"))
            {
                System.out.println("Post has been successfully posted");
                Toast.makeText(context, "Post has been created successfully!!!", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected String doInBackground(Void... voids)
        {
            System.out.println("In Do in Final:AddPostDataAPI");
            Call<String> postdata = Api.getClient().addPost(postDetails);
            try {
                Response<String> response = postdata.execute();
                System.out.println("In Do In Response:"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class PostPrivateAPI extends AsyncTask<Void,Void,String>
    {
        PostDetails postDetails = new PostDetails();
        Context mcontext;
        public PostPrivateAPI(Context applicationContext)
        {
            mcontext = applicationContext;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            PrefsManager obj = new PrefsManager(getApplicationContext());
            String email_id =obj.getStringData("email_id");
            postDetails.setOwner_email_id(email_id);
            postDetails.setPrivacy("Private");
            postDetails.setOwner_email_id(email_id);
            postDetails.setPost1(postEncryptString);
            postDetails.setSession_key(encryptedSessionKeString);
            postDetails.setDigital_signature("test");
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Success"))
            {
                Toast.makeText(mcontext, "Posted Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(mcontext, "Problem Occured", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            Call<String> callPrivatePost = Api.getClient().addPrivate(postDetails);
            try {
                Response<String> response = callPrivatePost.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}