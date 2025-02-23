package com.example.mindlog;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity_create extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_main_create);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_create), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Inicializa o banco de dados
            db = new Database(this);

            // Navegação para a tela de login
            findViewById(R.id.forgotpass).setOnClickListener(v -> Functions.navigateTo(MainActivity_create.this, MainActivity.class));

            // Configuração do autocomplete para nacionalidade
            AutoCompleteTextView nationalityTextView = findViewById(R.id.nationality);
            String[] nationalities = getResources().getStringArray(R.array.nationalities);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nationalities);
            nationalityTextView.setAdapter(adapter);
            nationalityTextView.setThreshold(1);

            // Evita crash ao girar a tela enquanto o dropdown está aberto
            nationalityTextView.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    v.postDelayed(() -> {
                        if (!isFinishing() && !isDestroyed()) {
                            nationalityTextView.showDropDown();
                        }
                    }, 300);
                }
            });

            // Alternar visibilidade da senha
            EditText passwordEditText = findViewById(R.id.password);
            passwordEditText.setOnClickListener(v -> Functions.togglePasswordVisibility(passwordEditText, this));

            // Configuração do botão de registro
            Button registerButton = findViewById(R.id.loginbtn);
            registerButton.setOnClickListener(v -> {
                String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
                String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
                String email = ((EditText) findViewById(R.id.Email)).getText().toString().trim();
                String nationality = ((AutoCompleteTextView) findViewById(R.id.nationality)).getText().toString().trim();

                TextView errorMessageText = findViewById(R.id.errorMessage);

                if (username.isEmpty() || password.isEmpty() || email.isEmpty() || nationality.isEmpty()) {
                    errorMessageText.setText("All fields must be filled in");
                    errorMessageText.setVisibility(View.VISIBLE);
                } else {
                    // Tenta adicionar o usuário ao banco de dados
                    String resultMessage = db.addUser(username, password, email, 0);

                    errorMessageText.setText(resultMessage);
                    errorMessageText.setVisibility(View.VISIBLE);

                    // Se o cadastro for bem-sucedido, muda a cor e direciona para o login
                    if (resultMessage.equals("Usuário cadastrado com sucesso!")) {
                        errorMessageText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_light));

                        new Handler().postDelayed(() -> {
                            clearFields();
                            Functions.navigateTo(MainActivity_create.this, MainActivity.class);
                        }, 1200);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing activity", Toast.LENGTH_SHORT).show();
        }
    }

    // Salva o estado da atividade
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putString("username", ((EditText) findViewById(R.id.username)).getText().toString());
            outState.putString("password", ((EditText) findViewById(R.id.password)).getText().toString());
            outState.putString("email", ((EditText) findViewById(R.id.Email)).getText().toString());
            outState.putString("nationality", ((AutoCompleteTextView) findViewById(R.id.nationality)).getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving state", Toast.LENGTH_SHORT).show();
        }
    }

    // Restaura o estado da atividade
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            ((AutoCompleteTextView) findViewById(R.id.nationality)).setText(savedInstanceState.getString("nationality"));

            // Reabre o dropdown se a atividade ainda estiver ativa
            findViewById(R.id.nationality).postDelayed(() -> {
                if (!isFinishing() && !isDestroyed()) {
                    ((AutoCompleteTextView) findViewById(R.id.nationality)).showDropDown();
                }
            }, 500);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error restoring state", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para limpar os campos após o cadastro
    private void clearFields() {
        ((EditText) findViewById(R.id.username)).setText("");
        ((EditText) findViewById(R.id.password)).setText("");
        ((EditText) findViewById(R.id.Email)).setText("");
        ((AutoCompleteTextView) findViewById(R.id.nationality)).setText("");
        ((TextView) findViewById(R.id.errorMessage)).setText("");
    }
}
