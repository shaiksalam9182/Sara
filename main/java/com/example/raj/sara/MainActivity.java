package com.example.raj.sara;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    EditText etuname,etphone,etemail,etpass,etcpass;
    Button breg;
    String username,phone,email,password,cpassword;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etuname = (EditText)findViewById(R.id.etusername);
        etphone = (EditText)findViewById(R.id.etphone);
        etemail = (EditText)findViewById(R.id.etemail);
        etpass = (EditText)findViewById(R.id.etpassword);
        etcpass = (EditText)findViewById(R.id.etcpassword);


        breg = (Button)findViewById(R.id.breg);
        startService(new Intent(this,Myservice.class));

        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etuname.getText().toString();
                phone = etphone.getText().toString();
                email = etemail.getText().toString();
                password = etpass.getText().toString();
                cpassword = etcpass.getText().toString();


                if (username.equals("")&&phone.equals("")&&email.equals("")&&password.equals("")&&cpassword.equals("")){
                    Toast.makeText(MainActivity.this,"Please fill all fields",Toast.LENGTH_SHORT).show();
                }else {
                    if (password.equals(cpassword)){
                        new Asynclogin().execute(username,phone,email,password);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Passwords are Not Matching",Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

    }

    private class Asynclogin extends AsyncTask<String,String,String>{

        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            pdLoading.setMessage("Registering\nPlease wait");
            pdLoading.setCancelable(false);
            pdLoading.show();



        }

        @Override
        protected String doInBackground(String... params) {

            try {
                url = new URL("http://sara69.000webhostapp.com/registration_app.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");



                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", params[0])
                        .appendQueryParameter("phone", params[1])
                        .appendQueryParameter("email", params[2])
                .appendQueryParameter("password", params[3]);
                String query = builder.build().getEncodedQuery();

                // Open connection for sending data
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e) {
                e.printStackTrace();
            }

            int response_code = 0;
            try {
                response_code = conn.getResponseCode();
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }


                    return(result.toString());

                }else{

                    return("unsuccessful");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdLoading.dismiss();
            if (s.equals("true")){
                Toast.makeText(MainActivity.this,"Successfully Registered",Toast.LENGTH_LONG).show();
                Bundle basket = new Bundle();
                basket.putString("email",email);
                Intent i = new Intent(MainActivity.this,Contacts.class);
                i.putExtras(basket);
                startActivity(i);
            }else {
                Toast.makeText(MainActivity.this,"Error in nothing",Toast.LENGTH_LONG).show();
            }

        }
    }
}
