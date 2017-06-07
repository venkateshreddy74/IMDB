package com.imdb.com.imdb.Services;


import android.util.Log;

import com.google.gson.Gson;
import com.imdb.com.imdb.domain.ServiceResponse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import rx.Observable;
import rx.Subscriber;


public class Service {

    private static final String TAG = "Service";
    private static Service serviceInstance = null;

    private Service() {

    }

    public static Service getInstance() {
        if (serviceInstance == null) {
            serviceInstance = new Service();
        }
        return serviceInstance;
    }

    public Observable<ServiceResponse> makeServiceCall() {

        //TODO Make the Service Call and return observable of ServiceResponse


        Observable<ServiceResponse> observable = Observable.create(new Observable.OnSubscribe<ServiceResponse>() {


            @Override
            public void call(Subscriber<? super ServiceResponse> subscriber) {

                HttpURLConnection httpURLConnection = null;
                String serviceJsonResponse = null;
                ServiceResponse serviceResponse = null;
                URL url = null;

                try {
                    url = new URL("http://api.wunderground.com/api/d27a25d2d400833f/conditions/q/CA/San_Francisco.json");
                } catch (MalformedURLException e) {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();
                }

                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    if (inputStream == null) {
                        //   return null;
                    }

                    StringBuffer buffer = new StringBuffer();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        // return null;
                    }

                    serviceJsonResponse = buffer.toString();
                    Gson gson = new Gson();
                    serviceResponse = gson.fromJson(serviceJsonResponse, ServiceResponse.class);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                } finally {
                    httpURLConnection.disconnect();
                }


                subscriber.onNext(serviceResponse);
                subscriber.onCompleted();
            }
        });

        return observable;
    }


}
