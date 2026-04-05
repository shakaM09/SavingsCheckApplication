package com.example.savingscheckapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private int savingIndex;
    private SharedPrefrences storage;
    private TextView tvDisplayBalance;
    private ImageView ivBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            // 1. Get the index sent from MainActivity
            savingIndex = getIntent().getIntExtra("INDEX", 0);
            storage = new SharedPrefrences(this);

            tvDisplayBalance = findViewById(R.id.tvDisplayBalance);
            ivBackground = findViewById(R.id.ivBackground);

            // 2. Show the data
            updateUI();

            // 3. Set up the buttons to go to AlertActivity
            findViewById(R.id.btnDeposit).setOnClickListener(view -> {
                startTransaction("Deposit");
            });

            findViewById(R.id.btnWithdraw).setOnClickListener(view -> {
                startTransaction("Withdraw");
            });
            return insets;
        });
    }
    private void updateUI() {
        List<SavingsItem> list = storage.loadSavings();
        SavingsItem currentItem = list.get(savingIndex);

        tvDisplayBalance.setText(currentItem.getBalance() + " NIS");

        if (!currentItem.getImageUri().equals("null")) {
            ivBackground.setImageURI(Uri.parse(currentItem.getImageUri()));
        }
    }

    private void startTransaction(String actionType) {
        Intent intent = new Intent(this, AlertActivity.class);
        intent.putExtra("ACTION", actionType);
        intent.putExtra("INDEX", savingIndex);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the balance when returning from AlertActivity
        updateUI();
    }
}