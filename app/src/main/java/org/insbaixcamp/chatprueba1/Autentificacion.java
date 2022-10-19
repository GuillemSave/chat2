package org.insbaixcamp.chatprueba1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class Autentificacion extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText tiNameAutentificacion;
    private TextInputEditText tiMailAutentificacion;
    private TextInputEditText tiPasswordAutentificacion;
    private Button btSignUp;
    private Button btLogIn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    static private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentificacion);


        btSignUp = findViewById(R.id.btSignUp);
        btLogIn = findViewById(R.id.btLogIn);
        tiNameAutentificacion = findViewById(R.id.tiNameAutentificacion);
        tiMailAutentificacion = findViewById(R.id.tiMailAutentificacion);
        tiPasswordAutentificacion = findViewById(R.id.tiPasswordAutentificacion);
        btLogIn.setOnClickListener(this);
        btSignUp.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();


    }

    public void reload(){
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
                usuario = new Usuario(tiNameAutentificacion.getText().toString(), tiMailAutentificacion.getText().toString());

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
    }

    public void tratarExcepciones(Task<AuthResult> task){
        if(task.getException() instanceof FirebaseAuthWeakPasswordException){
            showError(tiPasswordAutentificacion, getString(R.string.passerr1));
        } else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
            showError(tiMailAutentificacion, getString(R.string.mailerr1));
        } else if(task.getException() instanceof FirebaseAuthUserCollisionException){
            showError(tiMailAutentificacion, getString(R.string.mailerr2));
        } else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
            showError(tiMailAutentificacion, getString(R.string.mailpasserr));
        }else if(task.getException() instanceof FirebaseAuthInvalidUserException){
            showError(tiMailAutentificacion, getString(R.string.mailerr3));
        }
        else {
            Toast.makeText(this, task.getException().toString(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        String name = tiNameAutentificacion.getText().toString();
        String password =  tiPasswordAutentificacion.getText().toString();
        String email = tiMailAutentificacion.getText().toString();
        if(name.isEmpty()){
            showError(tiNameAutentificacion, getString(R.string.insertenombre));
        }
        else if(email.isEmpty()){
            showError(tiMailAutentificacion,getString(R.string.insertemail));
        }
        else if(password.isEmpty()){

            showError(tiPasswordAutentificacion,getString(R.string.passerr2));
        }
        else{
            if (v.getId() == btSignUp.getId()) {
                mAuth.createUserWithEmailAndPassword(email,
                        password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reload();
                        } else {
                            tratarExcepciones(task);
                        }
                    }
                });
            } else if (v.getId() == btLogIn.getId()) {
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            reload();
                        } else {
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                showError(tiMailAutentificacion, getString(R.string.mailpasserr));
                            }else {
                                tratarExcepciones(task);
                            }
                        }
                    }
                });
            }
        }
    }

    public void showError(TextInputEditText input, String s){
        input.setError(s);
        input.requestFocus();
    }

    public static Usuario getUsuario() {
        return usuario;
    }
}