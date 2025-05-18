package com.example.vacunacheck;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacunacheck.Helpers.DBHelper;

public class ConsultarUsuarioActivity extends AppCompatActivity {

    EditText editTextCedula, editTextNombre, editTextEmail;
    Button btnBuscar, btnActualizar, btnEliminar;
    DBHelper dbHelper;
    int idUsuarioEncontrado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_usuario);

        dbHelper = new DBHelper(this);

        editTextCedula = findViewById(R.id.editTextCedula);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextEmail = findViewById(R.id.editTextEmail);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Deshabilitar campos y botones al inicio para evitar edición sin búsqueda previa
        editTextNombre.setEnabled(false);
        editTextEmail.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);

        btnBuscar.setOnClickListener(v -> buscarUsuario());
        btnActualizar.setOnClickListener(v -> actualizarUsuario());
        btnEliminar.setOnClickListener(v -> eliminarUsuario());
    }

    private void buscarUsuario() {
        String cedula = editTextCedula.getText().toString().trim();

        if (cedula.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese la cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE cedula = ?", new String[]{cedula});
        Log.d("DB_QUERY", "Cédula: " + cedula);

        if (cursor.moveToFirst()) {
            idUsuarioEncontrado = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            editTextNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombres")));
            editTextEmail.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));

            editTextNombre.setEnabled(true);
            editTextEmail.setEnabled(true);
            btnActualizar.setEnabled(true);
            btnEliminar.setEnabled(true);
            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            // Limpiar y deshabilitar campos y botones si no se encontró usuario
            limpiarCampos();
            deshabilitarCampos();
            idUsuarioEncontrado = -1;
        }

        cursor.close();
        db.close();
    }

    private void actualizarUsuario() {
        if (idUsuarioEncontrado == -1) {
            Toast.makeText(this, "Primero busque un usuario válido", Toast.LENGTH_SHORT).show();
            return;
        }

        String nombre = editTextNombre.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Nombre y correo no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("email", email);

        int rows = db.update("usuarios", valores, "id = ?", new String[]{String.valueOf(idUsuarioEncontrado)});

        if (rows > 0) {
            Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void eliminarUsuario() {
        if (idUsuarioEncontrado == -1) {
            Toast.makeText(this, "Primero busque un usuario válido", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("usuarios", "id = ?", new String[]{String.valueOf(idUsuarioEncontrado)});

        if (rows > 0) {
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            deshabilitarCampos();
            idUsuarioEncontrado = -1;
        } else {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void limpiarCampos() {
        editTextNombre.setText("");
        editTextEmail.setText("");
        editTextCedula.setText("");
    }

    private void deshabilitarCampos() {
        editTextNombre.setEnabled(false);
        editTextEmail.setEnabled(false);
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }
}
