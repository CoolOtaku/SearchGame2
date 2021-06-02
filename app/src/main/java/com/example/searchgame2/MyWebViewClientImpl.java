package com.example.searchgame2;

import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

interface WebViewClient {
    @TargetApi(Build.VERSION_CODES.N)
    boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request);

    // Для старых устройств
    boolean shouldOverrideUrlLoading(WebView view, String url);
}
