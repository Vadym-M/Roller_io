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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder2> {


    private final LayoutInflater inflater;
    ArrayList<Product> products = new ArrayList<>();
    private OnProductsListener onProductsListener;
    Context context;

    public ProductsAdapter(Context context, ArrayList<Product> products, OnProductsListener mListener) {
        this.onProductsListener = mListener;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.context = context;

    }


    @NonNull
    @Override
    public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);

        return new ViewHolder2(view, onProductsListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {

        FirebaseStorage storage = FirebaseStorage.getInstance("gs://roller-io-ff7bb.appspot.com/");
        StorageReference storageRef = storage.getReference();
        storageRef.child(products.get(position).getImg()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("debug", "URI : " + uri.toString());
                Picasso.get().load(uri.toString()).into(holder.imageView);
                //holder.imageView.setImageResource(Integer.parseInt(products.get(position).getImg()));
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context,"Error loading image...",Toast.LENGTH_SHORT).show();
            }
        });

         products.get(position);

        holder.textView.setText(products.get(position).getNameProduct());
        holder.textView2.setText(products.get(position).getPrice());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductPageActivity.class);
                intent.putExtra("id",products.get(position).getId());
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView, textView2;
        Button button;
        ImageButton button2;
        OnProductsListener onProductsListener;
        View view;
        public ViewHolder2(@NonNull View itemView, OnProductsListener onProductsListener) {
            super(itemView);
            view = itemView;
            imageView =itemView.findViewById(R.id.productImg);
            textView = itemView.findViewById(R.id.productName);
            textView2 = itemView.findViewById(R.id.productPrice);
            button = itemView.findViewById(R.id.productDetails);
            button2 = itemView.findViewById(R.id.productFavorite);
            this.onProductsListener= onProductsListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onProductsListener.onClick(getAdapterPosition());
        }
    }
    public interface OnProductsListener
    {
        void onClick(int position);

    }
}
