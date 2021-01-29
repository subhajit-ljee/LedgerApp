package com.sourav.ledgerproject.profile.addledger.addvoucher.notification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationClient {

    private static Retrofit retrofit = null;

    public Retrofit getClient(String url){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }
}
