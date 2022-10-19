package org.insbaixcamp.chatprueba1;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String DATABASE_NAME = "https://chat-b1b1d-default-rtdb.europe-west1.firebasedatabase.app/";
    FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_NAME);
    DatabaseReference databaseRoot = database.getReference("Chats");
    RecyclerAdapter recyclerAdapter;
    List<Mensaje> chatList;
    LinearLayoutManager mLayoutManager;
    Button btAutentificacion;
    Usuario usuario;
    ImageView ivEnviar;
    RecyclerView mRecyclerView;
    Scanner leerArchivo;
    InputStream inputStream;
    String[] insultosArray;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        usuario = Autentificacion.getUsuario();
        mRecyclerView = findViewById(R.id.rvMensajes);
        ivEnviar = findViewById(R.id.IbEnviar);
        ivEnviar.setOnClickListener(this);
        chatList = new ArrayList();
        btAutentificacion = findViewById(R.id.btAutentificacion);
        btAutentificacion.setOnClickListener(this);

        recyclerAdapter = new RecyclerAdapter(chatList, MainActivity.this, usuario);
        mRecyclerView.setAdapter(recyclerAdapter);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        inputStream = getResources().openRawResource(R.raw.diccionario);
        leerArchivo = new Scanner(inputStream);
        while (leerArchivo.hasNext()){
            leerArchivo.nextLine();
            i++;
        }


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot item: snapshot.getChildren()){
                    Mensaje mensaje = item.getValue(Mensaje.class);
                    chatList.add(mensaje);
                }

                recyclerAdapter.notifyDataSetChanged();
                setScrollbar(recyclerAdapter.getItemCount() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        databaseRoot.addValueEventListener(postListener);

        crearArray();
    }
    public void setScrollbar(int itemCount){
        mRecyclerView.scrollToPosition(itemCount);
    }



    @Override
    public void onClick(View view) {

        if(view.getId() == ivEnviar.getId()){
            EditText etMensaje = findViewById(R.id.etMensaje);

            if(etMensaje.getText() != null && !etMensaje.getText().toString().equals("") && isAggresive(etMensaje.getText().toString())){
                writeText(etMensaje.getText().toString());
                etMensaje.setText("");
            } else {
            }
        }

    }

    public void crearArray(){
        inputStream = getResources().openRawResource(R.raw.diccionario);
        leerArchivo = new Scanner(inputStream);

        insultosArray = new String[i];
        for(int j = 0; j < insultosArray.length; j++){
            insultosArray[j] = leerArchivo.nextLine();
        }
    }
    public boolean isAggresive(String text){

        for(int i = 0; i < insultosArray.length; i++){
            if(text.toLowerCase(Locale.ROOT).contains(insultosArray[i].toLowerCase(Locale.ROOT))){
                return true;
            }
        }
        return false;
    }

    public void writeText(String text){
        String messageKey = databaseRoot.push().getKey();

        databaseRoot.child(messageKey).setValue( new Mensaje(messageKey, usuario.getName(),text));
    }




    /*  @Override
  public void onClick(View view) {

        String text = "fallé";
        EditText etMensaje = findViewById(R.id.etMensaje);
        if(etMensaje.getText() != null){
            text = etMensaje.getText().toString();
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();

        FirebaseDatabase database = FirebaseDatabase.getInstance(DATABASE_NAME);
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue(text);
    }*/
}