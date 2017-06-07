package com.imdb.com.imdb.domain;

import java.io.Serializable;

/**
 * Created by venkatesh on 3/8/17.
 */

public class ServiceResponse implements Serializable {


    private Response response;

    private CurrentObservation currentObservation;


    public Response getResponse() {

        return response;

    }

    public CurrentObservation getCurrentObservation() {

        return currentObservation;
    }


}
