package com.example.savingscheckapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SavingsItem extends AppCompatActivity {
    private String name;
    private double balance;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_savings_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public SavingsItem(String name, double balance, String imageUri) {
        this.name = name;
        this.balance = balance;
        this.imageUri = imageUri;
    }

    public String getName() { return name; }
    public double getBalance() { return balance; }
    public String getImageUri() { return imageUri; }
    public void setBalance(double balance) { this.balance = balance; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
    public SavingsItem() {
    }
}