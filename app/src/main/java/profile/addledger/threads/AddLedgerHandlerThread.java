package profile.addledger.threads;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;

public class AddLedgerHandlerThread extends HandlerThread {
    private static final String TAG = "AddLedgerHandlerThread";
    public static final int ADD_LEDGER_TASK = 1;
    private Handler handler;
    public AddLedgerHandlerThread() {
        super("AddLedgerHandlerThread", Process.THREAD_PRIORITY_BACKGROUND);
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case ADD_LEDGER_TASK:
                        Log.d(TAG, "handleMessage: before adding ledger");
                        break;
                }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }
}
