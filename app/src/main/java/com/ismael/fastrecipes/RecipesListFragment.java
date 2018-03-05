package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
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
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ismael.fastrecipes.utils.Const.f1;
import static com.ismael.fastrecipes.utils.Const.f2;
import static com.ismael.fastrecipes.utils.Const.f3;
import static com.ismael.fastrecipes.utils.Const.f4;
import static com.ismael.fastrecipes.utils.Const.f5;
import static com.ismael.fastrecipes.utils.Const.f6;


/**
 * RecipesListFragment.clas - Vista para mostrar las recetas filtradas desde el servidor
 */
public class RecipesListFragment extends Fragment implements RecipesPresenter.View{

    public RecipesListFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.lvRecipesList)
    ListView lvRecipes;
    @BindView(R.id.filteredTitle)
    TextView title;
    @BindView(R.id.filteredEmpty)
    TextView empty;


    static private RecipesListFragment rlfInstance;
    private RecipesListListener mCallback;
    RecipesPresenter presenter;
    FilteredRecipeAdapter adapter;
    ArrayList<Recipe> recList;

    /**
     * Muestra la vista de la receta seleccionada
     * @param b Datos de la receta seleccionada
     */
    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    /**
     * Listener para vinculación con HomeActivity
     */
    interface RecipesListListener{
        void showRecipesList(Bundle b);
        void showRecipe(Bundle args);
        ArrayList<Filter> getFilters();
        Filter getFilterByName(String s);
        User getUser();
    }

    /**
     * Instanciador de la vista
     * @param args Bundle con datos opcionales para la instancia
     * @return Instancia del fragment
     */
    public static RecipesListFragment getInstance(Bundle args){

        if(rlfInstance == null) {
            rlfInstance = new RecipesListFragment();
            rlfInstance.setArguments(new Bundle());
        }
        if(args != null) {
            rlfInstance.getArguments().putAll(args);
        }
        return  rlfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this, 0);
        recList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new FilteredRecipeAdapter(getContext(), 0, recList);
        empty.setVisibility(View.GONE);
        lvRecipes.setVisibility(View.VISIBLE);
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        title.setTypeface(font);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvRecipes.setAdapter(adapter);
        lvRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.getRecipe(adapter.getItem(i).getIdr(), mCallback.getUser().getId());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (RecipesListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement RecipesListListener interface");
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
        //presenter.getFavRecipes();
        presenter.getFilteredRecipes(getRModel());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    /**
     * Realiza la creación de una receta modelo con los datos de los filtros creados para la búsqueda de recetas
     * @return Objeto receta correctamente formateado para su correcta interpretación en la API
     */
    private Recipe getRModel(){
        Recipe rModel = new Recipe();
        Filter tmp;
        if((tmp = mCallback.getFilterByName(f1)) != null){
            rModel.setIngredients(tmp.getContent() + "-");
        }
        else{
            rModel.setIngredients("none-");
        }

        if((tmp = mCallback.getFilterByName(f2)) != null){
            String ingre = rModel.getIngredients();
            rModel.setIngredients(ingre + tmp.getContent());
        }
        else{
            String ingre = rModel.getIngredients();
            rModel.setIngredients(ingre + "none");
        }

        if((tmp = mCallback.getFilterByName(f3)) != null){
            rModel.setCategories(tmp.getContent());
        }
        else{
            rModel.setCategories("");
        }

        if((tmp = mCallback.getFilterByName(f4)) != null){
            rModel.setName(tmp.getContent());
        }
        else{
            rModel.setName("");
        }


        if((tmp = mCallback.getFilterByName(f5)) != null){
            String[] t = tmp.getContent().split(" ");
            rModel.setTime(Integer.parseInt(t[0]));
        }
        else{
            rModel.setTime(0);
        }


        if((tmp = mCallback.getFilterByName(f6)) != null){
            /*if(tmp.getContent().equals("Fácil"))
                rModel.setDifficulty("Facil");
            else if(tmp.getContent().equals("Media"))
                rModel.setDifficulty(tmp.getContent());
            else if(tmp.getContent().equals("Difícil"))
                rModel.setDifficulty("Dificil");*/
            rModel.setDifficulty(tmp.getContent());

        }
        else{
            rModel.setDifficulty("");
        }

        return rModel;
    }

    @Override
    public void setFavState(Recipe recipe) {}

    /**
     * Carga las recetas obtenidas de la busqueda del servidor en la vista
     * @param recs Listado de recetas obtenidas
     */
    @Override
    public void setListData(ArrayList<Recipe> recs) {
        adapter.clear();
        adapter.addAll(recs);
        adapter.notifyDataSetChanged();
    }

    /**
     * Cancela la carga de recetas por un error o por no haber ninguna receta coincidente con la búsqueda
     */
    @Override
    public void cancelSearch() {
        lvRecipes.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
    }

    @Override
    public void addNewComment(ArrayList<Comment> newComment) {
    }

    /**
     * Muestra un mensaje al realizar algunas acciones o al producirse un error
     * @param msg Mensaje a mostrar
     */
    @Override
    public void showNetworkError(String msg) {
        Toast t = Toast.makeText(FastRecipesApplication.getContext(), msg, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        empty.setVisibility(View.VISIBLE);
    }
}
