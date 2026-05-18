package com.example.savingscheckapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefrences {
    private SharedPreferences prefs;

    public SharedPrefrences(Context context) {
        prefs = context.getSharedPreferences("SavingsData", Context.MODE_PRIVATE);
    }

    public SharedPrefrences() {
    }

    public void saveSavings(List<SavingsItem> list) {
        if (prefs == null) return;

        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

        editor.putInt("list_size", list.size());

        for (int i = 0; i < list.size(); i++) {
            SavingsItem item = list.get(i);
            String imageUri = (item.getImageUri() != null) ? item.getImageUri() : "";
            String row = item.getName() + "|" + item.getBalance() + "|" + imageUri;
            editor.putString("item_" + i, row);
        }

        editor.apply();
    }

    public List<SavingsItem> loadSavings() {
        List<SavingsItem> list = new ArrayList<>();
        if (prefs == null) return list;

        int size = prefs.getInt("list_size", 0);

        for (int i = 0; i < size; i++) {
            String row = prefs.getString("item_" + i, "");
            if (!row.isEmpty()) {
                String[] parts = row.split("\\|", -1);
                if (parts.length >= 2) {
                    String name = parts[0];

                    double balance = 0.0;
                    try {
                        balance = Double.parseDouble(parts[1]);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    String imageUri = (parts.length > 2) ? parts[2] : "";

                    list.add(new SavingsItem(name, balance, imageUri));
                }
            }
        }
        return list;
    }
}