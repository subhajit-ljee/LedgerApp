package profile.addvoucher.notification;

import com.google.gson.JsonObject;

import profile.addvoucher.notification.model.Data;
import profile.addvoucher.notification.model.MyResponse;
import profile.addvoucher.notification.model.NotificationSender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers(
            {
                    "Content-Type: application/json",
                    "Authorization:key=AAAAXEGsKas:APA91bHuB3dgWT1B-ThfaXxH3PvdH_qpgl0XV75CrlskFnParxnlkmyubxUuDNMNowT5qLe0p_JQUsHLr-Re7St-xzx5pDU9-KLVZiCyGzeS6C173Ki5JPz11Vc3Z742w9lPQjV0K952"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body NotificationSender body);
}
