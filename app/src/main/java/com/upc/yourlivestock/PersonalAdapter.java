package com.upc.yourlivestock;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.upc.yourlivestock.models.ItemLivestock;

import java.util.ArrayList;

public class PersonalAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ItemLivestock> listItemLivestock;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private static final int REQUEST_CALL = 1;
    //ImageView viewItemPhoto;

    public PersonalAdapter(Context context, ArrayList<ItemLivestock> listItemLivestock) {
        this.context = context;
        this.listItemLivestock = listItemLivestock;
    }

    @Override
    public int getCount() {
        return listItemLivestock.size();
    }

    @Override
    public Object getItem(int position) {
        return listItemLivestock.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemLivestock itemLivestock = (ItemLivestock) getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.item_livestock, null);

        ImageView viewItemPhoto = (ImageView) convertView.findViewById(R.id.viewItemPhoto);
        StorageReference imageReference = storageReference.child(itemLivestock.getItemPhoto());
        long MAXBYTES = 1024 * 1024;
        imageReference.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewItemPhoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        TextView viewItemRace = (TextView) convertView.findViewById(R.id.viewItemRace);
        TextView viewItemGender = (TextView) convertView.findViewById(R.id.viewItemGender);
        TextView viewItemColor = (TextView) convertView.findViewById(R.id.viewItemColor);
        TextView viewItemYears = (TextView) convertView.findViewById(R.id.viewItemYears);
        TextView viewItemOwner = (TextView) convertView.findViewById(R.id.viewItemOwner);
        Button btnItemContact = (Button) convertView.findViewById(R.id.btnItemContact);
        Button btnItemMaps = (Button) convertView.findViewById(R.id.btnItemMaps);

        //retrieveFirebaseStorage();
        viewItemRace.setText(itemLivestock.getItemRace());
        viewItemGender.setText(itemLivestock.getItemGender());
        viewItemColor.setText(itemLivestock.getItemColor());
        viewItemYears.setText(itemLivestock.getItemYears());
        viewItemOwner.setText(itemLivestock.getItemOwner());

        btnItemContact.setOnClickListener((view) -> {
            makePhoneCall(itemLivestock.getItemContact());
        });

        return convertView;
    }

    /*private void retrieveFirebaseStorage(String itemPhoto) {
        System.out.println(itemPhoto);
        StorageReference imageReference = storageReference.child(itemPhoto);
        long MAXBYTES = 1024 * 1024;
        imageReference.getBytes(MAXBYTES).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                viewItemPhoto.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }*/

    private void makePhoneCall(String itemContact){
        if (itemContact.trim().length() >0){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            } else {
                String call = "tel:" + itemContact;
                context.startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(call)));
            }
        } else {
            Toast.makeText(context, "Number of contact is null", Toast.LENGTH_SHORT).show();
        }
    }

}
