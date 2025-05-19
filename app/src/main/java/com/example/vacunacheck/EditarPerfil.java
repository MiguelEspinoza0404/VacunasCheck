package com.example.vacunacheck;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vacunacheck.Helpers.DBHelper;

import java.util.Calendar;

public class EditarPerfil extends AppCompatActivity {

    private TextView txtUsuario, txtCedula, txtCorreo, editTextCedula, editTextNombre, editTextApellidos, editTextCorreo;
    TextView tvFechaNacimiento;
    Spinner spinnerGenero, spinnerPerfil;
    private ImageView btnRegresar;
    Calendar selectedDate;
    Button btnActualizar;
    DBHelper dbHelper;
    private String cedulaOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        txtCorreo = findViewById(R.id.txtCorreo);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtCedula = findViewById(R.id.txtCedula);
        txtCedula.setText(cedulaOriginal);
        editTextCedula = findViewById(R.id.editTextCedula);
        editTextCorreo = findViewById(R.id.editTextCorreo);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextApellidos = findViewById(R.id.editTextApellidos);
        btnRegresar = findViewById(R.id.btnRegresar);
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerPerfil = findViewById(R.id.spinnerPerfil);
        btnActualizar = findViewById(R.id.btnActualizar);

        // Configurar adaptadores para los Spinners
        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(this,
                R.array.generos, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> perfilAdapter = ArrayAdapter.createFromResource(this,
                R.array.perfil, android.R.layout.simple_spinner_item);
        perfilAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPerfil.setAdapter(perfilAdapter);

        // Mostrar DatePicker al pulsar sobre la fecha
        tvFechaNacimiento.setOnClickListener(v -> showDatePicker());

        dbHelper = new DBHelper(this);

        //Obtenemos la cedula para cargar los datos en el activity
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        cedulaOriginal = prefs.getString("cedula", null);

        if (cedulaOriginal == null) {
            Toast.makeText(this, "No se pudo obtener la cédula del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = dbHelper.obtenerUsuarioPorCedula(cedulaOriginal);

        if (cursor != null && cursor.moveToFirst()) {
            txtCorreo.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            txtUsuario.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombres")));
            txtCedula.setText(cursor.getString(cursor.getColumnIndexOrThrow("cedula")));
            editTextCedula.setText(cursor.getString(cursor.getColumnIndexOrThrow("cedula")));
            editTextNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombres")));
            editTextApellidos.setText(cursor.getString(cursor.getColumnIndexOrThrow("apellidos")));
            editTextCorreo.setText(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            tvFechaNacimiento.setText(cursor.getString(cursor.getColumnIndexOrThrow("fecha_nacimiento")));

            String genero = cursor.getString(cursor.getColumnIndexOrThrow("genero"));
            String perfil = cursor.getString(cursor.getColumnIndexOrThrow("perfil"));

            spinnerGenero.setSelection(((ArrayAdapter) spinnerGenero.getAdapter()).getPosition(genero));
            spinnerPerfil.setSelection(((ArrayAdapter) spinnerPerfil.getAdapter()).getPosition(perfil));

            cursor.close();
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }

        btnActualizar.setOnClickListener(v -> actualizar());
        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(EditarPerfil.this, MainPage.class);
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

    private void actualizar() {
        if (editTextNombre.getText().toString().trim().isEmpty()
                || editTextApellidos.getText().toString().trim().isEmpty()
                || tvFechaNacimiento.getText().toString().equals("Seleccione fecha de nacimiento")
                || spinnerGenero.getSelectedItemPosition() == 0
                || editTextCorreo.getText().toString().trim().isEmpty()
                || spinnerPerfil.getSelectedItemPosition() == 0) {

            Toast.makeText(this, "Por favor, complete todos los campos antes de registrar.", Toast.LENGTH_SHORT).show();
            return;
        }


        boolean exito = dbHelper.actualizarUsuario(
                cedulaOriginal,
                editTextNombre.getText().toString().trim(),
                editTextApellidos.getText().toString().trim(),
                tvFechaNacimiento.getText().toString().trim(),
                spinnerGenero.getSelectedItem().toString().trim(),
                editTextCorreo.getText().toString().trim(),
                spinnerPerfil.getSelectedItem().toString().trim()
        );

        if (exito) {
            Toast.makeText(this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show();
            borrarCampos();
        } else {
            Toast.makeText(this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
            Log.d("REGISTRO", "Registro fallido para cédula: " + cedulaOriginal);
        }

    }

    private void borrarCampos() {
        boolean hayDatos = !editTextCedula.getText().toString().isEmpty()
                || !editTextNombre.getText().toString().isEmpty()
                || !editTextApellidos.getText().toString().isEmpty()
                || !tvFechaNacimiento.getText().toString().equals("Seleccione fecha de nacimiento")
                || spinnerGenero.getSelectedItemPosition() != 0
                || !editTextCorreo.getText().toString().isEmpty()
                || spinnerPerfil.getSelectedItemPosition() != 0;

        if (!hayDatos) {
            Toast.makeText(this, "No hay datos para borrar", Toast.LENGTH_SHORT).show();
            return;
        }

        editTextCedula.setText("");
        editTextNombre.setText("");
        editTextApellidos.setText("");
        tvFechaNacimiento.setText("Seleccione fecha de nacimiento");
        spinnerGenero.setSelection(0);
        editTextCorreo.setText("");
        spinnerPerfil.setSelection(0);

        Toast.makeText(this, "Datos borrados correctamente", Toast.LENGTH_SHORT).show();
    }
}