package com.example.vacunacheck;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class registro extends AppCompatActivity {

    EditText etCedula, etNombres, etApellidos, etEdad;
    Spinner spinnerNacionalidad, spinnerGenero;
    RadioGroup rgEstadoCivil;
    TextView tvFechaNacimiento;
    RatingBar ratingIngles;
    Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etCedula = findViewById(R.id.etCedula);
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etEdad = findViewById(R.id.etEdad);
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        rgEstadoCivil = findViewById(R.id.rgEstadoCivil);
        tvFechaNacimiento = findViewById(R.id.tvFechaNacimiento);
        ratingIngles = findViewById(R.id.ratingIngles);

        ArrayAdapter<CharSequence> nacionalidadAdapter = ArrayAdapter.createFromResource(this,
                R.array.nacionalidades, android.R.layout.simple_spinner_item);
        nacionalidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNacionalidad.setAdapter(nacionalidadAdapter);

        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(this,
                R.array.generos, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        tvFechaNacimiento.setOnClickListener(v -> showDatePicker());

        findViewById(R.id.btnRegistrar).setOnClickListener(v -> registrar());
        findViewById(R.id.btnBorrar).setOnClickListener(v -> borrarCampos());
        findViewById(R.id.btnCancelar).setOnClickListener(v -> finish()); // vuelve al login
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
        String estadoCivil = ((RadioButton) findViewById(rgEstadoCivil.getCheckedRadioButtonId())).getText().toString();
        String datos = etCedula.getText() + ";" +
                etNombres.getText() + ";" +
                etApellidos.getText() + ";" +
                etEdad.getText() + ";" +
                spinnerNacionalidad.getSelectedItem().toString() + ";" +
                spinnerGenero.getSelectedItem().toString() + ";" +
                estadoCivil + ";" +
                tvFechaNacimiento.getText() + ";" +
                ratingIngles.getRating();

        try {
            FileOutputStream fos = openFileOutput("registro.txt", MODE_PRIVATE);
            fos.write(datos.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("RegistroActivity", datos);
        Toast.makeText(this, "Datos ingresados correctamente", Toast.LENGTH_SHORT).show();
    }


    private void borrarCampos() {
        etCedula.setText("");
        etNombres.setText("");
        etApellidos.setText("");
        etEdad.setText("");
        spinnerNacionalidad.setSelection(0);
        spinnerGenero.setSelection(0);
        rgEstadoCivil.clearCheck();
        tvFechaNacimiento.setText("Seleccione fecha de nacimiento");
        ratingIngles.setRating(0);
    }
}