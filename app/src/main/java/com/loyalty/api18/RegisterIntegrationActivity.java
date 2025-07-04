package com.loyalty.api18;


import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v3.order.Order;
import com.clover.sdk.v3.order.OrderConnector;
import com.clover.sdk.util.CloverAuth;

public class RegisterIntegrationActivity extends Activity {
    private static final String TAG = "RegisterIntegration";
    private Account cloverAccount;
    private OrderConnector orderConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Register Integration Activity started");

        // Initialize Clover account
        cloverAccount = CloverAccount.getAccount(this);

        if (cloverAccount == null) {
            Toast.makeText(this, "No Clover account found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Connect to Clover services
        connect();

        // Handle the button click - open your custom functionality
        openCustomOrderActivity();
    }

    private void connect() {
        if (cloverAccount != null) {
            orderConnector = new OrderConnector(this, cloverAccount, null);
            orderConnector.connect();
        }
    }

    private void openCustomOrderActivity() {
        Intent intent = new Intent(this, CustomOrderActivity.class);

        // Pass any data from Register if needed
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            intent.putExtras(extras);
        }

        startActivity(intent);

        // Close this activity to return to Register
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderConnector != null) {
            orderConnector.disconnect();
        }
    }
}
