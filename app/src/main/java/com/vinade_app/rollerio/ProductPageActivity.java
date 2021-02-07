package com.vinade_app.rollerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProductPageActivity extends AppCompatActivity {
    ImageView imageView;
    TextView details, productName, price;
    ImageButton btnBack, favorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDB= database.getReference("Products");
        setContentView(R.layout.activity_product_page);
        Intent intent = getIntent();
        String idProduct =  intent.getStringExtra("id");
        imageView = findViewById(R.id.productImgPage);
        details = findViewById(R.id.productDetails);
        productName = findViewById(R.id.productNamePage);
        price = findViewById(R.id.productPricePage);
        btnBack = findViewById(R.id.btnBack);
        favorite = findViewById(R.id.productFavoritePage);
        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d: snapshot.getChildren())
                {
                    if(d.getValue(Product.class).getId().equals(idProduct))
                    {
                        Product product = d.getValue(Product.class);
                        FirebaseStorage storage = FirebaseStorage.getInstance("gs://roller-io-ff7bb.appspot.com/");
                        StorageReference storageRef = storage.getReference();
                        storageRef.child(product.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("debug", "URI : " + uri.toString());
                                Picasso.get().load(uri.toString()).into(imageView);
                                //holder.imageView.setImageResource(Integer.parseInt(products.get(position).getImg()));
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductPageActivity.this,"Error loading image...",Toast.LENGTH_SHORT).show();
                            }
                        });
                        details.setText(product.getDetails());
                        productName.setText(product.getNameProduct());
                        price.setText(product.getPrice());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}