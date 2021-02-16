package anviinfotechs.hartikandharparivar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.Functions;

import static anviinfotechs.hartikandharparivar.Splash_Screen.g_p_s1;


public class Register extends AppCompatActivity {
    private static final String TAG = Register.class.getSimpleName();

    private Button btnRegister, btnLinkToLogin;
    private TextInputEditText inputName, inputEmail, inputPassword,inputPhone,inputWhatsapp,inputAddress,inputVillage,inputTehsil,inputDistrict,inputPincode,inputSurvey_Number,inputHouse_Number,inputCnfPass;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputName = findViewById(R.id.lEditName);
        inputAddress = findViewById(R.id.lEditAddress);
        inputVillage = findViewById(R.id.lEditVillage);
        inputTehsil = findViewById(R.id.lEditTehsil);
        inputDistrict = findViewById(R.id.lEditDistrict);
        inputPincode = findViewById(R.id.lEditPincode);
        inputSurvey_Number = findViewById(R.id.lEditSurvey_number);
        inputHouse_Number = findViewById(R.id.lEditHouse_number);
        inputPhone = findViewById(R.id.lEditPhone);
        inputWhatsapp = findViewById(R.id.lWhatsapp_Number);
        inputEmail = findViewById(R.id.lEditEmail);
        inputCnfPass =  findViewById(R.id.lEditCnfPass);
        inputPassword = findViewById(R.id.lEditPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLinkToLogin = findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Hide Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (!isDeviceOnline()) {
            activate_online_device();
        } else {
            init();
        }
    }


    private void activate_online_device() {
        android.app.AlertDialog.Builder alert112 = new android.app.AlertDialog.Builder(this);
        alert112.setTitle("Network Error");
        alert112.setIcon(R.drawable.ic_baseline_network_check_24);
        alert112.setMessage(g_p_s1);
        alert112.setPositiveButton("Activate Internet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface alert, int which) {
                Intent settingsIntent = new Intent(Settings.ACTION_SETTINGS);
                startActivityForResult(settingsIntent, 9003);
                alert.dismiss();
            }
        });
        alert112.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        alert112.show();
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void init() {
        // Login button Click Event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                // Hide Keyboard
                Functions.hideSoftKeyboard(Register.this);

                String name = inputName.getText().toString().trim();
                String address = inputAddress.getText().toString().trim();
                String village = inputVillage.getText().toString().trim();
                String tehsil = inputTehsil.getText().toString().trim();
                String distrct = inputDistrict.getText().toString().trim();
                String pincode = inputPincode.getText().toString().trim();
                String survey_number = inputSurvey_Number.getText().toString().trim();
                String house_number = inputHouse_Number.getText().toString().trim();
                String phone= inputPhone.getText().toString().trim();
                String whatsapp= inputWhatsapp.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String cnfpass= inputCnfPass.toString().trim();
                String profile="No";
                if (house_number.isEmpty()||survey_number.isEmpty()){
                    house_number="Not Available";
                    survey_number="Not Available";
                }
                // Check for empty data in the form
                if (!name.isEmpty() && !address.isEmpty() && !tehsil.isEmpty() && !tehsil.isEmpty() && !distrct.isEmpty()
                        && !pincode.isEmpty()  && !phone.isEmpty() && !email.isEmpty() && !password.isEmpty() && !cnfpass.isEmpty()) {
                    if (pincode.length()==6) {
                        if (phone.length() == 10) {
                            if (Functions.isValidEmailAddress(email)) {
                                if (password.length() >= 6) {
                                    if (whatsapp.isEmpty())
                                    {
                                        whatsapp="Not Available";
                                    }
                                    registerUser(name, address, village, tehsil , distrct , pincode , survey_number, house_number,phone, whatsapp, email, password,profile);
                                } else {
                                    inputPassword.setError("Password should be minimum 6 digit");
                                }
                            } else {
                                inputEmail.setError("Email is not valid!");
                            }
                        }
                        else{
                            inputPhone.setError("Enter 10 digit phone number");
                        }
                    }
                    else{
                        inputPincode.setError("Enter 6 digit pincode");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill all details!", Toast.LENGTH_LONG).show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(Register.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }
        });
    }

    private void registerUser(final String name, final String address, final String village, final String tehsil, final String district,
                              final String pincode, final String survey_number, final String house_number,
                              final String phone, final String whatsapp, final String email, final String password, final String profile) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.REGISTER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Functions logout = new Functions();
                        logout.logoutUser(getApplicationContext());

                        Intent i = new Intent(Register.this, LoginActivity.class);
                        Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        pDialog.dismiss();
                        finish();

                    } else {
                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
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
                params.put("name", name);
                params.put("address", address);
                params.put("village", village);
                params.put("tehsil", tehsil);
                params.put("district", district);
                params.put("pincode", pincode);
                params.put("survey_number", survey_number);
                params.put("house_number", house_number);
                params.put("mobile", phone);
                params.put("whatsapp_number", whatsapp);
                params.put("email", email);
                params.put("password", password);
                params.put("profile", profile);
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
