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
        itemHome.setIcon(getResources().getDrawable(R.drawable.home_active, null));
        itemProfile = menu.findItem(R.id.itemProfile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                itemFavorite.setEnabled(true);
                itemFavorite.setIcon(getResources().getDrawable(R.drawable.favorite, null));
                itemHome.setEnabled(true);
                itemHome.setIcon(getResources().getDrawable(R.drawable.home, null));
                itemProfile.setEnabled(true);
                itemProfile.setIcon(getResources().getDrawable(R.drawable.profile, null));
                switch(item.getItemId())
                {

                    case R.id.itemFavorite:
                        fragment = new FavoriteFragment();
                        itemFavorite.setIcon(getResources().getDrawable(R.drawable.favorite_active, null));
                         itemFavorite.setEnabled(false);
                        break;
                    case R.id.itemHome:
                        fragment = new ProductFragment();
                        itemHome.setIcon(getResources().getDrawable(R.drawable.home_active, null));
                        itemHome.setEnabled(false);
                        break;
                    case R.id.itemProfile:
                        fragment = new ProfilFragment();
                        itemProfile.setIcon(getResources().getDrawable(R.drawable.profile_active, null));
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