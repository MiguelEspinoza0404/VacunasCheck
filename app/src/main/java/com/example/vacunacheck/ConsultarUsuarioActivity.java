package com.example.vacunacheck;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        btnBuscar.setOnClickListener(v -> buscarUsuario());
        btnActualizar.setOnClickListener(v -> actualizarUsuario());
        btnEliminar.setOnClickListener(v -> eliminarUsuario());
    }

    private void buscarUsuario() {
        String cedula = editTextCedula.getText().toString();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE cedula = ?", new String[]{cedula});
        Log.d("DB_QUERY", "Cédula: " + cedula);

        if (cursor.moveToFirst()) {
            idUsuarioEncontrado = cursor.getInt(0);
            editTextNombre.setText(cursor.getString(1));
            editTextEmail.setText(cursor.getString(2));

            editTextNombre.setEnabled(true);
            editTextEmail.setEnabled(true);
            btnActualizar.setEnabled(true);
            btnEliminar.setEnabled(true);
            Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }

    private void actualizarUsuario() {
        String nombre = editTextNombre.getText().toString();
        String email = editTextEmail.getText().toString();

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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("usuarios", "id = ?", new String[]{String.valueOf(idUsuarioEncontrado)});

        if (rows > 0) {
            Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
            editTextNombre.setText("");
            editTextEmail.setText("");
            editTextCedula.setText("");
            editTextNombre.setEnabled(false);
            editTextEmail.setEnabled(false);
            btnActualizar.setEnabled(false);
            btnEliminar.setEnabled(false);
        } else {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}