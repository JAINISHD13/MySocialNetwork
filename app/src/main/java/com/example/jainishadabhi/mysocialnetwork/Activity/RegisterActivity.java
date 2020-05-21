package com.example.jainishadabhi.mysocialnetwork.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.security.KeyStore;
import android.util.Base64;

import com.example.jainishadabhi.mysocialnetwork.R;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

import com.example.jainishadabhi.mysocialnetwork.model.UserDetails;
import retrofit2.Call;
import retrofit2.Response;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;
import com.example.jainishadabhi.mysocialnetwork.util.PrefsManager;
import com.example.jainishadabhi.mysocialnetwork.webservice.Api;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.os.Build.VERSION_CODES.*;


public class RegisterActivity extends AppCompatActivity {

    private EditText UserEmail, UserPassword, UserFirstname,UserLastname,UserContactNo;
    private Button CreateAccountButton;
    private ProgressDialog loadingBar;
    private  String group_key1;

    @RequiresApi(api = O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText)findViewById(R.id.register_password);
        UserFirstname =(EditText)findViewById(R.id.register_firstname);
        UserLastname = (EditText) findViewById(R.id.register_lastname);
        UserContactNo = (EditText) findViewById(R.id.register_contactNo);
        CreateAccountButton = (Button)findViewById(R.id.register_create_account);
        loadingBar = new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = M)
            @Override
            public void onClick(View view) {

                try {
                    CreateNewAccount();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @SuppressLint("NewApi")
    @RequiresApi(api = M)
    private void CreateNewAccount() throws Exception {
        UserDetails userDetails = new UserDetails();

        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String firstname = UserFirstname.getText().toString();
        String lastname = UserLastname.getText().toString();
        String contactNo = UserContactNo.getText().toString();



        String aliasPublic = email + "public_private";

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA,"AndroidKeyStore");
        final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(aliasPublic,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setKeySize(2048)
                .build();

        keyGen.initialize(keyGenParameterSpec);
        KeyPair keypair = keyGen.genKeyPair();
        PrivateKey privateKey = keypair.getPrivate();
        PublicKey publicKey = keypair.getPublic();


       /* PrefsManager obj = new PrefsManager(getApplicationContext());
        obj.saveData("public_key",publicKey);
*/
        //byte[] privateKeyBytes = privateKey.getEncoded();
        byte[] publicKeyBytes = publicKey.getEncoded();

        String publickey = Base64.encodeToString(publicKeyBytes,Base64.DEFAULT);
        //String publickey= new String(Base64.encode(publicKeyBytes,Base64.DEFAULT));
        //String privatekey = new String(Base64.encode(privateKeyBytes,Base64.DEFAULT));

        PrefsManager obj = new PrefsManager(getApplicationContext());

        String alias= email+"Secret";

        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES);
        keyGenerator.init(new KeyGenParameterSpec.Builder(
                alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(128)
                .build());
        SecretKey secretKey = keyGenerator.generateKey();

        KeyGenerator keyGenerator3 = null;
        try {
            keyGenerator3 = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator3.init(128);
        SecretKey privateSecretKey = keyGenerator3.generateKey();

        String privateSecretKeyString =Base64.encodeToString(privateSecretKey.getEncoded(),Base64.DEFAULT);
        obj.saveData("privateSecretKey"+email,privateSecretKeyString);



        KeyGenerator keyGenerator2 = KeyGenerator.getInstance("AES");
        keyGenerator2.init(128);
        SecretKey groupKey = keyGenerator2.generateKey();


        /*byte[] encryption = obj.encrypt(groupKey.getEncoded(),secretKey);
        byte[] decryption = obj.decrypt(encryption,secretKey);

        String decrptS =Base64.encodeToString(decryption,Base64.DEFAULT);
        System.out.println("Decryption::::::::::::::::"+decrptS);
        System.out.println("Encryption------------------------ :"+obj.encrypt("Hello".getBytes(),groupKey));
        System.out.println("Decryption-------------------------:"+(new String(obj.decrypt(obj.encrypt("Hello".getBytes(),secretKey),groupKey))));
        *//*
        System.out.println("sdhadguagdbahsbdhasbdhasbdhasbdhasvdjvasdvhgasdchacdhvdhc:"+hellobytes);
        byte[] helloString = obj.decryptAsymmetric(hellobytes,privateKey);
        System.out.println("ahdvahvdhavdgasvdgasvdvadvadvjadhahvdjhsadvhjasvd:"+helloString);
        byte[] hellobytes= obj.encryptAsymmetric("Hello".getBytes(),publicKey);
*/
/*
        KeyGenerator SessionKey = null;
        try {
            SessionKey = KeyGenerator.getInstance("AES");
        } catch (Exception e) {
            e.printStackTrace();
        }

        SessionKey.init(128);
        SecretKey sessionkey = SessionKey.generateKey();
        System.out.println("In Login:Session key:"+sessionkey);
        byte[] sessionKeyBytes = sessionkey.getEncoded();

        byte[] helloencrypted = obj.encrypt("hello".getBytes(),sessionkey);

        SecretKey keySpec = new SecretKeySpec(sessionKeyBytes,0,sessionKeyBytes.length,"AES");

        System.out.println("Decrypted String using Session key:"+new String(obj.decrypt(helloencrypted,keySpec)));

        byte[] grpkeyBytes = groupKey.getEncoded();
        String grpKeyString= Base64.encodeToString(grpkeyBytes,Base64.DEFAULT);
        System.out.println("Group key in string:"+grpKeyString);
        byte[] groupKeyEncrypt = obj.encryptAsymmetric(grpkeyBytes,publicKey);
        String EncryptedgrpKeyString = Base64.encodeToString(groupKeyEncrypt,Base64.DEFAULT);
        System.out.println("Get Encrypted Group key:"+EncryptedgrpKeyString);

        byte[] decryptedGrpkey = Base64.decode(EncryptedgrpKeyString,Base64.DEFAULT);
        byte[] decryptgrpKeyBytes = obj.decryptAsymmetric(decryptedGrpkey,privateKey);
        System.out.println("Decrypted Group key:"+Base64.encodeToString(decryptgrpKeyBytes,Base64.DEFAULT));

        System.out.println("Compare:"+grpKeyString.equals(Base64.encodeToString(decryptgrpKeyBytes,Base64.DEFAULT)));

        byte[] sessionEncryptBytes = obj.encrypt(sessionKeyBytes,groupKey);
        String sessionkeyString = Base64.encodeToString(sessionEncryptBytes,Base64.DEFAULT);
        System.out.println("Sessionkey string:"+sessionkeyString);

        byte[] sessionkeyByteDecrypt = Base64.decode(sessionkeyString,Base64.DEFAULT);
        byte[] sessionDecrypt = obj.decrypt(sessionkeyByteDecrypt,groupKey);
        System.out.println("Decrypted Session compare:"+(Base64.encodeToString(sessionKeyBytes,Base64.DEFAULT)).equals(Base64.encodeToString(sessionDecrypt,Base64.DEFAULT)));
        System.out.println("Decrypted Sessionkey:"+Base64.encodeToString(sessionDecrypt,Base64.DEFAULT));


        System.out.println("New Sting Testing:\t\t hello... This is Final Test!");
        byte[] sessionKeyBytes2 = sessionkey.getEncoded();
        byte[] helloencrypted2 = obj.encrypt("hello... This is Final Test!".getBytes(),sessionkey);
        byte[] sessionEncryptBytes2 = obj.encrypt(sessionKeyBytes2,groupKey);
        String sessionkeyString2 = Base64.encodeToString(sessionEncryptBytes2,Base64.DEFAULT);
        byte[] sessionkeyByteDecrypt2 = Base64.decode(sessionkeyString2,Base64.DEFAULT);
        byte[] sessionDecrypt2 = obj.decrypt(sessionkeyByteDecrypt2,groupKey);
        SecretKey keySpec2 = new SecretKeySpec(sessionDecrypt2,0,sessionDecrypt2.length,"AES");

        System.out.println("Decrypted String using Session key:"+new String(obj.decrypt(helloencrypted2,keySpec2)));


*/


      /*  System.out.println("Hello session key encryption:");
        byte[] sessionencrypt = obj.encrypt(sessionKeyBytes,groupKey);
      */
/*
        System.out.println("decryption");
        byte[] decryptsession = obj.decrypt(sessionencrypt,groupKey);

        System.out.println("decryption of encrypted string");
        SecretKeySpec keyspec = new SecretKeySpec(decryptsession,"AES");
        byte[] decryptString = obj.decrypt(helloencrypted,keyspec);

        System.out.println("Decrypted String:"+new String(decryptString));
*/


/*
        byte[] IV = new byte[16];
        SecureRandom random;
        random = new SecureRandom();
        random.nextBytes(IV);
*/

  //      PrefsManager obj = new PrefsManager(getApplicationContext());
/*
        System.out.println("----------------------------------------------------------------");
        System.out.println("Raw Group key:"+groupKey);
        System.out.println("Group Key as an byte:"+groupKey.getEncoded());
        System.out.println("Group key in string:"+obj.encoderfun(groupKey.getEncoded()));
        byte[] groupEncrypted=obj.encryptAsymmetric(groupKey.getEncoded(),publicKey);
        System.out.println("grpkey encrypted in bytes:"+groupEncrypted);
        String encryptedString = obj.encoderfun(groupEncrypted);
        System.out.println("group key in string:"+encryptedString);
        System.out.println("Encrypted Group key done");
        System.out.println("Group key decryption--------------");
        byte[] decryptgrpkey = Base64.decode(encryptedString,Base64.DEFAULT);*/
/*
        System.out.println("Decrypted grp key in bytes:"+decryptgrpkey);
        System.out.println("Decrypted grp key in String:"+obj.encoderfun(decryptgrpkey));*//*

        String helloDecryptedwithPrivate = obj.decryptAsymmetric(decryptgrpkey,privateKey);

        System.out.println("Decrypt using private key:"+helloDecryptedwithPrivate);
        System.out.println("Hello Decrypted with private key:"+obj.encoderfun(Base64.decode(helloDecryptedwithPrivate,Base64.DEFAULT)));
        System.out.println("Check both Array same or not:"+ Arrays.equals(groupKey.getEncoded(),Base64.decode(helloDecryptedwithPrivate,Base64.DEFAULT)));
        System.out.println("Check both String same or not:"+helloDecryptedwithPrivate.equals(obj.encoderfun(groupKey.getEncoded())));
        System.out.println("----------------------------------------------------------------");
*/
/*
        byte[] decy = obj.encrypt("Hello".getBytes(),groupKey);
        System.out.println(":::::::::::::::::::Decrypted asns:::::::::::::::::::"+obj.decrypt(decy,groupKey));
*/

       // PrefsManager obj = new PrefsManager(getApplicationContext());
        byte[] cipherText= obj.encryptAsymmetric(groupKey.getEncoded(),publicKey);
        group_key1 = Base64.encodeToString(cipherText,Base64.DEFAULT);

        System.out.println("email:"+email);
        System.out.println("pass:"+password);
        System.out.println("fn:"+firstname);
        System.out.println("ln:"+lastname);
        System.out.println("cn:"+contactNo);
        System.out.println(publickey);

        userDetails.setEmail_id(email);
        userDetails.setPassword(password);
        userDetails.setFirstName(firstname);
        userDetails.setLastName(lastname);
        userDetails.setContactNo(Long.parseLong(contactNo));
        userDetails.setPublic_key(publickey);

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Write your email!!!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Write your Password!!!", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(firstname))
        {
            Toast.makeText(this, "Please Write your firstname!!!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(lastname))
        {
            Toast.makeText(this, "Please write the lastname!!!", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(contactNo))
        {
            Toast.makeText(this, "Please write the contactNO!!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (CommonUtils.checkInternetConnection(RegisterActivity.this)) {
                RegisterApi authenticate = new RegisterApi(getApplicationContext(),userDetails);
                authenticate.execute();
            } else {
                Toast.makeText(getApplicationContext(), "Something wrong with Internet Connection !", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private class RegisterApi extends AsyncTask<Void, Void, String>
    {
        private UserDetails userDetails;
        Context appcontext;

        public RegisterApi(Context applicationContext, UserDetails userDetails) {
            appcontext = applicationContext;
            this.userDetails = userDetails;
        }


        @Override
        protected void onPreExecute()
        {
            /*loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait until the process finished.");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
 */       }

        @Override
        protected void onPostExecute(String status)
        {
            if (status.toString()=="succeed")
            {

                PrefsManager obj = new PrefsManager(getApplicationContext());
                obj.saveData("to_email_id",userDetails.getEmail_id());
             //   loadingBar.dismiss();
                Toast.makeText(appcontext, "In Post", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);

             finish();

            }
            else if (status=="failed")
            {
                Toast.makeText(appcontext, "Error Handled", Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
            }
        }


        @Override
        protected String doInBackground(Void... voids) {
            try
            {
                Call<String> callRegister = Api.getClient().addUser(userDetails,group_key1);
                Response<String> respCallRegister = callRegister.execute();
                System.out.println("IN DO IN:"+respCallRegister.body());
                //return respCallRegister.body();
                if (respCallRegister.isSuccessful())
                {
                    return "succeed";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}
