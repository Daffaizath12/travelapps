package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        // Ambil URL dari intent
        String url = getIntent().getStringExtra("payment_url");

        // Inisialisasi WebView
        webView = findViewById(R.id.webView);

        // Aktifkan JavaScript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Set WebViewClient agar membuka link di WebView, bukan di browser eksternal
        webView.setWebViewClient(new WebViewClient());

        // Load URL ke WebView
        webView.loadUrl(url);

    }
}