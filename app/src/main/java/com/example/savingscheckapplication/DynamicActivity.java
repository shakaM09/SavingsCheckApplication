package com.example.savingscheckapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class DynamicActivity extends AppCompatActivity {
    // 1. Declare variables at the top
    private EditText nameInput;
    private EditText amountInput;
    private Button btnSave,btnLaunchCamera;
    private ImageView ivCameraPreview;
    private Bitmap capturedBitmap;
    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    try {
                        android.graphics.Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        capturedBitmap = bitmap;
                        ivCameraPreview.setImageBitmap(capturedBitmap);
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

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
                SharedPrefrences sp = new SharedPrefrences(DynamicActivity.this);
                List<SavingsItem> list = sp.loadSavings();


                double amount = Double.parseDouble(amountStr);
                String imageString = bitmapToString(capturedBitmap);

                list.add(new SavingsItem(name, amount, imageString));

                sp.saveSavings(list);
                android.widget.Toast.makeText(this, "Saved! Total: " + list.size(), android.widget.Toast.LENGTH_SHORT).show();
                finish();
            }
            });
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
            ivCameraPreview = findViewById(R.id.ivCameraPreview);
            btnLaunchCamera = findViewById(R.id.btnLaunchCamera);

            btnLaunchCamera.setText("Pick a picture");
            btnLaunchCamera.setOnClickListener(view -> {
                galleryLauncher.launch("image/*");
            });
            return insets;
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.menu_detail).setVisible(false);
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
        return super.onOptionsItemSelected(item);
    }

    private String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) return "";

        // 1. Shrink the actual Bitmap size first (400x400 is plenty for a button)
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 2. Compress the quality to 50% to make the String even smaller
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

        byte[] b = baos.toByteArray();
        return android.util.Base64.encodeToString(b, android.util.Base64.DEFAULT);
    }
}
