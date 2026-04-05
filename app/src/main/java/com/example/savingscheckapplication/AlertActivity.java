package com.example.savingscheckapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class AlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            final String action = getIntent().getStringExtra("ACTION");
            final int index = getIntent().getIntExtra("INDEX", -1);
            final EditText etMoney = findViewById(R.id.etMoney);

            Button confirmButton = findViewById(R.id.btnConfirmTransaction);

            confirmButton.setOnClickListener(view -> {
                final String amountStr = etMoney.getText().toString();

                if (amountStr.isEmpty()) {
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you want to " + action + " " + amountStr + " NIS?");

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    SharedPrefrences sp = new SharedPrefrences(this);
                    List<SavingsItem> list = sp.loadSavings();

                    double currentBalance = list.get(index).getBalance();
                    double amountToChange = Double.parseDouble(amountStr);

                    if (action.equals("Deposit")) {
                        list.get(index).setBalance(currentBalance + amountToChange);
                    } else {
                        list.get(index).setBalance(currentBalance - amountToChange);
                    }

                    sp.saveSavings(list);
                    finish();
                });

                builder.setNegativeButton("No", null);
                builder.show();
            });
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            return insets;
           });
       }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        else if (id == R.id.menu_detail) {
            // Just calling finish() takes them back to the previous screen (Detail)
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}