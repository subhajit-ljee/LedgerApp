package profile.addledger.threads;

import android.content.Context;
import android.content.Intent;

import profile.addledger.CreateLedgerActivity;
import profile.addledger.model.LedgerRepository;

public class AddLedgerRunnable implements Runnable {

    private LedgerRepository ledgerRepository;
    private Context context;
    private Intent intent;
    public AddLedgerRunnable(Context context, LedgerRepository ledgerRepository,Intent intent){
        this.ledgerRepository = ledgerRepository;
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void run() {
        ledgerRepository.saveLedger();
        context.startActivity(intent);
    }
}
