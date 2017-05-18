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

public class Contacts extends AppCompatActivity {


    EditText etnum1,etnum2,etnum3,etnum4,etnum5;

    Button btadd;

    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;

    String num1,num2,num3,num4,num5;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Bundle gotbasket = getIntent().getExtras();

        email = gotbasket.getString("email");

        etnum1 = (EditText)findViewById(R.id.etnum1);
        etnum2 = (EditText)findViewById(R.id.etnum2);
        etnum3 = (EditText)findViewById(R.id.etnum3);
        etnum4 = (EditText)findViewById(R.id.etnum4);
        etnum5 = (EditText)findViewById(R.id.etnum5);

        btadd = (Button)findViewById(R.id.btaddd);




        btadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num1 = etnum1.getText().toString();
                num2 = etnum2.getText().toString();
                num3 = etnum3.getText().toString();
                num4 = etnum4.getText().toString();
                num5 = etnum5.getText().toString();

                if (num1.equals("")){
                    Toast.makeText(Contacts.this,"Atleast Add First Phone No",Toast.LENGTH_LONG).show();
                }
                else if (num2.equals("")){
                    num2 = "";
                }
                else if (num2.equals("")){
                    num3 = "";
                }
                else if (num2.equals("")){
                    num4 = "";
                }
                else if (num2.equals("")){
                    num5 = "";
                }
                if (!num1.equals("")){
                    new Asyncaddcontact().execute(email,num1,num2,num3,num4,num5);
                }



            }
        });

    }

    private class Asyncaddcontact extends AsyncTask<String,String,String> {

        ProgressDialog plloading = new ProgressDialog(Contacts.this);

        HttpURLConnection conn;

        URL url;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            plloading.setMessage("Adding Contacts\n please wait");
            plloading.setCancelable(false);
            plloading.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL("http://sara69.000webhostapp.com/contacts_add_app.php");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");



                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("email", params[0])
                        .appendQueryParameter("contact1", params[1])
                        .appendQueryParameter("contact2", params[2])
                        .appendQueryParameter("contact3", params[3])
                        .appendQueryParameter("contact4", params[4])
                        .appendQueryParameter("contact5", params[5]);
                String query = builder.build().getEncodedQuery();


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
            plloading.dismiss();
            if (s.equals("true")){
                Toast.makeText(Contacts.this,"Succesfully added",Toast.LENGTH_LONG).show();
                Bundle basket = new Bundle();
                basket.putString("email",email);
                Intent in = new Intent(Contacts.this,Maps.class);
                in.putExtras(basket);
                startActivity(in);


            }else{
                Toast.makeText(Contacts.this,s,Toast.LENGTH_LONG).show();
            }
        }
    }
}
