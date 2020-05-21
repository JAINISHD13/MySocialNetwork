package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jainishadabhi.mysocialnetwork.Adapter.FriendRequestAdapter;
import com.example.jainishadabhi.mysocialnetwork.Adapter.GroupRequestAdapter;
import com.example.jainishadabhi.mysocialnetwork.Interface.RecyclerViewClickInterface;
import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.GroupInvitation;
import com.example.jainishadabhi.mysocialnetwork.model.GroupKeyDetails;
import com.example.jainishadabhi.mysocialnetwork.model.GroupMainDetails;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Response;

public class GroupRequestActivity extends AppCompatActivity implements RecyclerViewClickInterface {

    private Toolbar mToolbar;
    private RecyclerViewClickInterface recyclerViewClickInterface;
    ArrayList<GroupInvitation> mInvitation = new ArrayList<>();
    private RecyclerView requestList;
    GroupRequestAdapter adapter;
    CircleImageView accept,reject;
    TextView fullname;
    String from_email_id;
    String to_email_id;
    int group_id;
    String group_name;
    String publicKeyData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_request);

        mToolbar = (Toolbar) findViewById(R.id.grouprequest_appbar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Group Requests");

        accept =(CircleImageView)findViewById(R.id.accept_friend_request);
        reject =(CircleImageView)findViewById(R.id.reject_friend_request);
        fullname = (TextView) findViewById(R.id.all_requested_users_profile_full_name);


        DisplayAllGroupRequests();

    }

    public void removeItem(int position) {
        mInvitation.remove(position);
        adapter.notifyItemRemoved(position);
    }


    private void DisplayAllGroupRequests()
    {
        String to_email_id;
        PrefsManager obj = new PrefsManager(getApplicationContext());
        to_email_id = obj.getStringData("email_id");
        System.out.println("Login email_id:"+obj);

        if (CommonUtils.checkInternetConnection(getApplicationContext()))
        {
            System.out.println("In FriendRequestAPI");
            GroupRequestShowAPI showRequest = new GroupRequestShowAPI(getApplicationContext(),to_email_id);
            showRequest.execute();
        }
        else
        {
            Toast.makeText(this, "Check ur Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            sendUserToMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendUserToMainActivity()
    {
        Intent intent = new Intent(GroupRequestActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.group_request_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new GroupRequestAdapter(mInvitation,getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RecyclerViewClickInterface() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {

            }

            @Override
            public void onAcceptClick(int position) {

            }
        });


        adapter.setOnItemClickListener(new RecyclerViewClickInterface()
        {
            @Override
            public void onItemClick(int position) { }

            @Override
            public void onDeleteClick(int position)
            {
                from_email_id=mInvitation.get(position).getFrom_email_id();
                group_id = mInvitation.get(position).getGroup_id();
                PrefsManager obj = new PrefsManager(getApplicationContext());
                to_email_id=obj.getStringData("email_id");
                if (CommonUtils.checkInternetConnection(getApplicationContext()))
                {
                    CancelGroupRequestAPI cancelReq = new CancelGroupRequestAPI(getApplicationContext(),from_email_id,to_email_id,group_id);
                    cancelReq.execute();
                    removeItem(position);
                }
                else
                {
                    Toast.makeText(GroupRequestActivity.this, "Check your Internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAcceptClick(int position) {
                System.out.println("In On AcceptClick");
                from_email_id= mInvitation.get(position).getFrom_email_id();
                group_id = mInvitation.get(position).getGroup_id();
                group_name = mInvitation.get(position).getGroup_name();
                GetPublicKeyForGrpReqAPI getPublicKeyData = new GetPublicKeyForGrpReqAPI(getApplicationContext(),from_email_id,group_id,group_name);
                getPublicKeyData.execute();
                removeItem(position);
            }

        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onAcceptClick(int position) {

    }


    private class GroupRequestShowAPI extends AsyncTask<Void, Void, List<GroupInvitation>>
    {
        Context mcontext;
        String to_email_id;
        List<GroupInvitation> userDetails = new ArrayList<>();

        public GroupRequestShowAPI(Context applicationContext, String to_email_id)
        {
            mcontext = applicationContext;
            this.to_email_id = to_email_id;
        }

        @Override
        protected  void onPostExecute(List<GroupInvitation> invitations)
        {
            System.out.println("In GroupRequestAPI;on post");
            for (GroupInvitation invite: invitations)
            {
                mInvitation.add(invite);
            }
            // System.out.println("--------------------group_id:"+group_id);
            initRecyclerView();
        }

        @Override
        protected List<GroupInvitation> doInBackground(Void... voids) {
            Call<List<GroupInvitation>> getUserDetails = Api.getClient().getGroupRequest(to_email_id);
            try
            {
                Response<List<GroupInvitation>> response = getUserDetails.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class CancelGroupRequestAPI extends AsyncTask<Void,Void,String>
    {
        Context mcontext;
        String to_email_id,from_email_id;
        int group_id;
        public CancelGroupRequestAPI(Context applicationContext, String from_email_id, String to_email_id, int group_id)
        {
            mcontext= applicationContext;
            this.to_email_id = to_email_id;
            this.from_email_id = from_email_id;
            this.group_id=group_id;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println("Response:"+s);
            if (s.equals("Success"))
            {
                Toast.makeText(mcontext, "Successfully Canceled Friend Request", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(mcontext, "Not able to delete the request", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            System.out.println("agdhsadhahdahsdhasd");
            Call<String> removeReq = Api.getClient().cancelGroupRequest(from_email_id,to_email_id,group_id);
            try {
                Response<String> response = removeReq.execute();
                System.out.println("jbdjasbdjsabdjasbd:"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class GetPublicKeyForGrpReqAPI extends AsyncTask<Void,Void,String>
    {
        String to_email_id,group_name;
        Context mcontext;
        int group_id;

        public GetPublicKeyForGrpReqAPI(Context applicationContext, String from_email_id, int group_id, String group_name) {
            this.to_email_id=to_email_id;
            mcontext=applicationContext;
            this.group_id = group_id;
            this.group_name=group_name;
        }

        @Override
        protected  void onPostExecute(String publickeyData)
        {
            System.out.println("GetPublicKeyAPI:on Post");
                System.out.println(publickeyData);
            PrefsManager obj = new PrefsManager(getApplicationContext());
            obj.getStringData("email_id");

            try
            {
               /* KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                PrivateKey privateKey = (PrivateKey) keyStore.getKey("public_private"+to_email_id,null);
               */
                GetGroupKeyDataForGroupAPI getGroupKey = new GetGroupKeyDataForGroupAPI(getApplicationContext(),to_email_id);
                getGroupKey.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        protected String doInBackground(Void... voids)
        {
            Call<List<UserDetails>> getPublicData = Api.getClient().getPublicKeyData(from_email_id);
            try {
                Response<List<UserDetails>> response = getPublicData.execute();
                publicKeyData = response.body().get(0).getPublic_key();
                return publicKeyData;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class UpdatedGroupKeyForGroupAPI extends AsyncTask<Void,Void,String> {
        Context mcontext;
        GroupKeyDetails groupKeyDetails;
        GroupMainDetails groupMainDetails;

        public UpdatedGroupKeyForGroupAPI(Context applicationContext, GroupMainDetails groupMainDetails, GroupKeyDetails groupKeyDetails) {
            mcontext = applicationContext;
            this.groupMainDetails = groupMainDetails;
            this.groupKeyDetails = groupKeyDetails;
        }

        @Override
        protected void onPostExecute(String stat) {
            System.out.println("Status:::::::::::::::::" + stat);

            System.out.println("Status:::::::::::::::::"+stat);

            if (stat != null && stat.equals("Success"))
            {
                Toast.makeText(mcontext, "Accepted", Toast.LENGTH_SHORT).show();

            }else
            {
                Toast.makeText(mcontext, "Error Occured while accepting request", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            String to_email_id =groupKeyDetails.getEmail_id();
            String group_key1 = groupKeyDetails.getGroup_key1();
            int group_version = groupKeyDetails.getGroup_version();
            System.out.println("email_idddddddddddddddddddddddd:"+to_email_id);
            System.out.println("group_keyyyyyyyyyyyyyyyyyyyy:"+group_key1);
            System.out.println("group_versionnnnnnnnnnnnn:"+group_version);
            System.out.println("grpmain:"+groupMainDetails.getEmail_id());
            System.out.println("grpmain:"+groupMainDetails.getGroup_name());
            System.out.println("grpmain:"+groupMainDetails.getGroup_isOwner());
            System.out.println("grpmain:"+groupMainDetails.getIsFriend());
            System.out.println("grpmain:"+groupMainDetails.getIsFriend());
            Call<String> passData = Api.getClient().updateKeyDataForGroup(groupMainDetails,to_email_id,group_key1,group_version);
            try {
                Response<String> response = passData.execute();
                System.out.println("Response:"+response);
                System.out.println("Response:"+response.body());
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

    }

    private class GetGroupKeyDataForGroupAPI extends AsyncTask<Void,Void,String>
    {
        Context context;
        String to_email_id;
        public GetGroupKeyDataForGroupAPI(Context applicationContext, String to_email_id)
        {
            context=applicationContext;
            this.to_email_id = to_email_id;
        }

        @Override
        protected  void onPostExecute(String groupkeyData)
        {
            System.out.println("GetGroupKeyAPI:ON POST");
            System.out.println("clicked Group key:"+groupkeyData);
            String privateKeyAlias = to_email_id+"public_private";
            try {
                KeyStore keyStore = null;
                keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                PrivateKey privateKey = (PrivateKey) keyStore.getKey(privateKeyAlias,null);

                //Decrypted Group key with private key of the logged user

                System.out.println("GroupKey ecdscecsesdcs : " + groupkeyData);
                PrefsManager obj = new PrefsManager(getApplicationContext());
                byte[] groupkeyDataBytes = Base64.decode(groupkeyData,Base64.DEFAULT);
                byte[] decryptedGroupKey= obj.decryptAsymmetric(groupkeyDataBytes,privateKey);

                System.out.println("Public Key:"+publicKeyData);
/*
                byte[] publicBytes = Base64.decode(publicKeyData,Base64.DEFAULT);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(publicBytes);
                KeyFactory fact = KeyFactory.getInstance("RSA");
                PublicKey pubKey = fact.generatePublic(spec);
*//*
                KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
                ks.load(null);*/
                //KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(u_name+ "RSAkey", null);
                byte[] publicBytes = Base64.decode(publicKeyData,Base64.DEFAULT);
                X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicBytes);
                KeyFactory keyFact = null;
                keyFact = KeyFactory.getInstance("RSA");
                PublicKey pubkey = keyFact.generatePublic(x509KeySpec);

                /*X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
                keyStore.load(null);
                PublicKey pubKey = keyFactory.generatePublic(keySpec);
*/

                //Encrypted group key with public key of the requested used
                System.out.println("grupkey byesajbdhasdhasvdhvasd:"+Base64.encodeToString(groupkeyDataBytes,Base64.DEFAULT));
                byte[] encryptedDataGroupKey = obj.encryptAsymmetric(decryptedGroupKey,pubkey);
                String encryptedDataGroupKeyStr = Base64.encodeToString(encryptedDataGroupKey,Base64.DEFAULT);

                GroupMainDetails groupMainDetails = new GroupMainDetails();
                groupMainDetails.setGroup_id(group_id);
                groupMainDetails.setEmail_id(from_email_id);
                groupMainDetails.setGroup_name(group_name);
                groupMainDetails.setGroup_isOwner("NO");
                groupMainDetails.setIsFriend("NO");
                System.out.println(groupMainDetails);

                GroupKeyDetails groupKeyDetails = new GroupKeyDetails();
                groupKeyDetails.setEmail_id(to_email_id);
                groupKeyDetails.setGroup_key1(encryptedDataGroupKeyStr);
                groupKeyDetails.setGroup_version(1);
                System.out.println(groupKeyDetails);

                if (CommonUtils.checkInternetConnection(getApplicationContext()))
                {
                    UpdatedGroupKeyForGroupAPI updateKey = new UpdatedGroupKeyForGroupAPI(getApplicationContext(),groupMainDetails,groupKeyDetails);
                    updateKey.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids)
        {
            PrefsManager obj = new PrefsManager(getApplicationContext());
            to_email_id=obj.getStringData("email_id");

            System.out.println("to_email_id:"+to_email_id);
            Call<String> getGroupKey = Api.getClient().getGroupKeyDataForG(to_email_id,group_id,group_name);
            try {
                Response<String> response = getGroupKey.execute();
                System.out.println("issuccessful:"+response.isSuccessful());
                System.out.println("response body:"+response.body());
                return  response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
