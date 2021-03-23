package profile.addledger.threads;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import profile.addledger.CreateLedgerActivity;
import profile.addledger.model.Ledger;
import profile.addledger.model.LedgerRepository;
import profile.addledger.uivalidation.LedgerValidation;

import java.util.ArrayList;
import java.util.List;

public class AddLedgerRunnable implements Runnable {

    private Context context;
    private Intent intent;

    public AddLedgerRunnable(Context context,Intent intent){
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void run() {
        context.startActivity(intent);
    }
}
