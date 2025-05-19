package com.example.vacunacheck;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vacunacheck.Helpers.DBHelper;

import java.util.Calendar;

public class registro extends AppCompatActivity {

    EditText etCedula, etNombres, etApellidos, etUsuario, etContrasenia, etEmail;
    Spinner spinnerGenero, spinnerPerfil;
    RadioGroup rgEstadoCivil;
    TextView tvFechaNacimiento;
    Calendar selectedDate;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        dbHelper = new DBHelper(this);

        etCedula = findViewById(R.id.etCedula);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        rgEstadoCivil = findViewById(R.id.rgEstadoCivil);
        etEmail = findViewById(R.id.etEmail);
        etUsuario = findViewById(R.id.etUsuario);
        etContrasenia = findViewById(R.id.etContrasenia);
        spinnerPerfil = findViewById(R.id.spinnerPerfil);

        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(this,
                R.array.generos, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> perfilAdapter = ArrayAdapter.createFromResource(this,
                R.array.perfil, android.R.layout.simple_spinner_item);
        perfilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerfil.setAdapter(perfilAdapter);

        tvFechaNacimiento.setOnClickListener(v -> showDatePicker());

        findViewById(R.id.btnRegistrar).setOnClickListener(v -> registrar());
        findViewById(R.id.btnBorrar).setOnClickListener(v -> borrarCampos());
        findViewById(R.id.btnCancelar).setOnClickListener(v -> {
            Intent intent = new Intent(registro.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    tvFechaNacimiento.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    private void registrar() {
        if (etCedula.getText().toString().trim().isEmpty()
                || etNombres.getText().toString().trim().isEmpty()
                || etApellidos.getText().toString().trim().isEmpty()
                || tvFechaNacimiento.getText().toString().equals("Seleccione fecha de nacimiento")
                || spinnerGenero.getSelectedItemPosition() == 0
                || rgEstadoCivil.getCheckedRadioButtonId() == -1
                || etEmail.getText().toString().trim().isEmpty()
                || etUsuario.getText().toString().trim().isEmpty()
                || etContrasenia.getText().toString().trim().isEmpty()
                || spinnerPerfil.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Por favor, complete todos los campos antes de registrar.", Toast.LENGTH_SHORT).show();
            return;
        }

        String estadoCivil = ((RadioButton) findViewById(rgEstadoCivil.getCheckedRadioButtonId())).getText().toString();

        String cedula = etCedula.getText().toString().trim();
        if (dbHelper.cedulaExiste(cedula)) {
            Toast.makeText(this, "Ya existe un usuario con esta cédula", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exito = dbHelper.insertarUsuario(
                cedula,
                etNombres.getText().toString().trim(),
                etApellidos.getText().toString().trim(),
                tvFechaNacimiento.getText().toString().trim(),
                spinnerGenero.getSelectedItem().toString().trim(),
                estadoCivil.trim(),
                etEmail.getText().toString().trim(),
                etUsuario.getText().toString().trim(),
                etContrasenia.getText().toString().trim(),
                spinnerPerfil.getSelectedItem().toString().trim()
        );

        if (exito) {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            borrarCampos();
        } else {
            Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            Log.d("REGISTRO", "Registro fallido para cédula: " + cedula);
        }
    }

    private void borrarCampos() {
        boolean hayDatos = !etCedula.getText().toString().isEmpty()
                || !etNombres.getText().toString().isEmpty()
                || !etApellidos.getText().toString().isEmpty()
                || !tvFechaNacimiento.getText().toString().equals("Seleccione fecha de nacimiento")
                || spinnerGenero.getSelectedItemPosition() != 0
                || rgEstadoCivil.getCheckedRadioButtonId() != -1
                || !etEmail.getText().toString().isEmpty()
                || !etUsuario.getText().toString().isEmpty()
                || !etContrasenia.getText().toString().isEmpty()
                || spinnerPerfil.getSelectedItemPosition() != 0;

        if (!hayDatos) {
            Toast.makeText(this, "No hay datos para borrar", Toast.LENGTH_SHORT).show();
            return;
        }

        etCedula.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        tvFechaNacimiento.setText("Seleccione fecha de nacimiento");
        spinnerGenero.setSelection(0);
        rgEstadoCivil.clearCheck();
        etEmail.setText("");
        etUsuario.setText("");
        etContrasenia.setText("");
        spinnerPerfil.setSelection(0);

        Toast.makeText(this, "Datos borrados correctamente", Toast.LENGTH_SHORT).show();
    }
}
