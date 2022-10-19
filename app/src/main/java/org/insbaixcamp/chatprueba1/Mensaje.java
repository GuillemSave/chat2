package org.insbaixcamp.chatprueba1;

import androidx.recyclerview.widget.RecyclerView;

public class Mensaje {

    private String id;
    private String name;
    private String message;

    public Mensaje (){

    }
    public Mensaje (String id, String name, String message){
        this.name = name;
        this.message = message;
    }
    public String getId(){return this.id;}
    public String getName() {
        return this.name;
    }
    public String getMessage(){
        return this.message;
    }


}
