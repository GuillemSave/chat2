package org.insbaixcamp.chatprueba1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    public static List<Mensaje> chatList;
    private Context context;
    Usuario usuario;

    public RecyclerAdapter(List<Mensaje> chatList, Context context, Usuario usuario){
        this.chatList = chatList;
        this.context = context;
        this.usuario = usuario;

    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.mensaje_list, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        String chatListName = chatList.get(position).getName();
        String userName = usuario.name;
/*
        if(userName.equals(chatListName)){
            holder.cvMensajeEnviado.setCardBackgroundColor(context.getColor(R.color.purple_700));
        } else{
            holder.cvMensajeEnviado.setCardBackgroundColor(context.getColor(R.color.purple_200));
        }
        holder.getMensajeEnviado().setText(chatList.get(position).getMessage());
*/
            if(userName.equals(chatListName)){
            holder.getMensajeEnviado().setText(chatList.get(position).getMessage());
            holder.getTvUsuarioEnviado().setText(chatListName);

        } else {
            holder.getMensajeRecibido().setText(chatList.get(position).getMessage());
            holder.getTvUsuarioRecibido().setText(chatListName);
            }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMensajeRecibido;
        private TextView tvMensajeEnviado;
        private TextView tvUsuarioEnviado;
        private TextView tvUsuarioRecibido;
        CardView cvMensajeRecibido;
        CardView cvMensajeEnviado;


        public ViewHolder(View v){
            super(v);
            tvMensajeRecibido = v.findViewById(R.id.tvMensajeRecibido);
            tvMensajeEnviado = v.findViewById(R.id.tvMensajeEnviado);
            cvMensajeRecibido = v.findViewById(R.id.cvMensajeRecibido);
            cvMensajeEnviado = v.findViewById(R.id.cvMensajeEnviado);
            tvUsuarioEnviado = v.findViewById(R.id.tvUsuarioEnviado);
            tvUsuarioRecibido = v.findViewById(R.id.tvUsuarioRecibido);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        public TextView getMensajeRecibido() {
            //cvMensajeEnviado.setCardBackgroundColor(context.getColor(R.color.background));
            cvMensajeRecibido.setVisibility(View.VISIBLE);
            cvMensajeEnviado.setVisibility(View.GONE);
            return tvMensajeRecibido;
        }
        public TextView getMensajeEnviado() {
            //cvMensajeRecibido.setCardBackgroundColor(context.getColor(R.color.background));
            cvMensajeEnviado.setVisibility(View.VISIBLE);
            cvMensajeRecibido.setVisibility(View.GONE);
            return tvMensajeEnviado;
        }

        public TextView getTvUsuarioEnviado() {
            return tvUsuarioEnviado;
        }

        public TextView getTvUsuarioRecibido() {
            return tvUsuarioRecibido;
        }
    }


}
