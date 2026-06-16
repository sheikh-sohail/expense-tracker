package com.example.expensetracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private LineChart lineChart;
    private RecyclerView rvTopSpending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        db           = new DatabaseHelper(this);
        lineChart    = findViewById(R.id.line_chart);
        rvTopSpending = findViewById(R.id.rv_top_spending);

        rvTopSpending.setLayoutManager(new LinearLayoutManager(this));
        rvTopSpending.setNestedScrollingEnabled(false);

        // Back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // Bottom nav
        findViewById(R.id.nav_home).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        setupChart();
        loadTopSpending();
    }

    private void setupChart() {
        // Simple chart using sample data points
        List<Entry> entries = new ArrayList<>();
        String[] labels = {"16-Aug", "17-Aug", "18-Aug", "19-Aug", "20-Aug"};
        float[] values  = {10f, 200f, 2000f, 300f, 1000f};

        for (int i = 0; i < values.length; i++) {
            entries.add(new Entry(i, values[i]));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Expenses");
        dataSet.setColor(Color.parseColor("#2D9596"));
        dataSet.setCircleColor(Color.parseColor("#2D9596"));
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleRadius(5f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.parseColor("#2D9596"));
        dataSet.setFillAlpha(40);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleHoleColor(Color.WHITE);

        lineChart.setData(new LineData(dataSet));
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.getLegend().setEnabled(true);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();
    }

    private void loadTopSpending() {
        List<Transaction> top = db.getTopExpenses(10);
        TransactionAdapter adapter = new TransactionAdapter(this, top);
        rvTopSpending.setAdapter(adapter);
    }
}
