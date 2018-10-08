package br.com.senaijandira.mybooks;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.model.Utils;

public class AdapterLivro extends ArrayAdapter<Livro> {

    MyBooksDatabase appDB;

    public AdapterLivro(Context context){

        super(context, 0, new ArrayList<Livro>());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        appDB = Room.databaseBuilder(getContext(), MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        if (view == null ) {

            view = LayoutInflater.from(getContext()).inflate(R.layout.lista_livros, parent, false);
        }

        final Livro livro = getItem(position);

        ImageView imgLivroCapa = view.findViewById(R.id.imgLivroCapa);
        TextView txtLivroCapa = view.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = view.findViewById(R.id.txtLivroDescricao);

        ImageView imgDeleteLivro = view.findViewById(R.id.imgDeleteLivro);
        ImageView imgEditarLivro = view.findViewById(R.id.imgEditarLivro);
        ImageView imgLivrosLer = view.findViewById(R.id.imgLivrosLer);
        ImageView imgLivrosLidos = view.findViewById(R.id.imgLivrosLidos);


        return view;
    }
}
