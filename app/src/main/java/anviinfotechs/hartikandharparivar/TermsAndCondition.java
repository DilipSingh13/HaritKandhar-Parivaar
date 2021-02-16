package anviinfotechs.hartikandharparivar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.DatabaseHandler;
import anviinfotechs.hartikandharparivar.helper.Functions;

public class TermsAndCondition extends AppCompatActivity {

    Button Agree, Diagree;
    private static final String TAG = TermsAndCondition.class.getSimpleName();
    ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> JsonList;
    String email, mobile, status, statusYes = "Agree", statusNo = "Not Agree";
    LinearLayout ButtonLayout;
    private DatabaseHandler db;
    private HashMap<String, String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Agree = findViewById(R.id.agree);
        Diagree = findViewById(R.id.disagree);
        ButtonLayout = findViewById(R.id.bottom_button);

        JsonList = new ArrayList<>();
        // Progress dialog
        pDialog = new ProgressDialog(TermsAndCondition.this, R.style.MyDialogTheme);
        pDialog.setCancelable(false);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // Fetching user details from database
        mobile = user.get("mobile");
        email = user.get("email");

        FetchAgrementStatus(email, mobile);


        Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAgreementStatus(email, mobile, statusYes);
            }
        });

        Diagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAgreementStatus(email, mobile, statusNo);
            }
        });
    }

    private void FetchAgrementStatus(final String email, final String mobile) {
        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.VIEW_AGREEMENT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONArray jsonObject = new JSONArray(response);

                    if (response != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);

                            status = c.getString("agreement_status");

                            HashMap<String, String> fetchOrders = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fetchOrders.put("agreement_status", status);
                            JsonList.add(fetchOrders);
                        }
                        hideDialog();
                        if (status.equals("Not Agree")) {
                            ButtonLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(TermsAndCondition.this, "Kindly accept the terms and condition !", Toast.LENGTH_SHORT).show();
                        } else {
                            ButtonLayout.setVisibility(View.GONE);
                        }
                    }

                } catch (final Exception e) {
                    JsonList.clear();
                    hideDialog();
                    e.printStackTrace();
                }
                ;
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("mobile", mobile);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void UpdateAgreementStatus(final String email, final String mobile, final String status) {
        String tag_string_req = "req_register";
        pDialog.setMessage("Please wait...");
        hideDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.UPDATE_AGREEMENT_STATUS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String Msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("status", status);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}