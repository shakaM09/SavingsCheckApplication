package com.example.savingscheckapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout container;
    private SharedPrefrences storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            storage = new SharedPrefrences(this);

            // 2. Link the XML container second
            container = findViewById(R.id.savingsContainer);

            Button btnGo = findViewById(R.id.btnGoToDynamic);
            btnGo.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, DynamicActivity.class);
                startActivity(intent);
            });
            return insets;
        });
    }
        @Override
        protected void onResume() {
            super.onResume();
            // This runs every time you return to this screen
            refreshUI();
        }

        private void refreshUI() {
            // SAFETY: If container is null, stop here so we don't crash
            if (container == null) return;

            container.removeAllViews();

            List<SavingsItem> list = storage.loadSavings();

            // LOG: Check the bottom of Android Studio (Logcat) for "DEBUG_APP"
            android.util.Log.d("DEBUG_APP", "List size: " + list.size());

            for (int i = 0; i < list.size(); i++) {
                final int index = i;
                Button b = new Button(this);
                b.setText(list.get(i).getName());

                b.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("INDEX", index);
                    startActivity(intent);
                });

                container.addView(b);
            }
        }
}
