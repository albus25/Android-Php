package com.example.webserviceapi2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class ShowRecycler extends AppCompatActivity {
    TextView txtRSession;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
    JSONArray jsonArray;
    RecyclerView rv;
    RecyclerData radp;
    Button btnRAdd,btnRLogout;

//    private String TAG_STUDENTID = "studentID";
//    private String TAG_STUDENTNAME = "studentName";
//    private String TAG_GENDER = "gender";
//    private String TAG_BIRTHDATE = "birthDate";
//    private String TAG_PHONENO = "phoneNumber";
//    private String TAG_CITYID = "cityID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_recycler);
        setTitle("StudentRecycler");

        btnRAdd = findViewById(R.id.btnRAdd);
        btnRAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ShowRecycler.this,Insert.class);
                startActivity(it);
            }
        });

        txtRSession = findViewById(R.id.txtRSession);
        String studentName = loadPreference();
        txtRSession.setText(studentName);

        rv = findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(rv.VERTICAL);
        rv.setLayoutManager(linearLayoutManager);
        rv.addItemDecoration(new DividerItemDecoration(this,linearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URIs.URL_SHOW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("stud");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                HashMap<String,String> map = new HashMap<String, String>();
                                map.put("studentID",jsonObject1.getString("studentID"));
                                map.put("studentName",jsonObject1.getString("studentName"));
                                map.put("gender",jsonObject1.getString("gender"));
                                map.put("birthDate",jsonObject1.getString("birthDate"));
                                map.put("phoneNumber",jsonObject1.getString("phoneNumber"));
                                map.put("cityID",jsonObject1.getString("cityID"));
                                map.put("cityName",jsonObject1.getString("cityName"));

                                arrayList.add(map);
                            }
                            radp = new RecyclerData(getApplicationContext(),arrayList);
                            radp.setClickListener(new RecyclerData.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Toast.makeText(getApplicationContext(),"You clicked.." + arrayList.get(position) + " at "+ position,Toast.LENGTH_LONG).show();

                                }
                            });
                            radp.setClickListener(new RecyclerData.ItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    Toast.makeText(ShowRecycler.this, "Toast", Toast.LENGTH_SHORT).show();
                                    HashMap<String,String> map = arrayList.get(position);
                                    String studentID = map.get("studentID");
                                    String studentName = map.get("studentName");
                                    String gender = map.get("gender");
                                    String birthDate = map.get("birthDate");
                                    String phoneNumber = map.get("phoneNumber");
                                    String cityID = map.get("cityID");
                                    String cityName = map.get("cityName");

                                    Intent it = new Intent(ShowRecycler.this,UpdateDelete.class);
                                    it.putExtra("studentID",studentID);
                                    it.putExtra("studentName",studentName);
                                    it.putExtra("gender",gender);
                                    it.putExtra("birthDate",birthDate);
                                    it.putExtra("phoneNumber",phoneNumber);
                                    it.putExtra("cityID",cityID);
                                    it.putExtra("cityName",cityName);
                                    startActivity(it);
                                }
                            });
                            rv.setAdapter(radp);
                        } catch (JSONException e) {
                            Toast.makeText(ShowRecycler.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowRecycler.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        btnRLogout = findViewById(R.id.btnRLogout);
        btnRLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ShowRecycler.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent it = new Intent(ShowRecycler.this,MainActivity.class);
                startActivity(it);
            }
        });
    }

    public String loadPreference()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String studentName = sharedPreferences.getString("studentName","");
        if(studentName.length()>0)
        {
            return "Hola " + studentName;
        }
        else
        {
            return "Welcome Anonymous";
        }
    }
}
