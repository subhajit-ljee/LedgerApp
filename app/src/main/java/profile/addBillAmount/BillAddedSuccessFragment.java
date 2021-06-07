package profile.addBillAmount;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sourav.ledgerproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillAddedSuccessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillAddedSuccessFragment extends Fragment {

    public BillAddedSuccessFragment() {
        // Required empty public constructor
    }

    public static BillAddedSuccessFragment newInstance() {
        return new BillAddedSuccessFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.bill_amount_altered_success_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button thanks = view.findViewById(R.id.thanks_button);
        thanks.setOnClickListener( v -> {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_billAddedSuccessFragment_to_createQrCodeFragment);
        });
    }
}