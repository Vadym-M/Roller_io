package com.vinade_app.rollerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationBarActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch(item.getItemId())
                {

                    case R.id.itemFavorite:
                        fragment = new FavoriteFragment();
                        break;
                    case R.id.itemHome:
                        fragment = new ProductFragment();
                        break;
                    case R.id.itemProfile:
                        fragment = new ProfilFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProductFragment()).commit();

    }

}