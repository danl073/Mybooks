package br.com.senaijandira.mybooks;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

import br.com.senaijandira.mybooks.db.MyBooksDatabase;
import br.com.senaijandira.mybooks.model.Livro;
import br.com.senaijandira.mybooks.model.Utils;

public class CadastroActivity extends AppCompatActivity {

    Bitmap LivroCapa;
    ImageView imgLivroCapa;
    EditText txtTitulo, txtDescricao;
    AlertDialog alerta;

    private final int COD_REQ_GALERIA = 101;

    private MyBooksDatabase myBooksDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //Criando a instancia do banco de dados
        myBooksDatabase = Room.databaseBuilder(getApplicationContext(),
                MyBooksDatabase.class, Utils.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        imgLivroCapa = findViewById(R.id.imgLivroCapa);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescricao = findViewById(R.id.txtDescricao);
    }

    public void abriGaleria(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        intent.setType("image/*");

        startActivityForResult(
                Intent.createChooser(intent, "Selecione uma imagem"), COD_REQ_GALERIA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COD_REQ_GALERIA && resultCode == Activity.RESULT_OK) {


            try {

                InputStream input = getContentResolver().openInputStream(data.getData());

                LivroCapa = BitmapFactory.decodeStream(input);

                imgLivroCapa.setImageBitmap(LivroCapa);

            } catch (Exception ex) {

                ex.printStackTrace();
            }

        }

    }
        public void salvarLivro(View v) {

            byte[] capa = Utils.toByteArray(LivroCapa);

            String titulo = txtTitulo.getText().toString();

            String descricao = txtDescricao.getText().toString();

            Livro livro = new Livro(0, capa, titulo, descricao, 0);

            //Inserir na variavel estatica da MainActivity
//            int tamanhoArray = MainActivity.livros.length;
//
//            MainActivity.livros = Arrays.copyOf(MainActivity.livros, tamanhoArray + 1);
//
//            MainActivity.livros[tamanhoArray] = livro;


            //Inserir no banco de dados
            myBooksDatabase.livroDao().inserir(livro);


            alert("Sucesso","Livro Salvo com Sucesso", 1);


        }

        public void alert (String titulo, String mensagem, int tipoAlert) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(titulo);
            builder.setMessage(mensagem);
            builder.setCancelable(false);

            if (tipoAlert == 1 ){

                builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }

            alerta = builder.create();
            alerta.show();
        }
}
