package com.example.smartspend;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermActivity extends AppCompatActivity {
    private EditText termInput;
    private Button answerButton;
    private ImageButton backButton, nextButton;
    private AppPreferences appPreferences;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.term_window);

        appPreferences = new AppPreferences(this);
        calendar = Calendar.getInstance();

        termInput = findViewById(R.id.sum_Input); // Обратите внимание, что здесь используется sum_Input (как в XML)
        answerButton = findViewById(R.id.answerButton);
        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);

        // Загружаем сохраненный срок, если он есть
        String savedTerm = appPreferences.getTerm();
        if (!savedTerm.isEmpty()) {
            termInput.setText(savedTerm);
            answerButton.setEnabled(true);
            nextButton.setEnabled(true);
        }

        // Сделаем поле ввода нередактируемым, чтобы пользователь мог выбрать дату только через диалог
        termInput.setFocusable(false);
        termInput.setOnClickListener(v -> showDatePickerDialog());

        termInput.addTextChangedListener(new TextWatcher() {
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
            String term = termInput.getText().toString().trim();
            if (term.isEmpty()) {
                termInput.setError("Пожалуйста, выберите дату");
                return;
            }

            appPreferences.saveTerm(term);
            startActivity(new Intent(TermActivity.this, LoadingActivity.class));
            finish();
        });

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });

        nextButton.setOnClickListener(v -> {
            String term = termInput.getText().toString().trim();
            if (!term.isEmpty()) {
                appPreferences.saveTerm(term);
                // Здесь можно перейти к следующему экрану или завершить процесс
                // startActivity(new Intent(TermActivity.this, NextActivity.class));
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    termInput.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Установим минимальную дату - сегодня
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
}