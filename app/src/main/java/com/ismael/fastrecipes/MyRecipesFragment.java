package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ismael.fastrecipes.adapter.RecipeAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyRecipesFragment extends Fragment implements RecipesPresenter.View {


    public MyRecipesFragment() {
        // Required empty public constructor
    }
    @BindView(R.id.rcvMyRecipes)
    RecyclerView rcvMyRecipes;

    @BindView(R.id.fabAddRecipe)
    FloatingActionButton fabAddRecipe;

    static private MyRecipesFragment mrfInstance;
    private MyRecipeFragmentListener mCallback;
    RecipesPresenter presenter;
    RecipeAdapter rAdapter;

    @Override
    public void setCursorData(Cursor data) {
        rAdapter.swapCursor(data);
    }

    interface MyRecipeFragmentListener{
        void showMyRecipes();
        void showRecipe(Bundle args);
        void showAddRecipe(Bundle b);
    }

    public static MyRecipesFragment getInstance(Bundle args){

        if(mrfInstance == null) {
            mrfInstance = new MyRecipesFragment();
            mrfInstance.setArguments(args);
        }
        return  mrfInstance;
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
        View rootView = inflater.inflate(R.layout.fragment_my_recipes, container, false);
        ButterKnife.bind(this, rootView);

        rAdapter = new RecipeAdapter(getContext(), new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idRecipe) {
                presenter.getMyRecipe(idRecipe);
            }
        });
                RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(), 1);
        rcvMyRecipes.setLayoutManager(lm);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvMyRecipes.setAdapter(rAdapter);
        fabAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putParcelable("newR", new Recipe());
                mCallback.showAddRecipe(b);
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
        presenter.getMyRecipesList();
    }

    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    @Override
    public void setFavState(Recipe recipe) {

    }

    @Override
    public void setListData(ArrayList<Recipe> recs) {

    }

    Recipe getRecipe(){
        return null;
    }
}
