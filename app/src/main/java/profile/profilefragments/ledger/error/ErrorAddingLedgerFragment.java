package profile.profilefragments.ledger.error;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sourav.ledgerproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ErrorAddingLedgerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ErrorAddingLedgerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public ErrorAddingLedgerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ErrorAddingLedgerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ErrorAddingLedgerFragment newInstance(String param1) {
        ErrorAddingLedgerFragment fragment = new ErrorAddingLedgerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_error_adding_ledger, container, false);
    }
}