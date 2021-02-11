package com.vinade_app.rollerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class NavigationBarActivity extends AppCompatActivity {
private BottomNavigationView bottomNavigationView;
private MenuItem itemFavorite, itemHome, itemProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);
        bottomNavigationView = findViewById(R.id.bottomNav);
        Menu menu =  bottomNavigationView.getMenu();
        itemFavorite = menu.findItem(R.id.itemFavorite);
        itemHome = menu.findItem(R.id.itemHome);
        itemHome.setEnabled(false);
        itemProfile = menu.findItem(R.id.itemProfile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                itemFavorite.setEnabled(true);
                itemHome.setEnabled(true);
                itemProfile.setEnabled(true);
                switch(item.getItemId())
                {

                    case R.id.itemFavorite:
                        fragment = new FavoriteFragment();
                         itemFavorite.setEnabled(false);
                        break;
                    case R.id.itemHome:
                        fragment = new ProductFragment();
                        itemHome.setEnabled(false);
                        break;
                    case R.id.itemProfile:
                        fragment = new ProfilFragment();
                        itemProfile.setEnabled(false);
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();

                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProductFragment()).commit();

    }

}