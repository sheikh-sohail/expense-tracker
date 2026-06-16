package com.example.expensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private EditText etAmount, etDate;
    private DatabaseHelper db;
    private final Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = new DatabaseHelper(this);

        spinnerCategory = findViewById(R.id.spinner_category);
        etAmount        = findViewById(R.id.et_amount);
        etDate          = findViewById(R.id.et_date);
        Button btnAdd   = findViewById(R.id.btn_add_expense);

        // Setup Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.expense_categories,
                android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // Date Picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Back button
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());

        // Add Expense Button
        btnAdd.setOnClickListener(v -> addExpense());
    }

    private void showDatePicker() {
        new DatePickerDialog(this, (view, year, month, day) -> {
            selectedDate.set(year, month, day);
            String formatted = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    .format(selectedDate.getTime());
            etDate.setText(formatted);
        },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void addExpense() {
        String category = spinnerCategory.getSelectedItem().toString();
        String amountStr = etAmount.getText().toString().trim();
        String date = etDate.getText().toString().trim();

        if (amountStr.isEmpty()) {
            etAmount.setError("Enter amount");
            return;
        }
        if (date.isEmpty()) {
            etDate.setError("Select a date");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        // Treat "Salary" as income, all else as expense
        String type = category.equalsIgnoreCase("Salary")
                ? DatabaseHelper.TYPE_INCOME
                : DatabaseHelper.TYPE_EXPENSE;

        boolean success = db.insertTransaction(category, amount, date, type);
        if (success) {
            Toast.makeText(this, "Transaction added!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving transaction", Toast.LENGTH_SHORT).show();
        }
    }
}