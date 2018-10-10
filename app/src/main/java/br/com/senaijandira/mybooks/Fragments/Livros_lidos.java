package br.com.senaijandira.mybooks.Fragments;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.com.senaijandira.mybooks.AdapterLivroLido;
import br.com.senaijandira.mybooks.R;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.model.Utils;


public class Livros_lidos extends Fragment {
    ListView listViewLivros;

    AdapterLivroLido adapter;

    MyBooksDatabase appDB;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.livros_lidos, container, false);

        appDB = Room.databaseBuilder(getContext(), MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        listViewLivros = v.findViewById(R.id.list_view);

        adapter = new AdapterLivroLido(getContext());

        listViewLivros.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        atualizar();

    }

    public void atualizar(){

        adapter.clear();

        Livro[] livros = appDB.livroDao().selecionarLivrosLidos();

        adapter.addAll(livros);



    }

}
