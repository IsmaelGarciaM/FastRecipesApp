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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
 * A simple {@link Fragment} subclass.
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

    interface MyRecipeFragmentListener{
        void showMyRecipes();
        void showRecipe(Bundle args);
        void showAddRecipe(Bundle b);

        User getUser();
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

    @Override
    public void showRecipeInfo(Bundle b) {
        mCallback.showRecipe(b);
    }

    @Override
    public void setFavState(Recipe recipe) {}

    @Override
    public void setListData(ArrayList<Recipe> recs) {
            rAdapter.clear();
            rAdapter.addAll(recs);
            rAdapter.notifyDataSetChanged();
    }

    @Override
    public void cancelSearch() {
        rAdapter.clear();
        lvMyRecipes.setVisibility(GONE);
        txvEmpty.setVisibility(View.VISIBLE);
    }
    @Override
    public void addNewComment(ArrayList<Comment> newComment) {

    }
}
