package com.example.mindlog;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //region BD
    private Database db;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //region inicialization BD
        db = new Database(this);
        //endregion

        //region navegation to create account
        findViewById(R.id.forgotpass).setOnClickListener(v -> {
            Functions.navigateTo(this, MainActivity_create.class);
        });
        //endregion

        //region see password
        EditText passwordEditText = findViewById(R.id.password);
        passwordEditText.setOnClickListener(v -> Functions.togglePasswordVisibility(passwordEditText, this));
        //endregion

        //region click login
        findViewById(R.id.loginbtn).setOnClickListener(v -> {

            //region get user and password
            EditText usernameEditText = findViewById(R.id.username);
            EditText passwordText = findViewById(R.id.password);

            String username = usernameEditText.getText().toString();
            String password = passwordText.getText().toString();
            //endregion

            //region validation authentication
            if (username.isEmpty() || password.isEmpty()) {
                TextView errorMessageText = findViewById(R.id.errorMessage);
                errorMessageText.setText("Both fields must be filled");
                errorMessageText.setVisibility(View.VISIBLE);
            } else {

                boolean isAuthenticated = db.authenticateUser(username, password);
                Log.d("AuthenticationStatus", "Is user authenticated? " + isAuthenticated);
                if (isAuthenticated) {
                    TextView errorMessageText = findViewById(R.id.errorMessage);
                    errorMessageText.setText("Sucesso ao entrar!");
                    errorMessageText.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                    errorMessageText.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //region clean fields
                            EditText usernameEditText = findViewById(R.id.username);
                            EditText passwordEditText = findViewById(R.id.password);

                            Functions functions = new Functions();
                            functions.setUsername(username);

                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            errorMessageText.setText("");
                            //endregion

                            // Navegar para InicioActivity após login
                            Functions.navigateTo(MainActivity.this, InicioActivity.class);
                        }
                    }, 1200);

                } else {
                    TextView errorMessageText = findViewById(R.id.errorMessage);
                    errorMessageText.setText("Nome invalido ou senha incorreta");
                    errorMessageText.setVisibility(View.VISIBLE);
                }
            }
            //endregion
        });
        //endregion

        //region delete users
        // db.deleteAllUsers();
        //endregion

    }

    //region save state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        try {

            //region fields
            EditText usernameEditText = findViewById(R.id.username);
            EditText passwordEditText = findViewById(R.id.password);
            //endregion

            //region save
            outState.putString("username", usernameEditText.getText().toString());
            outState.putString("password", passwordEditText.getText().toString());
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving state", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region restore state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        try {

            //region get the save fields
            String username = savedInstanceState.getString("username");
            String password = savedInstanceState.getString("password");
            //endregion

            //region get fields
            EditText usernameEditText = findViewById(R.id.username);
            EditText passwordEditText = findViewById(R.id.password);
            //endregion

            //region put in layout
            usernameEditText.setText(username);
            passwordEditText.setText(password);
            //endregion

        } catch (Exception e) {
            e.printStackTrace(); // Log do erro (opcional)
            Toast.makeText(this, "Error restoring state", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

}
