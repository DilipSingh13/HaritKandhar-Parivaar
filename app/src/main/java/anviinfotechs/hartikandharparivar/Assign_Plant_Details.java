package anviinfotechs.hartikandharparivar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.DatabaseHandler;
import anviinfotechs.hartikandharparivar.helper.Functions;
import anviinfotechs.hartikandharparivar.helper.SessionManager;

public class Assign_Plant_Details extends AppCompatActivity {

    private static final String TAG = Assign_Plant_Details.class.getSimpleName();
    private SessionManager session;
    private DatabaseHandler db;
    private ProgressDialog pDialog;
    private HashMap<String, String> user = new HashMap<>();
    private String Name,Email,Mobile,count="0",approve_status="unapproved",Address;
    TextView txtName,txtEmail,txtMobile;
    Button Sumbit;
    CardView Card1,Card2;
    TextView Tree1,Tree2;
    TextView TreeDesc1,TreeDesc2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantation_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtName = findViewById(R.id.name);
        txtEmail = findViewById(R.id.email);
        txtMobile = findViewById(R.id.mobile);
        Card1 = findViewById(R.id.card1);
        Card2 = findViewById(R.id.card2);
        Tree1 = findViewById(R.id.plant_name1);
        Tree2 = findViewById(R.id.plant_name2);
        TreeDesc1 = findViewById(R.id.plant_description1);
        TreeDesc2 = findViewById(R.id.plant_description2);

        Card1.setVisibility(View.GONE);
        Card2.setVisibility(View.GONE);


        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from database
        Name = user.get("name");
        Mobile = user.get("mobile");
        Email = user.get("email");
        Address = user.get("address");

        checkTreeDetails(Email);
    }
    private void checkTreeDetails(final String email) {
        String tag_string_req = "req_register";

        pDialog.setMessage("Please wait...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.GET_TREE_DETAILS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String count = jObj.getString("count");
                        String Tree_a= jObj.getString("tree_name1");
                        String Tree_b= jObj.getString("tree_name2");
                        if (count.equals("2")) {
                            if (!Tree_a.isEmpty()) {
                                switch (Tree_a) {
                                    case "Mango": {
                                        Tree1.setText(R.string.mango);
                                        TreeDesc1.setText(R.string.Mango);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                    case "Chiku": {
                                        Tree1.setText(R.string.chiku);
                                        TreeDesc1.setText(R.string.Chiku);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                    case "Aawla": {
                                        Tree1.setText(R.string.amla);
                                        TreeDesc1.setText(R.string.Amla);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                    case "Jamun": {
                                        Tree1.setText(R.string.purple);
                                        TreeDesc1.setText(R.string.Purple);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    case "Jamb": {
                                        Tree1.setText(R.string.jamb);
                                        TreeDesc1.setText(R.string.Jamb);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                    case "Lemon": {
                                        Tree1.setText(R.string.lemon);
                                        TreeDesc1.setText(R.string.Lemon);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                    case "Citrus": {
                                        Tree1.setText(R.string.citrus);
                                        TreeDesc1.setText(R.string.Citrus);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                    case "Orange": {
                                        Tree1.setText(R.string.orange);
                                        TreeDesc1.setText(R.string.Orange);
                                        Card1.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                }
                            }
                            if (!Tree_b.isEmpty()) {
                            switch (Tree_b) {
                                case "Not Available": {
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                case "Mango": {
                                    Tree2.setText(R.string.mango);
                                    TreeDesc2.setText(R.string.Mango);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Chiku": {
                                    Tree2.setText(R.string.chiku);
                                    TreeDesc2.setText(R.string.Chiku);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Aawla": {
                                    Tree2.setText(R.string.amla);
                                    TreeDesc2.setText(R.string.Amla);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Jamun": {
                                    Tree2.setText(R.string.purple);
                                    TreeDesc2.setText(R.string.Purple);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                case "Jamb": {
                                    Tree2.setText(R.string.jamb);
                                    TreeDesc2.setText(R.string.Jamb);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Lemon": {
                                    Tree2.setText(R.string.lemon);
                                    TreeDesc2.setText(R.string.Lemon);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Citrus": {
                                    Tree2.setText(R.string.citrus);
                                    TreeDesc2.setText(R.string.Citrus);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Orange": {
                                    Tree2.setText(R.string.orange);
                                    TreeDesc2.setText(R.string.Orange);
                                    Card2.setVisibility(View.VISIBLE);
                                }
                                break;
                            }
                        }
                        }
                        else{
                            Card2.setVisibility(View.GONE);
                            switch (Tree_a){
                                case "Mango": {
                                    Tree1.setText(R.string.mango);
                                    TreeDesc1.setText(R.string.Mango);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Chiku": {
                                    Tree1.setText(R.string.chiku);
                                    TreeDesc1.setText(R.string.Chiku);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Aawla": {
                                    Tree1.setText(R.string.amla);
                                    TreeDesc1.setText(R.string.Amla);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Jamun": {
                                    Tree1.setText(R.string.purple);
                                    TreeDesc1.setText(R.string.Purple);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                case "Jamb": {
                                    Tree1.setText(R.string.jamb);
                                    TreeDesc1.setText(R.string.Jamb);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Lemon": {
                                    Tree1.setText(R.string.lemon);
                                    TreeDesc1.setText(R.string.Lemon);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Citrus": {
                                    Tree1.setText(R.string.citrus);
                                    TreeDesc1.setText(R.string.Citrus);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                                case "Orange": {
                                    Tree1.setText(R.string.orange);
                                    TreeDesc1.setText(R.string.Orange);
                                    Card1.setVisibility(View.VISIBLE);
                                }
                                break;
                            }
                        }
                    } else {
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
        }){

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}