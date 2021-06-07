package profile.profilefragments;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;

import profile.addclient.SelectAndAddClientActivity;
import profile.addclient.jobintentservices.SelectAndAddClientService;
import profile.addusers.SaveUserJobService;

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
    private ConstraintLayout see_debit, see_credit, add_client_view, see_client;

    BroadcastReceiver bReceiver;

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
        return new ProfileFragment();
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
        see_client = view.findViewById(R.id.see_client);

        NavController findNavController = Navigation.findNavController(requireActivity(),R.id.pro_main_fragment);

        see_debit.setOnClickListener( v -> new Handler().post( () -> findNavController.navigate(R.id.action_profileFragment_to_debitListFragment)));

        see_credit.setOnClickListener( v -> new Handler().post( () -> findNavController.navigate(R.id.action_profileFragment_to_creditListFragment)));

        see_client.setOnClickListener( v -> new Handler().post( () -> findNavController.navigate(R.id.action_profileFragment_to_myLedgerMainFragment)));

        add_client_view.setOnClickListener( v -> {
            View view1 = getLayoutInflater().inflate(R.layout.add_clientsid_layout,null);
            EditText clientid = view1.findViewById(R.id.add_client_id_edit);
            new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Add Client ID To Make Ledgers")
                    .setView(view1)
                    .setPositiveButton("Add",(dialog, which) -> {
                        Log.d(TAG, "onViewCreated: dialog, which: " + clientid.getText().toString());
                        Intent intent = new Intent(getActivity(),SelectAndAddClientService.class);
                        intent.putExtra("clientid",clientid.getText().toString());
                        SelectAndAddClientService.enqueueWork(getContext(),intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

        });

        bReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "onReceive: " + intent.getStringExtra("error"));
                String error = intent.getStringExtra("error");
                try {
                    assert error != null;
                    if (error.equals("no")) {
                        Log.d(TAG, "onReceive: true");
                        new MaterialAlertDialogBuilder(requireContext())
                                .setMessage("Client Added Successfully")
                                .setPositiveButton("go to Ledger Page", (dialog, which) -> startActivity(new Intent(getContext(), SelectAndAddClientActivity.class)))
                                .setNegativeButton("cancel", null)
                                .create()
                                .show();
                    } else {
                        new MaterialAlertDialogBuilder(requireContext())
                                .setMessage(error + "!")
                                .setPositiveButton("ok", null)
                                .create()
                                .show();
                    }
                } catch (NullPointerException e){
                    Log.e(TAG, "onReceive: ", e);
                }
            }
        };

        //users.put("image_url",user.getPhotoUrl().);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(bReceiver, new IntentFilter("errorMessage"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(bReceiver);
    }


}