package com.sourav.ledgerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import profile.addclient.SelectAndAddClientActivity;
import profile.addledger.ListOfAllClients;
import profile.deletevoucher.activities.ListofClientForDeleteActivity;
import profile.upload.PdfUploadActivity;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceLayoutId());
    }

    protected abstract void setUpBottomNavigation(BottomNavigationView bottomNavigationView);

    protected abstract int getResourceLayoutId();

    protected abstract void showLedgerWithVoucher();

    protected abstract void makeVoucher();

    protected abstract void makeLedger();

    protected abstract void uploadBill();

    protected abstract void deleteVoucher();


}