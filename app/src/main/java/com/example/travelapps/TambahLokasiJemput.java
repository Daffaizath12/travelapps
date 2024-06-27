package com.example.travelapps;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Model.TiketData;
import com.example.travelapps.pelanggan.CurrentLocationActivity;
import com.example.travelapps.pelanggan.FromMapsActivity;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.api.model.Address;
import com.midtrans.sdk.uikit.external.UiKitApi;
import com.midtrans.sdk.uikit.internal.util.UiKitConstants;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class TambahLokasiJemput extends AppCompatActivity {

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
    Double totalHarga;
    double hargaDouble;
    TiketData tiketData;


    TextView tvAsal, tvTujuan, tvWaktu, tvTanggal, tvNama, tvPenumpang, tvTotal;
    EditText etAlamatTujuan;
    AppCompatButton btnLokasi, btnLokasiMaps, btnBayar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lokasi_jemput);

        onBindView();
        initMidtransSdk();
//        buildUiKit();
        Intent intent = getIntent();
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

            btnBayar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                    MidtransSDK.getInstance().startPaymentUiFlow(TambahLokasiJemput.this, PaymentMethod.BANK_TRANSFER_MANDIRI);
                }
            });
        }
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TambahLokasiJemput.this, CurrentLocationActivity.class);
                startActivity(i);
            }
        });
        btnLokasiMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TambahLokasiJemput.this, FromMapsActivity.class);
                startActivity(i);
            }
        });

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
    private void initMidtransSdk(){
        String CLIENT_KEY = "SB-Mid-client-M2wdn0pfhe0Wld-l";
        String MERCHANT_URL = "https://hallomomswebsite.000webhostapp.com/midtrans.php/";
        SdkUIFlowBuilder.init()
                .setClientKey(CLIENT_KEY) // client_key is mandatory
                .setContext(TambahLokasiJemput.this) // context is mandatory
                .setTransactionFinishedCallback(new TransactionFinishedCallback() {
                    @Override
                    public void onTransactionFinished(TransactionResult result) {
                        if (result.getResponse() != null) {
                            switch (result.getStatus()) {
                                case TransactionResult.STATUS_SUCCESS:
                                    Toast.makeText(TambahLokasiJemput.this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                    break;
                                case TransactionResult.STATUS_PENDING:
                                    Toast.makeText(TambahLokasiJemput.this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                                    break;
                                case TransactionResult.STATUS_FAILED:
                                    Toast.makeText(TambahLokasiJemput.this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } else if (result.isTransactionCanceled()) {
                            Toast.makeText(TambahLokasiJemput.this, "Transaction Canceled", Toast.LENGTH_LONG).show();
                        } else {
                            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                                Toast.makeText(TambahLokasiJemput.this, "Transaction Invalid", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(TambahLokasiJemput.this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(MERCHANT_URL) //set merchant url (required)
                .enableLog(true) // enable sdk log (optional)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // set theme. it will replace theme on snap theme on MAP ( optional)
                .setLanguage("id")
                .buildSDK();
        uiKitCustomSetting();
    }
    private void uiKitCustomSetting() {
        UIKitCustomSetting uIKitCustomSetting = new UIKitCustomSetting();
        uIKitCustomSetting.setSaveCardChecked(true);
        MidtransSDK.getInstance().setUiKitCustomSetting(uIKitCustomSetting);
    }
    private CustomerDetails initCustomerDetails() {
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone(telpUser);
        mCustomerDetails.setFirstName(namaUser);
        mCustomerDetails.setEmail(emailUser);
        mCustomerDetails.setCustomerIdentifier(emailUser);
        return mCustomerDetails;
    }
    private com.midtrans.sdk.uikit.api.model.CustomerDetails cus() {
        Address shippingAddress = new Address(
                null,  // First Name
                null,  // Last Name
                "Jalan Andalas Gang Sebelah No. 1",  // Address
                "Jakarta",  // City
                "10220",  // PostCode
                null,  // Phone Number
                null // Country Code
        );
        com.midtrans.sdk.uikit.api.model.CustomerDetails mCustomerDetails = new com.midtrans.sdk.uikit.api.model.CustomerDetails(
                emailUser, namaUser, namaUser, emailUser, telpUser, shippingAddress, shippingAddress
        );
        return mCustomerDetails;
    }
    private TransactionRequest initTransactionRequest() {
        String orderId = "PETTA-Express-";
        TransactionRequest transactionRequestNew = new
                TransactionRequest(orderId + System.currentTimeMillis() + "", totalHarga);
        transactionRequestNew.setCustomerDetails(initCustomerDetails());

        ItemDetails itemDetails1 = new ItemDetails("idPerjalanan", hargaDouble, Integer.parseInt(penumpang), "ITEM_NAME_1");
        ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
        itemDetailsList.add(itemDetails1);
        transactionRequestNew.setItemDetails(itemDetailsList);
        return transactionRequestNew;
    }

    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    com.midtrans.sdk.uikit.api.model.TransactionResult transactionResult = result.getData()
                            .getParcelableExtra(UiKitConstants.KEY_TRANSACTION_RESULT);
                    if (transactionResult != null) {
                        switch (transactionResult.getStatus()) {
                            case UiKitConstants.STATUS_SUCCESS:
                                Toast.makeText(this, "Transaction Finished. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                break;
                            case UiKitConstants.STATUS_PENDING:
                                Toast.makeText(this, "Transaction Pending. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                break;
                            case UiKitConstants.STATUS_FAILED:
                                Toast.makeText(this, "Transaction Failed. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                break;
                            case UiKitConstants.STATUS_CANCELED:
                                Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show();
                                break;
                            case UiKitConstants.STATUS_INVALID:
                                Toast.makeText(this, "Transaction Invalid. ID: " +
                                        transactionResult.getTransactionId(), Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(this, "Transaction ID: " +
                                        transactionResult.getTransactionId() + ". Message: " +
                                        transactionResult.getStatus(), Toast.LENGTH_LONG).show();
                                break;
                        }
                    } else {
                        Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    private com.midtrans.sdk.uikit.api.model.SnapTransactionDetail initTransactionDetails() {
        return new com.midtrans.sdk.uikit.api.model.SnapTransactionDetail(
                UUID.randomUUID().toString(),
                20000.0, "IDR"
        );
    }
    private void buildUiKit() {
        String CLIENT_KEY = "SB-Mid-client-M2wdn0pfhe0Wld-l";
        String MERCHANT_URL = "https://hallomomswebsite.000webhostapp.com/midtrans.php/";
        new UiKitApi.Builder()
                .withContext(this.getApplicationContext())
                .withMerchantUrl(MERCHANT_URL)
                .withMerchantClientKey(CLIENT_KEY)
                .enableLog(true)
                .withColorTheme(new com.midtrans.sdk.uikit.api.model.CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .build();
        uiKitCustomSetting();
    }

}