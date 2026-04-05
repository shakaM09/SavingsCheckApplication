package com.example.savingscheckapplication;

import android.app.AlertDialog;
import android.os.Bundle;
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

            // Changed the variable name 'v' or 'btnDone' to 'confirmButton'
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
            return insets;
           });
       }
}