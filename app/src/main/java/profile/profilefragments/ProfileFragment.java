package profile.profilefragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;

import profile.addclient.SelectAndAddClientActivity;
import profile.addclient.jobintentservices.SelectAndAddClientService;
import profile.addledger.ListOfAllClients;
import profile.addusers.SaveUserJobService;
import profile.credit.CreditListActivity;
import profile.debit.DebitListActivity;
import profile.deletevoucher.activities.ListofClientForDeleteActivity;
import profile.upload.PdfUploadActivity;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "ProfileFragment";

    private ImageView imageView;
    private TextView name;

    private Button copyid;
    private ConstraintLayout see_debit, see_credit, add_client_view;


    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //name = view.findViewById(R.id.registered_user_name);
        //email = view.findViewById(R.id.registered_user_email);
        //id = view.findViewById(R.id.registered_user_id);
        //imageView = view.findViewById(R.id.user_image_registered);
        copyid = view.findViewById(R.id.copy_id);
        add_client_view = view.findViewById(R.id.add_client_view);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        //name.setText(username);


        copyid.setOnClickListener( v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", userid);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), "Id Copied Successfully! ", Toast.LENGTH_SHORT).show();
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {

            String messaging_token;

            if(task.isSuccessful()){

                messaging_token = task.getResult();

                Log.d(TAG, "onCreateView: messaging_token: " + messaging_token);
                    Intent intent = new Intent(getContext(), SaveUserJobService.class);
                    intent.putExtra("userid", userid);
                    intent.putExtra("useremail", useremail);
                    intent.putExtra("username", username);
                    intent.putExtra("messaging_token", messaging_token);

                    SaveUserJobService.enqueueWork(getActivity(), intent);

            }
        });

        /*Glide.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .into(imageView);
        */
        //name.setText(username);
        //email.setText(useremail);

        see_debit = view.findViewById(R.id.see_debit);
        see_credit = view.findViewById(R.id.see_credit);

        NavController findNavController = Navigation.findNavController(getActivity(),R.id.pro_main_fragment);
        see_debit.setOnClickListener( v -> new Handler().post( () -> findNavController.navigate(R.id.action_profileFragment_to_debitListFragment)));

        see_credit.setOnClickListener( v -> new Handler().post( () -> findNavController.navigate(R.id.action_profileFragment_to_creditListFragment)));
        add_client_view.setOnClickListener( v -> {

            View client_add_layout = getLayoutInflater().inflate(R.layout.add_clients_layout,null);
            EditText clientid = client_add_layout.findViewById(R.id.client_id_check);

            AlertDialog addclientalert = new AlertDialog.Builder(getActivity())
            .setView(R.layout.add_clients_layout)
            .setPositiveButton( "SAVE",(dialog, which) -> {

                String clientidcheck = clientid.getText().toString().trim();
                Log.d(TAG, "onViewCreated: clientid" + clientidcheck);
                Intent intent = new Intent(getActivity(),SelectAndAddClientService.class);
                intent.putExtra("clientid", clientidcheck);
                SelectAndAddClientService.enqueueWork(getContext(), intent);

            }).setNegativeButton("CANCEL", null)
            .create();
            addclientalert.show();

        });
        //users.put("image_url",user.getPhotoUrl().);
    }
}