package com.example.savingscheckapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class SavingsItem {
    private String name;
    private double balance;
    private String imageUri;

    public SavingsItem(String name, double balance, String imageUri) {
        this.name = name;
        this.balance = balance;
        this.imageUri = imageUri;
    }

    public SavingsItem() {}

    public String getName() { return name; }
    public double getBalance() { return balance; }
    public String getImageUri() { return imageUri; }
    public void setBalance(double balance) { this.balance = balance; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}