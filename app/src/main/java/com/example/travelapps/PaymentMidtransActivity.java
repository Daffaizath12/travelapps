package com.example.travelapps;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.os.LocaleListCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

public class PaymentMidtransActivity extends AppCompatActivity implements View.OnClickListener {
    
    TextView tvAsal, tvTujuan, tvWaktu, tvTanggal, tvNama, tvPenumpang, tvTotal;
    EditText etAlamatTujuan;
    AppCompatButton btnLokasi, btnLokasiMaps, btnBayar, btnMapsTujuan;
    ImageView btnBack;
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
    String lat_jemput = "";
    String lng_jemput = "";
    String lat_tujuan="";
    String lng_tujuan="";
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
        btnBack.setOnClickListener(this);
//        btnMapsTujuan.setOnClickListener(this);
        buildUiKit();
    }

    @Override
    public void onClick(View view) {
        if (view == btnLokasi) {
            Intent i = new Intent(PaymentMidtransActivity.this, FromMapsActivity.class);
            i.putExtra("tiket", tiketData);
            i.putExtra("id_user", idUser);
            i.putExtra("nama_user", namaUser);
            i.putExtra("email_user", emailUser);
            i.putExtra("telp_user", telpUser);
            i.putExtra("alamat_user", alamatUser);
            i.putExtra("penumpang", penumpang);
            startActivity(i);
        } else if(view == btnBack) {
            onBackPressed();
        }
//        else if (view == btnLokasiMaps) {
//            Intent i = new Intent(PaymentMidtransActivity.this, FromMapsActivity.class);
//            i.putExtra("tiket", tiketData);
//            i.putExtra("id_user", idUser);
//            i.putExtra("nama_user", namaUser);
//            i.putExtra("email_user", emailUser);
//            i.putExtra("telp_user", telpUser);
//            i.putExtra("alamat_user", alamatUser);
//            i.putExtra("penumpang", penumpang);
//            startActivity(i);
//        }
        else if (view == btnLokasiMaps){
            Intent i = new Intent(PaymentMidtransActivity.this, MapsTujuanActivity.class);
            i.putExtra("tiket", tiketData);
            i.putExtra("id_user", idUser);
            i.putExtra("nama_user", namaUser);
            i.putExtra("email_user", emailUser);
            i.putExtra("telp_user", telpUser);
            i.putExtra("alamat_user", alamatUser);
            i.putExtra("penumpang", penumpang);
            startActivity(i);
        }
        else {
            if (etAlamatTujuan.getText().toString().trim().isEmpty()) {
                etAlamatTujuan.setText("silahkan isi detail alamat tujuan");
            } else {
                if (Objects.equals(lat_tujuan, "") && Objects.equals(lng_tujuan, "")){
                    Toast.makeText(this, "LatLngTujuan tidak ditemukan", Toast.LENGTH_SHORT).show();
                } else {
                    startPayment(lat_tujuan, lng_tujuan);
                }
            }
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
        btnBack = findViewById(R.id.backtodetailpemesanan);
//        btnMapsTujuan = findViewById(R.id.mapsTujuan);
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

//            String latJemputCurrent = intent.getStringExtra("lat_jemput_f");
//            String lngJemputCurrent = intent.getStringExtra("lng_jemput_f");
            String latJemputMaps = intent.getStringExtra("lat_tujuan");
            String lngJemputMaps = intent.getStringExtra("lng_tujuan");
//            if (latJemputCurrent != null && lngJemputCurrent != null ) {
//                lat_jemput = latJemputCurrent;
//                lng_jemput = lngJemputCurrent;
//                Log.e("latJemput:" , latJemputCurrent);
//                Log.e("lngJemput:" , lngJemputCurrent);
//            } else {
//                Log.e("ltlngJemput:" , "gada");
//            }
            if (latJemputMaps != null && lngJemputMaps != null ) {
                lat_tujuan = latJemputMaps;
                lng_tujuan = lngJemputMaps;
                Log.e("latTujuan:" , latJemputMaps);
                Log.e("lngTUjuan:" , lngJemputMaps);
            } else {
                Log.e("ltlngTujuan:" , "gada");
            }

//            lat_tujuan = intent.getStringExtra("lat_tujuan");
//            lng_tujuan = intent.getStringExtra("lng_tujuan");
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

    private void startPayment(String latTujuan, String lngTujuan){
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
                ApiServices.pemesanan(PaymentMidtransActivity.this, idUser, idPerjalanan, penumpang, idOrder, alamatJemput, alamatTujuan, waktu, "Menunggu", tanggalFormatted, String.valueOf(totalHarga), latTujuan,lngTujuan, new ApiServices.PemesananResponseListener() {
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
                                PaymentMethod.BANK_TRANSFER,
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