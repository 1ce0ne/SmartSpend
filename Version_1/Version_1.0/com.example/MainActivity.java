package com.example.smartspend;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private AppPreferences prefs;
    private TextView balanceAmount, expensesAmount, incomeAmount;

    // Категории для доходов и расходов
    private final List<String> incomeCategories = Arrays.asList(
            "Зарплата", "Подарок", "Инвестиции", "Фриланс", "Другое");
    private final List<String> expenseCategories = Arrays.asList(
            "Еда", "Транспорт", "Жилье", "Развлечения", "Одежда", "Другое");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = new AppPreferences(this);
        balanceAmount = findViewById(R.id.balanceAmount);
        expensesAmount = findViewById(R.id.expensesAmount);
        incomeAmount = findViewById(R.id.incomeAmount);

        // Устанавливаем имя пользователя
        TextView helloText = findViewById(R.id.helloText);
        String userName = getIntent().getStringExtra("USER_NAME");
        if (userName == null || userName.isEmpty()) {
            userName = prefs.getName();
        }
        helloText.setText("Привет, " + userName);

        // Устанавливаем шрифт Inter
        try {
            Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/inter.ttf");
            helloText.setTypeface(typeface);
            TextView purposeText = findViewById(R.id.purposeText);
            purposeText.setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateUI();

        // Обработчики нажатий на кнопки добавления доходов/расходов
        ImageView addIncomeBtn = findViewById(R.id.imageView3);
        ImageView addExpenseBtn = findViewById(R.id.imageView4);
        TextView historyTitle = findViewById(R.id.historyTitle);

        addIncomeBtn.setOnClickListener(v -> showTransactionDialog(true));
        addExpenseBtn.setOnClickListener(v -> showTransactionDialog(false));
        historyTitle.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void updateUI() {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("ru", "RU"));
        balanceAmount.setText(format.format(prefs.getBalance()));
        incomeAmount.setText(format.format(prefs.getIncome()));
        expensesAmount.setText(format.format(prefs.getExpenses()));
    }

    private void showTransactionDialog(boolean isIncome) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isIncome ? "Добавить доход" : "Добавить расход");

        View view = getLayoutInflater().inflate(R.layout.dialog_transaction, null);
        TextInputLayout amountLayout = view.findViewById(R.id.amountLayout);
        TextInputEditText amountInput = view.findViewById(R.id.amountInput);
        TextInputLayout categoryLayout = view.findViewById(R.id.categoryLayout);
        TextInputEditText categoryInput = view.findViewById(R.id.categoryInput);

        // Настройка выпадающего списка категорий
        List<String> categories = isIncome ? incomeCategories : expenseCategories;
        categoryInput.setOnClickListener(v -> {
            String[] items = categories.toArray(new String[0]);
            new AlertDialog.Builder(this)
                    .setTitle("Выберите категорию")
                    .setItems(items, (dialog, which) -> {
                        categoryInput.setText(items[which]);
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        builder.setView(view);
        builder.setPositiveButton("Добавить", (dialog, which) -> {
            try {
                float amount = Float.parseFloat(amountInput.getText().toString());
                String category = categoryInput.getText().toString().trim();

                if (category.isEmpty()) {
                    category = isIncome ? "Другой доход" : "Другой расход";
                }

                Transaction transaction = new Transaction(
                        amount,
                        category,
                        isIncome ? Transaction.Type.INCOME : Transaction.Type.EXPENSE
                );

                prefs.addTransaction(transaction);

                if (isIncome) {
                    prefs.setIncome(prefs.getIncome() + amount);
                    prefs.setBalance(prefs.getBalance() + amount);
                } else {
                    prefs.setExpenses(prefs.getExpenses() + amount);
                    prefs.setBalance(prefs.getBalance() - amount);
                }

                updateUI();
            } catch (NumberFormatException e) {
                amountLayout.setError("Введите корректную сумму");
            }
        });
        builder.setNegativeButton("Отмена", null);

        builder.show();
    }
}
