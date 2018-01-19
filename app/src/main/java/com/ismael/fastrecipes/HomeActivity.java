package com.ismael.fastrecipes;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity implements SearchRecipeFragment.SearchFragmentListener {

    private long mBackPressed = 0;
    private static final long MAX_TIME = 2500;
    SearchRecipeFragment searchFragment;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            BottomNavigationView bnvTabMenu = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            bnvTabMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_search_recipes:

                        case R.id.action_my_recipes:

                        case R.id.action_activity:

                        case R.id.action_shoplist:

                        case R.id.action_config:

                    }
                    return true;
                }
            });
        }


    @Override
    public void onBackPressed() {
        if (mBackPressed + MAX_TIME > System.currentTimeMillis() || getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void showSearchFragment() {
        searchFragment = SearchRecipeFragment.getInstance(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, searchFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
