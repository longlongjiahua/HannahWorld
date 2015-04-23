package com.hannah.hannahworld;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


 
public class PostDataActivity extends Activity implements OnClickListener {
 
    TextView tvIsConnected;
    EditText etName,etCountry;
    Button btnPost;
 
    Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_data);
 

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
        etName = (EditText) findViewById(R.id.etName);
        etCountry = (EditText) findViewById(R.id.etCountry);
        btnPost = (Button) findViewById(R.id.btnPost);
 
        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are connected");
        }
        else{
            tvIsConnected.setText("You are NOT connected");
        }
 
        // add click listener to Button "POST"
        btnPost.setOnClickListener(this);
 
    }
 

 
    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) 
                return true;
            else
                return false;    
    }
    @Override
    public void onClick(View view) {
 
        switch(view.getId()){
            case R.id.btnPost:
                if(validate()) {
                    new HttpAsyncTask().execute();
                    //Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                }
                // call AsynTask to perform network operation on separate thread
                //new HttpAsyncTask().execute("http://hmkcode.appspot.com/jsonservlet");

                //mPost();
            break;
        }
 
    }

    void mPost(){
        String path = "https://instacoin.net/json/get_post_data.php";

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
        // Limit
        HttpResponse response;
        JSONObject json = new JSONObject();
        try {
            HttpPost post = new HttpPost(path);
           // json.put("service", "GOOGLE");
            json.put("name",etName.getText().toString());
            json.put("country",etCountry.getText().toString());
            Log.i("json Object", json.toString());
            post.setHeader("json", json.toString());
            StringEntity se = new StringEntity(json.toString());
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);
        /* Checking response */
            if (response != null) {
                InputStream in = response.getEntity().getContent(); // Get the

                String a = convertStreamToString(in);
                Log.i("Read from Server", a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }




    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            //person = new Person();
            //person.setName(etName.getText().toString());

                mPost();

            return "done";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
       }
    }
 
    private boolean validate(){
        if(etName.getText().toString().trim().equals(""))
            return false;
        else if(etCountry.getText().toString().trim().equals(""))
            return false;
        else
            return true;    
    }

}
