package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ismael.fastrecipes.adapter.FilteredRecipeAdapter;
import com.ismael.fastrecipes.adapter.RecipeAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;


/**
 * MyRecipesFragment.class - Fragment que muestra la lista de recetas de propias del usuario activo
 * @author Ismael García
 */
public class MyRecipesFragment extends Fragment implements RecipesPresenter.View {
    public MyRecipesFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.lvMyRecipes)
    ListView lvMyRecipes;

    @BindView(R.id.emptyListMyRec)
    TextView txvEmpty;
    @BindView(R.id.fabAddRecipe)
    FloatingActionButton fabAddRecipe;

    static private MyRecipesFragment mrfInstance;
    private MyRecipeFragmentListener mCallback;
    RecipesPresenter presenter;
    FilteredRecipeAdapter rAdapter;
    ArrayList<Recipe> myRecs;

    /**
     * Interfaz para las llamadas al contenedor
     */
    interface MyRecipeFragmentListener{
        void showMyRecipes();
        void showRecipe(Bundle args);
        void showAddRecipe(Bundle b);
        User getUser();
    }

    /**
     * Instanciador del fragment
     * @param args Datos previos opcionales
     * @return Instancia del fragment
     */
    public static MyRecipesFragment getInstance(Bundle args){

        if(mrfInstance == null) {
            mrfInstance = new MyRecipesFragment();
            mrfInstance.setArguments(new Bundle());
        }
        if(args != null) {
            mrfInstance.getArguments().putAll(args);
        }
        return  mrfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this, 0);
        myRecs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        ButterKnife.bind(this, rootView);

        rAdapter = new FilteredRecipeAdapter(getContext(), 0, myRecs);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMyRecipes.setAdapter(rAdapter);
        fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.showAddRecipe(null);
            }
        });
        lvMyRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.getRecipe(rAdapter.getItem(i).getIdr(), mCallback.getUser().getId());
            }
        });


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (MyRecipeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement MyRecipeFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getMyRecipesList(mCallback.getUser().getId());
    }

    /**
     * Llama al contenedor para que cargue la vista de una receta
     * @param b Datos para realizar la carga de la receta
     */
    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    /**
     * Cambia el estado de favorito de una receta tras actualizarlo en la base de datos
     * @param recipe Datos de la receta tras la consulta para actualizar el estado
     */
    @Override
    public void setFavState(Recipe recipe) {}

    /**
     * Carga los datos obtenidos del servidor
     * @param recs Listado de recetas publicadas por el usuario activo
     */
    @Override
    public void setListData(ArrayList<Recipe> recs) {
            rAdapter.clear();
            rAdapter.addAll(recs);
            rAdapter.notifyDataSetChanged();
    }

    /**
     * Cancela la carga de datos si se produce un error o no hay recetas publicadas por el usuario activo
     */
    @Override
    public void cancelSearch() {
        rAdapter.clear();
        lvMyRecipes.setVisibility(GONE);
        txvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void addNewComment(ArrayList<Comment> newComment) {

    }

    /**
     * Muestra un mensaje en la pantalla tras la realización de ciertas acciones
     * @param msg Mensaje a mostrar
     */
    @Override
    public void showNetworkError(String msg) {
        Toast t = Toast.makeText(FastRecipesApplication.getContext(), msg, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        txvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
