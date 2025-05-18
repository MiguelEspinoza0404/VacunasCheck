package com.example.vacunacheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacunacheck.Helpers.DBHelper;

public class MainActivity extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonRegister;
    CheckBox checkboxRemember;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        if (prefs.getBoolean("sesion_activa", false)) {
            // Si la sesión está activa, ir directo a MainPage
            startActivity(new Intent(MainActivity.this, MainPage.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        // Inicialización de vistas
        editTextUsername = findViewById(R.id.etUsuario);
        editTextPassword = findViewById(R.id.etConstrasenia);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        checkboxRemember = findViewById(R.id.checkboxRemember);

        buttonLogin.setOnClickListener(view -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar usuario en la base de datos
            if (dbHelper.validarUsuario(username, password)) {

                if (checkboxRemember.isChecked()) {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("sesion_activa", true);
                    editor.apply();
                }

                Toast.makeText(MainActivity.this, "Acceso concedido", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, MainPage.class));
                finish();

            } else {
                Toast.makeText(MainActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, registro.class);
            startActivity(intent);
            finish();
        });
    }
}
