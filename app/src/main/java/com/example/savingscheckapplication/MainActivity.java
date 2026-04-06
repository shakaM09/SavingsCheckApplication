package com.example.savingscheckapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
        storage = new SharedPrefrences(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);


            container = findViewById(R.id.savingsContainer);

            Button btnGo = findViewById(R.id.btnGoToDynamic);
            btnGo.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, DynamicActivity.class);
                startActivity(intent);
            });
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            refreshUI();
            return insets;
        });
    }
        @Override
        protected void onResume() {
            super.onResume();
            refreshUI();
        }

    private void refreshUI() {
        if (storage == null) storage = new SharedPrefrences(this);
        if (container == null) return;

        // This "post" trick ensures the container width is NOT 0
        container.post(() -> {
            container.removeAllViews();
            List<SavingsItem> list = storage.loadSavings();

            // Get the real width of the phone screen
            int targetWidth = container.getWidth();
            if (targetWidth <= 0) targetWidth = getResources().getDisplayMetrics().widthPixels;

            int targetHeight = 450; // Set a fixed height for all buttons

            for (int i = 0; i < list.size(); i++) {
                final int index = i;
                SavingsItem item = list.get(i);

                Button b = new Button(this);
                b.setText(item.getName() + "\n" + item.getBalance() + " NIS");
                b.setTextColor(android.graphics.Color.WHITE);
                b.setTextSize(22);
                b.setAllCaps(false);

                // Layout Params
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, targetHeight);
                params.setMargins(0, 15, 0, 15);
                b.setLayoutParams(params);

                // --- THE IMAGE ENGINE ---
                String encodedImage = item.getImageUri();
                if (encodedImage != null && !encodedImage.isEmpty() && !encodedImage.equals("null")) {
                    try {
                        byte[] decodedString = android.util.Base64.decode(encodedImage, android.util.Base64.DEFAULT);
                        android.graphics.Bitmap raw = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        if (raw != null) {
                            // 1. Calculate how to scale the photo so it fills the button perfectly
                            float scale = Math.max((float) targetWidth / raw.getWidth(), (float) targetHeight / raw.getHeight());
                            int scaledW = Math.round(raw.getWidth() * scale);
                            int scaledH = Math.round(raw.getHeight() * scale);

                            android.graphics.Bitmap scaled = android.graphics.Bitmap.createScaledBitmap(raw, scaledW, scaledH, true);

                            // 2. Crop from the exact center
                            int x = (scaledW - targetWidth) / 2;
                            int y = (scaledH - targetHeight) / 2;
                            android.graphics.Bitmap finalImage = android.graphics.Bitmap.createBitmap(scaled, x, y, targetWidth, targetHeight);

                            android.graphics.drawable.BitmapDrawable drawable = new android.graphics.drawable.BitmapDrawable(getResources(), finalImage);

                            // 3. Darken for white text visibility
                            drawable.setColorFilter(android.graphics.Color.argb(150, 0, 0, 0), android.graphics.PorterDuff.Mode.SRC_ATOP);

                            b.setBackground(drawable);
                        }
                    } catch (Exception e) {
                        b.setBackgroundColor(android.graphics.Color.DKGRAY);
                    }
                } else {
                    b.setBackgroundColor(android.graphics.Color.parseColor("#4A148C"));
                }

                b.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("INDEX", index);
                    startActivity(intent);
                });

                container.addView(b);
            }
        });
    }
}
