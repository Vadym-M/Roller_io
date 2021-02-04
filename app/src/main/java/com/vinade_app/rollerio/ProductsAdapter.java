package com.vinade_app.rollerio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductsAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Product> products = new ArrayList<>();

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.grid_item, null);

        ImageView imageView = convertView.findViewById(R.id.productImg);
        TextView textView = convertView.findViewById(R.id.productName);
        TextView textView2 = convertView.findViewById(R.id.productPrice);
        Button button = convertView.findViewById(R.id.productDetails);
        Button button2 = convertView.findViewById(R.id.productFavorite);
        imageView.setImageResource(Integer.parseInt(products.get(position).getImg()));
        textView.setText(products.get(position).getNameProduct());
        textView2.setText(products.get(position).getPrice());
        return convertView;
    }
}
