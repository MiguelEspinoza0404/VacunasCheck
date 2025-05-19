package com.example.vacunacheck;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacunacheck.Helpers.DBHelper;

import java.util.Calendar;
import java.util.List;

public class HistorialVacunasActivity extends AppCompatActivity {

    private ListView listaHistorial;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_vacunas);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText etTipoVacuna = findViewById(R.id.etTipoVacuna);
        EditText etLote = findViewById(R.id.etLote);
        EditText etFecha = findViewById(R.id.etFecha);
        EditText etLugar = findViewById(R.id.etLugar);

        Button btnGuardarVacuna = findViewById(R.id.btnGuardarVacuna);
        Button btnConsultarHistorial = findViewById(R.id.btnConsultarHistorial);

        etFecha.setOnClickListener(v -> {
            final Calendar calendario = Calendar.getInstance();
            int año = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            int dia = calendario.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog selectorFecha = new DatePickerDialog(
                    HistorialVacunasActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        etFecha.setText(fechaSeleccionada);
                    },
                    año, mes, dia
            );

            selectorFecha.show();
        });

        dbHelper = new DBHelper(this);
        listaHistorial = findViewById(R.id.listaHistorial);

        List<String> datosHistorial = dbHelper.obtenerHistorial();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datosHistorial);
        listaHistorial.setAdapter(adapter);

        btnGuardarVacuna.setOnClickListener(v -> {
            String tipo = etTipoVacuna.getText().toString();
            String lote = etLote.getText().toString();
            String fecha = etFecha.getText().toString();
            String lugar = etLugar.getText().toString();

            if (tipo.isEmpty() || lote.isEmpty() || fecha.isEmpty() || lugar.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean insertado = dbHelper.insertarVacuna(tipo, lote, fecha, lugar);

            if (insertado) {
                Toast.makeText(this, "Vacuna guardada exitosamente", Toast.LENGTH_SHORT).show();
                etTipoVacuna.setText("");
                etLote.setText("");
                etFecha.setText("");
                etLugar.setText("");
            } else {
                Toast.makeText(this, "Error al guardar vacuna", Toast.LENGTH_SHORT).show();
            }
        });

        btnConsultarHistorial.setOnClickListener(v -> {
            List<String> nuevoHistorial = dbHelper.obtenerHistorial();
            ArrayAdapter<String> nuevoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nuevoHistorial);
            listaHistorial.setAdapter(nuevoAdapter);
            Toast.makeText(this, "Historial actualizado", Toast.LENGTH_SHORT).show();
        });
    }
}