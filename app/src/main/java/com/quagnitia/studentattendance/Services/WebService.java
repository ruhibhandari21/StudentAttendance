package com.quagnitia.studentattendance.Services;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.quagnitia.studentattendance.R;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class WebService extends AsyncTask<String, String, String> {

    OnTaskCompleted listener;
    Context mContext;
    HashMap<String, String> postDataParams;
    String TAG, dup_url = "";
    ProgressDialog progress;


    public WebService(Context mContext, OnTaskCompleted listener, HashMap<String, String> postDataParams, String TAG) {
        this.listener = listener;
        this.TAG = TAG;
        this.mContext = mContext;
        this.postDataParams = postDataParams;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    HttpURLConnection conn;
    public String uploadData(String requestURL, HashMap<String, String> postDataParams, String TAG) {
        InputStream is = null;
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            if (TAG.equalsIgnoreCase("Update_Profile_Info")) {
                final MultipartUtility http = new MultipartUtility(url);
                http.addFilePartData(postDataParams);
                final byte[] bytes = http.finish();
                InputStream myInputStream = new ByteArrayInputStream(bytes);
                response = decodeResponse(myInputStream);
            } else {
                 conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded" );
//                conn.setRequestProperty("Authorization", "basic " +
//                        Base64.encode("root:".getBytes(), Base64.NO_WRAP));
                if (postDataParams != null) {
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(getPostDataString(postDataParams));
                    writer.flush();
                    writer.close();
                    os.close();
                } else {
                    conn.setRequestMethod("GET");
                }

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }
                } else {
                    response = "";

                }
            }

        } catch (SocketTimeoutException s) {
            s.printStackTrace();
            conn.disconnect();
            cancel(true);
            progress.dismiss();
            showAlert();

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            conn.disconnect();
            cancel(true);
            progress.dismiss();
            showAlert();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
            cancel(true);
            progress.dismiss();
            showAlert();

        } catch (Exception e) {
            conn.disconnect();
            cancel(true);
            e.printStackTrace();
            progress.dismiss();
            showAlert();

        }
        Log.v(TAG, "Response: " + response);
        return response;
    }


    private static String decodeResponse(InputStream is) {
        String result = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString();

        } catch (Exception EXCEPTION) {
            EXCEPTION.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        progress = new ProgressDialog((Context) listener);
        progress.setMessage("Loading.......");

        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();


    }

    @Override
    protected String doInBackground(String... url) {
        dup_url = url[0];
        return uploadData(url[0], postDataParams, TAG);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {

            if (progress.isShowing()) {
                progress.dismiss();

            }

            JSONObject jsonObject = new JSONObject(result);
            conn.disconnect();
            listener.onTaskCompleted(jsonObject, result, TAG);


        } catch (Exception e) {
            Log.e("ReadJSONFeedTask", e.getLocalizedMessage() + "");
        }

    }

    public void showAlert() {

        ((Activity) listener).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog((Context) listener);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                LayoutInflater lf = (LayoutInflater) ((Context) listener)
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogview = lf.inflate(R.layout.retry_dialog, null);
                TextView title = (TextView) dialogview.findViewById(R.id.title);
                title.setText("Note");
                TextView body = (TextView) dialogview
                        .findViewById(R.id.dialogBody);
                body.setText("Please check your network connection");
                dialog.setContentView(dialogview);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                dialog.show();
                TextView cancel = (TextView) dialogview
                        .findViewById(R.id.dialogCancel);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        progress.dismiss();
                        dialog.dismiss();
                    }
                });

                TextView retry = (TextView) dialogview
                        .findViewById(R.id.dialogRetry);
                retry.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new WebService(mContext, listener, postDataParams, TAG).execute(dup_url);
                        dialog.dismiss();
                    }
                });

            }

        });
    }

}//end of class
