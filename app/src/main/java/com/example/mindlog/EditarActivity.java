package com.example.mindlog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class EditarActivity extends AppCompatActivity {
    EditText updateTitle, updateDesc;
    Button updateBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteeBtn);
        updateTitle = findViewById(R.id.updateTitle);
        updateDesc = findViewById(R.id.updateDesc);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Recebe os dados da Intent
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");
        int id = getIntent().getIntExtra("id", 0);
        updateTitle.setText(title);
        updateDesc.setText(desc);

        String sId = String.valueOf(id);

        // Atualizar nota
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateTitle.length() > 0 && updateDesc.length() > 0) {
                    NoteHelper noteHelper = new NoteHelper(EditarActivity.this);
                    noteHelper.updateData(updateTitle.getText().toString(), updateDesc.getText().toString(), sId);
                    Toast.makeText(EditarActivity.this, "Atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditarActivity.this, InicioActivity.class));
                    finish();
                } else {
                    Toast.makeText(EditarActivity.this, "Não foi possível!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Deletar nota
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pega o id da nota que foi passado pela Intent
                int id = getIntent().getIntExtra("id", 0);

                if (id != 0) {
                    // Apaga a nota do banco de dados
                    NoteHelper noteHelper = new NoteHelper(EditarActivity.this);
                    noteHelper.deleteData(String.valueOf(id));

                    // Mostra uma mensagem de sucesso
                    Toast.makeText(EditarActivity.this, "Nota apagada com sucesso!", Toast.LENGTH_SHORT).show();

                    // Volta para a página principal
                    startActivity(new Intent(EditarActivity.this, InicioActivity.class));
                    finish(); // Fecha a tela atual
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditarActivity.this, InicioActivity.class));
        super.onBackPressed();
    }
}