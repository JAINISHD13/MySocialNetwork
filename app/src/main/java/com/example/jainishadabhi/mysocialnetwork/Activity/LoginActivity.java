package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.jainishadabhi.mysocialnetwork.R;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import retrofit2.Call;
import retrofit2.Response;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class LoginActivity extends AppCompatActivity {

    Button LoginButton;
    EditText UserEmail;
    EditText UserPassword;
    TextView NeedNewAccountLink;
    private ProgressDialog loadingBar;
    UserDetails userDetails;
    String emailPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NeedNewAccountLink = (TextView) findViewById(R.id.register_account_link);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        LoginButton = (Button) findViewById(R.id.login_button);
        loadingBar = new ProgressDialog(this);
        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToRegisterActivity();
            }
        });


      LoginButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              String email_id = UserEmail.getText().toString();
              String password = UserPassword.getText().toString();

              if (CommonUtils.checkInternetConnection(LoginActivity.this)) {
                  LogInApi authenticate = new LogInApi(getApplicationContext(),email_id,password);
                  authenticate.execute();
              } else {
                  Toast.makeText(getApplicationContext(), "Something wrong with Internet Connection !", Toast.LENGTH_SHORT).show();
              }
          }
      });
    }

    private void SendUserToRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    class LogInApi extends AsyncTask<Void, Void, List<UserDetails>>
    {
        Context appContext;
        String email_id,password;
        public LogInApi(Context context, String email_id, String password)
        {
            appContext = context;
            this.email_id = email_id;
            this.password = password;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getApplicationContext(),"Verifying Credentials...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(List<UserDetails> usersDetails)
        {
            if (usersDetails!=null && usersDetails.size()>0)
            {
               /* Intent passIntent = new Intent(LoginActivity.this,GroupActivity.class);
                passIntent.putExtra("emailid",userDetails.getEmail_id());
              */
                PrefsManager obj =new PrefsManager(getApplicationContext());
                String email = obj.getStringData("email_id");


                KeyGenerator keyGenerator2 = null;
                try {
                    keyGenerator2 = KeyGenerator.getInstance("AES");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                keyGenerator2.init(128);
                final SecretKey sessionKey = keyGenerator2.generateKey();

                byte[] sessionkeyBytes = sessionKey.getEncoded();

                String sessionkeyString = Base64.encodeToString(sessionkeyBytes,Base64.DEFAULT);
                System.out.println("Sessionkey at login:hahvjhavjhavf------------------------------------:"+sessionkeyString);
                obj.saveData("session_key_string",sessionkeyString);

                String publickey =usersDetails.get(0).getPublic_key();

                obj.saveData("email_id",usersDetails.get(0).getEmail_id());
                obj.saveData("public_key",publickey);
                Toast.makeText(appContext, "In Post", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
  //              Intent groupIntent = new Intent(LoginActivity.this,GroupActivity.class);
//                groupIntent.putExtra("Public key",userDetails.getPublic_key());
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(appContext, "Error Handled", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected List<UserDetails> doInBackground(Void... params) {
            try
            {
                Call<List<UserDetails>> callAuth = Api.getClient().authenticate(email_id, password);
                Response<List<UserDetails>> response=callAuth.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
