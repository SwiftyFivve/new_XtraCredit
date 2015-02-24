package com.jordanweaver.jordan_devaunte_xtracredit;

import android.app.Activity;
//<<<<<<< HEAD
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends Activity {


    Spinner weatherSpinner;
    //Context mContext;
    Context mContext;
    LinearLayout currentWeatherView;
    LinearLayout hourlyWeatherView;
    LinearLayout tenDayWeatherView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        weatherSpinner = (Spinner) findViewById(R.id.weatherSpinner);
        currentWeatherView = (LinearLayout) findViewById(R.id.currentWeatherView);
        hourlyWeatherView = (LinearLayout) findViewById(R.id.hourlyWeatherView);
        tenDayWeatherView = (LinearLayout) findViewById(R.id.tenDayWeatherView);

        mContext = this;


        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                mContext,
                R.array.spinner_stuff,
                android.R.layout.simple_dropdown_item_1line
        );

        weatherSpinner.setAdapter(spinnerAdapter);

         weatherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 switch (position){
                     case 0:
                         currentWeatherView.setVisibility(View.VISIBLE);
                         hourlyWeatherView.setVisibility(View.GONE);
                         tenDayWeatherView.setVisibility(View.GONE);
                         break;
                     case 1:
                         hourlyWeatherView.setVisibility(View.VISIBLE);
                         currentWeatherView.setVisibility(View.GONE);
                         tenDayWeatherView.setVisibility(View.GONE);
                         break;
                     case 2:
                         tenDayWeatherView.setVisibility(View.VISIBLE);
                         currentWeatherView.setVisibility(View.GONE);
                         hourlyWeatherView.setVisibility(View.GONE);
                 }


             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         });



        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                MyTask myTask = new MyTask();

                myTask.execute("http://api.wunderground.com/api/4bfff3e6246abd8a/conditions/forecast10day/hourly/settings/q/FL/orlando.json");

            } else {
                Toast.makeText(MainActivity.this, "Internet connection unavailable", Toast.LENGTH_LONG).show();
            }
        }
    }



    public class MyTask extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("Loading Data");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Please wait while we load your data");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = "";

            try {

                URL url = new URL(params[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                result = IOUtils.toString(is);
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                result = "Error";

            } catch (IOException e) {
                e.printStackTrace();
                result = "Error";
            }

            return result;
        }


        @Override
        protected void onPostExecute(String s) {


            super.onPostExecute(s);

            if (s.equals("Error")) {

                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT);

            }
            else {
                try {
                    JSONObject mainObject = new JSONObject(s);

                    // TODO Log current conditions and  the temperature location.


                    Log.e("Json array", "Children array");
//                    JSONArray BookArray = mainObject.getJSONArray("items");
                    JSONObject theweatherObject = mainObject.getJSONObject("current_observation");
                    JSONObject weatherObject = mainObject.getJSONObject("current_observation").getJSONObject("display_location");

                    // TODO Log
                    Log.e("Going to for loop", "For loop");


                    // First Screen
                        String location;

                        String condition;
                        String temperature;
                        //String myImageLink = "";
//                        JSONObject childObjectPicture = BookArray.getJSONObject(i).getJSONObject("volumeInfo").getJSONObject("imageLinks");



                    //Second screen is an hourly forecast shown in a list.




                    //
                        // TODO Log
                        if (weatherObject.has("full")) {
                            location = weatherObject.getString("full");
                            Log.e("City", location+"");

                        } else {
                            location = "N/A";
                            Log.e("City", location+"");
                        }
                        if (theweatherObject.has("temperature_string")) {
                            temperature = theweatherObject.getJSONArray("temperature_string").toString();
                            //Log.i("Authors name", "............ AuthorsName:" + temperature);
                            Log.e("City", temperature+"");

                        } else {
                            temperature = "N/A";
                            Log.e("City", temperature+"");
                        }

                        if (theweatherObject.has("description")) {
                            condition = theweatherObject.getString("weather");
                            Log.e("City", condition+"");
                        } else {
                            condition = "N/A";
                            Log.e("City", condition+"");

                        }

//                        if (childObject.has("imageLinks")) {
//
//                            //image.setImageUrl(childObjectPicture.getString("smallThumbnail"));
//                            myImageLink = childObjectPicture.getString("thumbnail");
//                            Log.i("Image being passed", "Image being passed:" + myImageLink);
//                        }

//                        books.add(new Book(bookName, description, myImageLink, authorsName));




                } catch (JSONException e) {
                    e.printStackTrace();
                }



//                MyAdapter adapter = new MyAdapter(MainActivity.this, books);
//
//                gridView.setAdapter(adapter);

                dialog.cancel();
            }


        }



    }


}
