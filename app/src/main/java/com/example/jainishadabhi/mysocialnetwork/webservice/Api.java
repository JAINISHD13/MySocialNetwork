package com.example.jainishadabhi.mysocialnetwork.webservice;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.jainishadabhi.mysocialnetwork.util.CommonUtils;

public class Api {

    private static Retrofit retrofit = null;

    public static APIInterface getClient() {

        TrustManager[] myTrustManagerArray = new TrustManager[]{new TrustEveryoneManager()};
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, myTrustManagerArray, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HostnameVerifier hostVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) myTrustManagerArray[0]).hostnameVerifier(hostVerifier).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(CommonUtils.MlaBaseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

        return retrofit.create(APIInterface.class);
    }

}


class TrustEveryoneManager implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] arg0, String arg1){}
    public void checkServerTrusted(X509Certificate[] arg0, String arg1){}
    public X509Certificate[] getAcceptedIssuers() {
        return new java.security.cert.X509Certificate[]{};
    }
}
