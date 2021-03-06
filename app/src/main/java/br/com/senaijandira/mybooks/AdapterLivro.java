package br.com.senaijandira.mybooks;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
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
import br.com.senaijandira.mybooks.model.EditarLivro;
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

            view = LayoutInflater.from(getContext()).inflate(R.layout.livro_layout, parent, false);
        }

        final Livro livro = getItem(position);

        ImageView imgLivroCapa = view.findViewById(R.id.imgLivroCapa);
        TextView txtLivroTitulo = view.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = view.findViewById(R.id.txtLivroDescricao);

        ImageView imgDeleteLivro = view.findViewById(R.id.imgDeleteLivro);
        ImageView imgEditarLivro = view.findViewById(R.id.imgEditarLivro);
        ImageView imgLivrosLer = view.findViewById(R.id.imgLivrosLer);
        ImageView imgLivrosLidos = view.findViewById(R.id.imgLivrosLidos);


        txtLivroTitulo.setText(livro.getTitulo());
        imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));
        txtLivroDescricao.setText(livro.getDescricao());


        imgDeleteLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (livro.getStatus() == 1 || livro.getStatus() == 2){

                    Toast.makeText(getContext(), "Não é possível excluir o livro enquanto estiver em outra lista", Toast.LENGTH_SHORT).show();

                } else {

                    appDB.livroDao().deletar(livro);

                    remove(livro);

                }


            }
        });

        imgEditarLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), EditarLivro.class);
                intent.putExtra("livro", livro.getId());


                getContext().startActivity(intent);

            }
        });

        imgLivrosLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Livro livroLer = new Livro(livro.getId(), livro.getCapa(), livro.getTitulo(), livro.getDescricao(), 1);

                appDB.livroDao().atualizar(livroLer);
                Livro[] livros = appDB.livroDao().selecionarTodos();

                if (livro.getStatus() != 1)
                    Toast.makeText(getContext(), "Livro adicionado na lista de ler", Toast.LENGTH_SHORT).show();




            }
        });

        imgLivrosLidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Livro livroslidos = new Livro(livro.getId(), livro.getCapa(), livro.getTitulo(), livro.getDescricao(), 2);

                appDB.livroDao().atualizar(livroslidos);

                Livro[] livros = appDB.livroDao().selecionarTodos();


                if (livro.getStatus() != 2)
                    Toast.makeText(getContext(), "Livro adicionado na lista de lidos", Toast.LENGTH_SHORT).show();



            }
        });




        return view;
    }
}
