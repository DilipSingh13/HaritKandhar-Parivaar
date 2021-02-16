package anviinfotechs.hartikandharparivar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import anviinfotechs.hartikandharparivar.helper.DatabaseHandler;
import anviinfotechs.hartikandharparivar.helper.Functions;
import anviinfotechs.hartikandharparivar.helper.SessionManager;

public class View_Plantation_Plan extends AppCompatActivity {

    ListView DetailsListView;
    ProgressBar progressBar;
    TextView Result;
    private static final String TAG = View_Plantation_Plan.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactJsonList;
    private ProgressDialog pDialog;
    String Name,Email,Mobile,grant_status,tree_name,code,month_slot;
    Button Upload;
    private SessionManager session;
    private DatabaseHandler db;
    private HashMap<String, String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantation_plan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DetailsListView = findViewById(R.id.listView_search_result);
        progressBar = findViewById(R.id.progressbar);
        Upload=findViewById(R.id.upload);
        Result=findViewById(R.id.result);
        contactJsonList = new ArrayList<>();

        pDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // session manager
        session = new SessionManager(getApplicationContext());
        // Fetching user details from database
        Name = user.get("name");
        Email = user.get("email");
        Mobile = user.get("mobile");
        FetchData(Name,Email,Mobile);
    }

    public void FetchData(final String Name,final  String Email, final String Mobile) {

        pDialog.setMessage("Please wait...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                Functions.FETCH_PLAN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    JSONArray jsonObject = new JSONArray(response);

                    if(response !=null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject c = jsonArray.getJSONObject(i);
                            tree_name = c.getString("Tree_name");
                            grant_status = c.getString("Grant_Status");
                            code = c.getString("Unique_code");
                            month_slot = c.getString("Month_Slot");
                            HashMap<String, String> fetchOrders = new HashMap<>();

                            // adding each child node to HashMap key => value
                            fetchOrders.put("Tree_name", tree_name);
                            fetchOrders.put("Grant_Status", grant_status);
                            fetchOrders.put("Unique_code", code);
                            fetchOrders.put("Month_Slot", month_slot);

                            contactJsonList.add(fetchOrders);
                        }
                        progressBar.setVisibility(View.GONE);
                        Result.setVisibility(View.GONE);
                    }
                } catch (final Exception e ) {
                    contactJsonList.clear();
                    progressBar.setVisibility(View.GONE);
                    Result.setVisibility(View.VISIBLE);
                    Result.setText("No Result Found !");
                    e.printStackTrace();
                }
                ListAdapter adapter = new SimpleAdapter(
                        View_Plantation_Plan.this, contactJsonList,
                        R.layout.fetch_data_list, new String[]{"Tree_name","Grant_Status","Unique_code","Month_Slot"}, new int[]{R.id.tree_name, R.id.grant_status, R.id.code, R.id.month});

                DetailsListView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                DetailsListView.setAdapter(adapter);

                DetailsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        final TextView Tree_name = (TextView) view.findViewById(R.id.tree_name);
                        final TextView Click_pic = (TextView) view.findViewById(R.id.click_pic);
                        final TextView Grant_Status = (TextView) view.findViewById(R.id.grant_status);
                        final TextView Code = (TextView) view.findViewById(R.id.code);
                        final TextView Month = (TextView) view.findViewById(R.id.month);
                        tree_name=Tree_name.getText().toString();
                        grant_status=Grant_Status.getText().toString();
                        code=Code.getText().toString();
                        month_slot=Month.getText().toString();
                        Click_pic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (grant_status.equals("Yes")) {
                                    Intent i =new Intent(getApplicationContext(), Upload_Image.class);
                                    i.putExtra("Code",code);
                                    i.putExtra("Name",Name);
                                    i.putExtra("Email",Email);
                                    i.putExtra("Mobile",Mobile);
                                    i.putExtra("Tree_Name",tree_name);
                                    i.putExtra("Month_Slot",month_slot);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(View_Plantation_Plan.this, "Currently month plane is not active", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
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
                return params;
            }

        };

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
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