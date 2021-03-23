package profile.addvoucher.threads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import profile.addvoucher.model.VoucherRepository;


public class AddVoucherHandlerThread extends HandlerThread {
    private static final String TAG = "AddVoucherHandlerThread";
    public static final int ADD_VOUCHER_TASK = 1;
    public static final int APPROVE_VOUCHER_TASK = 2;
    private Handler handler;
    private VoucherRepository voucherRepository;
    private Intent intent;



    private Context context;

    public AddVoucherHandlerThread(Context context) {
        super("addvoucherhandlerthread", Process.THREAD_PRIORITY_BACKGROUND);
        this.context = context;
        //db = FirebaseFirestore.getInstance();
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onLooperPrepared() {
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case ADD_VOUCHER_TASK:



                        break;
                    case APPROVE_VOUCHER_TASK:
                        Log.d(TAG, "handleMessage: before adding voucher " + msg.what);

                        break;
                }
            }
        };
    }



    public Handler getHandler(){ return handler; }

}
