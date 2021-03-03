package profile.addvoucher.threads.servicethreads;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;

import androidx.annotation.NonNull;

public class AddVoucherHandlerThread extends HandlerThread {
    private Handler handler;
    public AddVoucherHandlerThread(String name) {
        super(name, Process.THREAD_PRIORITY_BACKGROUND);
    }

    @Override
    @SuppressLint("HandlerLeak")
    protected void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    public Handler getHandler(){ return handler; }
}
