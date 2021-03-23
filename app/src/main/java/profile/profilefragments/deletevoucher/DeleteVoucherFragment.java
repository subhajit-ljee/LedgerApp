package profile.profilefragments.deletevoucher;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sourav.ledgerproject.R;

import profile.deletevoucher.service.DeleteVoucherService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeleteVoucherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeleteVoucherFragment extends Fragment {


    private static final String TAG = "DeleteVoucherFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String VOUCHER_ID = "voucherid";
    private static final String CLIENT_ID = "clientid";
    private static final String LEDGER_ID = "ledgerid";
    private static final String NOTIFY_FROM = "notifyfrom";

    // TODO: Rename and change types of parameters
    private String voucherid;
    private String clientid;
    private String ledgerid;
    private String notifyfrom;

    private Button grant_per;
    private Button deny_per;

    public DeleteVoucherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param voucherid Parameter 1.
     * @param clientid Parameter 2.
     * @param ledgerid Parameter 2.
     * @param ledgerid Parameter 2.
     * @return A new instance of fragment DeleteVoucherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeleteVoucherFragment newInstance(String voucherid, String clientid, String ledgerid, String notifyfrom) {
        DeleteVoucherFragment fragment = new DeleteVoucherFragment();
        Bundle args = new Bundle();
        args.putString(VOUCHER_ID, voucherid);
        args.putString(CLIENT_ID, clientid);
        args.putString(LEDGER_ID, ledgerid);
        args.putString(NOTIFY_FROM, notifyfrom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            voucherid = getArguments().getString(VOUCHER_ID);
            clientid = getArguments().getString(CLIENT_ID);
            ledgerid = getArguments().getString(LEDGER_ID);
            notifyfrom = getArguments().getString(NOTIFY_FROM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delete_voucher, container, false);

        grant_per = view.findViewById(R.id.permission_grant_heading_button);
        deny_per = view.findViewById(R.id.permission_deny_heading_button);

        grant_per.setOnClickListener( v -> {
            if(voucherid != null && clientid != null && ledgerid != null){
                Intent intent = new Intent(getActivity(),DeleteVoucherService.class);
                Log.d(TAG, "onMessageReceived inside cond 1: title: vid: " + voucherid + ", clientid: " + clientid + ", notifyfrom: " + notifyfrom);
                intent.putExtra("vid",voucherid);
                intent.putExtra("clientid",clientid);
                intent.putExtra("ledger_id",ledgerid);
                intent.putExtra("notifyfrom",notifyfrom);
                Log.d(TAG, "onCreateView: connecting with service");
                DeleteVoucherService.enqueueWork(getActivity(),intent);
                inflater.inflate(R.layout.fragment_error_adding_ledger,null);
            }
        });

        return view;
    }
}