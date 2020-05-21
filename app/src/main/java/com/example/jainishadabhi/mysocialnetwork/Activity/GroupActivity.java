package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Xml;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jainishadabhi.mysocialnetwork.R;
import com.example.jainishadabhi.mysocialnetwork.model.GroupMainDetails;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Response;

public class GroupActivity extends AppCompatActivity {
    private EditText group_name;
    private Button create_group_button;
    private String publickey;
    private byte[] cipherText;
    private String encrypted;
    private String emailid;
    private String versionNumber;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent intent = getIntent();
        publickey = intent.getStringExtra("Public key");
        emailid = intent.getStringExtra("emailid");
        group_name = (EditText) findViewById(R.id.group_name);
        create_group_button = (Button) findViewById(R.id.create_group_button);

        mToolbar = (Toolbar) findViewById(R.id.group_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Create Group");

        create_group_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    CreateGroup();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
        Intent intent = new Intent(GroupActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void CreateGroup() throws Exception {
        GroupMainDetails groupMainDetails = new GroupMainDetails();
        String groupname = group_name.getText().toString();


        if (TextUtils.isEmpty(groupname)) {
            Toast.makeText(this, "Please enter the name of the group", Toast.LENGTH_SHORT).show();
        } else {
            KeyGenerator keyGenerator2 = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES);
            keyGenerator2.init(128);
            SecretKey groupKey = keyGenerator2.generateKey();


            PrefsManager obj = new PrefsManager(getApplicationContext());
            String publicKeyString = obj.getStringData("public_key");
            byte[] publicBytes = Base64.decode(publicKeyString, Base64.DEFAULT);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFact = null;
            keyFact = KeyFactory.getInstance("RSA");
            PublicKey pubkey = keyFact.generatePublic(x509KeySpec);

            byte[] encryptedgrpkey = obj.encryptAsymmetric(groupKey.getEncoded(), pubkey);

            String encryptedgrpkeyString = Base64.encodeToString(encryptedgrpkey,Base64.DEFAULT);

            groupMainDetails.setEmail_id(obj.getStringData("email_id"));
            groupMainDetails.setIsFriend("NO");
            groupMainDetails.setGroup_isOwner("YES");
            groupMainDetails.setGroup_name(groupname);
            String group_status = "Key_Activated";
            int group_version = 1;

            System.out.println("gropu email:"+groupMainDetails.getEmail_id());
            System.out.println("gropu email:"+groupMainDetails.getIsFriend());
            System.out.println("gropu email:"+groupMainDetails.getGroup_isOwner());

            if (CommonUtils.checkInternetConnection(getApplicationContext())) {
                GroupCreateAPI create = new GroupCreateAPI(getApplicationContext(), groupMainDetails, group_status, encryptedgrpkeyString, group_version);
                create.execute();
            } else {
                Toast.makeText(this, "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GroupCreateAPI extends AsyncTask<Void, Void, String>
    {

        Context mcontext;
        GroupMainDetails groupMainDetails;
        String group_status, group_key1;
        int group_version;

        public GroupCreateAPI(Context applicationContext, GroupMainDetails groupMainDetails, String group_status, String encryptedgrpkeyString, int group_version)
        {
            mcontext = applicationContext;
            this.groupMainDetails = groupMainDetails;
            this.group_status = group_status;
            this.group_version = group_version;
            group_key1 = encryptedgrpkeyString;
        }

        @Override
        protected  void onPostExecute(String status)
        {
            if(status.equals("succeed")){
                Toast.makeText(mcontext, "Group created", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(mcontext, "Problem Occured", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(Void... voids)
        {
            System.out.println("email:"+groupMainDetails.getEmail_id());
            Call<String> createGrp = Api.getClient().create_new_group(groupMainDetails, group_status, group_key1, group_version);
            try
            {
                Response<String> response = createGrp.execute();
                System.out.println("ajbfhaevhjvfvefvjwe:"+response.body());
                return response.body();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            return "";
        }
    }
}
