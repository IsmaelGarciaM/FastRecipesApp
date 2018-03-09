package com.ismael.fastrecipes;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.ismael.fastrecipes.adapter.FilteredRecipeAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * FavRecipesFragment.class - Muestra un listado con las recetas marcadas como favoritas por el usuario
 * @author Ismael García
 */
public class FavRecipesFragment extends Fragment implements RecipesPresenter.View {

    @BindView(R.id.lvfavList)
    ListView lvFavRecipes;
    @BindView(R.id.emptyFavs)
    TextView emptyList;
    @BindView(R.id.txvFavRecsTitle)
    TextView title;
    FavRecipesListener mCallback;
    RecipesPresenter presenter;
    static FavRecipesFragment frfInstance;
    FilteredRecipeAdapter adapterFavRec;
    ArrayList<Recipe> favRecs;


    public FavRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Carga los datos obtenidos del servidor en la lista de recetas
     * @param data Listado de recetas marcadas como favoritas por el usuario
     */
    @Override
    public void setListData(ArrayList<Recipe> data) {
            adapterFavRec.clear();
            adapterFavRec.addAll(data);
            adapterFavRec.notifyDataSetChanged();
    }

    /**
     * Instanciador del fragment
     * @param args Datos opcionales para la creación de la instancia
     * @return Instancia única dl fragmeny
     */
    public static FavRecipesFragment getInstance(Bundle args) {

        if (frfInstance == null) {
            frfInstance = new FavRecipesFragment();
            frfInstance.setArguments(new Bundle());
        }
        if(args != null) {
            frfInstance.getArguments().putAll(args);
        }
        return  frfInstance;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try{
            mCallback = (FavRecipesListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e.getMessage() + " activity must implement FavRecipeListener interface");
        }
    }

    public interface FavRecipesListener{
        void showFavRecipes();
        void showRecipe(Bundle recipe);

        User getUser();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this, 0);
        favRecs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fav_recipes, container, false);
        ButterKnife.bind(this, root);
        adapterFavRec = new FilteredRecipeAdapter(getContext(), 0, favRecs);


        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        title.setTypeface(font);
        emptyList.setTypeface(font);
        lvFavRecipes.setAdapter(adapterFavRec);


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvFavRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.getRecipe(favRecs.get(i).getIdr(), mCallback.getUser().getId());
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getFavRecipes(mCallback.getUser().getId());
    }


    /**
     * Muestra la vista que presenta los datos de la receta
     * @param b Bundle con los datos de la receta a mostrar
     */
    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    @Override
    public void setFavState(Recipe recipe) {

    }

    /**
     * Cancela la carga de datos si la conexión falla o la respuesta del servidor no contiene elementos
     */
    @Override
    public void cancelSearch() {
        adapterFavRec.clear();
        emptyList.setVisibility(View.VISIBLE);
        lvFavRecipes.setVisibility(View.GONE);
    }

    /**
     * Muestra un mensaje de error en la pantalla.
     * @param msg Mensaje de error a mostrar
     */
    @Override
    public void showNetworkError(String msg) {
        try{
            Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }catch (Exception e){}
        emptyList.setVisibility(View.VISIBLE);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void addNewComment(ArrayList<Comment> newComment) {

    }

}
