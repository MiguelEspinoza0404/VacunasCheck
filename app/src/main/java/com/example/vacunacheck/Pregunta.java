package com.example.vacunacheck;

import java.util.List;

public class Pregunta {
    String pregunta;
    String[] opciones;//4 opciones
    int respuestaCorrecta;

    public Pregunta(String pregunta, String[] opciones, int respuestaCorrecta) {
        this.pregunta = pregunta;
        this.opciones = opciones;
        this.respuestaCorrecta = respuestaCorrecta;
    }
}
