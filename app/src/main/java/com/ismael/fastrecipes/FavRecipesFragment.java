package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ismael.fastrecipes.adapter.RecipeAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavRecipesFragment extends Fragment implements RecipesPresenter.View {

    @BindView(R.id.rcvfavList)
    RecyclerView rcvFavRecipes;

    FavRecipesListener mCallback;
    RecipesPresenter presenter;
    static FavRecipesFragment frfInstance;
    RecipeAdapter adapterFavRec;


    public FavRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void setCursorData(Cursor data) {
        adapterFavRec.swapCursor(data);
    }

    public static FavRecipesFragment getInstance(Bundle args){

        if(frfInstance == null) {
            frfInstance = new FavRecipesFragment();
        }
        frfInstance.setArguments(args);
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
        View root = inflater.inflate(R.layout.fragment_fav_recipes, container, false);
        ButterKnife.bind(this, root);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rcvFavRecipes.setLayoutManager(lm);
        adapterFavRec = new RecipeAdapter(getContext(), new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idRecipe) {
                presenter.getFavRecipe(idRecipe);
            }
        });

        rcvFavRecipes.setAdapter(adapterFavRec);


        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onStart() {
        super.onStart();
        presenter.getFavRecipes();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    @Override
    public void setFavState() {

    }

}
