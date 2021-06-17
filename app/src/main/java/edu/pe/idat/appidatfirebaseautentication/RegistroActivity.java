package edu.pe.idat.appidatfirebaseautentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.pe.idat.appidatfirebaseautentication.databinding.ActivityLoginBinding;
import edu.pe.idat.appidatfirebaseautentication.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        binding.btnregfirebase.setOnClickListener(view -> {
            if(binding.etemailreg.getText().toString().equals("")
                    && binding.etpasswordreg.getText().toString().equals("")){
                enviarMensaje("Email y password obligatorio");
            }else{
                registrarUsuarioFirebase(binding.etemailreg.getText().toString(),
                        binding.etpasswordreg.getText().toString());
            }
        });


    }

    private void registrarUsuarioFirebase(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            enviarMensaje("Usuario registrado.");
                            startActivity(
                                    new Intent(
                                            getApplicationContext(),
                                            LoginActivity.class
                                    )
                            );
                        } else {
                            // If sign in fails, display a message to the user.
                            enviarMensaje("Error al registrar usuario.");
                        }
                    }
                });
    }

    private void enviarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),
                mensaje, Toast.LENGTH_LONG).show();
    }
}