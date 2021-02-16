package anviinfotechs.hartikandharparivar;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.DatabaseHandler;
import anviinfotechs.hartikandharparivar.helper.Functions;
import anviinfotechs.hartikandharparivar.helper.SessionManager;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_ADDRESS = "address";
    private static String KEY_PINCODE = "pincode";
    private static String KEY_PHONE = "mobile";
    private static String KEY_EMAIL = "email";

    static String g_p_s1 = "No network is connection is available.";
    private static final String[] LOCATION_AND_READ_WRITE_STORAGE =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int RC_LOCATION_AND_READ_WRITE_STORAGE_PERM = 124;
    private Button btnLogin, btnForgotPass,btnRegister;
    private TextInputEditText inputEmail, inputPassword;
    private ProgressDialog pDialog;

    private SessionManager session;
    private DatabaseHandler db;
    private HashMap<String, String> user = new HashMap<>();



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        inputEmail = findViewById(R.id.lEditEmail);
        inputPassword = findViewById(R.id.lEditPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPass = findViewById(R.id.btnForgotPassword);
        btnRegister = findViewById(R.id.register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
        if (!isDeviceOnline()) {
            activate_online_device();

        } else {
            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // create sqlite database
            db = new DatabaseHandler(getApplicationContext());
            user = db.getUserDetails();
            // session manager
            session = new SessionManager(getApplicationContext());

            if (session.isLoggedIn()) {
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }
            // Hide Keyboard
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            init();

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Register.class);
                    startActivity(i);
                }
            });
        }
    }
    @AfterPermissionGranted(RC_LOCATION_AND_READ_WRITE_STORAGE_PERM)
    private void allpermission_activate_haritkandhar_app() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    RC_LOCATION_AND_READ_WRITE_STORAGE_PERM,
                    LOCATION_AND_READ_WRITE_STORAGE);
        }
    }

    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_READ_WRITE_STORAGE);
    }

    private void init () {
            // Login button Click Event
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                public void onClick(View view) {
                    // Hide Keyboard
                    Functions.hideSoftKeyboard(LoginActivity.this);
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        loginProcess(email, password);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(), "Please enter the credentials!", Toast.LENGTH_LONG).show();
                    }
                }

            });

            // Forgot Password Dialog
            btnForgotPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forgotPasswordDialog();
                }
            });
        }

    private void forgotPasswordDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reset_password, null);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setIcon(R.drawable.logo0);
        dialogBuilder.setTitle("Forgot Password");
        dialogBuilder.setCancelable(false);

        final TextInputLayout mEditEmail = dialogView.findViewById(R.id.editEmail);

        dialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        mEditEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditEmail.getEditText().getText().length() > 0) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setEnabled(false);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = mEditEmail.getEditText().getText().toString();

                        if (!email.isEmpty()) {
                            if (Functions.isValidEmailAddress(email)) {
                                resetPassword(email);
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Email is not valid!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Fill all values!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        alertDialog.show();
    }

    private void loginProcess(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        JSONObject json_user = jObj.getJSONObject("user");
                        String checkVerify=(json_user.getString("verified"));
                        if (checkVerify.contains("unverified")) {
                            Bundle b = new Bundle();
                            b.putString("email", email);
                            Intent upanel = new Intent(LoginActivity.this, EmailVerify.class);
                            upanel.putExtras(b);
                            upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(upanel);
                        } else {
                            db.addUser(json_user.getString(KEY_UID), json_user.getString(KEY_NAME), json_user.getString(KEY_ADDRESS),json_user.getString(KEY_PINCODE), json_user.getString(KEY_PHONE), json_user.getString(KEY_EMAIL));
                            user = db.getUserDetails();
                            // session manager
                            session = new SessionManager(getApplicationContext());
                            session.setLogin(true);
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();

                        }

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void resetPassword(final String email) {
        // Tag used to cancel the request
        String tag_string_req = "req_reset_pass";

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.RESET_PASS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

                params.put("tag", "forgot_pass");
                params.put("email", email);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        // Adding request to volley request queue
        strReq.setRetryPolicy(new DefaultRetryPolicy(5 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
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
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        allpermission_activate_haritkandhar_app();
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {

    }

    @Override
    public void onRationaleDenied(int requestCode) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasLocationAndContactsPermissions() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
            ) ;
        }
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
