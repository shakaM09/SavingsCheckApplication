package com.example.savingscheckapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private Button btnDeposit, btnWithdraw, btnDelete;
    private SharedPrefrences storage;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            List<SavingsItem> list = storage.loadSavings();

            if (index != -1 && list != null && index < list.size()) {
                list.remove(index);
                storage.saveSavings(list);
                android.widget.Toast.makeText(this, "Saving Deleted", android.widget.Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }

        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_detail).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_main) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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