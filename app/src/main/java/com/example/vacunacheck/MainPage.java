package com.example.vacunacheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.Toast;


public class MainPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnRecuperar = findViewById(R.id.btnMostrarDatos);
        btnRecuperar.setOnClickListener(v -> recuperarDatos());

        Button btnConsultarUsuario = findViewById(R.id.btnConsultarUsuario);
        btnConsultarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, ConsultarUsuarioActivity.class);
            startActivity(intent);
        });
        Button btnCrearUsuario = findViewById(R.id.btnCrearUsuario);
        btnCrearUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, CrearUsuarioActivity.class);
            startActivity(intent);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Toast.makeText(this, "Aplicación creada por Grupo 5", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sesion_activa", false);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_about, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button btnClose = dialogView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(v -> dialog.dismiss());
    }

    private void recuperarDatos() {
        StringBuilder datos = new StringBuilder();

        try {
            FileInputStream fis = openFileInput("registro.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String linea;
            while ((linea = reader.readLine()) != null) {
                datos.append(linea);
            }
            reader.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String[] campos = datos.toString().split(";");
        String mensaje = "Cédula: " + campos[0] + "\n" +
                "Nombres: " + campos[1] + "\n" +
                "Apellidos: " + campos[2] + "\n" +
                "Edad: " + campos[3] + "\n" +
                "Nacionalidad: " + campos[4] + "\n" +
                "Género: " + campos[5] + "\n" +
                "Estado Civil: " + campos[6] + "\n" +
                "Fecha de Nacimiento: " + campos[7] + "\n" +
                "Nivel de Inglés: " + campos[8];

        new AlertDialog.Builder(this)
                .setTitle("Datos Registrados")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar", null)
                .show();
    }



}