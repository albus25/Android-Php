package com.example.webserviceapi2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText txtName,txtPass;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");

        txtName = findViewById(R.id.txtUsername);
        txtPass = findViewById(R.id.txtLPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtName.getText().toString()))
                {
                    txtName.setError("StudentName is required");
                    txtName.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(txtPass.getText().toString()))
                {
                    txtPass.setError("Password is required");
                    txtPass.requestFocus();
                    return;
                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URIs.URL_LOGIN,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);

                                if(jsonObject.getInt("success") == 1)
                                {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("studentName",txtName.getText().toString());
                                    editor.commit();

                                    Intent it = new Intent(MainActivity.this,ShowRecycler.class);
                                    startActivity(it);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Naah", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(MainActivity.this, "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "New:"+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("studentName",txtName.getText().toString());
                        params.put("password",txtPass.getText().toString());

                        return params;
                    }
                };
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
        loadSharedPreference();
    }

    public void loadSharedPreference()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String studentName = sharedPreferences.getString("studentName","");
        if(studentName != null && !studentName.equals(""))
        {
            Intent it = new Intent(MainActivity.this,ShowRecycler.class);
            startActivity(it);
        }
        else
        {
            Toast.makeText(this, "Username & Password wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
