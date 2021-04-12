package com.example.a1020tech;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

      private final String TAG = MainActivity.class.getSimpleName();
      private WebView mWebView = null;
      private final String URL = "http://env-emap.eba-jj5pnmwy.us-west-2.elasticbeanstalk.com/";
      private LinearLayout mlLayoutRequestError = null, splashScreen = null;
      private Handler mhErrorLayoutHide = null;

      private boolean mbErrorOccured = false;
      private boolean mbReloadPressed = false;

      @SuppressLint("SetJavaScriptEnabled")
      @Override
      protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);

          ((Button) findViewById(R.id.btnRetry)).setOnClickListener(this);
          mlLayoutRequestError = (LinearLayout) findViewById(R.id.lLayoutRequestError);
          splashScreen = (LinearLayout) findViewById(R.id.splash);
          mhErrorLayoutHide = getErrorLayoutHideHandler();

          mWebView = (WebView) findViewById(R.id.webviewMain);
          mWebView.setWebViewClient(new MyWebViewClient());

          //activation du JS
          WebSettings settings = mWebView.getSettings();
          settings.setJavaScriptEnabled(true);
          mWebView.setWebChromeClient(getChromeClient());
          mWebView.loadUrl(URL);

          //splash screen
          splashScreen.setVisibility(View.VISIBLE);
          mlLayoutRequestError.setVisibility(View.GONE);
          mWebView.setVisibility(View.GONE);
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  mbReloadPressed = true;
                  mWebView.reload();
                  mbErrorOccured = false;
              }
          }, 2000);
      }

      @Override
      public boolean onSupportNavigateUp() {
          return super.onSupportNavigateUp();
      }

      @Override
      public void onClick(View v) {
          int id = v.getId();

          if (id == R.id.btnRetry) {
              if (!mbErrorOccured) {
                  return;
              }

              mbReloadPressed = true;
              mWebView.reload();
              mbErrorOccured = false;
          }
      }

      @Override
      public void onBackPressed() {
          if (mWebView.canGoBack()) {
              mWebView.goBack();
              return;
          }
          else {
              finish();
          }

          super.onBackPressed();
      }

      class MyWebViewClient extends WebViewClient {
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url) {
              return super.shouldOverrideUrlLoading(view, url);
          }

          @Override
          public void onPageStarted(WebView view, String url, Bitmap favicon) {
              super.onPageStarted(view, url, favicon);
          }

          @Override
          public void onLoadResource(WebView view, String url) {
              super.onLoadResource(view, url);
          }

          @Override
          public void onPageFinished(WebView view, String url) {
              if (mbErrorOccured == false && mbReloadPressed) {
                  hideErrorLayout();
                  mbReloadPressed = false;
              }

              super.onPageFinished(view, url);
          }

          @Override
          public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
              mbErrorOccured = true;
              showErrorLayout();
              super.onReceivedError(view, errorCode, description, failingUrl);
          }
      }

      private WebChromeClient getChromeClient() {
          final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
          progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
          progressDialog.setCancelable(false);

          return new WebChromeClient() {
              @Override
              public void onProgressChanged(WebView view, int newProgress) {
                  super.onProgressChanged(view, newProgress);
              }
          };
      }

      private void showErrorLayout() {
          mlLayoutRequestError.setVisibility(View.VISIBLE);
          mWebView.setVisibility(View.GONE);
          splashScreen.setVisibility(View.GONE);
      }

      private void hideErrorLayout() {
          mhErrorLayoutHide.sendEmptyMessageDelayed(10000, 200);
      }

      private Handler getErrorLayoutHideHandler() {
          return new Handler() {
              @Override
              public void handleMessage(Message msg) {
                  mlLayoutRequestError.setVisibility(View.GONE);
                  mWebView.setVisibility(View.VISIBLE);
                  splashScreen.setVisibility(View.GONE);
                  super.handleMessage(msg);
              }
          };
      }


}