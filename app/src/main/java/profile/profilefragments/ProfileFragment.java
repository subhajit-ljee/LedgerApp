package profile.profilefragments;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sourav.ledgerproject.R;

import profile.addclient.SelectAndAddClientActivity;
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

    private Button debitView, creditView, copyid;
    private BottomNavigationView bottomNavigationView;

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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        name = view.findViewById(R.id.registered_user_name);
        //email = view.findViewById(R.id.registered_user_email);
        //id = view.findViewById(R.id.registered_user_id);
        imageView = view.findViewById(R.id.user_image_registered);
        copyid = view.findViewById(R.id.copy_id);
        bottomNavigationView = view.findViewById(R.id.profile_menu_nav_view);

        setUpBottomNavigation(bottomNavigationView);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String useremail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        name.setText(username);


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

                Intent intent = new Intent(getActivity(), SaveUserJobService.class);
                intent.putExtra("userid", userid);
                intent.putExtra("useremail", useremail);
                intent.putExtra("username", username);
                intent.putExtra("messaging_token", messaging_token);

                SaveUserJobService.enqueueWork(getActivity(), intent);

            }
        });

        Glide.with(this)
                .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                .into(imageView);

        name.setText(username);
        //email.setText(useremail);

        debitView = view.findViewById(R.id.debit_view);
        creditView = view.findViewById(R.id.credit_view);

        debitView.setOnClickListener( v -> new Handler().post( () -> startActivity(new Intent(getActivity(), DebitListActivity.class))));

        creditView.setOnClickListener( v -> new Handler().post( () -> startActivity(new Intent(getActivity(), CreditListActivity.class))));

        //users.put("image_url",user.getPhotoUrl().);

        return view;
    }

    public void setUpBottomNavigation(BottomNavigationView bottomNavigationView){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            selectDrawerItem(item);
            return true;
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void selectDrawerItem(MenuItem item) {

        switch (item.getItemId()){
            case R.id.addnewledger:
                makeLedger();
                break;
            case R.id.addnewvoucher:
                makeVoucher();
                break;
            case R.id.deletevoucher:
                deleteVoucher();
                break;
            case R.id.showledger:
                showLedgerWithVoucher();
                break;
            case R.id.uploadbill:
                uploadBill();
                break;
        }

        item.setChecked(true);
    }

    private void showLedgerWithVoucher() {
        new Handler().post( () -> startActivity(new Intent(getActivity(), ListOfAllClients.class)));

    }

    private void makeVoucher() {
        new Handler().post( () -> startActivity(new Intent(getActivity(), ListOfAllClients.class)));
    }

    private void makeLedger() {
        new Handler().post( () -> startActivity(new Intent(getActivity(), SelectAndAddClientActivity.class)));
    }

    private void uploadBill(){
        new Handler().post( () -> startActivity(new Intent(getActivity(), PdfUploadActivity.class)));
    }

    public void deleteVoucher(){
        new Handler().post( () -> startActivity(new Intent(getActivity(), ListofClientForDeleteActivity.class)));
    }

}