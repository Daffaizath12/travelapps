package com.example.travelapps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class PaymentMidtransWebView extends AppCompatActivity {
    private WebView webView;
    private ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_midtrans_web_view);

        String paymentUrl = getIntent().getStringExtra("payment_url");

        webView = findViewById(R.id.webView);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setupWebView();
        loadPaymentUrl(paymentUrl);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Aktifkan JavaScript jika diperlukan
        webView.setWebViewClient(new WebViewClient());
    }

    private void loadPaymentUrl(String paymentUrl) {
        if (paymentUrl != null && !paymentUrl.isEmpty()) {
            webView.loadUrl(paymentUrl);
        } else {
            // URL pembayaran tidak tersedia, tangani sesuai kebutuhan aplikasi Anda
        }
    }
}