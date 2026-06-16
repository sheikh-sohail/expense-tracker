package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private CheckBox cbRemember;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Auto-login if remembered
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        if (prefs.contains("username")) {
            goToMain(prefs.getString("name", "User"));
            return;
        }

        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cbRemember = findViewById(R.id.cb_remember);

        findViewById(R.id.btn_login).setOnClickListener(v -> loginUser());
        findViewById(R.id.tv_register).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        findViewById(R.id.tv_forgot).setOnClickListener(v ->
                Toast.makeText(this, "Contact support to reset password",
                        Toast.LENGTH_SHORT).show());
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Enter username");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter password");
            return;
        }

        String name = db.loginUser(username, password);
        if (name != null) {
            if (cbRemember.isChecked()) {
                getSharedPreferences("login", MODE_PRIVATE)
                        .edit()
                        .putString("username", username)
                        .putString("name", name)
                        .apply();
            }
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_SHORT).show();
            goToMain(name);
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToMain(String name) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name);
        startActivity(intent);
        finish();
    }
}
