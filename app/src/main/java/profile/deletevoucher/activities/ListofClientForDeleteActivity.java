package profile.deletevoucher.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sourav.ledgerproject.R;

import profile.profilefragments.deletevoucher.ListofClientForDeleteFragment;

public class ListofClientForDeleteActivity extends AppCompatActivity {

    private ListofClientForDeleteFragment listofClientForDeleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_client_for_delete);

        listofClientForDeleteFragment = ListofClientForDeleteFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.listofclientfordelete,listofClientForDeleteFragment).commit();
    }
}