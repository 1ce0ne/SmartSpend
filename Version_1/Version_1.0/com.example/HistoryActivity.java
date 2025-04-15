package com.example.smartspend;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    private PieChart pieChart;
    private TextView chartTitle;
    private ImageView nextChartBtn;

    private enum ChartType { BALANCE, INCOME, EXPENSE }
    private ChartType currentChartType = ChartType.BALANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        pieChart = findViewById(R.id.pieChart);
        chartTitle = findViewById(R.id.chartTitle);
        nextChartBtn = findViewById(R.id.nextChartBtn);

        setupPieChart();
        updateChart();

        nextChartBtn.setOnClickListener(v -> {
            switch (currentChartType) {
                case BALANCE:
                    currentChartType = ChartType.INCOME;
                    break;
                case INCOME:
                    currentChartType = ChartType.EXPENSE;
                    break;
                case EXPENSE:
                    currentChartType = ChartType.BALANCE;
                    break;
            }
            updateChart();
        });
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(false); // Отключаем использование процентов
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
    }

    private void updateChart() {
        AppPreferences prefs = new AppPreferences(this);
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();
        boolean hasData = false;

        switch (currentChartType) {
            case BALANCE:
                chartTitle.setText("Соотношение доходов и расходов");
                if (prefs.getIncome() > 0 || prefs.getExpenses() > 0) {
                    hasData = true;
                    if (prefs.getIncome() > 0) {
                        entries.add(new PieEntry(prefs.getIncome(), "Доходы")); // Сумма доходов
                        colors.add(Color.GREEN);
                    }
                    if (prefs.getExpenses() > 0) {
                        entries.add(new PieEntry(prefs.getExpenses(), "Расходы")); // Сумма расходов
                        colors.add(Color.RED);
                    }
                }
                break;

            case INCOME:
                chartTitle.setText("Доходы по категориям");
                Map<String, Float> incomeByCategory = getTransactionsByCategory(Transaction.Type.INCOME);
                if (!incomeByCategory.isEmpty()) {
                    hasData = true;
                    for (Map.Entry<String, Float> entry : incomeByCategory.entrySet()) {
                        if (entry.getValue() > 0) {
                            entries.add(new PieEntry(entry.getValue(), entry.getKey())); // Сумма по категориям доходов
                            colors.add(ColorTemplate.MATERIAL_COLORS[entries.size() % ColorTemplate.MATERIAL_COLORS.length]);
                        }
                    }
                }
                break;

            case EXPENSE:
                chartTitle.setText("Расходы по категориям");
                Map<String, Float> expenseByCategory = getTransactionsByCategory(Transaction.Type.EXPENSE);
                if (!expenseByCategory.isEmpty()) {
                    hasData = true;
                    for (Map.Entry<String, Float> entry : expenseByCategory.entrySet()) {
                        if (entry.getValue() > 0) {
                            entries.add(new PieEntry(entry.getValue(), entry.getKey())); // Сумма по категориям расходов
                            colors.add(ColorTemplate.MATERIAL_COLORS[entries.size() % ColorTemplate.MATERIAL_COLORS.length]);
                        }
                    }
                }
                break;
        }

        if (!hasData) {
            entries.add(new PieEntry(1f, "Нет данных"));
            colors.add(Color.GRAY);
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.WHITE);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate();
    }

    private Map<String, Float> getTransactionsByCategory(Transaction.Type type) {
        AppPreferences prefs = new AppPreferences(this);
        Map<String, Float> result = new HashMap<>();

        for (Transaction transaction : prefs.getTransactions()) {
            if (transaction.getType() == type) {
                String category = transaction.getCategory();
                float amount = transaction.getAmount();
                result.put(category, result.containsKey(category) ? result.get(category) + amount : amount);
            }
        }

        return result;
    }
}