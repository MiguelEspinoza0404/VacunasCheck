package com.example.vacunacheck;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.activity.EdgeToEdge;

import android.view.Menu;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.vacunacheck.Helpers.DBHelper;


public class MainPage extends AppCompatActivity {

    DBHelper dbHelper;
    String cedulaRecibida;

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

        // Ajustar padding para sistema de barras (status, nav)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnHistorialVacunas = findViewById(R.id.btnHistorialVacunas);
        btnHistorialVacunas.setOnClickListener(view -> {
            Intent intent = new Intent(MainPage.this, HistorialVacunasActivity.class);
            startActivity(intent);
        });

        Button btnAbrirCalendario = findViewById(R.id.btnAbrirCalendario);
        btnAbrirCalendario.setOnClickListener(v -> {
            Intent intent = new Intent(MainPage.this, CalendarioActivity.class);
            startActivity(intent);
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
        cedulaRecibida = getIntent().getStringExtra("cedula");

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        } else if (id == R.id.perfil) {
            Intent intent = new Intent(this, Perfil.class);
            intent.putExtra("cedula", cedulaRecibida);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_logout) {
            SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sesion_activa", false);
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

            editor.clear();
            editor.apply();

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
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SharedPreferences prefs = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String usuarioGuardado = prefs.getString("usuario", null);

        if (usuarioGuardado == null) {
            Toast.makeText(this, "No hay usuario en sesión.", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = db.rawQuery("SELECT cedula, nombres, apellidos, fecha_nacimiento, genero, estado_civil, email, usuario, contrasena, perfil FROM usuarios WHERE usuario = ?", new String[]{usuarioGuardado});

        if (cursor.moveToFirst()) {
            String mensaje = "Cédula: " + cursor.getString(0) + "\n" +
                    "Nombres: " + cursor.getString(1) + "\n" +
                    "Apellidos: " + cursor.getString(2) + "\n" +
                    "Fecha de Nacimiento: " + cursor.getString(3) + "\n" +
                    "Género: " + cursor.getString(4) + "\n" +
                    "Estado Civil: " + cursor.getString(5) + "\n" +
                    "Correo: " + cursor.getString(6) + "\n" +
                    "Usuario: " + cursor.getString(7) + "\n" +
                    "Contraseña: " + cursor.getString(8) + "\n" +
                    "Perfil: " + cursor.getString(9);

            new AlertDialog.Builder(this)
                    .setTitle("Datos del Usuario")
                    .setMessage(mensaje)
                    .setPositiveButton("Cerrar", null)
                    .show();
        } else {
            Toast.makeText(this, "Usuario no encontrado en la base de datos.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}
