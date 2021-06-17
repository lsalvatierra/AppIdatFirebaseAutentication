package edu.pe.idat.appidatfirebaseautentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.pe.idat.appidatfirebaseautentication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
        if(usuarioActual != null){
            startActivity(new Intent(getApplicationContext(),
                    MainActivity.class));
        }

        binding.btnloginfirebase.setOnClickListener(view -> {
            if(binding.etemail.getText().toString().equals("")
                    && binding.etpassword.getText().toString().equals("")){
                enviarMensaje("Email y password obligatorio");
            }else{
                autenticacionFirebase(binding.etemail.getText().toString(),
                        binding.etpassword.getText().toString());
            }
        });

        binding.tvregistrarusuario.setOnClickListener(view ->{
            startActivity(new Intent(getApplicationContext(),
                    RegistroActivity.class));
        });

        //Autenticación a Firebase con Google
        binding.btnlogingoogle.setOnClickListener(view ->{
            autenticacionGoogle();
        });



    }
    private void autenticacionGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,
                googleSignInOptions);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1888){
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                autenticarFirebaseConGoogle(account.getIdToken());
            }catch (ApiException ex){

            }
        }
    }

    private void autenticarFirebaseConGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this,
                                    MainActivity.class));
                        }else{
                            enviarMensaje("Error en la autentcación .");
                        }
                    }
                });
    }

    private void autenticacionFirebase(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull  Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    startActivity(
                                            new Intent(
                                                    getApplicationContext(),
                                                    MainActivity.class
                                            )
                                    );
                                }else{
                                    enviarMensaje("Email o password incorrecto.");
                                }
                            }
                        });
    }

    private void enviarMensaje(String mensaje){
        Toast.makeText(getApplicationContext(),
                mensaje, Toast.LENGTH_LONG).show();
    }
}