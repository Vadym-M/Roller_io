package com.vinade_app.rollerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductPageActivity extends AppCompatActivity {
    private String FAVORITES = "favorites";
    private String USERS = "Users";
    private String PRODUCTS = "Products";
    private String CURRENT_USER;
    private final String KEY_ID_PRODUCT = "id";
    private final String KEY_FAVORITES = "favorite";
    private final String REF_STORAGE = "gs://roller-io-ff7bb.appspot.com/";

    private ImageView imageView;
    private TextView details, productName, price;
    private ImageButton btnBack, favorite;
    boolean change = false;
    private DatabaseReference refDB;
    private FirebaseUser currentUser;

    private ArrayList<String> favorites = new ArrayList<>();
    private Product product;
    private String idProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        initFirebaseDatabase();
        initFirebaseAuth();
        initView();

        initFavoriteBtn();

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change)
                {
                    favorite.setBackgroundResource(R.drawable.favorite);
                    refDB.child(USERS).child(CURRENT_USER).child(FAVORITES).child(idProduct).removeValue();
                    Toast.makeText(ProductPageActivity.this, "Removed from favorites!", Toast.LENGTH_SHORT).show();

                }else{
                    favorite.setBackgroundResource(R.drawable.favorite_active);
                    refDB.child(USERS).child(CURRENT_USER).child(FAVORITES).child(product.getId()).setValue(product.getNameProduct());
                    Toast.makeText(ProductPageActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                }
                change = !change;
            }
        });

        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot d: snapshot.child(PRODUCTS).getChildren())
                {
                    if(d.getValue(Product.class).getId().equals(idProduct))
                    {
                        product = d.getValue(Product.class);
                        FirebaseStorage storage = FirebaseStorage.getInstance(REF_STORAGE);
                        StorageReference storageRef = storage.getReference();
                        storageRef.child(product.getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri.toString()).into(imageView);
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
    private void initFirebaseDatabase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refDB= database.getReference();
    }
    private void initFirebaseAuth()
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        CURRENT_USER = currentUser.getUid();
    }
    private void initView()
    {
        imageView = findViewById(R.id.productImgPage);
        details = findViewById(R.id.productDetails);
        productName = findViewById(R.id.productNamePage);
        price = findViewById(R.id.productPricePage);
        btnBack = findViewById(R.id.btnBack);
        favorite = findViewById(R.id.productFavoritePage);
    }

    private void initFavoriteBtn()
    {
        Intent intent = getIntent();
        idProduct =  intent.getStringExtra(KEY_ID_PRODUCT);
        favorites =  intent.getStringArrayListExtra(KEY_FAVORITES);
        if(favorites != null){
            for(String s: favorites)
            {
                if(s.equals(idProduct))
                { favorite.setBackgroundResource(R.drawable.favorite_active);
                    change = true;
                }
            }
        }else
        {
            favorite.setBackgroundResource(R.drawable.favorite_active);
            change = true;
        }

    }

}