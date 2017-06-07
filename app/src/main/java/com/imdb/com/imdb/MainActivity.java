package com.imdb.com.imdb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.imdb.com.imdb.Services.Service;
import com.imdb.com.imdb.domain.ServiceResponse;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    EditText editText;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.edit_text_field);
        editText.setText("This is the new text");

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.imdb_main_activity_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView =
                (SearchView) MenuItemCompat.getActionView(searchItem);
        setupSearchView(searchView);

        return true;
    }

    public void setupSearchView(SearchView searchView) {

        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeServiceCall();
                doSomethingOnMainthread();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search_bar:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void makeServiceCall() {
        progressDialog.setTitle("Loading data....");
        progressDialog.show();
        Observable<ServiceResponse> serviceResponseObservable = Service.getInstance().makeServiceCall();

        Observer<ServiceResponse> observer = new Observer<ServiceResponse>() {
            @Override
            public void onCompleted() {
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ServiceResponse serviceResponse) {

                Log.d(TAG + " Updated ", serviceResponse.getResponse().getVersion());
            }

        };
        serviceResponseObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);

    }

    private void doSomethingOnMainthread() {

        ArrayList<String> listOfStrings = new ArrayList<>();

        listOfStrings.add("first");
        listOfStrings.add("second");
        listOfStrings.add("three");
        listOfStrings.add("four");

        Iterator<String> it = listOfStrings.iterator();
        while (it.hasNext()) {

            Log.d(TAG, it.next());

        }

    }


    private class ServiceCallTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setTitle("Loading data....");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.d(TAG, "onMainThread in background !!!!!!!");
            }


            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {

                Log.d(TAG, e.getMessage());
                e.printStackTrace();
            }


           /* HttpURLConnection httpURLConnection=null;
            String serviceJsonResponse;
            URL url=null;

            try {
                url = new URL("http://api.wunderground.com/api/d27a25d2d400833f/conditions/q/CA/San_Francisco.json");
            } catch (MalformedURLException e) {
                Log.d(TAG,e.getMessage());
                e.printStackTrace();
            }

            try{
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                if(inputStream==null){
                    return null;
                }

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line=bufferedReader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                serviceJsonResponse =buffer.toString();
                Gson gson = new Gson();
                ServiceResponse serviceResponse = gson.fromJson(serviceJsonResponse,ServiceResponse.class);
                Log.d(TAG,serviceResponse.getResponse().getVersion());
            }catch (Exception e){
              Log.d(TAG,e.getMessage());
            }finally {
                httpURLConnection.disconnect();
            }


            return null;*/

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (Looper.myLooper() == Looper.getMainLooper()) {
                Log.d(TAG, "onMainThread  in PostExecute");
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }

}
