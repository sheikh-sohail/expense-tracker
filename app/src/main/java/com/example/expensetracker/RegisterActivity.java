package com.example.expensetracker;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etUsername, etPassword, etConfirmPassword;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        etName            = findViewById(R.id.et_name);
        etUsername        = findViewById(R.id.et_username);
        etPassword        = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        findViewById(R.id.btn_register).setOnClickListener(v -> registerUser());
        findViewById(R.id.tv_login).setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name     = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm  = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etName.setError("Enter your name");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Enter a username");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Enter a password");
            return;
        }
        if (password.length() < 4) {
            etPassword.setError("Password must be at least 4 characters");
            return;
        }
        if (!password.equals(confirm)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        boolean success = db.registerUser(name, username, password);
        if (success) {
            Toast.makeText(this, "Account created! Please login.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Username already taken", Toast.LENGTH_SHORT).show();
        }
    }
}
