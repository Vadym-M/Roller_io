package com.vinade_app.rollerio;

import android.content.Context;
import android.content.Intent;
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
import java.util.Map;

public class ProductAdapterFavorite extends RecyclerView.Adapter<ProductAdapterFavorite.ViewHolderFavorite> {
    private final String FAVORITES = "favorites";
    private final String USERS = "Users";
    private final String KEY_ID_PRODUCT = "id";
    private String CURRENT_USER;

    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    int pos = 0;
    Context context;
    private ArrayList<Product> products;
    private final LayoutInflater inflater;

    public ProductAdapterFavorite(Context context, ArrayList<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolderFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorite, parent, false);

        return new ViewHolderFavorite(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolderFavorite holder, int position) {
        loaderImg(position, holder);
        updateData();


        holder.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProduct(position);
            }
        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pos = position;
                Intent intent = new Intent(context, ProductPageActivity.class);
                intent.putExtra(KEY_ID_PRODUCT, products.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.productName.setText(products.get(position).getNameProduct());
        holder.productPrice.setText(products.get(position).getPrice());
    }

    private void removeProduct(int pos)
    {
        databaseReference.child(USERS).child(CURRENT_USER).child(FAVORITES).child(products.get(pos).getId()).removeValue();
        products.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, products.size());




    }

    private void updateData()
    {
        databaseReference.child(USERS).child(CURRENT_USER).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> updateFavorite = new ArrayList<>();
                Product pr = null;
                HashMap<String, String> fav;
                fav = snapshot.getValue(User.class).getFavorites();
                for (Map.Entry<String, String> data : fav.entrySet()) {
                    updateFavorite.add(data.getKey());
                }
                if(updateFavorite.size() != products.size()){
                    for(Product p: products)
                    {
                        for(int i = 0; i<products.size(); i++){
                        if(!p.getId().equals(products.get(i).getId()))
                         {
                            pr = p;
                         }
                        }
                    }
                if(pr!=null)
                {
                    products.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, products.size());
                }
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loaderImg(int position, ViewHolderFavorite holder)
    {
        storageReference.child(products.get(position).getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(holder.productImgFavorite);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Error loading image...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolderFavorite extends RecyclerView.ViewHolder {
        ImageButton favoriteBtn;
        ImageView productImgFavorite;
        TextView productName, productPrice;
        Button details;
        public ViewHolderFavorite(@NonNull View itemView) {
            super(itemView);
            firebaseStorage = FirebaseStorage.getInstance();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            storageReference = FirebaseStorage.getInstance().getReference();
            currentUser = firebaseAuth.getCurrentUser();
            favoriteBtn = itemView.findViewById(R.id.btnFavorite);
            productName = (TextView) itemView.findViewById(R.id.productNameFavorite);
            productImgFavorite = (ImageView) itemView.findViewById(R.id.productImgFavorite);
            productPrice = itemView.findViewById(R.id.productPriceFavorite);
            details = (Button) itemView.findViewById(R.id.productDetailsFavorite);
            CURRENT_USER = currentUser.getUid();
        }

    }
}
