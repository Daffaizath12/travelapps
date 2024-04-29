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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetMidtransToken {
    private static final String MIDTRANS_API_URL = "https://app.sandbox.midtrans.com/snap/v1/transactions";
    private final Context context;

    public interface TokenResponseListener {
        void onSuccess(String token, String url);
        void onError(String message);
    }

    public GetMidtransToken(Context context) {
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
                headers.put("authorization", "Basic " + Base64.encodeToString("SB-Mid-server-yIQsPkGPeYXJDAM2voIw1mp_:".getBytes(), Base64.NO_WRAP));
                return headers;
            }
        };

        // Adding request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
}
