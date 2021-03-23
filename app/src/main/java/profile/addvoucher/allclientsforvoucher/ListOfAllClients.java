package profile.addvoucher.allclientsforvoucher;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.sourav.ledgerproject.R;
import profile.profilefragments.voucher.ClientListForVoucherFragment;

public class ListOfAllClients extends AppCompatActivity {

    private static final String TAG = "ListOfAllClients";

    private ClientListForVoucherFragment clientListForVoucherFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_all_clients);

        clientListForVoucherFragment = ClientListForVoucherFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.ledger_holder_list_frag,clientListForVoucherFragment).commit();

    }

}