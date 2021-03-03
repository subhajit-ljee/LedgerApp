package profile.addclient.threads;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

public class AddClientHandlerThread extends HandlerThread {
    private static final String TAG = "AddClientHandlerThread";
    public static final int ADD_CLIENT_TASK = 1;
    private Handler handler;
    public AddClientHandlerThread() {
        super("AddClientHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case ADD_CLIENT_TASK:
                        Log.d(TAG, "handleMessage: before adding client");
                        break;
                }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }
}
