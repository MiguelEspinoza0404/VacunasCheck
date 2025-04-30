package com.example.vacunacheck;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import android.view.Menu;
import android.view.MenuItem;


public class MainPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_page);

        Button btnRecuperar = findViewById(R.id.btnMostrarDatos);
        btnRecuperar.setOnClickListener(v -> recuperarDatos());

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
        if (item.getItemId() == R.id.action_about) {
            showAboutDialog();
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