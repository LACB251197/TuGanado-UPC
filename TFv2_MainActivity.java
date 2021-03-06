package com.upc.yourlivestock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.upc.yourlivestock.interfaces.ClientApi;
import com.upc.yourlivestock.models.AuthRequest;
import com.upc.yourlivestock.models.AuthResponse;
import com.upc.yourlivestock.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText txtDocumentNumber = (EditText) findViewById(R.id.txtLoginDocumentNumber);
        final EditText txtPassword = (EditText) findViewById(R.id.txtLoginPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener((view) -> {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setDocumentNumber(txtDocumentNumber.getText().toString());
            authRequest.setPassword(txtPassword.getText().toString());
            login(authRequest);
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignup:
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login(AuthRequest authRequest) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://yourlivestock-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ClientApi clientApi = retrofit.create(ClientApi.class);
        Call<AuthResponse> call = clientApi.authentication(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                try {
                    if (response.isSuccessful()){
                        AuthResponse authResponse = response.body();
                        if (authResponse.getCode().equals(Constant.Number.ZERO)){
                            AuthResponse.Client client = authResponse.getBody();

                            Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                            intent.putExtra("clientName",client.getName());
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid user", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
