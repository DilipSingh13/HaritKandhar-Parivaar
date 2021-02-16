package anviinfotechs.hartikandharparivar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.Functions;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Upload_Image extends AppCompatActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private static final String TAG = Upload_Image.class.getSimpleName();
    private String Mobile, Email, Name, Tree_Name, Code, longitude = "0", latitudes = "0", month, Month, year = "0";
    Button Upload;
    private ImageView Image;
    Drawable myDrawable;
    String Dp_Name;
    private ProgressDialog Dialog;
    static String g_p_s1 = "No network is connection is available.";
    String imageurl, filePath, fileLocation = "", longi, lati, prev_month,lon,lat;
    private Uri imageUri;
    Bitmap bmp;
    Criteria criteria;
    String bestProvider;
    int previous_Month;
    private static final String[] LOCATION_AND_READ_WRITE_STORAGE =
            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int RC_LOCATION_AND_READ_WRITE_STORAGE_PERM = 124;
    private GoogleMap mMap;
    final static int REQUEST_CODE = 1;
    Circle circle;
    LatLng location;
    public LocationManager lm;
    double alarm_location_latitude = 0;
    double alarm_location_longitutde = 0;
    double current_location_latitude = 0;
    double current_location_longitutde = 0;
    boolean state = false;

    private String SERVER_URL = "***YOUR SERVER URL***/Upload_Tree_Picture.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Upload = findViewById(R.id.upload);
        Image = findViewById(R.id.img);

        Dialog = new ProgressDialog(Upload_Image.this);
        Dialog.setCancelable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Upload_Image.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }
        if (!isDeviceOnline()) {
            activate_online_device();

        } else {

            myDrawable = getResources().getDrawable(R.drawable.tree_image);

            Intent in = getIntent();
            Bundle b = in.getExtras();

            if (android.os.Build.VERSION.SDK_INT > 19) {
                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }

            if (b != null) {
                Code = (String) b.get("Code");
                Name = (String) b.get("Name");
                Email = (String) b.get("Email");
                Mobile = (String) b.get("Mobile");
                Tree_Name = (String) b.get("Tree_Name");
                Month = (String) b.get("Month_Slot");
            }
            // Toast.makeText(this, Name+"\n"+Email+"\n"+Mobile+"\n"+Code+"\n"+Tree_Name, Toast.LENGTH_LONG).show();
            Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getMyLocation();
                    if (longi.equals("0") && lati.equals("0") && month.equals("0")) {
                        invokeCamera();
                    } else {
                        checkLocationRadiusMethod();
                    }
                }
            });
            Upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Notification();
                    uploadFile(fileLocation);
                }
            });
            if (Month.equals("1")) {
                getMyLocation();
                fetchLocationFromDB(Name, Email, Mobile, Code, Month);
            } else {
                getMyLocation();
                previous_Month = Integer.parseInt(Month);
                previous_Month = previous_Month - 1;
                prev_month = Integer.toString(previous_Month);
                fetchLocationFromDB(Name, Email, Mobile, Code, prev_month);
            }
            getMyLocation();
        }
    }

    private void Notification() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Upload_Image.this, R.style.MyDialogTheme);

        dialogBuilder.setIcon(R.drawable.logo0);
        dialogBuilder.setTitle("Please Wait");
        dialogBuilder.setMessage("Uploading picture...\nNow, click 'OK' to go back");
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
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

        alertDialog.show();
    }

    private void fetchLocationFromDB(final String name, final String email, final String mobile, final String code, final String mon) {
        String tag_string_req = "uploading";

        Dialog.setMessage("Uploading Picture...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.FETCH_PERVIOUS_PLANT_LOCATION_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        getMyLocation();
                        longi = jObj.getString("longitude");
                        lati = jObj.getString("latitudes");
                        month = jObj.getString("month");
                        if (longi.equals("0") && lati.equals("0")) {
                            alarm_location_longitutde = current_location_longitutde;
                            alarm_location_latitude = current_location_latitude;
                        }
                    } else {
                        getMyLocation();
                        longi = jObj.getString("longitude");
                        lati = jObj.getString("latitudes");
                        month = jObj.getString("month");
                        alarm_location_longitutde = Double.parseDouble(longi);
                        alarm_location_latitude = Double.parseDouble(lati);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("code", code);
                params.put("month", mon);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void checkLocationRadiusMethod() {

        location = new LatLng(alarm_location_latitude, alarm_location_longitutde);
        mMap.addMarker(new MarkerOptions().position(location).title("Alarm Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        // Add a circle of radius 50 meter
        circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(alarm_location_latitude, alarm_location_longitutde))
                .radius(50).strokeColor(Color.RED).fillColor(Color.BLUE));

        //--------------- Check user is in Range or Not after 5 Seconds --------
        getMyLocation();
        if (IsInCircle()) {
            invokeCamera();
        } else {
            Toast.makeText(this, "Go to respective area to click picture", Toast.LENGTH_SHORT).show();
        }
    }

    public void invokeCamera() {
        ContentValues values = new ContentValues(2);
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 0);
    }

    public void uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);

        String[] parts = selectedFilePath.split("/");
        Dp_Name = parts[parts.length - 1];

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_SHORT).show();
            }
        });
        if (!selectedFile.isFile()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Source File Doesn't Exist: ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(SERVER_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty(
                        "Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);


                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {

                    try {

                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                    } catch (OutOfMemoryError e) {
                        Toast.makeText(getApplicationContext(), "Insufficient Memory!", Toast.LENGTH_SHORT).show();
                    }
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                try {
                    serverResponseCode = connection.getResponseCode();
                } catch (OutOfMemoryError e) {
                    Toast.makeText(getApplicationContext(), "Memory Insufficient!", Toast.LENGTH_SHORT).show();
                }
                String serverResponseMessage = connection.getResponseMessage();

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lon=Double.toString(current_location_longitutde);
                            lat=Double.toString(current_location_latitude);
                            InsertUploadData(Code, Name, Email, Mobile, lon, lat, Month, Tree_Name, Dp_Name);
                        }
                    });
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File Not Found", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Cannot Read/Write File", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            hideDialog();
        }

    }

    private void InsertUploadData(final String code, final String name, final String email,final String mobile,
                                  final String lon, final String lat, final String month,final String tree_name,
                                  final String dp_name) {

        String tag_string_req = "uploading";

        Dialog.setMessage("Uploading Picture...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.UPLOAD_TREE_PICTURE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                        fileLocation="0";
                        Image.setImageDrawable(myDrawable);
                    } else {
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
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
                params.put("code", code);
                params.put("name", name);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("longitude", lon);
                params.put("latitudes", lat);
                params.put("month", month);
                params.put("tree_name", tree_name);
                params.put("file_name", dp_name);
                return params;
            }
        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //InsertUploadData(Name,Email,Mobile,longitude,latitudes,month,year,Tree_Name,file_Name,Code);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = getString(R.string.yes);
            String no = getString(R.string.no);
        }
        if (resultCode == Activity.RESULT_OK) {
            try {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    Image.setImageBitmap(bmp);
                    String fileName = Code+"_"+Mobile + "_" + Name + "_" + Month + ".jpg";
                    File extBaseDir = Environment.getExternalStorageDirectory();
                    File file = new File(extBaseDir.getAbsoluteFile() + "/Harit_Kandhar");
                    if (!file.exists()) {
                        if (!file.mkdirs()) {
                            throw new Exception("Could not create directories, " + file.getAbsolutePath());
                        }
                    }
                    String filePath = file.getAbsolutePath() + "/" + fileName;
                    FileOutputStream out = new FileOutputStream(filePath);
                    fileLocation = filePath;
                    bmp.compress(Bitmap.CompressFormat.JPEG, 40, out);     //control the jpeg quality
                    out.flush();
                    out.close();
                    imageurl = getRealPathFromURI(imageUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void showDialog() {
        if (!Dialog.isShowing())
            Dialog.show();
    }

    private void hideDialog() {
        if (Dialog.isShowing())
            Dialog.dismiss();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //current_location_latitude = 32.361114 ;
        //current_location_longitutde = 74.207883 ;

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getMyLocation();
        location = new LatLng(current_location_latitude, current_location_longitutde);
        mMap.addMarker(new MarkerOptions().position(location).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f));  // zoom in
    }

    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            Location loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            current_location_latitude = loc.getLatitude();
            current_location_longitutde = loc.getLongitude();
            // Toast.makeText(getApplicationContext(),current_locaton_latitude+" , "+ current_location_longitutde , Toast.LENGTH_SHORT).show();
        }
    }

    // Checks whether user is inside of circle or not
    public boolean IsInCircle() {
        float distance[] = {0, 0, 0};
        Location.distanceBetween(current_location_latitude, current_location_longitutde,
                circle.getCenter().latitude, circle.getCenter().longitude, distance);
        if (distance[0] > circle.getRadius())
            return false;
        else

            return true;
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
}