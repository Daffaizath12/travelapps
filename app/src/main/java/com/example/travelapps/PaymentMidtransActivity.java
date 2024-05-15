package com.example.travelapps;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Model.TiketData;
import com.example.travelapps.Services.ApiServices;
import com.example.travelapps.Services.MidtransServices;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.uikit.api.model.Address;
import com.midtrans.sdk.uikit.api.model.Authentication;
import com.midtrans.sdk.uikit.api.model.BankType;
import com.midtrans.sdk.uikit.api.model.CreditCard;
import com.midtrans.sdk.uikit.api.model.CustomColorTheme;
import com.midtrans.sdk.uikit.api.model.CustomerDetails;
import com.midtrans.sdk.uikit.api.model.Expiry;
import com.midtrans.sdk.uikit.api.model.ItemDetails;
import com.midtrans.sdk.uikit.api.model.PaymentType;
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail;
import com.midtrans.sdk.uikit.api.model.TransactionResult;
import com.midtrans.sdk.uikit.external.UiKitApi;
import com.midtrans.sdk.uikit.internal.util.UiKitConstants;
import com.example.travelapps.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class PaymentMidtransActivity extends AppCompatActivity implements View.OnClickListener {
    
    TextView tvAsal, tvTujuan, tvWaktu, tvTanggal, tvNama, tvPenumpang, tvTotal;
    EditText etAlamatTujuan;
    AppCompatButton btnLokasi, btnLokasiMaps, btnBayar;
    TiketData tiketData;
    String idPerjalanan = "";
    String asal = "";
    String waktu = "";
    Date tanggal;
    String tujuan = "";
    String idUser = "";
    String harga = "";
    String namaUser = "";
    String emailUser = "";
    String telpUser = "";
    String alamatUser = "";
    String penumpang = "";
    double hargaDouble;
    double totalHarga;
    String idOrder = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_midtrans);
        onBindView();
        Intent intent = getIntent();
        getIntentData(intent);
        btnLokasi.setOnClickListener(this);
        btnLokasiMaps.setOnClickListener(this);
        btnBayar.setOnClickListener(this);
        buildUiKit();
    }

    @Override
    public void onClick(View view) {
        if (view == btnLokasi) {
            Intent i = new Intent(PaymentMidtransActivity.this, CurrentLocationActivity.class);
            startActivity(i);
        } else if (view == btnLokasiMaps) {
            Intent i = new Intent(PaymentMidtransActivity.this, FromMapsActivity.class);
            startActivity(i);
        } else {
            if (etAlamatTujuan.getText().toString().trim().isEmpty()) {
                etAlamatTujuan.setText("silahkan isi detail alamat tujuan");
            } else {
            startPayment();
//                pembayaran();
            }
        }
    }

    private void pembayaran() {
        MidtransServices midtransServices = new MidtransServices(PaymentMidtransActivity.this);
        idOrder = "PETTA-Express-" + UUID.randomUUID().toString();
        JSONObject requestBody = new JSONObject();
        JSONObject transactionDetails = new JSONObject();
        JSONObject expiry = new JSONObject();
        JSONObject customerDetails = new JSONObject();
        JSONArray enabledPaymentsArray = new JSONArray();
        JSONArray itemDetailsArray = new JSONArray();
        try {
            // Transaction details
            transactionDetails.put("order_id", idOrder );
            transactionDetails.put("gross_amount", 100000);

            // Expiry details
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm Z", Locale.getDefault());
            String startTime = sdf.format(new Date());
            expiry.put("start_time", startTime);
            expiry.put("duration", 1);
            expiry.put("unit", "days");

            // Customer details
            customerDetails.put("first_name", namaUser);
            customerDetails.put("last_name", namaUser);
            customerDetails.put("email", emailUser);
            customerDetails.put("phone", telpUser);
            customerDetails.put("notes", "Thank you for your purchase. Please follow the instructions to pay.");
            JSONArray requiredFieldsArray = new JSONArray();
            requiredFieldsArray.put("first_name");
            requiredFieldsArray.put("phone");
            requiredFieldsArray.put("email");
            customerDetails.put("customer_details_required_fields", requiredFieldsArray);

            // Enabled payments
            enabledPaymentsArray.put("bca_va");
            enabledPaymentsArray.put("indomaret");
            enabledPaymentsArray.put("gopay");
            enabledPaymentsArray.put("bni_va");
            enabledPaymentsArray.put("bri_va");
            enabledPaymentsArray.put("shopeepay");
            enabledPaymentsArray.put("permata_va");

            // Item details
            JSONObject itemDetails = new JSONObject();
            itemDetails.put("id", idPerjalanan);
            itemDetails.put("name", asal + tujuan);
            itemDetails.put("price", hargaDouble);
            itemDetails.put("quantity", Integer.parseInt(penumpang));
            itemDetails.put("brand", "PETTA Express");
            itemDetails.put("category", "Furniture");
            itemDetails.put("merchant_name", "Tour and Travel");
            itemDetailsArray.put(itemDetails);

            requestBody.put("transaction_details", transactionDetails);
            requestBody.put("customer_required", false);
            requestBody.put("usage_limit", 1);
            requestBody.put("expiry", expiry);
            requestBody.put("enabled_payments", enabledPaymentsArray);
            requestBody.put("item_details", itemDetailsArray);
            requestBody.put("customer_details", customerDetails);
            requestBody.put("title", "Pembayaran Tiket");
            requestBody.put("payment_link_type", "FIXED_AMOUNT");
            SharedPreferences preferences = PaymentMidtransActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
            String token = preferences.getString("token", "");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            String tanggalFormatted = sdf1.format(tanggal);
            ApiServices.checkLatlong(PaymentMidtransActivity.this, token, new ApiServices.CheckLatlongResponseListener() {
                @Override
                public void onResult(boolean success, double latitude, double longitude) {
                    String alamatJemput = getAddressFromLocation(latitude, longitude);
                    String alamatTujuan = etAlamatTujuan.getText().toString().trim();
                    ApiServices.pemesanan(PaymentMidtransActivity.this, idUser, idPerjalanan, penumpang, idOrder, alamatJemput, alamatTujuan, waktu, "Menunggu", tanggalFormatted, String.valueOf(totalHarga), new ApiServices.PemesananResponseListener() {
                        @Override
                        public void onSuccess(String message) {
                            midtransServices.getPaymentLink(requestBody, new MidtransServices.PaymentLinkResponseListener() {
                                @Override
                                public void onSuccess(String orderId, String paymentUrl) {
                                    Intent intent = new Intent(PaymentMidtransActivity.this, PaymentMidtransWebView.class);
                                    intent.putExtra("payment_url", paymentUrl);
                                    startActivity(intent);
                                }

                                @Override
                                public void onError(String message) {
                                    // Handle error
                                    Toast.makeText(PaymentMidtransActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(PaymentMidtransActivity.this, "Gagal melakukan pemesanan" + message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onError(String message) {
                    Log.e("Error", "Gagal mendapatkan alamat, " + message);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void onBindView(){
        tvAsal = findViewById(R.id.asal);
        tvTujuan = findViewById(R.id.Tujuan);
        tvWaktu = findViewById(R.id.waktu);
        tvTanggal = findViewById(R.id.date);
        tvPenumpang = findViewById(R.id.txtJumlahPenumpang);
        tvNama = findViewById(R.id.txtpenumpang);
        tvTotal = findViewById(R.id.tvTotal);
        etAlamatTujuan = findViewById(R.id.et_alamat);
        btnLokasi = findViewById(R.id.btnCurrentLoc);
        btnLokasiMaps = findViewById(R.id.maps);
        btnBayar = findViewById(R.id.bayarsekarang);
    }
    private void getIntentData(Intent intent){
        if (intent.getExtras() != null) {
            tiketData = (TiketData) intent.getSerializableExtra("tiket");
            idPerjalanan = tiketData.getId();
            asal = tiketData.getAsal();
            tujuan = tiketData.getTujuan();
            waktu = tiketData.getWaktu();
            tanggal = tiketData.getTanggal();
            hargaDouble = tiketData.getHarga();

            idUser = intent.getStringExtra("id_user");
            namaUser = intent.getStringExtra("nama_user");
            emailUser = intent.getStringExtra("email_user");
            telpUser = intent.getStringExtra("telp_user");
            alamatUser = intent.getStringExtra("alamat_user");
            penumpang = intent.getStringExtra("penumpang");

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

            String tanggalFormatted = sdf.format(tanggal);
            tvAsal.setText(asal);
            tvWaktu.setText(waktu);
            tvTanggal.setText(tanggalFormatted);
            tvTujuan.setText(tujuan);
            int penumpangInt = Integer.parseInt(penumpang);
            totalHarga = hargaDouble * penumpangInt;
            tvTotal.setText(String.valueOf(totalHarga));
            tvNama.setText(namaUser);
            tvPenumpang.setText(penumpang);
        }
    }

    private void setLocaleNew(String languageCode) {
        LocaleListCompat locales = LocaleListCompat.forLanguageTags(languageCode);
        AppCompatDelegate.setApplicationLocales(locales);
    }

    private void buildUiKit() {
        String CLIENT_KEY = "SB-Mid-client-M2wdn0pfhe0Wld-l";
        String MERCHANT_URL = "https://hallomomswebsite.000webhostapp.com/midtrans.php/";
        new UiKitApi.Builder()
                .withContext(PaymentMidtransActivity.this)
                .withMerchantUrl(MERCHANT_URL)
                .withMerchantClientKey(CLIENT_KEY)
                .enableLog(true)
                .withColorTheme(new CustomColorTheme("#1e3d59", "#B61548", "#1e3d59"))
                .build();
        setLocaleNew("id");
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    TransactionResult transactionResult = result.getData()
                            .getParcelableExtra(UiKitConstants.KEY_TRANSACTION_RESULT);
                    if (transactionResult != null) {
                        switch (transactionResult.getStatus()) {
                            case UiKitConstants.STATUS_SUCCESS:
                                Toast.makeText(this, "Transaction Finished. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                ApiServices.status(PaymentMidtransActivity.this, idOrder, "Berhasil", new ApiServices.AddLatlongResponseListener() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Log.e("status", message);
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.e("status", message);
                                    }
                                });
                                break;
                            case UiKitConstants.STATUS_PENDING:
                                Toast.makeText(this, "Transaction Pending. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                                break;
                            case UiKitConstants.STATUS_FAILED:
                                Toast.makeText(this, "Transaction Failed. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                Intent intent1 = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                                startActivity(intent1);
                                finish();
                                ApiServices.status(PaymentMidtransActivity.this, idOrder, "Gagal", new ApiServices.AddLatlongResponseListener() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Log.e("status", message);
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.e("status", message);
                                    }
                                });
                                break;
                            case UiKitConstants.STATUS_CANCELED:
                                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show();
                                Intent intent2 = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                                startActivity(intent2);
                                finish();
                                ApiServices.status(PaymentMidtransActivity.this, idOrder, "Gagal", new ApiServices.AddLatlongResponseListener() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Log.e("statuspesan", "cancel");
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.e("statuspesan", message);

                                    }
                                });
                                break;
                            case UiKitConstants.STATUS_INVALID:
                                Toast.makeText(this, "Transaction Invalid. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                Intent intent3 = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                                startActivity(intent3);
                                finish();
                                break;
                            default:
                                Toast.makeText(this, "Transaction ID: " +
                                        transactionResult.getTransactionId() + ". Message: " +
                                        transactionResult.getStatus(), Toast.LENGTH_LONG).show();
                                Intent intent4 = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                                startActivity(intent4);
                                finish();
                                break;
                        }
                    } else {
                        Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PaymentMidtransActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
    );

    private CustomerDetails initCustomerDetails() {
        Address shippingAddress = new Address(
                null,
                null,
                "Jalan Andalas Gang Sebelah No. 1",
                "Jakarta",
                "10220",
                null,
                null
        );
        Address billingAddress = new Address(
                null,
                null,
                "Jalan Andalas Gang Sebelah No. 1",
                "Jakarta",
                "10220",
                null,
                null
        );
        CustomerDetails mCustomerDetails = new CustomerDetails(
                emailUser, namaUser, namaUser, emailUser, telpUser, shippingAddress, shippingAddress
        );
        return mCustomerDetails;
    }

    private List<ItemDetails> initItemDetails(){
        ItemDetails itemDetails = new ItemDetails(idPerjalanan, hargaDouble, Integer.parseInt(penumpang), asal + tujuan);
        List<ItemDetails> itemDetailsList = new ArrayList<>();
        itemDetailsList.add(itemDetails);
        return  itemDetailsList;
    }

    private SnapTransactionDetail initTransactionDetails() {
        String orderId = "PETTA-Express-";
        return new SnapTransactionDetail(
                idOrder,
                totalHarga, "IDR"
        );
    }
    private CreditCard initCreditCard(){
        CreditCard creditCard = new CreditCard(
                false,
                null,
                Authentication.AUTH_3DS,
                CreditCard.MIGS,
                BankType.MANDIRI,
                null,
                null,
                null,
                null,
                null
        );
        return creditCard;
    }

    private Expiry initExpiry(){
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        String currentTimeString = dateFormat.format(currentDate);
       Expiry expiry = new Expiry(currentTimeString, Expiry.Companion.getUNIT_HOUR(), 1);
       return expiry;
    }
    private String getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                android.location.Address address = addresses.get(0);
                String addressLine = address.getAddressLine(0);
                return addressLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void startPayment(){
        idOrder = "PETTA-Express-" + UUID.randomUUID().toString();
        SharedPreferences preferences = PaymentMidtransActivity.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String token = preferences.getString("token", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String tanggalFormatted = sdf.format(tanggal);
        ApiServices.checkLatlong(PaymentMidtransActivity.this, token, new ApiServices.CheckLatlongResponseListener() {
            @Override
            public void onResult(boolean success, double latitude, double longitude) {
                String alamatJemput = getAddressFromLocation(latitude, longitude);
                String alamatTujuan = etAlamatTujuan.getText().toString().trim();
                        ApiServices.pemesanan(PaymentMidtransActivity.this, idUser, idPerjalanan, penumpang, idOrder, alamatJemput, alamatTujuan, waktu, "Menunggu", tanggalFormatted, String.valueOf(totalHarga), new ApiServices.PemesananResponseListener() {
                            @Override
                            public void onSuccess(String message) {
                                UiKitApi.Companion.getDefaultInstance().startPaymentUiFlow(
                                        PaymentMidtransActivity.this,
                                        launcher,
                                        initTransactionDetails(),
                                        initCustomerDetails(),
                                        initItemDetails(),
                                        initCreditCard(),
                                        idUser,
                                        null,
                                        null,
                                        null,
                                        initExpiry(),
                                        null,
                                        Arrays.asList(PaymentType.CREDIT_CARD, PaymentType.BANK_TRANSFER, PaymentType.GOPAY, PaymentType.SHOPEEPAY, PaymentType.UOB_EZPAY),
                                        null,
                                        null,
                                        null,
                                        null,
                                        "Cash1",
                                        "Debit2",
                                        "Credit3"
                                );
                            }

                            @Override
                            public void onError(String message) {
                                Toast.makeText(PaymentMidtransActivity.this, "Gagal melakukan pemesanan" + message, Toast.LENGTH_SHORT).show();
                            }
                        });

            }

            @Override
            public void onError(String message) {
                Log.e("Error", "Gagal mendapatkan alamat, " + message);
            }
        });


    }

}