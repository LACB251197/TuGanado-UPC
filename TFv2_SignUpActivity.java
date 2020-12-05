package com.upc.yourlivestock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.upc.yourlivestock.interfaces.ClientApi;
import com.upc.yourlivestock.models.ClientRequest;
import com.upc.yourlivestock.models.SimpleResponse;
import com.upc.yourlivestock.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText txtDocumentNumber = (EditText) findViewById(R.id.txtClientDocumentNumber);
        final EditText txtName = (EditText) findViewById(R.id.txtClientName);
        final EditText txtEmailAddress = (EditText) findViewById(R.id.txtClientEmailAddress);
        final EditText txtPassword = (EditText) findViewById(R.id.txtClientPassword);
        final EditText txtAddress = (EditText) findViewById(R.id.txtClientAddress);
        final EditText txtPhoneNumber = (EditText) findViewById(R.id.txtClientPhone);

        Button btnClientRegister = (Button) findViewById(R.id.btnClientRegister);
        btnClientRegister.setOnClickListener((view) -> {
            ClientRequest clientRequest = new ClientRequest();
            clientRequest.setDocumentNumber(txtDocumentNumber.getText().toString());
            clientRequest.setName(txtName.getText().toString());
            clientRequest.setEmailAddress(txtEmailAddress.getText().toString());
            clientRequest.setPassword(txtPassword.getText().toString());
            clientRequest.setAddress(txtAddress.getText().toString());
            clientRequest.setPhoneNumber(txtPhoneNumber.getText().toString());
            createClient(clientRequest);
        });
    }

    private void createClient (ClientRequest clientRequest){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://yourlivestock-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ClientApi clientApi = retrofit.create(ClientApi.class);
        Call<SimpleResponse> call = clientApi.createClient(clientRequest);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    if (response.isSuccessful()){
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse.getCode().equals(Constant.Number.ZERO)){
                            Toast.makeText(SignUpActivity.this, "Successful registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
