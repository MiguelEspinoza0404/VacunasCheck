package com.example.vacunacheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EducacionyConsejos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_educaciony_consejos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void regresar(View v){
        Intent ventanaHome = new Intent(this, MainPage.class);
        startActivity(ventanaHome);
    }

    public void preguntas (View v){
        Intent ventanaPreguntas = new Intent(this, EduYC_PreguntasFrecuentes.class);
        startActivity(ventanaPreguntas);
    }

    public void cuestionario(View v){
        Intent ventanaCuestionario = new Intent(this, EduYC_TestVacunacion.class);
        startActivity(ventanaCuestionario);
    }

    public void articulos(View v){
        Intent ventanaArticulos = new Intent(this, EduYC_Articulos_Informativos.class);
        startActivity(ventanaArticulos);
    }
}