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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdicionarActivity extends AppCompatActivity {

    EditText edTitle, edDesc;

    Button button;

    NoteHelper noteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adicionar);

        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        button = findViewById(R.id.addButton);
        noteHelper = new NoteHelper(AdicionarActivity.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edTitle.length() > 0 && edDesc.length() > 0) {


                    noteHelper.insertData(edTitle.getText().toString(), edDesc.getText().toString());
                    Toast.makeText(AdicionarActivity.this, "Sucesso ao Adicionar", Toast.LENGTH_SHORT).show();
                    edDesc.setText("");
                    edTitle.setText("");
                    startActivity(new Intent(AdicionarActivity.this, InicioActivity.class));

                } else {

                    Toast.makeText(AdicionarActivity.this, "Caixa Está Vazia", Toast.LENGTH_SHORT).show();


                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AdicionarActivity.this, InicioActivity.class));
        super.onBackPressed();

    }
}
