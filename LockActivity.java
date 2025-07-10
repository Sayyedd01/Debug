package com.example.digitalshield; // Aapke package ka naam yahan aayega

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class LockActivity extends AppCompatActivity {

    private TextView lockTimerTextView;
    private static final long LOCK_DURATION = 3 * 60 * 1000; // 3 minute

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        lockTimerTextView = findViewById(R.id.lock_timer);

        new CountDownTimer(LOCK_DURATION, 1000) {
            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000) / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                lockTimerTextView.setText("Device " + timeLeftFormatted + " me unlock hoga");
            }

            public void onFinish() {
                // Timer khatam hone par, activity band kar dein
                finish();
            }
        }.start();
    }

    // Back button ko disable kar dein taaki user lock screen se bahar na ja sake
    @Override
    public void onBackPressed() {
        // Kuch na karein
    }
}
