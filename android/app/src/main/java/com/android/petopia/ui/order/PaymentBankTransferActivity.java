package com.android.petopia.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.petopia.R;

public class PaymentBankTransferActivity extends AppCompatActivity {

    ImageButton ibtBack;
    Button btnComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bank_transfer);
        initUi();
    }

    private void initUi() {
        ibtBack = findViewById(R.id.ibtBack);
        btnComplete = findViewById(R.id.btnComplete);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMyOrder();
            }
        });
    }

    private void gotoMyOrder() {
        Intent intent = new Intent(PaymentBankTransferActivity.this,MyOrderActivity.class);
        startActivity(intent);
    }
}