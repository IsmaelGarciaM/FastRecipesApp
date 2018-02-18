package com.ismael.fastrecipes;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.model.User;

import java.util.ArrayList;

/**
 * Clase con la actividad contenedora de toda la aplicacion. Maneja el menu y contiene el objeto con los filtros de busqueda
 *
 */
public class HomeActivity extends AppCompatActivity implements UsersListFragment.UsersListListener, AddIngredientsFragment.AddIngredientsListener, AddRecipeFragment.AddRecipeFragmentListener, RecipeFragment.RecipeFragmentListener, RecipesListFragment.RecipesListListener, ProfileFragment.ProfileListener, MyRecipesFragment.MyRecipeFragmentListener, MyCommentsFragment.MyCommentsFragmentListener, SettingsPreferences.PrefsListener, FavRecipesFragment.FavRecipesListener, SearchRecipeFragment.SearchFragmentListener, SocialActivityFragment.SocialActivityFragmentListener, SearchByIngredientFragment.SearchIngredientsListener, SearchByCategoriesFragment.SearchCategoriesListener{

    int where = -1;
    private long mBackPressed = 0;
    private static final long MAX_TIME = 2500;
    SearchRecipeFragment searchFragment;
    SocialActivityFragment socialFragment;
    SearchByCategoriesFragment sbcFragment;
    SearchByIngredientFragment sbiFragment;
    ProfileFragment profileFr;
    FavRecipesFragment favRecFr;
    AddRecipeFragment addRecipeFr;
    RecipeFragment recipeFrg;
    RecipesListFragment rlFragment;
    AddIngredientsFragment addingFr;
    UsersListFragment ulfFragment;
    Dialog customDialog = null;

    public ArrayList<Filter> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<Filter> filters) {
        this.filters = filters;
    }

    ArrayList<Filter> filters;
    Bundle filtersData;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    User user;
    BottomNavigationView bnvTabMenu;
    boolean settingsOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            bnvTabMenu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
            filters = new ArrayList<>();
            Log.d( "BACKSTACKCOUNT", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        user = new User(20,"ismagm94@gmail.com", "Ismael ", "García", "Málaga", "09/01/18", 2, null, "uid");

/*
            try {
                if(getIntent().getExtras().getParcelable("user") != null) {
                    user = getIntent().getExtras().getParcelable("user");
                }else{
                    user = new User("Anónimo", "email", "dd/mm/aa", "uid");
                }
            }catch (NullPointerException npe) {
                user = new User("Anónimo", "email", "dd/mm/aa", "uid");
            }*/
            bnvTabMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(settingsOp)
                        closeConfig();
                    switch (item.getItemId()) {
                        //muestra el fragment para añadir filtros a una nueva busqueda
                        case R.id.action_search_recipes:
                            //filtersData.putParcelableArrayList("filters", filters);
                            showSearchFragment(filtersData);
                            break;
                            //muestra el fragment con el perfil, las recetas y los comentarios
                        case R.id.action_social:
                            showSocialFragment();
                            break;

                        //muestra el listado de favoritos
                        case R.id.action_favs:
                            showFavRecipes();
                            break;

                            //muestra la configuracion
                        case R.id.action_config:
                            showConfig();

                    }
                    return true;
                }
            });
            showSearchFragment(null);
        }

    @Override
    public void onBackPressed() {
        Log.d("BACK PRESSED", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
           super.onBackPressed();
        }
        else if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().findFragmentByTag("list") != null){
                getSupportFragmentManager().popBackStackImmediate();
                showSearchFragment(null);
            }
            else if (getSupportFragmentManager().findFragmentByTag("ing") != null){
                getSupportFragmentManager().popBackStackImmediate();
                showSearchFragment(null);
            }
            else if (getSupportFragmentManager().findFragmentByTag("cat") != null){
                getSupportFragmentManager().popBackStackImmediate();
                showSearchFragment(null);
            }

            else if(getSupportFragmentManager().findFragmentByTag("profile") != null){
                getSupportFragmentManager().popBackStackImmediate();
                showSocialFragment();
            }

            else if(getSupportFragmentManager().findFragmentByTag("recipe") != null){
                getSupportFragmentManager().popBackStackImmediate();
                switch (where){
                    case 1:
                        where = -1;
                        showSocialFragment();
                        break;
                    case 2:
                        where = -1;
                        showFavRecipes();
                        break;
                    default: showSearchFragment(null);
                        break;

                }

            }else if(getSupportFragmentManager().findFragmentByTag("add") != null){
                getSupportFragmentManager().popBackStackImmediate();
                showSocialFragment();
            }

        }
        else{
            if (mBackPressed + MAX_TIME > System.currentTimeMillis()) {
                getSupportFragmentManager().popBackStackImmediate();
                super.onBackPressed();
                return;
            }
            else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            }
            mBackPressed = System.currentTimeMillis();
        }

    }

    @Override
    public void showAddIngredients(Bundle b) {
        addingFr = AddIngredientsFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, addingFr).addToBackStack("");
        ft.commit();
    }

    void setSettingsOp(boolean v){settingsOp = v;}

    @Override
    public void showSearchFragment(Bundle data) {
        bnvTabMenu.setVisibility(View.VISIBLE);
        searchFragment = SearchRecipeFragment.getInstance(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, searchFragment);
        if (getSupportFragmentManager().findFragmentByTag("ing") != null || getSupportFragmentManager().findFragmentByTag("cat") != null ) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        setSettingsOp(false);
        ft.commit();
    }


    @Override
    public void showSocialFragment() {
        where = 1;
        bnvTabMenu.setVisibility(View.VISIBLE);
        Bundle b = new Bundle();
        b.putParcelable("user", user);
        socialFragment = SocialActivityFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, socialFragment);
        setSettingsOp(false);
        ft.commit();

    }

    @Override
    public void showSearchByCategories(Bundle data) {
        bnvTabMenu.setVisibility(View.GONE);
        sbcFragment = SearchByCategoriesFragment.getInstance(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, sbcFragment, "cat").addToBackStack("");
        ft.commit();
    }

    @Override
    public void showSearchByIngredients(Bundle data) {
        bnvTabMenu.setVisibility(View.GONE);
        sbiFragment = SearchByIngredientFragment.getInstance(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, sbiFragment, "ing").addToBackStack("");
        ft.commit();
    }

    @Override
    public String getUserName(){
        return user.getName() + "\r\n" + user.getEmail();
    }

    @Override
    public void showProfile(Bundle b) {
        bnvTabMenu.setVisibility(View.GONE);
        profileFr = ProfileFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, profileFr, "profile").addToBackStack("");
        ft.commit();
    }

    @Override
    public void showUsersList(Bundle args) {
        ulfFragment = UsersListFragment.getInstance(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, profileFr).addToBackStack("");
        ft.commit();
    }


    @Override
    public void showFavRecipes() {
        where = 2;
        bnvTabMenu.setVisibility(View.VISIBLE);
        favRecFr = FavRecipesFragment.getInstance(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, favRecFr);
        setSettingsOp(false);
        ft.commit();
    }


    @Override
    public void showRecipesList(Bundle b) {
        bnvTabMenu.setVisibility(View.GONE);
        rlFragment = RecipesListFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, rlFragment, "list").addToBackStack("");
        setSettingsOp(false);
        ft.commit();
    }

    @Override
    public void showMyRecipes() {

    }

    @Override
    public void showRecipe(Bundle recipe) {
        bnvTabMenu.setVisibility(View.GONE);
        recipeFrg = RecipeFragment.getInstance(recipe);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, recipeFrg, "recipe").addToBackStack("");
        ft.commit();
    }

    @Override
    public void showAddRecipe(Bundle b) {
        bnvTabMenu.setVisibility(View.GONE);
        addRecipeFr = AddRecipeFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, addRecipeFr, "add").addToBackStack("");
        ft.commit();

    }

    @Override
    public void showConfig() {
        bnvTabMenu.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, new android.support.v4.app.Fragment());
        ft.commit();
        setSettingsOp(true);
        getFragmentManager().beginTransaction().replace(R.id.framehome, new SettingsPreferences()).commit();
    }

    public void closeConfig() {
        getFragmentManager().beginTransaction().replace(R.id.framehome, new android.app.Fragment()).commit();
    }

    @Override
    public Filter getFilter(int pos){
        return filters.get(pos);
    }
    @Override
    public void addFilter(Filter f){
         filters.add(f);
    }

    @Override
    public void removeFilter(int pos){
         filters.remove(pos);
    }

    @Override
    public int getNFilters(){
        return filters.size();
    }

    @Override
    public int getPos(String type) {
        int p = 10;
        for (int i = 0; i < filters.size(); i++){
            if(filters.get(i).getType().equals(type))
                p = i;
        }
        return p;
    }

    @Override
    public Filter getFilterByName(String name){
        Filter ftmp = null;
        for (int i = 0; i < filters.size(); i++){
            if(filters.get(i).getType().equals(name))
                ftmp = filters.get(i);
        }
        return ftmp;
    }

    @Override
    public void deleteFilters() {
        for(int i = 0; i< filters.size(); i++){
            removeFilter(i);
        }
    }

    @Override
    public void showMyComments() {

    }
}
