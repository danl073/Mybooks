package br.com.senaijandira.mybooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.model.Utils;

public class MainActivity extends AppCompatActivity {

    LinearLayout ListaLivros;

    public static Livro[] livros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       ListaLivros = findViewById(R.id.ListaLivros);

       //Criar um Livro

        livros = new Livro[]{
                new Livro(1,Utils.toByteArray(getResources(), R.drawable.pequeno_principe),
                        "O pequeno principe", getString(R.string.pequeno_pricipe)),

                new Livro(2,Utils.toByteArray(getResources(), R.drawable.cinquenta_tons_cinza),
                        "50 Tons De Cinza", getString(R.string.pequeno_pricipe)),

                new Livro(3,Utils.toByteArray(getResources(), R.drawable.kotlin_android),
                        "Kotlin com Android", getString(R.string.pequeno_pricipe)),



        };
    }


    @Override
    protected void onResume() {
        super.onResume();

        ListaLivros.removeAllViews();

        for (Livro l : livros){
            criarLivro(l, ListaLivros);
        }
    }

    public void criarLivro(Livro livro, ViewGroup root){

        View v = LayoutInflater.from(this).inflate(R.layout.livro_layout, root, false);


        ImageView imgLivroCapa = v.findViewById(R.id.imgLivroCapa);
        TextView txtLivroTitulo = v.findViewById(R.id.txtLivroTitulo);
        TextView txtLivroDescricao = v.findViewById(R.id.txtLivroDescricao);


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
