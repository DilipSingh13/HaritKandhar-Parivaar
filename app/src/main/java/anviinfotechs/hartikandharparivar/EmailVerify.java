package anviinfotechs.hartikandharparivar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import anviinfotechs.hartikandharparivar.helper.DatabaseHandler;
import anviinfotechs.hartikandharparivar.helper.Functions;
import anviinfotechs.hartikandharparivar.helper.SessionManager;


public class EmailVerify extends AppCompatActivity {
    private static final String TAG = EmailVerify.class.getSimpleName();

    private TextInputLayout textVerifyCode;
    private Button btnVerify, btnResend;
    private TextView otpCountDown;

    private SessionManager session;
    private DatabaseHandler db;
    private ProgressDialog pDialog;

    private static final String FORMAT = "%02d:%02d";
    private int counter;
    Bundle bundle;
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_ADDRESS = "address";
    private static String KEY_PINCODE = "pincode";
    private static String KEY_PHONE = "mobile";
    private static String KEY_EMAIL = "email";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verify);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textVerifyCode = findViewById(R.id.verify_code);
        btnVerify = findViewById(R.id.btnVerify);
        btnResend = findViewById(R.id.btnResendCode);
        otpCountDown = findViewById(R.id.otpCountDown);



        bundle = getIntent().getExtras();
        startService(new Intent(this, TimerActivity.class));

        db = new DatabaseHandler(getApplicationContext());
        session = new SessionManager(getApplicationContext());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        init();
        textVerifyCode.requestFocus();
        showKeyboard();
    }

    private void init() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide Keyboard
                closeKeyboard();
                String email = bundle.getString("email");
                String otp = textVerifyCode.getEditText().getText().toString();
                if (!otp.isEmpty()) {
                    verifyCode(email, otp);
                   // textVerifyCode.setError(false);
                } else {
                    textVerifyCode.setError("Please enter verification code");
                }
            }
        });

        btnResend.setEnabled(false);
        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = bundle.getString("email");
                resendCode(email);
            }
        });

        countDown();
    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void countDown() {
        new CountDownTimer(70000, 1000){
            public void onTick(long millisUntilFinished){
                otpCountDown.setText(String.format(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)) )));
                counter++;
            }
            public  void onFinish(){
                otpCountDown.setVisibility(View.GONE);
                btnResend.setEnabled(true);
            }
        }.start();
    }

    private void verifyCode(final String email, final String otp) {
        // Tag used to cancel the request
        String tag_string_req = "req_verify_code";

        pDialog.setMessage("Checking in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.OTP_VERIFY_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONObject json_user = jObj.getJSONObject("user");

                        Functions logout = new Functions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_UID), json_user.getString(KEY_NAME), json_user.getString(KEY_ADDRESS),json_user.getString(KEY_PINCODE), json_user.getString(KEY_PHONE), json_user.getString(KEY_EMAIL));
                        session.setLogin(true);
                        Intent upanel = new Intent(EmailVerify.this, HomeActivity.class);
                        upanel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(upanel);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Verification Code", Toast.LENGTH_LONG).show();
                        textVerifyCode.setError("Invalid Verification Code");
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<>();

                params.put("tag", "verify_code");
                params.put("email", email);
                params.put("otp", otp);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }

        };
        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void resendCode(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_resend_code";

        pDialog.setMessage("Resending code ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.OTP_VERIFY_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Code successfully sent to your email!", Toast.LENGTH_LONG).show();
                        btnResend.setEnabled(false);
                        countDown();
                    } else {
                        Toast.makeText(getApplicationContext(), "Code sending failed!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("tag", "resend_code");
                params.put("email", email);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
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

    @Override
    public void onResume(){
        super.onResume();
        countDown();
    }
}
