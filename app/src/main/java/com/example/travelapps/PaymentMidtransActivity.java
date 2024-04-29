package com.example.travelapps;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.travelapps.Model.TiketData;
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
            startPayment();
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
                orderId + UUID.randomUUID().toString(),
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

    private void startPayment(){
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
                PaymentMethod.BANK_TRANSFER_MANDIRI,
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
}