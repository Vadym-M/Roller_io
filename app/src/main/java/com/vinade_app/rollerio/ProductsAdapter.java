package com.vinade_app.rollerio;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;
import java.util.Map;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder2> {

    private final String FAVORITES = "favorites";
    private final String USERS = "Users";
    private final String KEY_ID_PRODUCT = "id";
    private final String KEY_FAVORITES = "favorite";
    private final String REF_STORAGE = "gs://roller-io-ff7bb.appspot.com/";
    private String CURRENT_USER;

    private FirebaseAuth mAuth;
    private final LayoutInflater inflater;
    ArrayList<Product> products;
    ArrayList<String> favorites;
    FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Context context;

    public ProductsAdapter(Context context, ArrayList<Product> products, ArrayList<String> favorites) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.context = context;
        this.favorites = favorites;

    }


    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);

        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {

        FirebaseStorage storage = FirebaseStorage.getInstance(REF_STORAGE);
        StorageReference storageRef = storage.getReference();
        storageRef.child(products.get(position).getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(holder.imageView);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error loading image...", Toast.LENGTH_SHORT).show();
            }
        });
        for (String s : favorites) {
            if (s.equals(products.get(position).getId())) {
                holder.button2.setBackgroundResource(R.drawable.favorite_active);
            }
        }
         UpdateFavorite(position, holder);

        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeIcon(position, holder);
            }
        });

        holder.textView.setText(products.get(position).getNameProduct());
        holder.textView2.setText(products.get(position).getPrice());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductPageActivity.class);
                intent.putExtra(KEY_ID_PRODUCT, products.get(position).getId());
                intent.putStringArrayListExtra(KEY_FAVORITES, favorites);
                context.startActivity(intent);
            }
        });
    }

    private void UpdateFavorite(int position, ViewHolder2 holder) {
        databaseReference.child(USERS).child(CURRENT_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> updateFavorite = new ArrayList<>();

                HashMap<String, String> fav;
                fav = snapshot.getValue(User.class).getFavorites();
                for (Map.Entry<String, String> data : fav.entrySet()) {
                    updateFavorite.add(data.getKey());
                }

                favorites = updateFavorite;
                checkFavorite(holder, position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeIcon(int position, ViewHolder2 holder) {

        boolean isFav = false;
        String idFav = null;

        for (String s : favorites) {
            if (s.equals(products.get(position).getId())) {

                idFav = s;
                isFav = true;
            }
        }
        if (isFav) {
            holder.button2.setBackgroundResource(R.drawable.favorite);
            databaseReference.child(USERS).child(CURRENT_USER).child(FAVORITES).child(idFav).removeValue();
            Toast.makeText(context, "Removed from favorites!", Toast.LENGTH_SHORT).show();

        } else {
            holder.button2.setBackgroundResource(R.drawable.favorite_active);
            databaseReference.child(USERS).child(CURRENT_USER).child(FAVORITES).child(products.get(position).getId()).setValue(products.get(position).getNameProduct());
            Toast.makeText(context, "Added to favorites!", Toast.LENGTH_SHORT).show();
        }
        UpdateFavorite(position, holder);

    }

    private void checkFavorite(ViewHolder2 holder, int position) {
        boolean isFav = false;
        for (String s : favorites) {
            if (s.equals(products.get(position).getId())) {
                isFav = true;
            }
        }
        if (isFav) {
            holder.button2.setBackgroundResource(R.drawable.favorite_active);
        } else {
            holder.button2.setBackgroundResource(R.drawable.favorite);
        }
    }


    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, textView2;
        Button button;
        ImageButton button2;
        View view;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.productImg);
            textView = itemView.findViewById(R.id.productName);
            textView2 = itemView.findViewById(R.id.productPrice);
            button = itemView.findViewById(R.id.productDetails);
            button2 = itemView.findViewById(R.id.productFavorite);
            mAuth = FirebaseAuth.getInstance();
            currentUser = mAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            CURRENT_USER = currentUser.getUid();
        }

    }
}
