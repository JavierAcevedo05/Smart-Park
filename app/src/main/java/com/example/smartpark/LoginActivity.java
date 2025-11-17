package com.example.smartpark;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText editTextCorreo, editTextContrasena;
    Button btnLogin;
    FirebaseAuth mAuth;
    TextView textView;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextCorreo = findViewById(R.id.loginCorreo);
        editTextContrasena = findViewById(R.id.loginContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        textView = findViewById(R.id.gotoRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textView = findViewById(R.id.contrasenaOlvidada);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CambiarContrasenyaPrimero.class);
                startActivity(intent);
                finish();
            }
        });

        Button volverBtn = findViewById(R.id.volver3);
        volverBtn.setOnClickListener(view -> {
            finish(); // cierra esta actividad y vuelve a la anterior
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo, contrasena;
                correo = editTextCorreo.getText().toString();
                contrasena = editTextContrasena.getText().toString();

                if(TextUtils.isEmpty(correo)) {
                    Toast.makeText(LoginActivity.this, "Introduce tu correo electrónico", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(contrasena)) {
                    Toast.makeText(LoginActivity.this, "Introduce la contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(correo, contrasena)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Sesión iniciada.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Autenticación Fallida.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}