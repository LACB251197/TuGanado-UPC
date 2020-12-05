package com.upc.yourlivestock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ListViewAutoScrollHelper;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.upc.yourlivestock.interfaces.LivestockApi;
import com.upc.yourlivestock.models.ItemLivestock;
import com.upc.yourlivestock.models.LivestockResponse;
import com.upc.yourlivestock.utils.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportActivity extends AppCompatActivity {

    private PersonalAdapter personalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ArrayList<ItemLivestock> list = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://yourlivestock-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        LivestockApi livestockApi = retrofit.create(LivestockApi.class);
        Call<LivestockResponse> call = livestockApi.getLivestock();
        call.enqueue(new Callback<LivestockResponse>() {
            @Override
            public void onResponse(Call<LivestockResponse> call, Response<LivestockResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        LivestockResponse livestockResponse = response.body();
                        if (livestockResponse.getCode().equals(Constant.Number.ZERO)) {

                            for (LivestockResponse.Livestock livestock : livestockResponse.getBody()) {
                                ItemLivestock itemLivestock = new ItemLivestock(livestock.getPhotoPath(), livestock.getRace(), livestock.getGender(), livestock.getColor(), livestock.getYears(), livestock.getOwner(), livestock.getPhoneNumber(), null);
                                list.add(itemLivestock);
                            }
                            ListView listView = (ListView) findViewById(R.id.listViewReport);
                            ListViewAutoScrollHelper autoScrollHelper = new ListViewAutoScrollHelper(listView);
                            autoScrollHelper.setEnabled(true);

                            personalAdapter = new PersonalAdapter(ReportActivity.this, list);
                            listView.setOnTouchListener(autoScrollHelper);
                            listView.setAdapter(personalAdapter);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ReportActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LivestockResponse> call, Throwable t) {
                Toast.makeText(ReportActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }
}