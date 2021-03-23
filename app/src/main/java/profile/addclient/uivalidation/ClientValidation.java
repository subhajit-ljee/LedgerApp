package profile.addclient.uivalidation;

import android.util.Log;

public class ClientValidation {

    private static final String TAG = "ClientValidation";

    private String error = "";


    public String isValid(String clientid){
        if(clientid.isEmpty()){
            error = "Please give an User Id";
            Log.d(TAG, "isValid 1st: "+error);
        }else if(clientid.length() > 60){
            error = "Id length is too long";
            Log.d(TAG, "isValid 2nd: "+error);
        }else{
            error="";
            Log.d(TAG, "No error on Client Id Validation " + error);
        }
        Log.d(TAG, "isValid: client id: "+clientid);
        return error;
    }
}
