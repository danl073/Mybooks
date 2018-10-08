package br.com.senaijandira.mybooks;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.senaijandira.mybooks.Fragments.Livros_lidos;
import br.com.senaijandira.mybooks.Fragments.Livros_para_ler;
import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.model.Utils;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;

    BottomNavigationView bottom_nav;

    LinearLayout ListaLivros;

    public static Livro[] livros;

    private MyBooksDatabase myBooksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fm = getSupportFragmentManager();

        //Criando a instancia do Banco de Dados
        myBooksDatabase = Room.databaseBuilder(getApplicationContext(),
                MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        ListaLivros = findViewById(R.id.ListaLivros);

        //Criar um Livro

        livros = new Livro[]{
                /*
                  new Livro(1,Utils.toByteArray(getResources(), R.drawable.pequeno_principe),
                          "O pequeno principe", getString(R.string.pequeno_pricipe)),

                  new Livro(2,Utils.toByteArray(getResources(), R.drawable.cinquenta_tons_cinza),
                          "50 Tons De Cinza", getString(R.string.pequeno_pricipe)),

                  new Livro(3,Utils.toByteArray(getResources(), R.drawable.kotlin_android),
                          "Kotlin com Android", getString(R.string.pequeno_pricipe)),

  */

        };


        bottom_nav = findViewById(R.id.menu_bottom_nav);

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_frag1){
                    openContextMenu(null);
                    return true;
                }

                if (item.getItemId() == R.id.menu_frag2){
                    openFragment1(null);
                    return true;
                }

                if (item.getItemId() == R.id.menu_frag3){
                    openFragment2(null);
                    return true;
                }


                return false;
            }
        });

    }


    public void openFragment1(View view) {

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frame_layout, new Livros_para_ler());


        ft.commit();

    }


    public void openFragment2(View view) {

        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.frame_layout, new Livros_lidos());


        ft.commit();
    }



    @Override
    protected void onResume() {
        super.onResume();

        //Aqui faz um select no banco
        livros =myBooksDatabase.livroDao().selecionarTodos();


        ListaLivros.removeAllViews();

        for (Livro l : livros){
            criarLivro(l, ListaLivros);
        }
    }

    private void deletarLivro(Livro livro, View v){

        //Remover do banco de dados
        myBooksDatabase.livroDao().deletar(livro);

        //Remover item da dela
        ListaLivros.removeView(v);

    }


    public void criarLivro( final Livro livro, ViewGroup root){

       final View v = LayoutInflater.from(this).inflate(R.layout.livro_layout, root, false);


        ImageView imgLivroCapa = v.findViewById(R.id.imgLivroCapa);
        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);

        ImageView imgDeleteLivro = v.findViewById(R.id.imgDeleteLivro);


        imgDeleteLivro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletarLivro(livro, v);
            }
        });


        //Setando a imagem
        imgLivroCapa.setImageBitmap(Utils.toBitmap(livro.getCapa()));

        txtLivroTitulo.setText(livro.getTitulo());
        txtLivroDescricao.setText(livro.getDescricao());

        //Exibir na tela

        root.addView(v);




    }

    public void abrirCadastro(View v ) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

}
