package com.example.savingscheckapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView tvDisplayBalance;
    private TextView tvDisplayName;
    private Button btnDeposit, btnWithdraw;
    private SharedPrefrences storage;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        index = getIntent().getIntExtra("INDEX", -1);

        storage = new SharedPrefrences(this);

        tvDisplayBalance = findViewById(R.id.tvDisplayBalance);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnWithdraw = findViewById(R.id.btnWithdraw);

        btnDeposit.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, AlertActivity.class);
            intent.putExtra("ACTION", "Deposit");
            intent.putExtra("INDEX", index);
            startActivity(intent);
        });

        btnWithdraw.setOnClickListener(view -> {
            Intent intent = new Intent(DetailActivity.this, AlertActivity.class);
            intent.putExtra("ACTION", "Withdraw");
            intent.putExtra("INDEX", index);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        List<SavingsItem> list = storage.loadSavings();

        if (index == -1 || list == null || index >= list.size()) {
            return;
        }

        SavingsItem currentItem = list.get(index);

        if (tvDisplayName != null) {
            tvDisplayName.setText(currentItem.getName());
        }
        if (tvDisplayBalance != null) {
            tvDisplayBalance.setText(currentItem.getBalance() + " NIS");
        }
    }
}