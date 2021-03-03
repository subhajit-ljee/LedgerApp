package profile.addvoucher.allclientsforvoucher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sourav.ledgerproject.LedgerApplication;
import com.sourav.ledgerproject.R;

//import com.sourav.ledgerproject.profile.model.Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import profile.profilefragments.ledger.ClientListFragment;
import profile.profilefragments.voucher.ClientListForVoucherFragment;
import profile.profilefragments.voucher.LedgerHolderFragment;

public class ListOfAllClients extends AppCompatActivity {

    private static final int MY_REQUEST_CODE_PERMISSION = 1000;
    private static final int MY_RESULT_CODE_FILECHOOSER = 2000;

    private static final String TAG = "ListOfAllClients";

    Toolbar listItemToolbar;
    private ClientListForVoucherFragment clientListForVoucherFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_all_clients);

        clientListForVoucherFragment = ClientListForVoucherFragment.newInstance(TAG);
        getSupportFragmentManager().beginTransaction().replace(R.id.ledger_holder_list_frag,clientListForVoucherFragment).commit();

        listItemToolbar = findViewById(R.id.allvoucherlistpagetoolbar);
        setSupportActionBar(listItemToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bill_save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem){

        switch (menuItem.getItemId()){
            case R.id.bill_upload_item:
                //uploadBill();
            case R.id.bill_save_item:
                //saveBill();
        }
        return true;
    }

    /*private void uploadBill(){

        ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    ,PackageManager.PERMISSION_GRANTED
                );

        Log.d(TAG,"calling upload bill method");
        doBrowseFile();
    }

    private void saveBill(){
        String filepath = Environment.getExternalStorageDirectory().toString()+"/newfile.pdf";
        //writeData(filepath);
    }

    private void doBrowseFile()  {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("application/pdf");
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);

        switch (requestCode) {
            case MY_REQUEST_CODE_PERMISSION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG,"Permission granted!");
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                    this.doBrowseFile();
                }
                else {
                    Log.i(TAG,"Permission denied!");
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        switch (requestCode) {
            case MY_RESULT_CODE_FILECHOOSER:
                if (resultCode == Activity.RESULT_OK ) {
                    if(data != null)  {
                        Uri fileUri = data.getData();
                        Log.i(TAG, "Uri: " + fileUri);

                        String filePath = null;
                        try {
                            filePath = BillFileUtils.getPath(this,fileUri);
                            if(isExternalStorageAvailable() || isExternalStorageReadable()){
                                readData(filePath);
                                //writeData(filePath,filedata);
                            }else{
                                Log.d(TAG,"isStorageReadable() : "+isExternalStorageReadable()+",isStorageAvailable() : "+isExternalStorageAvailable());
                            }

                        } catch (Exception e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG,"File name: "+filePath);
                    }
                }
                break;
        }
        

    }

    private void readData(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            //InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

           // FileOutputStream outputStream = new FileOutputStream(new File("/storage/emulated/0/Download/mydownload.pdf"));

            PrintBill printBill = new PrintBill(filePath,fileInputStream);
            printBill.makeBillPdf();

            /*int length;
            byte[] filebyte = new byte[40*1024*1024];
            int i=0;
            while ((length = fileInputStream.read(filebyte)) > 0) {
                outputStream.write(filebyte,0,length);
            }
            //stringBuilder.append("my name is subhajit");
            //Files.copy(Paths.get(filePath),outputStream);


            Log.d(TAG,"file data is: "+filebyte.toString());
            fileInputStream.close();
            outputStream.close();
           // return stringBuilder.toString();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //return null;

    }
    */
    /*public void writeData(String filepath, String filedata){

            try{

            }catch (FileNotFoundException fe){
                fe.printStackTrace();
            }catch (IOException io){
                io.printStackTrace();
            }

    }*/
/*
    public boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }


    public String getStorageDir(String fileName) {
        //create folder
        File file = new File(Environment.getExternalStorageDirectory().toString());
        if (!file.mkdirs()) {
            file.mkdirs();
        }
        String filePath = file.getAbsolutePath() + File.separator + fileName;
        return filePath;
    }
    //public String getPath()  {
      //  return this.editTextPath.getText().toString();
    //}

  */
}