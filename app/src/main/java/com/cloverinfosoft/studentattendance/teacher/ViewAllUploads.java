package com.cloverinfosoft.studentattendance.teacher;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloverinfosoft.studentattendance.R;
import com.cloverinfosoft.studentattendance.Services.AppConstants;
import com.cloverinfosoft.studentattendance.Services.OnTaskCompleted;
import com.cloverinfosoft.studentattendance.Services.WebService;
import com.cloverinfosoft.studentattendance.adapters.ViewAllFilesAdapter;
import com.cloverinfosoft.studentattendance.models.AllFilesModel;
import com.cloverinfosoft.studentattendance.utils.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewAllUploads extends AppCompatActivity implements OnTaskCompleted,View.OnClickListener {
    private RecyclerView recycler_view_classes;
    private ImageView img_back;
    private ViewAllFilesAdapter viewAllClassAdapter;
    private Context mContext;
    private int pos=-1;
    private TextView tv_no_records;
    private PreferencesManager preferencesManager;
    List<AllFilesModel> list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_uploads);
        preferencesManager=PreferencesManager.getInstance(this);
        mContext=this;
        initUI();
        initListener();
        callFetchAllUploadsWS();
    }

    public void callViewFileWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("ImageName",list.get(pos).getImagename());
        new WebService(this,this,hashMap,"viewFile").execute(AppConstants.BASE_URL+AppConstants.VIEW_FILE);
    }

    public void callDeleteClassWS(int pos)
    {
        this.pos=pos;
        HashMap hashMap=new HashMap();
        hashMap.put("ImageName",list.get(pos).getImagename());
        new WebService(this,this,hashMap,"deleteImage").execute(AppConstants.BASE_URL+AppConstants.DELETE_FILE);
    }
    public void callFetchAllUploadsWS()
    {
        new WebService(this,this,null,"FetchAllFile").execute(AppConstants.BASE_URL+AppConstants.FETCH_FILE);
    }



    public void initUI()
    {
        tv_no_records=(TextView)findViewById(R.id.tv_no_records);
        recycler_view_classes=(RecyclerView)findViewById(R.id.recycler_view_classes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recycler_view_classes.setLayoutManager(mLayoutManager);
        recycler_view_classes.setItemAnimator(new DefaultItemAnimator());
        img_back=(ImageView)findViewById(R.id.img_back);
    }

    public void initListener()
    {
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {
        if(result.equals(""))
        {
            Toast.makeText(mContext, "No Response From Server", Toast.LENGTH_SHORT).show();
            return;
        }
        if(jsonObject.optBoolean("success"))
        {
            switch (TAG)
            {
                case "FetchAllFile":
                    try
                    {
                        JSONArray jsonArray=new JSONArray(jsonObject.optString("result"));
                        if(jsonArray.length()!=0)
                        {

                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.optJSONObject(i);
                                AllFilesModel getAllHomework=new AllFilesModel();
                                getAllHomework.setImagename(jsonObject1.optString("ImageName"));
                                list.add(getAllHomework);
                            }
                            tv_no_records.setVisibility(View.GONE);
                            recycler_view_classes.setVisibility(View.VISIBLE);
                            viewAllClassAdapter=new ViewAllFilesAdapter(mContext,list);
                            recycler_view_classes.setAdapter(viewAllClassAdapter);
                        }
                        else
                        {
                            tv_no_records.setVisibility(View.VISIBLE);
                            recycler_view_classes.setVisibility(View.GONE);
                            Toast.makeText(mContext, "No Records Found", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;


                case "deleteImage":
                    Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                    Intent in=getIntent();
                    startActivity(in);
                    finish();
                    break;

                case "viewFile":
                    new DownloadFileFromURL(jsonObject.optString("imagename")).execute(jsonObject.optString("image"));
//                    Intent intent=new Intent(mContext,ViewImageActivity.class);
//                    intent.putExtra("Url",jsonObject.optString("image"));
//                    intent.putExtra("ImageName",jsonObject.optString("imagename"));
//                    startActivity(intent);
//                    finish();
                    break;


            }
        }
        else
        {
            tv_no_records.setVisibility(View.VISIBLE);
            recycler_view_classes.setVisibility(View.GONE);
            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
        }

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog progress;
        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
String imageName="";
        DownloadFileFromURL(String imageName)
        {
            this.imageName=imageName;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(ViewAllUploads.this);
            progress.setMessage("Loading.......");

            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/data/"+imageName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
           progress.dismiss();

        }
    }

}
