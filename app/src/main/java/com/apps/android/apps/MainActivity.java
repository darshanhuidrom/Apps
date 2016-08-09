package com.apps.android.apps;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apps.android.apps.utils.ConnectionDetector;
import com.apps.android.apps.utils.Constant;
import com.apps.android.apps.utils.DialogBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private LinearLayout ll_loading;
    private ImageView iv_no_internet;

    public enum Displayer {LOADER_VIEW, NO_INTERNET_VIEW, WEB_VIEW}

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        iv_no_internet = (ImageView) findViewById(R.id.iv_no_internet);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                setTitle("Loading..." + newProgress);
                displayView(Displayer.LOADER_VIEW);
                if (newProgress == 100) {
                    setTitle(R.string.app_name);
                    displayView(Displayer.WEB_VIEW);
                }

            }


        });
        if (ConnectionDetector.isNetworkAvailable(getApplicationContext())) {
            startLoading();

        } else {
            displayView(Displayer.NO_INTERNET_VIEW);
        }

    }


    public InputStream getFileFromAsset(String filenameWithextention) {
        AssetManager mgr = getBaseContext().getAssets();
        InputStream in = null;
        try {
            in = mgr.open(filenameWithextention, AssetManager.ACCESS_BUFFER);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            WebResourceRequest request1 = request;
            WebResourceResponse error1 = errorResponse;
            view.loadUrl("file:///android_asset/nointernet.html");


        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            WebResourceRequest request1 = request;
            WebResourceError error1 = error;
            view.loadUrl("file:///android_asset/nointernet.html");


        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            String htmlContentInStringFormat = StreamToString(getFileFromAsset("nointernet.html"));
            //    view.loadUrl("file:///android_asset/nointernet.html");
            view.loadDataWithBaseURL(null, htmlContentInStringFormat, "text/html", "utf-8", null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        new DialogBox(MainActivity.this) {
                            @Override
                            public void onPositive(DialogInterface dialog) {
                                dialog.dismiss();
                                finish();
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();

                            }
                        }.setValues("OK", "CANCEL", "Do you want to exit?");

                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void startLoading() {
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(Constant.MAIN_URL);
        //  displayView(Displayer.LOADER_VIEW);
    }

    private void displayView(Displayer displayer) {
        switch (displayer) {
            case LOADER_VIEW:
                ll_loading.setVisibility(View.VISIBLE);
                iv_no_internet.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                break;

            case NO_INTERNET_VIEW:
                ll_loading.setVisibility(View.GONE);
                iv_no_internet.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                break;

            case WEB_VIEW:
                ll_loading.setVisibility(View.GONE);
                iv_no_internet.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                break;


        }
    }


    public static String StreamToString(InputStream in) {
        if (in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return writer.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.load:
                if (webView.getUrl().equals("about:blank")) {
                    webView.loadUrl(Constant.MAIN_URL);
                } else {
                    webView.reload();
                }
                Toast.makeText(getApplicationContext(), webView.getUrl(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


}
