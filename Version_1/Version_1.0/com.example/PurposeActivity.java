package com.example.smartspend;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class PurposeActivity extends AppCompatActivity {
    private EditText purposeInput;
    private Button answerButton;
    private ImageButton backButton, nextButton;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purpose_window);

        appPreferences = new AppPreferences(this);

        purposeInput = findViewById(R.id.purpose_Input);
        answerButton = findViewById(R.id.answerButton);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        // Загружаем сохраненную цель, если она есть
        String savedPurpose = appPreferences.getPurpose();
        if (!savedPurpose.isEmpty()) {
            purposeInput.setText(savedPurpose);
            answerButton.setEnabled(true);
            nextButton.setEnabled(true);
        }

        purposeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasText = s.toString().trim().length() > 0;
                answerButton.setEnabled(hasText);
                nextButton.setEnabled(hasText);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        answerButton.setOnClickListener(v -> {
            String purpose = purposeInput.getText().toString().trim();

            if (purpose.isEmpty()) {
                purposeInput.setError("Пожалуйста, введите вашу цель");
                purposeInput.requestFocus();
                return;
            }

            appPreferences.savePurpose(purpose);
            startActivity(new Intent(PurposeActivity.this, SumActivity.class));
        });

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        nextButton.setOnClickListener(v -> {
            String purpose = purposeInput.getText().toString().trim();
            if (!purpose.isEmpty()) {
                appPreferences.savePurpose(purpose);
                startActivity(new Intent(PurposeActivity.this, SumActivity.class));
            }
        });
    }
}