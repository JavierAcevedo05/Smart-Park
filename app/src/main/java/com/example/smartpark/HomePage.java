package com.example.smartpark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    private TextView bienvenidoText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home_page);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        bienvenidoText = findViewById(R.id.textView13);

        // Obtener usuario actual
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // Buscar en Firestore el nombre del usuario
            db.collection("usuarios").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String nombre = documentSnapshot.getString("nombre");
                            if (nombre == null || nombre.isEmpty()) {
                                nombre = "usuario";
                            }
                            bienvenidoText.setText("Bienvenido, " + nombre);
                        } else {
                            bienvenidoText.setText("Bienvenido, usuario");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                        bienvenidoText.setText("Bienvenido, usuario");
                    });
        } else {
            bienvenidoText.setText("Bienvenido, usuario");
        }

        // Botón "Reservar"
        Button reservarBtn = findViewById(R.id.btn_Reservar);
        reservarBtn.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, Reserva.class);
            startActivity(intent);
            finish();
        });

        // Tab "Home"
        LinearLayout homeTab = findViewById(R.id.home);
        homeTab.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, HomePage.class);
            startActivity(intent);
        });

        // Botón "Cuenta"
        LinearLayout cuentaTab = findViewById(R.id.cuenta);
        cuentaTab.setOnClickListener(view -> {
            Intent intent = new Intent(HomePage.this, Perfil.class);
            startActivity(intent);
        });
    }
}
