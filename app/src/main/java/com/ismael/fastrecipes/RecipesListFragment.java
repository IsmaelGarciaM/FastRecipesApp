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
public class RecipesListFragment extends Fragment implements RecipesPresenter.View{
    @Override
    public void setFavState() {

    }

    public RecipesListFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.lvRecipesList)
    RecyclerView lvRecipes;

    static private RecipesListFragment rlfInstance;
    private RecipesListListener mCallback;
    RecipesPresenter presenter;
    RecipeAdapter adapter;

    @Override
    public void setCursorData(Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    interface RecipesListListener{
        void showRecipesList(Bundle b);
        void showRecipe(Bundle args);
    }

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
        adapter = new RecipeAdapter(getContext(), new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idRecipe) {
                presenter.getRecipe(1);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this.getContext());
        lvRecipes.setLayoutManager(lm);
        lvRecipes.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        presenter.getFavRecipes();
        //presenter.getFilteredRecipes();
    }


}
