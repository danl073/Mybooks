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
import android.widget.Toast;

import java.util.ArrayList;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.model.Utils;

public class AdapterLivroLido extends ArrayAdapter<Livro> {

    MyBooksDatabase appDB;

    public AdapterLivroLido(Context context){

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

            view = LayoutInflater.from(getContext()).inflate(R.layout.layout_livrolido, parent, false);
        }

        final Livro livro = getItem(position);

        ImageView imgLivroCapa = view.findViewById(R.id.imgLivroCapa);
        TextView txtLivroTitulo = view.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = view.findViewById(R.id.txtLivroDescricao);

        ImageView imgRemover = view.findViewById(R.id.imgRemover);
        ImageView imgLivrosLer = view.findViewById(R.id.imgLivrosLer);


        txtLivroTitulo.setText(livro.getTitulo());
        imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));
        txtLivroDescricao.setText(livro.getDescricao());

        imgLivrosLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Livro livroLer = new Livro(livro.getId(), livro.getCapa(), livro.getTitulo(), livro.getDescricao(), 1);

                appDB.livroDao().atualizar(livroLer);

                clear();

                Livro[] livros = appDB.livroDao().selecionarLivrosLidos();


                addAll(livros);

                if (livro.getStatus() != 1)
                    Toast.makeText(getContext(), "Livro adicionado na lista de ler", Toast.LENGTH_SHORT).show();

            }
        });

        imgRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Livro listaLivros = new Livro(livro.getId(), livro.getCapa(), livro.getTitulo(), livro.getDescricao(), 0);

                appDB.livroDao().atualizar(listaLivros);

                clear();

                Livro[] livros = appDB.livroDao().selecionarLivrosLidos();
                Toast.makeText(getContext(), "Livro removido da lista", Toast.LENGTH_SHORT).show();

                addAll(livros);
            }
        });

        return view;
    }
}
