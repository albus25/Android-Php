package com.example.webserviceapi2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateDelete extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText txtID,txtName,txtPass,txtCPass,txtBD,txtPNo;
    RadioGroup rdg;
    Spinner sp;
    Button btnUpdate,btnDelete;
    String gender="";
    DatePickerDialog datePicker;
    JSONArray jsonArray;
    ArrayList<String> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        setTitle("Manage");

        final Intent it = getIntent();

        txtID = findViewById(R.id.txtUID);
        txtID.setText(it.getStringExtra("studentID"));
        txtName = findViewById(R.id.txtUName);
        txtName.setText(it.getStringExtra("studentName"));
        txtPass = findViewById(R.id.txtUPass);
        txtCPass = findViewById(R.id.txtUCPass);
        txtBD = findViewById(R.id.txtUBD);
        txtBD.setText(it.getStringExtra("birthDate"));
        txtPNo = findViewById(R.id.txtUNo);
        txtPNo.setText(it.getStringExtra("phoneNumber"));
        rdg = findViewById(R.id.RGU);
        String gen = it.getStringExtra("gender");
        RadioButton rd1 = findViewById(R.id.rdUF);
        RadioButton rd2 = findViewById(R.id.rdUM);
        if(rd1.getText().toString().equals(gen))
        {
            rd1.setChecked(true);
            rd2.setChecked(false);
        }
        else if(rd2.getText().toString().equals(gen))
        {
            rd2.setChecked(true);
            rd1.setChecked(false);
        }

        final String cmbID = it.getStringExtra("cityID");

        sp = findViewById(R.id.comboUCity);
        sp.setOnItemSelectedListener(this);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH + 1);
        int year = c.get(Calendar.YEAR);
        datePicker = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dt = year + "-" + month + "-" + dayOfMonth;
                txtBD.setText(dt);
            }
        },year,month,day);
        txtBD.setInputType(InputType.TYPE_NULL);
        txtBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URIs.URL_FILL_CITY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("city");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String cityID = jsonObject1.getString("cityID");
                                String cityName = jsonObject1.getString("cityName");

                                cityList.add(cityName);
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,cityList);
                            sp.setAdapter(arrayAdapter);
                            sp.setSelection(Integer.parseInt(cmbID)-1);
                        } catch (JSONException e) {
                            Toast.makeText(UpdateDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateDelete.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtName.getText().toString()))
                {
                    txtName.setError("Name is required");
                    txtName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(txtPass.getText().toString()))
                {
                    txtPass.setError("Password is required");
                    txtPass.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(txtCPass.getText().toString()))
                {
                    txtCPass.setError("Confirm Password is required");
                    txtCPass.requestFocus();
                    return;
                }
                else if(!txtPass.getText().toString().equals(txtCPass.getText().toString()))
                {
                    txtCPass.setError("Confirm Password is not matched");
                    txtCPass.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(txtPNo.getText().toString()))
                {
                    txtPNo.setError("Phone Number is required");
                    txtPNo.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(txtBD.getText().toString()))
                {
                    txtBD.setError("BirthDate is required");
                    txtBD.requestFocus();
                    return;
                }
                if(rdg.getCheckedRadioButtonId() == R.id.rdUF)
                {
                    gender = "Female";
                }
                else
                {
                    gender = "Male";
                }

                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URIs.URL_UPDATE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if(jsonObject.getInt("success")==1)
                                    {
                                        Toast.makeText(UpdateDelete.this, "Updated", Toast.LENGTH_SHORT).show();

                                        Intent it = new Intent(UpdateDelete.this,ShowStudent.class);
                                        startActivity(it);
                                    }
                                    else
                                    {
                                        Toast.makeText(UpdateDelete.this, "Ille", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(UpdateDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(UpdateDelete.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("studentID",txtID.getText().toString());
                        params.put("studentName",txtName.getText().toString());
                        params.put("password",txtPass.getText().toString());
                        params.put("gender",gender);
                        params.put("birthDate",txtBD.getText().toString());
                        params.put("phoneNumber",txtPNo.getText().toString());
                        params.put("cityID",cityID + "");
                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
            }
        });

        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URIs.URL_DELETE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);

                                    if(jsonObject.getInt("success")==1)
                                    {
                                        Toast.makeText(UpdateDelete.this, "Deleted", Toast.LENGTH_SHORT).show();

                                        Intent it = new Intent(UpdateDelete.this,ShowStudent.class);
                                        startActivity(it);
                                    }
                                    else
                                    {
                                        Toast.makeText(UpdateDelete.this, "Ille", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(UpdateDelete.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(UpdateDelete.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("studentID",txtID.getText().toString());
                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);
            }
        });
    }

    int cityID;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cityID = sp.getSelectedItemPosition() + 1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
