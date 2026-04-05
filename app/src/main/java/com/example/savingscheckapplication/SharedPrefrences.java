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

public class SharedPrefrences extends AppCompatActivity {
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shared_prefrences);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public SharedPrefrences(Context context) {
        prefs = context.getSharedPreferences("SavingsData", Context.MODE_PRIVATE);
    }

    public List<SavingsItem> loadSavings() {
        List<SavingsItem> list = new ArrayList<>();
        Set<String> dataSet = prefs.getStringSet("list", new HashSet<String>());

        for (String s : dataSet) {
            String[] parts = s.split("\\|");
            // Name | Balance | ImageUri
            list.add(new SavingsItem(parts[0], Double.parseDouble(parts[1]), parts[2]));
        }
        return list;
    }

    public void saveSavings(List<SavingsItem> list) {
        Set<String> dataSet = new HashSet<>();
        for (SavingsItem item : list) {
            String row = item.getName() + "|" + item.getBalance() + "|" + item.getImageUri();
            dataSet.add(row);
        }
        prefs.edit().putStringSet("list", dataSet).apply();
    }
}