package com.example.webserviceapi2;

import androidx.appcompat.app.AppCompatActivity;

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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowStudent extends AppCompatActivity {
    TextView txtSession;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<HashMap<String,String>>();
    JSONArray jsonArray;
    ListView lv;
    Button btnAdd,btnLogout;

//    private String TAG_STUDENTID = "studentID";
//    private String TAG_STUDENTNAME = "studentName";
//    private String TAG_GENDER = "gender";
//    private String TAG_BIRTHDATE = "birthDate";
//    private String TAG_PHONENO = "phoneNumber";
//    private String TAG_CITYID = "cityID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        setTitle("StudentList");

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ShowStudent.this,Insert.class);
                startActivity(it);
            }
        });

        txtSession = findViewById(R.id.txtSession);
        String studentName = loadPreference();
        txtSession.setText(studentName);

        lv = findViewById(R.id.lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String> map = arrayList.get(position);
                String studentID = map.get("studentID");
                String studentName = map.get("studentName");
                String gender = map.get("gender");
                String birthDate = map.get("birthDate");
                String phoneNumber = map.get("phoneNumber");
                String cityID = map.get("cityID");
                String cityName = map.get("cityName");

                Intent it = new Intent(ShowStudent.this,UpdateDelete.class);
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
                        SimpleAdapter simpleAdapter = new SimpleAdapter(ShowStudent.this,arrayList,R.layout.displaystudents,
                                new String[]{"studentID","studentName","gender","birthDate","phoneNumber","cityName"},
                                new int[]{R.id.txtStudentID,R.id.txtStudentName,R.id.txtGender,R.id.txtBDt,R.id.txtPNo,R.id.txtCityName});

                        lv.setAdapter(simpleAdapter);
                    } catch (JSONException e) {
                        Toast.makeText(ShowStudent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ShowStudent.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ShowStudent.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent it = new Intent(ShowStudent.this,MainActivity.class);
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
