package com.quagnitia.studentattendance.teacher;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.quagnitia.studentattendance.R;

public class ViewImageActivity extends AppCompatActivity {
private WebView webView;
    private TextView tv_title;
    private ProgressDialog progress;
    public void openProgressDialog() {
        progress = new ProgressDialog(this);
        progress.setMessage("Loading.......");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        webView = (WebView) findViewById(R.id.webView);
        tv_title=(TextView)findViewById(R.id.tv_title);
        tv_title.setText(getIntent().getStringExtra("ImageName"));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(progress!=null)
                {
                    if(!progress.isShowing())
                        openProgressDialog();
                }
                else
                {
                    openProgressDialog();
                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(progress!=null)
                {
                    if(progress.isShowing())
                        progress.dismiss();
                }

            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        if (getIntent() != null) {
            webView.loadUrl(getIntent().getStringExtra("Url"));
        }

    }
}
