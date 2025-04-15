package com.example.smartspend;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AppPreferences {
    // Общие настройки
    private static final String PREFS_NAME = "SmartSpendPrefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_PURPOSE = "purpose";
    private static final String KEY_SUM = "sum";
    private static final String KEY_TERM = "term";

    // Финансовые данные
    private static final String BALANCE_KEY = "balance";
    private static final String INCOME_KEY = "income";
    private static final String EXPENSES_KEY = "expenses";
    private static final String TRANSACTIONS_KEY = "transactions";

    private SharedPreferences sharedPreferences;
    private Gson gson;

    public AppPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    // Методы для личных данных
    public void saveName(String name) {
        sharedPreferences.edit().putString(KEY_NAME, name).apply();
    }

    public String getName() {
        return sharedPreferences.getString(KEY_NAME, "");
    }

    public void savePurpose(String purpose) {
        sharedPreferences.edit().putString(KEY_PURPOSE, purpose).apply();
    }

    public String getPurpose() {
        return sharedPreferences.getString(KEY_PURPOSE, "");
    }

    public void saveSum(String sum) {
        sharedPreferences.edit().putString(KEY_SUM, sum).apply();
    }

    public String getSum() {
        return sharedPreferences.getString(KEY_SUM, "");
    }

    public void saveTerm(String term) {
        sharedPreferences.edit().putString(KEY_TERM, term).apply();
    }

    public String getTerm() {
        return sharedPreferences.getString(KEY_TERM, "");
    }

    public boolean isAllDataComplete() {
        return !getName().isEmpty() &&
                !getPurpose().isEmpty() &&
                !getSum().isEmpty() &&
                !getTerm().isEmpty();
    }

    // Методы для финансовых данных
    public float getBalance() {
        return sharedPreferences.getFloat(BALANCE_KEY, 0);
    }

    public void setBalance(float balance) {
        sharedPreferences.edit().putFloat(BALANCE_KEY, balance).apply();
    }

    public float getIncome() {
        return sharedPreferences.getFloat(INCOME_KEY, 0);
    }

    public void setIncome(float income) {
        sharedPreferences.edit().putFloat(INCOME_KEY, income).apply();
    }

    public float getExpenses() {
        return sharedPreferences.getFloat(EXPENSES_KEY, 0);
    }

    public void setExpenses(float expenses) {
        sharedPreferences.edit().putFloat(EXPENSES_KEY, expenses).apply();
    }

    public List<Transaction> getTransactions() {
        String json = sharedPreferences.getString(TRANSACTIONS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Transaction>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void addTransaction(Transaction transaction) {
        List<Transaction> transactions = getTransactions();
        transactions.add(transaction);
        sharedPreferences.edit().putString(TRANSACTIONS_KEY, gson.toJson(transactions)).apply();
    }
}