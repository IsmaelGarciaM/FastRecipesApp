package com.ismael.fastrecipes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismael.fastrecipes.interfaces.IngredientPresenter;
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.IngredientsPresenterImpl;
import com.ismael.fastrecipes.provider.FastRecipesContract;
import com.ismael.fastrecipes.utils.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ismael.fastrecipes.utils.Const.f5;

/**
 * HomeActivity.class - Clase con la actividad contenedora de toda la aplicacion.
 * Maneja el menu y contiene el objeto con los filtros de busqueda
 * @author Ismael García
 */
public class HomeActivity extends AppCompatActivity implements UsersListFragment.UsersListListener, AddIngredientsFragment.AddIngredientsListener, AddRecipeFragment.AddRecipeFragmentListener, RecipeFragment.RecipeFragmentListener, RecipesListFragment.RecipesListListener, ProfileFragment.ProfileListener, MyRecipesFragment.MyRecipeFragmentListener, MyCommentsFragment.MyCommentsFragmentListener, SettingsPreferences.PrefsListener, FavRecipesFragment.FavRecipesListener, SearchRecipeFragment.SearchFragmentListener, SocialActivityFragment.SocialActivityFragmentListener, SearchByIngredientFragment.SearchIngredientsListener, SearchByCategoriesFragment.SearchCategoriesListener{

    IngredientPresenter presenter;
    //int para gestionar OnBackPressed de algunas vistas
    int where = -1;
    //longs para gestionar la salida de la aplicacion mediante OnBacPressed
    private long mBackPressed = 0;
    private static final long MAX_TIME = 2500;

    //Fragmentos de las vistas de la aplicación
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
    User user;
    BottomNavigationView bnvTabMenu;
    boolean settingsOp;
    Recipe newR;
    FirebaseAuth auth;

    //Array de filtros para buscar recetas
    ArrayList<Filter> filters;

    public ArrayList<Filter> getFilters() {
        return filters;
    }
    public void setFilters(ArrayList<Filter> filters) {
        this.filters = filters;
    }

    //Usuario activo actualmente
    public User getUser() {
        return user;
    }

    public void setUser(User us) {
        this.user.setLocation(us.getLocation());
        this.user.setName(us.getName());
        this.user.setImage(us.getImage());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bnvTabMenu = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        filters = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        Log.d( "BACKSTACKCOUNT", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        try {
            if(getIntent().getExtras() != null && getIntent().getExtras().getParcelable("user") != null) {
                user = getIntent().getExtras().getParcelable("user");
            }else{
                user = new User("anónimo@anonimail.com", "Anónimo", "??/??/??", "uid");
            }

            if(getIntent().getExtras().getParcelable("newR") != null){
                try {
                    newR = getIntent().getExtras().getParcelable("newR");
                }catch (Exception e){}
                Log.d("NEWR IS NOT NULL", newR.getElaboration());

            }
        }catch (NullPointerException npe) {
        }
            //Menu de la aplicacion
        bnvTabMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(settingsOp)
                        closeConfig();
                    switch (item.getItemId()) {
                        //muestra el fragment para añadir filtros a una nueva busqueda
                        case R.id.action_search_recipes:
                                showSearchFragment(null);
                            break;
                            //muestra el fragment con el perfil, las recetas y los comentarios
                        case R.id.action_social:
                            if(auth.getCurrentUser().isEmailVerified()) {
                                showSocialFragment("");
                            }
                            else
                                showSearchFragment(null);
                            break;

                        //muestra el listado de favoritos
                        case R.id.action_favs:
                            if(auth.getCurrentUser().isEmailVerified())
                                showFavRecipes();
                            else
                                showSearchFragment(null);
                            break;

                            //muestra la configuracion
                        case R.id.action_config:
                            showConfig();

                    }
                    return true;
                }
            });

        if(auth.getCurrentUser().isEmailVerified()) {
            if (newR != null) {
                bnvTabMenu.setSelectedItemId(R.id.action_social);
                Bundle b = new Bundle();
                b.putParcelable("recipe", newR);
                showAddRecipe(b);
                Log.d("NEW R EXISTS", "IS HERE");
            } else {
                showSearchFragment(null);
                Log.d("NEW R NULL", "SHOWSEARCH");
            }
        }
        else{
            showSearchFragment(null);
        }
    }


    /**
     * Gestión de pulsación de la tecla 'Atrás'
     */
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
                showSocialFragment("");
            }

            else if(getSupportFragmentManager().findFragmentByTag("recipe") != null){
                getSupportFragmentManager().popBackStackImmediate();
                switch (where){
                    case 1:
                        where = -1;
                        showSocialFragment("");
                        break;
                    case 2:
                        where = -1;
                        showFavRecipes();
                        break;
                    case 3:
                        where = -1;
                        showSearchFragment(null);
                    default: showSearchFragment(null);
                        break;

                }

            }else if(getSupportFragmentManager().findFragmentByTag("add") != null){
                getSupportFragmentManager().popBackStackImmediate();
                showSocialFragment("");
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

    /**
     * Muestra la vista para añadir ingredientes a una nueva receta
     * @param b Datos de los indredientes añadidos si los hay
     */
    @Override
    public void showAddIngredients(Bundle b) {
        addingFr = AddIngredientsFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, addingFr, "adding").addToBackStack("");
        ft.commit();
    }


    /**
     * Carga la vista para añadir filtros y buscar recetas
     * @param data Datos necesarios para la carga de la vista
     */
    @Override
    public void showSearchFragment(Bundle data) {
        where = 3;
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
    public void showSocialFragment(String msg) {
        where = 1;
        bnvTabMenu.setVisibility(View.VISIBLE);
        Bundle b = new Bundle();
        b.putString("message", msg);

        socialFragment = SocialActivityFragment.getInstance(b);
        if(getSupportFragmentManager().findFragmentByTag("recipe") != null)
            getSupportFragmentManager().popBackStackImmediate();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, socialFragment);
        setSettingsOp(false);
        ft.commit();

    }

    /**
     * Muestra la vista de búsqueda de categorias para añadir a un filtro
     * @param data Datos de las categorias añadidos anteriormente si las hubiera
     */
    @Override
    public void showSearchByCategories(Bundle data) {
        bnvTabMenu.setVisibility(View.GONE);
        sbcFragment = SearchByCategoriesFragment.getInstance(data);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, sbcFragment, "cat").addToBackStack("");
        ft.commit();
    }

    /**
     * Muestra la vista de búsqueda de igredientes para añadir a un filtro
     * @param data Datos de los ingredientes añadidos anteriormente si los hubiera
     */
    @Override
    public void showSearchByIngredients(Bundle data) {
        bnvTabMenu.setVisibility(View.GONE);
        sbiFragment = SearchByIngredientFragment.getInstance(null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, sbiFragment, "ing").addToBackStack("");
        ft.commit();
    }

    /**
     * Obtiene el nombre del usuario actual
     * @return Nombre del usuario actual
     */
    @Override
    public String getUserName(){
        return user.getName() + "\r\n" + user.getEmail();
    }

    /**
     * Carga la vista del perfil de un usuario
     * @param b Datos del perfil a cargar
     */
    @Override
    public void showProfile(Bundle b) {
        bnvTabMenu.setVisibility(View.GONE);
        profileFr = ProfileFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, profileFr, "profile").addToBackStack("");
        ft.commit();
    }

    /**
     * Muestra la vista de un listado de usuarios
     * @param args Datos necesarios para cargar la vista
     */
    @Override
    public void showUsersList(Bundle args) {
        ulfFragment = UsersListFragment.getInstance(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, ulfFragment, "ulist").addToBackStack("");
        ft.commit();
    }


    /**
     * Carga la vista de recetas favoritas almacenadas en la base de datos interna
     */
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

    /**
     * Muestra la lista de recetas filtradas
     * @param b Datos necesarios para la carga de la vista
     */
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

    /**
     * Muestra la vista de una receta concreta
     * @param recipe Receta a mostrar
     */
    @Override
    public void showRecipe(Bundle recipe) {
        bnvTabMenu.setVisibility(View.GONE);
        recipeFrg = RecipeFragment.getInstance(recipe);
        if(getSupportFragmentManager().findFragmentByTag("ulist") != null ||
                getSupportFragmentManager().findFragmentByTag("add") != null) {
            getSupportFragmentManager().popBackStackImmediate();
            Log.d("BACKCOUNTSR", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, recipeFrg, "recipe").addToBackStack("");
        ft.commit();
    }

    /**
     * Muestra la vista para añadir recetas
     * @param b Datos previos para la carga de la vista
     */
    @Override
    public void showAddRecipe(Bundle b) {
        bnvTabMenu.setVisibility(View.GONE);
        addRecipeFr = AddRecipeFragment.getInstance(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentByTag("adding") != null
                || getSupportFragmentManager().findFragmentByTag("cat") != null
                || getSupportFragmentManager().findFragmentByTag("recipe") != null) {
            getSupportFragmentManager().popBackStackImmediate();
            Log.d("BACKCOUNTADD", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));

        }
        if (getSupportFragmentManager().findFragmentByTag("add") != null)
        {
            getSupportFragmentManager().popBackStackImmediate();
            Log.d("BACKCOUNTADDOWN", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        }


        ft.replace(R.id.framehome, addRecipeFr, "add").addToBackStack("");
        ft.commit();

    }

    /**
     * Muesta la vista de la configuración
     */
    @Override
    public void showConfig() {
        bnvTabMenu.setVisibility(View.VISIBLE);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framehome, new android.support.v4.app.Fragment());
        ft.commit();
        setSettingsOp(true);
        getFragmentManager().beginTransaction().replace(R.id.framehome, new SettingsPreferences()).commit();
    }

    @Override
    public void closeSession() {
        startActivity(new Intent().setClass(this, LoginActivity.class).putExtra("close", true));
        finish();
    }

    public void closeConfig() {
        getFragmentManager().beginTransaction().replace(R.id.framehome, new android.app.Fragment()).commit();
    }

    /**
     * Obtiene un filtro según su posición
     * @param pos Posición del filtro a obtener
     * @return Filtro según posición
     */
    @Override
    public Filter getFilter(int pos){
        return filters.get(pos);
    }
    /**
     * Añade un filtro para la búsqueda de recetas
     * @param f Filtro a añadir
     */
    @Override
    public void addFilter(Filter f){
         filters.add(f);
    }

    /**
     * Elimina un filtro por su posición
     * @param pos Posición del filtro a eliminar
     */
    @Override
    public void removeFilter(int pos){
         filters.remove(pos);
    }

    /**
     * Obtiene el número de filtro que el usuario ha añadido
     * @return Numero de filtros
     */
    @Override
    public int getNFilters(){
        return filters.size();
    }

    /**
     * Obtiene la posición de un filtro en el arraylist
     * @param type Tipo del filtro a buscar
     * @return Posición del filtro en el arraylist
     */
    @Override
    public int getPos(String type) {
        int p = 10;
        for (int i = 0; i < filters.size(); i++){
            if(filters.get(i).getType().equals(type))
                p = i;
        }
        return p;
    }

    /**
     * Obtiene un filtro por su nombre
     * @param name Nombre del filtro a obtener
     * @return Filtro buscado
     */
    @Override
    public Filter getFilterByName(String name){
        Filter ftmp = null;
        for (int i = 0; i < filters.size(); i++){
            if(filters.get(i).getType().equals(name))
                ftmp = filters.get(i);
        }
        return ftmp;
    }

    /**
     * Borra todos los filtros de búsqueda
     */
    @Override
    public void deleteFilters() {

        int lenght =  filters.size();
        for(int i = 0; i < lenght; i++){
            removeFilter(0);
        }
    }

    @Override
    public void showMyComments() {

    }

    /**
     * Cambia el valor de settingsOp para controlar la vista de la configuración
     * @param v True para mostrar la vista, false para no mostrarla
     */
    void setSettingsOp(boolean v){settingsOp = v;}


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
