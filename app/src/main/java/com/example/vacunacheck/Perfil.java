package com.example.vacunacheck;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacunacheck.Helpers.DBHelper;

public class Perfil extends AppCompatActivity {

    private TextView txtUsuario, txtNumeroFamilia, txtNumeroVacunas;
    private Button btnVerPerfil, btnAgregarFamilia;
    private ImageView btnRegresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtUsuario = findViewById(R.id.txtusuario);
        txtNumeroFamilia = findViewById(R.id.txtNumeroFamilia);
        txtNumeroVacunas = findViewById(R.id.txtNumeroVacunas);
        btnVerPerfil = findViewById(R.id.btnVerPerfil);
        btnAgregarFamilia = findViewById(R.id.btnAgregarFamilia);
        btnRegresar = findViewById(R.id.btnRegresar);

        //Crear una tabla de vacunas y Familia
        txtNumeroFamilia.setText("3");
        txtNumeroVacunas.setText("5");

        DBHelper dbHelper = new DBHelper(this);

        //Obtenemos la cedula para cargar los datos en el activity
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        String cedula = prefs.getString("cedula", null);

        if (cedula == null) {
            Toast.makeText(this, "No se pudo obtener la cédula del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = dbHelper.obtenerUsuarioPorCedula(cedula);

        if (cursor != null && cursor.moveToFirst()) {
            txtUsuario.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombres")));
            cursor.close();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }


        btnRegresar.setOnClickListener(v -> finish());

        btnVerPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, EditarPerfil.class);

            startActivity(intent);
        });

        btnAgregarFamilia.setOnClickListener(v -> {
            Intent intent = new Intent(Perfil.this, CrearUsuarioActivity.class);
            startActivity(intent);
        });

        btnRegresar.setOnClickListener(v -> {
            finish();
        });

    }
}
