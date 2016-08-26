package com.phoneutils.crosspromotion;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vinodtakhar on 5/7/16.
 */
public class AppsLoader{

    private static final String TAG = AppsLoader.class.getName();

    public AppsLoader() {
    }

    public static Thread thread;

    public static void load(final Context context) {
        if(thread!=null){
            Log.e(TAG,"Already loading...");
            return;
        }

        thread = new Thread(){
            public void run(){
                try {
                    String json = getApps(context);
                    Log.e(TAG,"json:"+json);

                    AppPreferences.setSharedPreference(context,AppPreferences.KEY_APPS_JSON,json);
                    AppPreferences.setLongSharedPreference(context,AppPreferences.KEY_LAST_SYNC_TIME, System.currentTimeMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                thread = null;
            }
        };
        thread.start();
    }

    private static String getApps(Context context) throws IOException {
        URL url = new URL(AppPreferences.getSharedPreference(context,AppPreferences.KEY_URL));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("POST");
        conn.setDoInput(true);
        conn.setDoOutput(true);

        HashMap<String,String> params = new HashMap<>();

        params.put("package",context.getPackageName());
        params.put("category",AppPreferences.getSharedPreference(context,AppPreferences.KEY_CATEGORY));

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));

        writer.write(getQuery(params));

        writer.flush();
        writer.close();
        os.close();

        conn.connect();

        int responseCode = conn.getResponseCode();
        String response="";

        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line=br.readLine()) != null) {
                response+=line;
            }
        }
        else {
            response="";
        }

        return  response;
    }

    private static String getQuery(HashMap<String,String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (String key : params.keySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(params.get(key), "UTF-8"));
        }

        return result.toString();
    }
}
