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

public class SumActivity extends AppCompatActivity {
    private EditText sumInput;
    private Button answerButton;
    private ImageButton backButton, nextButton;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sum_window);

        appPreferences = new AppPreferences(this);

        sumInput = findViewById(R.id.sum_Input);
        answerButton = findViewById(R.id.answerButton);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        // Загружаем сохраненную сумму, если она есть
        String savedSum = appPreferences.getSum();
        if (!savedSum.isEmpty()) {
            sumInput.setText(savedSum);
            answerButton.setEnabled(true);
            nextButton.setEnabled(true);
        }

        // Устанавливаем inputType для ввода только цифр
        sumInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

        sumInput.addTextChangedListener(new TextWatcher() {
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
            String sum = sumInput.getText().toString().trim();

            if (sum.isEmpty()) {
                sumInput.setError("Пожалуйста, введите сумму");
                sumInput.requestFocus();
                return;
            }

            appPreferences.saveSum(sum);
            startActivity(new Intent(SumActivity.this, TermActivity.class));
        });

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        nextButton.setOnClickListener(v -> {
            String sum = sumInput.getText().toString().trim();
            if (!sum.isEmpty()) {
                appPreferences.saveSum(sum);
                startActivity(new Intent(SumActivity.this, TermActivity.class));
            }
        });
    }
}