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

public class GreetingActivity extends AppCompatActivity {
    private EditText nameInput;
    private Button answerButton;
    private ImageButton nextButton;
    private AppPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greeting_window);

        appPreferences = new AppPreferences(this);

        nameInput = findViewById(R.id.nameInput);
        answerButton = findViewById(R.id.answerButton);
        nextButton = findViewById(R.id.nextButton);

        // Загружаем сохраненное имя, если оно есть
        String savedName = appPreferences.getName();
        if (!savedName.isEmpty()) {
            nameInput.setText(savedName);
            answerButton.setEnabled(true);
            nextButton.setEnabled(true);
        }

        nameInput.addTextChangedListener(new TextWatcher() {
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
            String name = nameInput.getText().toString().trim();

            if (name.isEmpty()) {
                nameInput.setError("Пожалуйста, введите ваше имя");
                nameInput.requestFocus(); // Фокусируемся на поле ввода
                return; // Прерываем выполнение
            }

            appPreferences.saveName(name);
            startActivity(new Intent(GreetingActivity.this, PurposeActivity.class));
        });

        nextButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            if (!name.isEmpty()) {
                appPreferences.saveName(name);
                startActivity(new Intent(GreetingActivity.this, PurposeActivity.class));
            }
        });
    }
}