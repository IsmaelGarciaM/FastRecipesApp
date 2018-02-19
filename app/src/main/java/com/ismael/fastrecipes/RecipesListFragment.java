package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ismael.fastrecipes.adapter.FilteredRecipeAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.model.Recipe;
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

    static private RecipesListFragment rlfInstance;
    private RecipesListListener mCallback;
    RecipesPresenter presenter;
    FilteredRecipeAdapter adapter;
    ArrayList<Recipe> recList;

    @Override
    public void setCursorData(Cursor data) {}

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
    }

    /**
     * Instanciador de la vista
     * @param args
     * @return
     */
    public static RecipesListFragment getInstance(Bundle args){

        if(rlfInstance == null) {
            rlfInstance = new RecipesListFragment();
        }
        rlfInstance.setArguments(args);
        return  rlfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new FilteredRecipeAdapter(getContext(), 0, recList);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvRecipes.setAdapter(adapter);
        lvRecipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.getRecipe(adapter.getItem(i).getId());
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

    private Recipe getRModel(){
        Recipe rModel = new Recipe();

        for(int i = 0; i < mCallback.getFilters().size(); i++){
            switch (mCallback.getFilters().get(i).getType()){
                case f1: rModel.setIngredients(mCallback.getFilters().get(i).getContent());
                    break;
                case f2: rModel.setIngredients("$"+mCallback.getFilters().get(i).getContent());
                    break;
                case f3: rModel.setCategories(mCallback.getFilters().get(i).getContent());
                    break;
                case f4: rModel.setName(mCallback.getFilters().get(i).getContent());
                    break;
                case f5:
                    String t[] = mCallback.getFilters().get(i).getContent().split(" ");
                    int time = Integer.parseInt(t[0]);
                    rModel.setTime(time);
                    break;
                case f6: rModel.setDifficulty(mCallback.getFilters().get(i).getContent());
                    break;
            }
        }
        return rModel;
    }

    @Override
    public void setFavState(Recipe recipe) {}

    @Override
    public void setListData(ArrayList<Recipe> recs) {adapter.addAll(recs);}


}
