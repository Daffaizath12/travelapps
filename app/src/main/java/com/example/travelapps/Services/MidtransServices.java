package com.example.travelapps.Services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.travelapps.Model.Bank;
import com.example.travelapps.Model.TransactionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MidtransServices {
    private static final String MIDTRANS_API_URL = "https://app.sandbox.midtrans.com/snap/v1/transactions";
    private static final String CLIENT_API = "SB-Mid-server-yIQsPkGPeYXJDAM2voIw1mp_:";
    private static final String MIDTRANS_TRANSACTION = "https://api.sandbox.midtrans.com/v2/";
    private final Context context;

    public interface TokenResponseListener {
        void onSuccess(String token, String url);
        void onError(String message);
    }

    public interface TransactionStatusResponseListener {
        void onSuccess(TransactionModel transactionModel);
        void onError(String message);
    }
    public MidtransServices(Context context) {
        this.context = context;
    }

    public void getToken(final String orderId, final int grossAmount, final TokenResponseListener listener) {
        JSONObject postData = new JSONObject();
        JSONObject transactionDetails = new JSONObject();
        try {
            transactionDetails.put("order_id", orderId);
            transactionDetails.put("gross_amount", grossAmount);
            postData.put("transaction_details", transactionDetails);
            postData.put("credit_card", new JSONObject().put("secure", true));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, MIDTRANS_API_URL, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String token = response.getString("token");
                            String url = response.getString("token");
                            listener.onSuccess(token, url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Error parsing token response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GetMidtransToken", "Error: " + error.getMessage());
                        listener.onError("Error getting token: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Basic " + Base64.encodeToString(CLIENT_API.getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };

        // Adding request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public void getTransactionStatus(final String orderId, final TransactionStatusResponseListener listener) {
        String url = MIDTRANS_TRANSACTION + orderId + "/status";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("response transaction", response.toString());
                            String statusCode = response.getString("status_code");
                            String statusMessage = response.getString("status_message");
                            String transactionId = response.getString("transaction_id");
                            String orderId = response.getString("order_id");
                            String paymentType = response.getString("payment_type");
                            String transactionTime = response.getString("transaction_time");
                            String transactionStatus = response.getString("transaction_status");
                            String fraudStatus = response.getString("fraud_status");
                            String grossAmount = response.getString("gross_amount");
                            String approvalCode = response.optString("approval_code", "");
//                            JSONArray vaNumbersArray = response.getJSONArray("va_numbers");
                            String bank = "";
                            String va = "";

                            // Check if the key "va_numbers" exists and is not null
                            if (response.has("va_numbers") && !response.isNull("va_numbers")) {
                                JSONArray vaNumbersArray = response.getJSONArray("va_numbers");
                                if (vaNumbersArray.length() > 0) {
                                    JSONObject vaObject = vaNumbersArray.getJSONObject(0);
                                    bank = vaObject.getString("bank");
                                    va = vaObject.getString("va_number");
                                }
                            }

                            Bank bank2 = new Bank(bank, va);

                            TransactionModel transactionModel = new TransactionModel(
                                    statusCode, statusMessage, transactionId, null, orderId,
                                    paymentType, transactionTime, transactionStatus, fraudStatus,
                                    approvalCode, null , bank2, grossAmount, null,
                                    null, null, null, null,null
                            );

                            listener.onSuccess(transactionModel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Error parsing transaction status response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GetMidtransToken", "Error: " + error.getMessage());
                        listener.onError("Error getting transaction status: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("authorization", "Basic " + Base64.encodeToString(CLIENT_API.getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    public void getPaymentLink(final JSONObject requestBody, final PaymentLinkResponseListener listener) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "https://api.sandbox.midtrans.com/v1/payment-links", requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String orderId = response.getString("order_id");
                            String paymentUrl = response.getString("payment_url");
                            listener.onSuccess(orderId, paymentUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onError("Error parsing payment link response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GetPaymentLink", "Error: " + error.toString());
                        listener.onError("Error getting payment link: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Basic " + Base64.encodeToString("SB-Mid-server-yIQsPkGPeYXJDAM2voIw1mp_".getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };

        // Adding request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public interface PaymentLinkResponseListener {
        void onSuccess(String orderId, String paymentUrl);
        void onError(String message);
    }
}
