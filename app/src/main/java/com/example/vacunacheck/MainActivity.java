package com.example.vacunacheck;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin;

    Button buttonRegister;

    private final String USERNAME = "admin";
    private final String PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonRegister = findViewById(R.id.buttonRegister);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (username.equals(USERNAME) && password.equals(PASSWORD)) {
                Toast.makeText(MainActivity.this, "Acceso Concedido", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, MainPage.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(MainActivity.this, "Datos incorrectos", Toast.LENGTH_SHORT).show();

            }

        });

        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this , registro.class);
            startActivity(intent);
            finish();
        });

    }
}