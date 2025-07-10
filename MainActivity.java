package com.example.digitalshield; // Aapke package ka naam yahan aayega

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText editTextKeyword;
    private Button buttonAddKeyword, buttonEnableAccessibility, buttonEnableAdmin;
    private TextView textViewKeywords;
    private SharedPreferences prefs;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName compName;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEYWORDS_SET = "keywordsSet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextKeyword = findViewById(R.id.editTextKeyword);
        buttonAddKeyword = findViewById(R.id.buttonAddKeyword);
        textViewKeywords = findViewById(R.id.textViewKeywords);
        buttonEnableAccessibility = findViewById(R.id.buttonEnableAccessibility);
        buttonEnableAdmin = findViewById(R.id.buttonEnableAdmin);

        prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        compName = new ComponentName(this, DeviceAdmin.class);

        updateKeywordsList();

        buttonAddKeyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = editTextKeyword.getText().toString().trim().toLowerCase();
                if (!keyword.isEmpty()) {
                    Set<String> keywords = new HashSet<>(prefs.getStringSet(KEYWORDS_SET, new HashSet<>()));
                    keywords.add(keyword);
                    prefs.edit().putStringSet(KEYWORDS_SET, keywords).apply();
                    editTextKeyword.setText("");
                    updateKeywordsList();
                    Toast.makeText(MainActivity.this, "Keyword jod diya gaya!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonEnableAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User ko Accessibility settings me bhej rahe hain
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
                Toast.makeText(v.getContext(), "List me se 'Digital Shield' dhoondein aur On karein", Toast.LENGTH_LONG).show();
            }
        });

        buttonEnableAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // User se Device Admin permission maang rahe hain
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Uninstall protection ke liye isko activate karna zaroori hai.");
                startActivity(intent);
            }
        });
    }

    private void updateKeywordsList() {
        Set<String> keywords = prefs.getStringSet(KEYWORDS_SET, new HashSet<>());
        StringBuilder sb = new StringBuilder();
        for (String s : keywords) {
            sb.append(s).append("\n");
        }
        textViewKeywords.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check karein ki admin active hai ya nahi
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        if (isActive) {
            buttonEnableAdmin.setText("Uninstall Protection Active Hai");
            buttonEnableAdmin.setEnabled(false);
        }
    }
}
