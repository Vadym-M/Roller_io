package com.vinade_app.rollerio;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements SectionAdapter.OnSectionListener, ProductsAdapter.OnProductsListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Section> sections = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductFragment newInstance(String param1, String param2) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        Toolbar toolbar = view.findViewById(R.id.topAppBar);
        sectionLoader();
        recyclerInit(view);
        productAdapterInit(view);
        return view;
    }

    private void sectionLoader()
    {
        sections.add( new Section(String.valueOf(R.drawable.seba), "Seba"));
        sections.add( new Section(String.valueOf(R.drawable.rollerblade), "Rollerblade"));
        sections.add( new Section(String.valueOf(R.drawable.fila), "Fila"));
        sections.add( new Section(String.valueOf(R.drawable.ktwo), "K2"));
        sections.add( new Section(String.valueOf(R.drawable.powerslide), "Powerslide"));
    }
    private void recyclerInit(View view)
    {
        RecyclerView recyclerView = view.findViewById(R.id.recyclreView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(getContext(), sections, this);
        recyclerView.setAdapter(sectionAdapter);
    }
    private void productAdapterInit(View view)
    {
        RecyclerView gridView = view.findViewById(R.id.gridView);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridView.setLayoutManager(gridLayoutManager);
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));
        products.add(new Product("Seba K2X65","2500zl", String.valueOf(R.drawable.example), "Seba", "INFO"));

        ProductsAdapter productsAdapter = new ProductsAdapter(getContext(),products, this);
        gridView.setAdapter(productsAdapter);

        /*// implement setOnItemClickListener event on GridView
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("image", logos[position]); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });

         */
    }


    @Override
    public void onClick(int position) {
        Toast.makeText(getContext(),"EBAAAAAAAAAAT + " + position, Toast.LENGTH_LONG).show();
    }


}