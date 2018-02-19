package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ismael.fastrecipes.adapter.CommentsAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment implements RecipesPresenter.View{


    public RecipeFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.imvRecipeImage)
    ImageView imvRecipe;

    @BindView(R.id.imvRecipeImageUser)
    ImageView imvRecipeUser;
    @BindView(R.id.txvRecipeNamePrinView)
    TextView txvRecipeName;
    @BindView(R.id.txvRecipeCatPV)
    TextView txvRecipeCategories;
    @BindView(R.id.txvRecipeTimePR)
    TextView txvRecipeTime;
    @BindView(R.id.txvRecipeDifficultPR)
    TextView txvRecipeDifficulty;

    @BindView(R.id.txvRecipeRatingPR)
    TextView txvRecipeRating;

    @BindView(R.id.txvRecipeLikesPR)
    TextView txvRecipeLikes;
    @BindView(R.id.txvRecipeDescription)
    TextView txvRecipeDescrip;

    @BindView(R.id.txvRecipeNameUser)
    TextView txvRecipeUserName;

    @BindView(R.id.txvRecipeIngredients)
    TextView txvIngredients;
    @BindView(R.id.lvComments)
    ListView lvComments;

    @BindView(R.id.fabDeleteRecipe)
    FloatingActionButton fabDelete;

    @BindView(R.id.fabEditRecipe)
    FloatingActionButton fabEdit;

    @BindView(R.id.fabSetFav)
    FloatingActionButton fabFav;

    static private RecipeFragment rfInstance;
    private RecipeFragmentListener mCallback;
    Recipe recetaActual;
    CommentsAdapter cAdapter;
    ArrayList<Comment> comments;

    public void setRecipeData(Recipe r){
        try {
            try {
                Picasso.with(getContext())
                        .load(r.getImage())
                        .into(imvRecipe);
            }catch (Exception e){}
            txvRecipeCategories.setText(r.getCategories());
            txvRecipeName.setText(r.getName());
            txvRecipeUserName.setText("FastRecipesTeam");//r.getAuthorName());
            txvIngredients.setText(r.getIngredients());
            txvRecipeDescrip.setText(r.getElaboration());
            txvRecipeTime.setText(String.valueOf(r.getTime()) + " minutos");
            txvRecipeDifficulty.setText(r.getDifficulty());
            txvRecipeRating.setText(String.valueOf(r.getRating())+ "/5");
        }
        catch (Exception e){}
    }


    RecipesPresenter presenter;

    @Override
    public void setCursorData(Cursor data) {

    }

    @Override
    public void showRecipeInfo(Bundle recipe) {

    }

    @Override
    public void setFavState(Recipe recipe) {
        if (recipe.getFav() == 0){
            fabFav.setImageResource(R.drawable.heart_outline);
        }else{
            fabFav.setImageResource(R.drawable.heart);
        }
    }

    @Override
    public void setListData(ArrayList<Recipe> recs) {

    }

    interface RecipeFragmentListener{
        void showRecipe(Bundle recipe);

        User getUser();

        void showAddRecipe(Bundle b);

        void showProfile(Bundle b);

        void showUsersList(Bundle b);
    }

    public static RecipeFragment getInstance(Bundle args){

        if(rfInstance == null) {
            rfInstance = new RecipeFragment();
        }
        rfInstance.setArguments(args);
        return  rfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this);
        if(rfInstance.getArguments() != null){
            if(rfInstance.getArguments().getParcelable("recipe") != null){
                recetaActual = rfInstance.getArguments().getParcelable("recipe");
            }
            if(rfInstance.getArguments().getParcelableArrayList("comments") != null){
                comments = rfInstance.getArguments().getParcelableArrayList("comments");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        fabDelete.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);
        if(comments != null){
            cAdapter = new CommentsAdapter(getContext(), R.layout.item_comment, comments);
        }else{
            comments = new ArrayList<>();
            cAdapter = new CommentsAdapter(getContext(), R.layout.item_comment, comments);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mCallback.getUser().getId() == recetaActual.getAuthor()){
            fabDelete.setEnabled(true);
            fabDelete.setVisibility(View.VISIBLE);
            fabEdit.setEnabled(true);
            fabEdit.setVisibility(View.VISIBLE);
            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putParcelable("recipe", recetaActual);
                    mCallback.showAddRecipe(b);
                }
            });
        }

        lvComments.setAdapter(cAdapter);
        lvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle b = new Bundle();
                b.putInt("id", cAdapter.getItem(i).getId());
                mCallback.showProfile(b);
            }
        });


        fabFav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle b = new Bundle();
                b.putInt("id", recetaActual.getId());
              //  mCallback.showUsersList(b);
                return true;
            }
        });

        if(mCallback.getUser().getId() == recetaActual.getAuthor()) {

            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putParcelable("recipe", recetaActual);
                    mCallback.showAddRecipe(b);
                }
            });

            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    presenter.deleteRecipe(recetaActual.getId());
                }
            });
            fabFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recetaActual.getFav() == 0){
                        presenter.setFavourite(mCallback.getUser().getId(), recetaActual.getId(), true);
                    }
                    else
                        presenter.setFavourite(mCallback.getUser().getId(), recetaActual.getId(), false);

                }
            });

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (RecipeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement RecipeFragmentListener interface");
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
        if(recetaActual != null)
            setRecipeData(recetaActual);
        cAdapter.add(new Comment(0, "Ismael García", 1, "Excelente receta, la recomiendo :)", "18/02/18", 0, ""));

    }
}
