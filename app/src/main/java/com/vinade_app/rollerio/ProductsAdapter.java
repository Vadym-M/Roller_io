package com.vinade_app.rollerio;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder2> implements Filterable {

    private final String FAVORITES = "favorites";
    private final String USERS = "Users";
    private final String KEY_ID_PRODUCT = "id";
    private final String KEY_FAVORITES = "favorite";
    private final String REF_STORAGE = "gs://roller-io-ff7bb.appspot.com/";
    private String CURRENT_USER;

    private boolean isRecommendations = false;
    private FirebaseAuth mAuth;
    private final LayoutInflater inflater;
    ArrayList<Product> products;
    ArrayList<Product> filterProducts;
    ArrayList<String> favorites;
    FirebaseUser currentUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    Context context;

    public ProductsAdapter(Context context, ArrayList<Product> products, ArrayList<String> favorites , boolean isRecommendations) {
        this.isRecommendations = isRecommendations;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.context = context;
        this.favorites = favorites;
        filterProducts = new ArrayList<>(products);

    }


    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        return new ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
        Picasso.get().load(products.get(position).getUrl()).into(holder.imageView);

    if(holder.cardView != null) {
    holder.cardView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ProductPageActivity.class);
            intent.putExtra(KEY_ID_PRODUCT, products.get(position).getId());
            intent.putStringArrayListExtra(KEY_FAVORITES, favorites);
            context.startActivity(intent);
        }
    });
    }
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
            Toasty.normal(context,"Removed from favorites!", context.getResources().getDrawable(R.drawable.favorite_for_toast, null)).show();

        } else {
            holder.button2.setBackgroundResource(R.drawable.favorite_active);
            databaseReference.child(USERS).child(CURRENT_USER).child(FAVORITES).child(products.get(position).getId()).setValue(products.get(position).getNameProduct());
            Toasty.normal(context,"Added to favorites!", context.getResources().getDrawable(R.drawable.favorite_active_for_toast, null)).show();
        }
        //UpdateFavorite(position, holder);

    }

    private void checkFavorite(ViewHolder2 holder, int position) {

        boolean isFav = false;
        if(products.size()>position){
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
    }}


    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Product> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length()==0)
            {
                filteredList.addAll(filterProducts);
            }else
                {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for(Product p: filterProducts)
                    {
                        if(p.getNameProduct().toLowerCase().contains(filterPattern))
                        {
                            filteredList.add(p);
                        }
                    }
                }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            products.clear();
            products.addAll((Collection<? extends Product>) results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder2 extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView, textView2;
        Button button;
        ImageButton button2;
        View view;
        CardView cardView;
        RelativeLayout relativeLayout;
        LinearLayout backgroundTextItem;


        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            imageView = itemView.findViewById(R.id.productImg);
            textView = itemView.findViewById(R.id.productName);
            textView2 = itemView.findViewById(R.id.productPrice);
            button = itemView.findViewById(R.id.productDetails);
            button2 = itemView.findViewById(R.id.productFavorite);
            backgroundTextItem = itemView.findViewById(R.id.backgroundItemSeeAlso);
            mAuth = FirebaseAuth.getInstance();
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLay);
            currentUser = mAuth.getCurrentUser();
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            CURRENT_USER = currentUser.getUid();

            if(isRecommendations)
            {
                LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams paramsLl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                paramsText.weight = 1.0f;
                paramsText.gravity = Gravity.CENTER_HORIZONTAL;

                Collections.shuffle(products);
                cardView = itemView.findViewById(R.id.card);
                imageView.getLayoutParams().height= 250;
                cardView.getLayoutParams().height = 500;
                cardView.getLayoutParams().width = 500;
                cardView.setBackground(context.getResources().getDrawable(R.drawable.style_for_recycler_see_also, null));
                textView.setTextSize(15);
                textView.setLayoutParams(paramsText);
                textView2.setTextSize(17);
                textView2.setPadding(0,20,0,0);
                textView2.setLayoutParams(paramsText);
                backgroundTextItem.setBackgroundColor(Color.parseColor("#F1F3F6"));
                backgroundTextItem.setLayoutParams(paramsLl);
                button.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.GONE);
            }
        }

    }
}
