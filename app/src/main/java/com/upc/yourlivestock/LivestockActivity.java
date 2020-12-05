package com.upc.yourlivestock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.upc.yourlivestock.interfaces.LivestockApi;
import com.upc.yourlivestock.models.LivestockRequest;
import com.upc.yourlivestock.models.SimpleResponse;
import com.upc.yourlivestock.utils.Constant;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LivestockActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_PERMISSION_CODE = 101;

    private StorageReference storageReference;

    ImageView viewLivestock;
    EditText txtLivestockDateofBirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livestock);

        Spinner spinner = findViewById(R.id.txtLivestockGender);
        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.livestock_gender_array, android.R.layout.simple_spinner_item);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        viewLivestock = (ImageView) findViewById(R.id.viewLivestock);

        txtLivestockDateofBirth = (EditText) findViewById(R.id.txtLivestockDateofBirth);
        txtLivestockDateofBirth.setOnClickListener((view) -> {
            showDatePickerDialog();
        });

        //final EditText txtGender = (EditText) findViewById(R.id.txtLivestockGender);
        final EditText txtType = (EditText) findViewById(R.id.txtLivestockType);
        final EditText txtRace = (EditText) findViewById(R.id.txtLivestockRace);
        final EditText txtColor = (EditText) findViewById(R.id.txtLivestockColor);

        Button btnLocation = (Button) findViewById(R.id.btnLivestockLocation);
        btnLocation.setOnClickListener((view) -> {
            Intent location = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(location);
        });
        Button btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener((view) -> {
            askCameraPermissions();
        });

        Button btnGallery = (Button) findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener((view) -> {
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            gallery.setType("image/*");
            startActivityForResult(gallery, GALLERY_REQUEST_CODE);
        });

        Button btnRegister = (Button) findViewById(R.id.btnLivestockRegister);
        btnRegister.setOnClickListener((view) -> {
            SharedPreferences sharedPreferences = getSharedPreferences("login_data", Context.MODE_PRIVATE);
            int clientId = sharedPreferences.getInt("clientId", 1);

            Integer genderId;
            String text = spinner.getSelectedItem().toString();
            if (text.equals("Hembra")) {
                genderId = 1;
            } else {
                genderId = 2;
            }
            SharedPreferences sharedPreferencesMap = getSharedPreferences("map_data", Context.MODE_PRIVATE);
            Double latitude = Double.parseDouble(sharedPreferencesMap.getString("latitude", "-9.189967"));
            Double longitude = Double.parseDouble(sharedPreferencesMap.getString("longitude", "-75.015152"));

            LivestockRequest livestockRequest = new LivestockRequest();
            livestockRequest.setClientId(clientId);
            livestockRequest.setDateOfBirth(txtLivestockDateofBirth.getText().toString());
            livestockRequest.setGender(genderId);
            livestockRequest.setType(txtType.getText().toString());
            livestockRequest.setRace(txtRace.getText().toString());
            livestockRequest.setColor(txtColor.getText().toString());
            createLivestock(livestockRequest);
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                txtLivestockDateofBirth.setText(selectedDate);
            }
        });
        datePickerFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    private void askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            viewLivestock.setImageBitmap(photo);
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            Uri selectedImage = data.getData();
            viewLivestock.setImageURI(selectedImage);
        }
    }

    private void createLivestock(LivestockRequest livestockRequest) {
        String photoPath = System.currentTimeMillis() + ".jpg";
        StorageReference imageReference = storageReference.child(photoPath);
        imageReference.putBytes(getByteArray(viewLivestock)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(LivestockActivity.this, "Upload photo", Toast.LENGTH_SHORT).show();
            }
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://yourlivestock-app.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        LivestockApi livestockApi = retrofit.create(LivestockApi.class);
        livestockRequest.setPhoto(photoPath);
        Call<SimpleResponse> call = livestockApi.createLivestock(livestockRequest);
        call.enqueue(new Callback<SimpleResponse>() {
            @Override
            public void onResponse(Call<SimpleResponse> call, Response<SimpleResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        SimpleResponse simpleResponse = response.body();
                        if (simpleResponse.getCode().equals(Constant.Number.ZERO)) {
                            Toast.makeText(LivestockActivity.this, "Successful registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(LivestockActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SimpleResponse> call, Throwable t) {
                Toast.makeText(LivestockActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private byte[] getByteArray(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildLayer();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        return data;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}