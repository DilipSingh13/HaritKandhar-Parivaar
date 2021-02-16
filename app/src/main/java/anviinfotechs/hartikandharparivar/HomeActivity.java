package anviinfotechs.hartikandharparivar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.navigation.NavigationView;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.DatabaseHandler;
import anviinfotechs.hartikandharparivar.helper.Functions;
import anviinfotechs.hartikandharparivar.helper.SessionManager;
import anviinfotechs.hartikandharparivar.imageSliderFragment.CustomViewPagerAdapter;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private SlidingRootNav slidingRootNav;
    private static final int Home = 0;
    private static final int My_Account = 1;
    private static final int About = 3;
    private static final int Contact_Dev = 4;
    private static final int Exit = 6;
    private SessionManager session;
    private DatabaseHandler db;
    static String g_p_s1 = "No network is connection is available.";
    private HashMap<String, String> user = new HashMap<>();
    private String Email, Name, Mobile, grant_status = "Yes";
    private static final String[] LOCATION_AND_READ_WRITE_STORAGE =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int RC_LOCATION_AND_READ_WRITE_STORAGE_PERM = 124;
    private String[] screenTitles;
    private Drawable[] screenIcons;

    private TextView Logout_btn,Lable;
    private Toast toast;
    private long lastBackPressTime = 0;

    private static final long SLIDER_TIMER = 2000;
    private int currentPage = 0;
    private boolean isCountDownTimerActive = false;
    private Handler handler;
    private ViewPager viewPager;
    CardView Upload_Tree_Pic, plantation_Request, Terms, Inro, Manogat;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private ProgressDialog pDialog;

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (!isCountDownTimerActive) {
                automateSlider();
            }
            handler.postDelayed(runnable, 2500);

        }
    };

    private void automateSlider() {
        isCountDownTimerActive = true;
        new CountDownTimer(SLIDER_TIMER, 2500) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                int nextSlider = currentPage + 1;
                if (nextSlider == 5) {
                    nextSlider = 0;
                }

                viewPager.setCurrentItem(nextSlider);
                isCountDownTimerActive = false;
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        Logout_btn = findViewById(R.id.logout_icon);
        Upload_Tree_Pic = findViewById(R.id.upload);
        plantation_Request = findViewById(R.id.plant_request);
        Inro = findViewById(R.id.intro_org);
        Lable = findViewById(R.id.lable);
        Terms = findViewById(R.id.terms);
        Manogat = findViewById(R.id.sponsor);
        viewPager = findViewById(R.id.view_pager_slider);
        dotsLayout = findViewById(R.id.layoutDots);
        dotsLayout = findViewById(R.id.layoutDots);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomeActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
        if (!isDeviceOnline()) {
            activate_online_device();

        } else {
            // Progress dialog
            pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
            pDialog.setCancelable(false);

            db = new DatabaseHandler(getApplicationContext());
            user = db.getUserDetails();

            session = new SessionManager(getApplicationContext());

            Email = user.get("email");
            Name = user.get("name");
            Mobile = user.get("mobile");

            Name = Name.toUpperCase();
            Lable.setText(" स्वागत आहे");


            handler = new Handler();

            handler.postDelayed(runnable, 2500);
            runnable.run();

            CustomViewPagerAdapter viewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(viewPagerAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);
                    if (position == 0) {
                        currentPage = 0;
                    } else if (position == 1) {
                        currentPage = 1;
                    } else if (position == 2) {
                        currentPage = 2;
                    } else if (position == 3) {
                        currentPage = 3;
                    } else {
                        currentPage = 4;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            if (!session.isLoggedIn()) {
                logoutUser();
            }

            Logout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout_Conformation();
                }
            });
            Upload_Tree_Pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckPlantPlan(Email);
                }
            });
            plantation_Request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AssignTree(Email);
                }
            });
            Terms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplication(), TermsAndCondition.class));
                }
            });
            Inro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplication(), IntroActivity.class));

                }
            });
            Manogat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplication(), MoanogatActivity.class));
                }
            });
            FetchData(Name, Email, Mobile, grant_status);
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

    public void FetchData(final String Name, final String Email, final String Mobile, final String grant_status) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.NOTIFICATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Notification();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", Name);
                params.put("email", Email);
                params.put("mobile", Mobile);
                params.put("status", grant_status);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void CheckPlantPlan(final String email) {
        String tag_string_req = "req_register";

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                Functions.CHECK_ACCOUNT_STATUS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Intent i = new Intent(getApplicationContext(), View_Plantation_Plan.class);
                        startActivity(i);
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
                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void AssignTree(final String email) {
        String tag_string_req = "req_register";

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(com.android.volley.Request.Method.POST,
                Functions.CHECK_ACCOUNT_STATUS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Intent i = new Intent(getApplicationContext(), Assign_Plant_Details.class);
                        startActivity(i);
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
                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void logoutUser() {
        db.resetTables();
        session.setLogin(false);
        // Launching the login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.scale, R.anim.scale);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast= Toast.makeText(this, "Press back again to close this app", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        }
        else {
            if (toast != null) {
                toast.cancel();
                finish();
            }
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myaccount) {
            startActivity(new Intent(this, MyAccount.class));
        }
        if (id == R.id.about_us) {
            startActivity(new Intent(this, AboutUs.class));
        }
        if (id == R.id.email) {
            Intent mailIntent = new Intent();
            mailIntent.setAction(Intent.ACTION_SEND);
            mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"haritkandhar2017@gmail.com"});
            mailIntent.setData(Uri.parse("haritkandhar2017@gmail.com"));
            mailIntent.setPackage("com.google.android.gm");
            mailIntent.setType("text/plain");
            mailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
            startActivity(mailIntent);
        }
        if (id == R.id.exit) {
            exit_Conformation();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout_Conformation() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);

        dialogBuilder.setIcon(R.drawable.logo0);
        dialogBuilder.setTitle("Logout");
        dialogBuilder.setMessage("Do you want to logout?");
        dialogBuilder.setCancelable(false);


        dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
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

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Conform Delete Action Method
                        logoutUser();
                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void exit_Conformation() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);

        dialogBuilder.setIcon(R.drawable.logo0);
        dialogBuilder.setTitle("Exit");
        dialogBuilder.setMessage("Do you want to exit application?");

        dialogBuilder.setCancelable(false);


        dialogBuilder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
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

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Conform Delete Action Method
                        System.exit(0);
                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[5];
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void Notification() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this, R.style.MyDialogTheme);

        dialogBuilder.setIcon(R.drawable.logo0);
        dialogBuilder.setTitle("Notification !");
        dialogBuilder.setMessage("Your tree photo slot is active now. Kindly upload your tree photo");
        dialogBuilder.setCancelable(false);


        dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // empty
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                final Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });

        alertDialog.show();
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

    @SuppressLint("StringFormatInvalid")
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