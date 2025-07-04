package com.loyalty.api18;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.BindingException;
import com.clover.sdk.v1.ClientException;
import com.clover.sdk.v1.ServiceException;
import com.clover.sdk.v1.merchant.MerchantConnector;
import com.clover.sdk.v3.apps.AppsConnector;
import com.clover.sdk.v3.merchant.Merchant;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MegaStore_MainActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Check if launched from Register app
            setContentView(R.layout.activity_main);


        Button btnOpenCustomOrder = (Button) findViewById(R.id.btn_open_custom_order);
        btnOpenCustomOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomOrderActivity.class);
                startActivity(intent);
            }
        });
    }



}