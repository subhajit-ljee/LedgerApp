package profile.addvoucher.notification.model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationClient {

    private static Retrofit retrofit;

    public static Retrofit getApiService(String url){
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}
