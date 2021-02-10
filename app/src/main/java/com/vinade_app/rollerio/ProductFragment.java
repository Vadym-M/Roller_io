package com.vinade_app.rollerio;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.internal.cache.DiskLruCache;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductFragment extends Fragment implements SectionAdapter.OnSectionListener{
    private final String FAVORITES = "favorites";
    private final String USERS = "Users";
    private final String PRODUCTS = "Products";
    private final String TOOL_BAR_TITLE = "What are you looking for?";
    private final String KEY_ID_PRODUCT = "id";
    private final String KEY_FAVORITES = "favorite";
    private final String REF_STORAGE = "gs://roller-io-ff7bb.appspot.com/";
    private String CURRENT_USER;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Section> sections = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    Toolbar toolbar;
    RecyclerView gridView;
    AnimationDrawable animationDrawable;
    ImageView anim, backgroundForAnim;
    private View view;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
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
        this.view = view;
        animationStart();
        toolBarInit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDB= database.getReference();
        FirebaseAuth refUser = FirebaseAuth.getInstance();
        FirebaseUser currentUser = refUser.getCurrentUser();

        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> favorites = new ArrayList<>();
                if(products.size() > 0)
                    products.clear();

                for(DataSnapshot s: snapshot.child(PRODUCTS).getChildren())
                {
                    Product p = s.getValue(Product.class);
                    assert p !=null;
                    products.add(p);
                }

                for(DataSnapshot s: snapshot.child(USERS).getChildren()){
                    if(s.getKey().equals(currentUser.getUid()))
                    {
                        HashMap<String, String> fav;
                        fav = s.getValue(User.class).getFavorites();
                        for(Map.Entry<String, String> data: fav.entrySet())
                        { favorites.add(data.getKey()); }
                    }
                }
                productAdapterInit(favorites);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        sectionLoader();
        recyclerInit();

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
    private void recyclerInit()
    {
        RecyclerView recyclerView = view.findViewById(R.id.recyclreView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        SectionAdapter sectionAdapter = new SectionAdapter(getContext(), sections, this);
        recyclerView.setAdapter(sectionAdapter);
    }
    private void productAdapterInit(ArrayList<String> favorites)
    {
        gridView = view.findViewById(R.id.gridView);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        gridView.setLayoutManager(gridLayoutManager);

        ProductsAdapter productsAdapter = new ProductsAdapter(view.getContext(),products,favorites);
        gridView.setAdapter(productsAdapter);

        animationStop();

    }
    private void animationStart()
    {
        anim = view.findViewById(R.id.animationProduct);
        backgroundForAnim = view.findViewById(R.id.backgroundWhite);
        anim.setBackgroundResource(R.drawable.loading_animation);
        animationDrawable = (AnimationDrawable) anim.getBackground();
        animationDrawable.start();
    }
    private void animationStop()
    {
        backgroundForAnim.setVisibility(View.INVISIBLE);
        anim.setVisibility(View.INVISIBLE);
        animationDrawable.stop();
    }

    private void toolBarInit()
    {
        toolbar = view.findViewById(R.id.topAppBar);
        toolbar.inflateMenu(R.menu.item_tool_bar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setTitle(TOOL_BAR_TITLE);
    }


    @Override
    public void onClick(int position) {

    }




}