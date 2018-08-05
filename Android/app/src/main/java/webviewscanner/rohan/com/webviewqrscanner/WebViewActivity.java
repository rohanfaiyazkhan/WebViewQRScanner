package webviewscanner.rohan.com.webviewqrscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    WebView mWebView;
    private static final String TAG = "WebViewActivity";
    private static final int QR_SCAN = 0;
    String url;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = (WebView) findViewById(R.id.activity_main_webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        getSupportActionBar().setTitle(getString(R.string.action_webview));

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new MyAppWebViewClient(getBaseContext(), progressBar));
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
                // Update the progress bar with page loading progress
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    // Hide the progressbar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Bundle extras = getIntent().getExtras();

        url = getString(R.string.app_start_url);

        mWebView.loadUrl(url);
        //mWebView.loadUrl("http://www.google.com");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case QR_SCAN:
            if (resultCode == RESULT_OK) {
                String QR_URL = data.getStringExtra("Scan Result");
                Log.d("QR Url: ", QR_URL);
                Toast.makeText(getBaseContext(), "Scan successful", Toast.LENGTH_LONG).show();
                mWebView.loadUrl(QR_URL);
            }
            break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
//            Intent webViewIntent = new Intent();
//            webViewIntent.putExtra("Webview status", "finished");
//            setResult(RESULT_OK, webViewIntent);
            super.onBackPressed();
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_scanner:
                startActivityForResult(new Intent(WebViewActivity.this, SimpleScannerActivity.class), QR_SCAN);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}
