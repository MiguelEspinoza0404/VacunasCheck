package com.example.vacunacheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class EduYC_TestVacunacion extends AppCompatActivity {

    TextView tvPregunta,tvPuntaje;
    RadioGroup rgOpciones;
    RadioButton[] Opciones = new RadioButton[4];
    Pregunta[] preguntas;
    Button btnSiguiente;
    int preguntaActual = 0;
    int puntaje = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edu_yc_test_vacunacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicializar las vistas

        tvPregunta = findViewById(R.id.tv_pregunta);
        Opciones[0] = findViewById(R.id.rb_opcion1);
        Opciones[1] = findViewById(R.id.rb_Opcion2);
        Opciones[2] = findViewById(R.id.rb_Opcion3);
        Opciones[3] = findViewById(R.id.rb_Opcion4);
        rgOpciones = findViewById(R.id.rg_Opciones);
        tvPuntaje = findViewById(R.id.tvPuntaje);

        btnSiguiente = findViewById(R.id.Test_btnSiguiente);

        preguntas = new Pregunta[]{
                new Pregunta("¿Qué efectos secundarios pueden causar las vacunas?", new String[]{"Fiebre, enrojecimientoo o dolor en el sitio de aplicación", "Nada", "Hepatitis A", "Influenza"}, 0),
                new Pregunta("¿Pueden las vacunas causar enfermedades crónicas o autismo?", new String[]{"Sí", "No", "Solo a los adultos mayores", "Solo a los niños"}, 1),
                new Pregunta("¿Las vacunas son obligatorias en Ecuador?", new String[]{"Sí", "No", "Solo para niños", "Solo en pandemia"}, 0),
                new Pregunta("¿Puedo vacunarme si estoy embarazada?", new String[]{"Sí", "No", "No sé", "Solo en pandemia"}, 0),
                new Pregunta("¿Cúantas dosis son para la vacuna de la fiebre amarilla?", new String[]{"Una dosis para toda la vida", "Dos", "Tres", "Cuatro"}, 0)
        };

        mostrarPregunta();

        btnSiguiente.setOnClickListener(v -> {
            int seleccion = rgOpciones.getCheckedRadioButtonId();
            if (seleccion == -1) {
                Toast.makeText(this, "Seleccione una opción", Toast.LENGTH_SHORT).show();
                return;
            }

            int respuestaIndex = rgOpciones.indexOfChild(findViewById(seleccion));
            if (respuestaIndex == preguntas[preguntaActual].respuestaCorrecta) {
                Toast.makeText(this, "Respuesta Correcta", Toast.LENGTH_SHORT).show();
                puntaje++;
            } else {
                Toast.makeText(this, "Respuesta Incorrecta ", Toast.LENGTH_SHORT).show();
            }

            preguntaActual++;
            rgOpciones.clearCheck();

            if (preguntaActual < preguntas.length) {
                mostrarPregunta();
            } else {
                mostrarResultadoFinal();
            }
        });
    }

    private void mostrarPregunta() {
        Pregunta q = preguntas[preguntaActual];
        tvPregunta.setText(q.pregunta);
        for (int i = 0; i < 4; i++) {
            Opciones[i].setText(q.opciones[i]);
        }
    }

    private void mostrarResultadoFinal() {
        tvPregunta.setVisibility(View.GONE);
        rgOpciones.setVisibility(View.GONE);
        btnSiguiente.setVisibility(View.GONE);
        tvPuntaje.setText("Puntaje: " +puntaje+"/5.");
        tvPuntaje.setVisibility(View.VISIBLE);
    }

    public void cancelar(View v){
        Intent ventanaEducacion = new Intent(this, EducacionyConsejos.class);
        startActivity(ventanaEducacion);
    }
}



