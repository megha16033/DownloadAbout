package com.example.mnrr.downloadabout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Calling function when download is clicked
    public void onDownload(View view)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
        {
            Toast.makeText(this,"Network is  available",Toast.LENGTH_SHORT).show();
            new DownloadAboutFunction().execute();
        }
        else{
            Toast.makeText(this,"Network is not available",Toast.LENGTH_SHORT).show();
        }
    }

    public static String getHtml(String url) throws IOException {
        // Build and set timeout values for the request.
        URLConnection connection = (new URL(url)).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        // Read and store the result line by line then return the entire string.
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder html = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            html.append(line);
        }
        in.close();

        return html.toString();
    }

    private class DownloadAboutFunction extends AsyncTask<Void, Void, Document> {


        @Override
        protected Document doInBackground(Void... params)

        {
            //String doc = "hello";
            Document doc = null;

            try {
                    doc = Jsoup.connect("http://www.iiitd.ac.in/about").get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return doc;

        }

        @Override
        protected void onPostExecute(Document document) {
            //if you had a ui element, you could display the title
            //((TextView)findViewById (R.id.myTeoaxtView)).setText (result);
            if (document == null) {

                Toast.makeText(getApplicationContext(), "Cannot access internet !", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println(document.text());
                Elements els = document.select("p");
                int i=0;
                String data="";
                for(Element ele :els)
                {
                    if(i>5)
                    data = data + ele.text();
                    ++i;
                }

                TextView text = (TextView) findViewById(R.id.data);
                //String stripped = document.replaceAll("<[^>]*>", "");
                text.setMovementMethod(new ScrollingMovementMethod());
                text.setText(data);
            }
        }
        }

    }
