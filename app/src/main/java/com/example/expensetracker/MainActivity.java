package com.example.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.SharedPreferences;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TransactionAdapter adapter;
    private RecyclerView rvTransactions;
    private TextView tvBalance, tvIncome, tvExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = getIntent().getStringExtra("name");
        if (name == null) {
            SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
            name = prefs.getString("name", "User");
        }
        TextView tvUsername = findViewById(R.id.tv_username);
        tvUsername.setText(name);

        db = new DatabaseHelper(this);

        tvBalance  = findViewById(R.id.tv_balance_amount);
        tvIncome   = findViewById(R.id.tv_income);
        tvExpense  = findViewById(R.id.tv_expense);
        rvTransactions = findViewById(R.id.rv_transactions);

        rvTransactions.setLayoutManager(new LinearLayoutManager(this));

        // FAB -> Add Expense
        findViewById(R.id.fab_add).setOnClickListener(v ->
                startActivity(new Intent(this, AddExpenseActivity.class)));

        // See All -> Statistics
        findViewById(R.id.tv_see_all).setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));

        // Bottom nav
        findViewById(R.id.nav_stats).setOnClickListener(v ->
                startActivity(new Intent(this, StatisticsActivity.class)));

        // ADD THIS in onCreate()
        findViewById(R.id.iv_more).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
            popup.getMenu().add(0, 1, 0, "Clear");    // Clear option first
            popup.getMenu().add(0, 2, 1, "Logout");   // Logout option second

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == 1) {
                    showClearConfirmDialog();
                    return true;
                } else if (item.getItemId() == 2) {
                    logout();
                    return true;
                }
                return false;
            });
            popup.show();
        });

        TextView tvGreeting = findViewById(R.id.tv_greeting);
        tvGreeting.setText(getGreeting());

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        double income  = db.getTotalIncome();
        double expense = db.getTotalExpense();
        double balance = db.getTotalBalance();

        tvBalance.setText(String.format(Locale.US, "₹%.2f", balance));
        tvIncome.setText(String.format(Locale.US, "₹%.2f", income));
        tvExpense.setText(String.format(Locale.US, "₹%.2f", expense));

        List<Transaction> recent = db.getRecentTransactions(10);
        if (adapter == null) {
            adapter = new TransactionAdapter(this, recent);
            rvTransactions.setAdapter(adapter);
        } else {
            adapter.updateData(recent);
        }
    }

    private String getGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 12) {
            return "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon";
        } else if (hour >= 17 && hour < 21) {
            return "Good Evening";
        } else {
            return "Good Night";
        }
    }

    private void logout() {
        getSharedPreferences("login", MODE_PRIVATE).edit().clear().apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void showClearConfirmDialog() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Clear All Transactions")
                .setMessage("Are you sure you want to delete all transactions? This cannot be undone.")
                .setPositiveButton("Clear", (dialog, which) -> {
                    db.clearAllTransactions();
                    loadData(); // refresh UI immediately
                    Toast.makeText(this, "All transactions cleared", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}