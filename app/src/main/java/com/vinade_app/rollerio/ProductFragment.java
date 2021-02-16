package com.vinade_app.rollerio;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import es.dmoral.toasty.Toasty;
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
    private ProductsAdapter productsAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<Section> sections = new ArrayList<>();
    ArrayList<Product> products = new ArrayList<>();
    ArrayList<String> favorites = new ArrayList<>();
    Toolbar toolbar;
    RecyclerView gridView;
    AnimationDrawable animationDrawable;
    ImageView anim, backgroundForAnim;
    Spinner spinner;
    private String[] sortBy = {"name", "price"};
    private Toast toast;
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
        setHasOptionsMenu(true);
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
        Thread thread;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                animationStop();
            }
        };thread = new Thread(run);
        thread.start();


        spinner = view.findViewById(R.id.spinnerSort);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.custom_spinner_item, sortBy);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        Collections.sort(products, new Comparator<Product>() {
                            @Override
                            public int compare(Product o1, Product o2) {
                                return o1.getNameProduct().compareTo(o2.getNameProduct());
                            }
                        });
                        productAdapterInit();
                        break;
                    case 1:
                        Collections.sort(products, new Comparator<Product>() {
                            @Override
                            public int compare(Product o1, Product o2) {
                                return o1.getPrice().compareTo(o2.getPrice());
                            }
                        });
                        productAdapterInit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.view = view;
        toolBarInit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDB= database.getReference();
        FirebaseAuth refUser = FirebaseAuth.getInstance();
        FirebaseUser currentUser = refUser.getCurrentUser();

        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(products.size() > 0)
                    products.clear();

                for(DataSnapshot s: snapshot.child(PRODUCTS).getChildren())
                {
                    Product p = s.getValue(Product.class);
                    assert p !=null;
                    products.add(p);
                }

                for(DataSnapshot s: snapshot.child(USERS).getChildren()){
                    if(s.getKey().equals(currentUser.getUid()) && s.getValue(User.class).getFavorites() != null)
                    {
                        HashMap<String, String> fav;
                        fav = s.getValue(User.class).getFavorites();
                        for(Map.Entry<String, String> data: fav.entrySet())
                        { favorites.add(data.getKey()); }
                    }
                }

                productAdapterInit();
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
        sections.add( new Section(String.valueOf(R.drawable.fila), "Fila skates"));
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
    private void productAdapterInit()
    {
        gridView = view.findViewById(R.id.gridView);
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        gridView.setLayoutManager(gridLayoutManager);
        updateAdapterProduct(products);
    }
    private void updateAdapterProduct(ArrayList<Product> prdct)
    {
        productsAdapter = new ProductsAdapter(view.getContext(),prdct,favorites , false);
        gridView.setAdapter(productsAdapter);
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
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setPadding(30,0,0,0);
        searchView.setOnCloseListener(new androidx.appcompat.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setBackground(getResources().getDrawable(R.color.mainColor));
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setBackground(getResources().getDrawable(R.drawable.style_for_search, null));

            }
        });

        searchView.setQueryHint("Enter");
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productsAdapter.getFilter().filter(newText);
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setTitle(TOOL_BAR_TITLE);
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(String position) {
        if(toast != null)
        { toast.cancel();}
        toast = Toasty.custom(getContext(), position, getResources().getDrawable(R.drawable.filter,null), getResources().getColor(R.color.mainColor, null), getResources().getColor(R.color.white, null), Toasty.LENGTH_SHORT, true, true);
        toast.show();


        ArrayList<Product> newProducts = new ArrayList<>();
        switch(position)
        {
            case "Seba":
                updateSectionClick(newProducts, position);
                break;
            case "Rollerblade":
                updateSectionClick(newProducts, position);
                break;
            case "Fila skates":
                updateSectionClick(newProducts, position);
                break;
            case "K2":
                updateSectionClick(newProducts, position);
                break;
            case "Powerslide":
                updateSectionClick(newProducts, position);
                break;
        }

    }
    private void updateSectionClick(ArrayList<Product> newProducts, String position)
    {
        for(Product p: products)
        {
            if(p.getCompany().equals(position))
            {
                newProducts.add(p);
            }
        }
        updateAdapterProduct(newProducts);
    }
}