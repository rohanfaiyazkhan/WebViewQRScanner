package webviewscanner.rohan.com.webviewqrscanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import static java.security.AccessController.getContext;

public class MyAppWebViewClient extends WebViewClient {
    Context context;
    private ProgressBar progressBar;

    public MyAppWebViewClient(Context c, ProgressBar progress){
        context = c;
        progressBar = progress;
        progress.setVisibility(View.GONE);
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {

        if (!DetectConnection.checkInternetConnection(context)){
            view.loadUrl("file:///android_asset/error.html");
        }
        Log.d("Webview url", url);
        progressBar.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        if (!DetectConnection.checkInternetConnection(context)){
            view.loadUrl("file:///android_asset/error.html");
        }else {
            view.loadUrl(url);
            return false;
        }
        return false;
    }


    public void onReceivedHttpError (WebView view,
                                     WebResourceRequest request,
                                     WebResourceResponse errorResponse){
        //Log.e("WebviewError", errorResponse.toString());
        //view.loadUrl("file:///android_asset/error.html");
        super.onReceivedHttpError(view, request, errorResponse);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}