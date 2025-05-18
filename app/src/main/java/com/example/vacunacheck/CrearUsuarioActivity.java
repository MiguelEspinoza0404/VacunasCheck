package com.example.vacunacheck;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacunacheck.Helpers.DBHelper;

public class CrearUsuarioActivity extends AppCompatActivity {

    EditText etCedula, etNombre, etApellidos;
    Spinner spinnerGenero, spinnerPerfil;
    Button btnGuardar;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        dbHelper = new DBHelper(this);

        etCedula = findViewById(R.id.etCedula);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerPerfil = findViewById(R.id.spinnerPerfil);

        // Configurar adaptadores para los Spinners
        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(this,
                R.array.generos, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> perfilAdapter = ArrayAdapter.createFromResource(this,
                R.array.perfil, android.R.layout.simple_spinner_item);
        perfilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerfil.setAdapter(perfilAdapter);

        btnGuardar = findViewById(R.id.btnGuardarUsuario);

        btnGuardar.setOnClickListener(v -> guardarUsuario());
    }

    private void guardarUsuario() {
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String genero = spinnerGenero.getSelectedItem().toString();
        String perfil = spinnerPerfil.getSelectedItem().toString();


        if (cedula.isEmpty() || nombre.isEmpty() || apellidos.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean insertado = dbHelper.insertarFamiliar(cedula, nombre, apellidos, genero, perfil);

        if (insertado) {
            Toast.makeText(this, "Usuario guardado", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error al guardar. La cédula podría estar repetida.", Toast.LENGTH_SHORT).show();
        }
    }
}
