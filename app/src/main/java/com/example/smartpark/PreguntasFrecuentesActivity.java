package com.example.smartpark;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PreguntasFrecuentesActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<Pregunta> preguntas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preguntas_frecuentes);


        findViewById(R.id.btnVolverPreguntas).setOnClickListener(v -> finish());
        recycler = findViewById(R.id.recyclerPreguntas);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        preguntas = new ArrayList<>();
        preguntas.add(new Pregunta("¿Cómo reservo un parking?", "Selecciona un parking en el mapa y pulsa en 'Reservar'."));
        preguntas.add(new Pregunta("¿Puedo cancelar una reserva?", "Sí, desde el apartado 'Mis Reservas'."));
        preguntas.add(new Pregunta("¿Cómo veo los parkings más visitados?", "En el menú principal selecciona 'Parkings más visitados'."));
        preguntas.add(new Pregunta("¿Necesito cuenta?", "Sí, es necesario registrarse para guardar reservas."));
        preguntas.add(new Pregunta("¿La app es gratuita?", "Sí, el uso de la aplicación es totalmente gratuito."));

        recycler.setAdapter(new PreguntasAdapter(preguntas));
    }
}