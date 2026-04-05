package com.example.savingscheckapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DynamicActivity extends AppCompatActivity {
    // 1. Declare variables at the top
    private EditText nameInput;
    private EditText amountInput;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dynamic);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            nameInput = findViewById(R.id.etName);
            amountInput = findViewById(R.id.etAmount);
            btnSave = findViewById(R.id.btnSave);

            btnSave.setOnClickListener(view -> {
            String name = nameInput.getText().toString();
            String amountStr = amountInput.getText().toString();

            if (!name.isEmpty() && !amountStr.isEmpty()) {
                double amount = Double.parseDouble(amountStr);
                // Save the data...
                finish();
            }
            });
            return insets;
        });
    }
}
