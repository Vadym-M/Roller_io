package com.vinade_app.rollerio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteFragment extends Fragment {

    private final String USERS = "Users";
    private final String PRODUCTS = "Products";
    private String CURRENT_USER;

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<String> favorites = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refDB= database.getReference();

        FirebaseAuth refUser = FirebaseAuth.getInstance();
        FirebaseUser currentUser = refUser.getCurrentUser();
        CURRENT_USER = currentUser.getUid();
        refDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(favorites.size() > 0)
                    favorites.clear();
                for(DataSnapshot s: snapshot.child(USERS).getChildren()){
                    if(s.getKey().equals(CURRENT_USER))
                    {
                        HashMap<String, String> fav;
                        fav = s.getValue(User.class).getFavorites();
                        for(Map.Entry<String, String> data: fav.entrySet())
                        {
                            favorites.add(data.getKey());
                        }
                    }
                }

                if(products.size() > 0)
                    products.clear();

                for(DataSnapshot s: snapshot.child(PRODUCTS).getChildren())
                {
                    Product p = s.getValue(Product.class);
                    assert p !=null;

                    for(int i = 0; i< favorites.size(); i ++)
                    {

                       // Log.d("debug", "Favorites for = "+ favorites.get(i));
                        if(p.getId().equals(favorites.get(i)))
                        {

                            products.add(p);

                        }
                    }

                }

                productAdapterInit(view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
    private void productAdapterInit(View view)
    {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerFavorite);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        ProductAdapterFavorite productsAdapter = new ProductAdapterFavorite(view.getContext(),products);
        recyclerView.setAdapter(productsAdapter);


    }
}