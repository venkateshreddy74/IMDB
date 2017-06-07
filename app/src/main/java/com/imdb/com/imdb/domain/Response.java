package com.imdb.com.imdb.domain;

import java.io.Serializable;

/**
 * Created by venkatesh on 3/8/17.
 */

public class Response implements Serializable {

    private String version;
    private String termsofService;
    private Features features;

    public String getVersion() {
        return version;
    }

    public String getTermsofService() {
        return termsofService;
    }

    public Features getFeatures() {
        return features;
    }

}
