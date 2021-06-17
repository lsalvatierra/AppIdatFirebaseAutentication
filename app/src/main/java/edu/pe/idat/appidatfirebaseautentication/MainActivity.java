package edu.pe.idat.appidatfirebaseautentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.pe.idat.appidatfirebaseautentication.databinding.ActivityLoginBinding;
import edu.pe.idat.appidatfirebaseautentication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseUser usuarioActual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioActual != null) {
            binding.tvemail.setText(usuarioActual.getProviderId());
        }
        binding.btnlogoutfirebase.setOnClickListener(view->{
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(
                    getApplicationContext(),
                    LoginActivity.class
            ));
        });
    }
}