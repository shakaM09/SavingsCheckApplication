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

    public void saveSavings(List<SavingsItem> list) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("list_size", list.size());

        for (int i = 0; i < list.size(); i++) {
            SavingsItem item = list.get(i);
            String row = item.getName() + "|" + item.getBalance() + "|" + item.getImageUri();
            editor.putString("item_" + i, row);
        }

        editor.apply();
    }

    public List<SavingsItem> loadSavings() {
        List<SavingsItem> list = new ArrayList<>();
        int size = prefs.getInt("list_size", 0);

        for (int i = 0; i < size; i++) {
            String row = prefs.getString("item_" + i, "");
            if (!row.isEmpty()) {
                String[] parts = row.split("\\|");
                list.add(new SavingsItem(parts[0], Double.parseDouble(parts[1]), parts[2]));
            }
        }
        return list;
    }
    public SharedPrefrences() {
    }
}