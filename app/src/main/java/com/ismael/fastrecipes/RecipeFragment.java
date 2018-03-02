package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.ismael.fastrecipes.adapter.CommentsAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;
import com.ismael.fastrecipes.utils.Const;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment implements RecipesPresenter.View{

    private FirebaseAnalytics mFirebaseAnalytics;
    public RecipeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.edtComment)
    EditText edtComment;
    @BindView(R.id.llrating)
    LinearLayout ratingLayout;
    @BindView(R.id.smlRating)
    SmileRating ratingBar;
    @BindView(R.id.btnSendRating)
    Button btnSetRating;
    @BindView(R.id.btnSendComment)
    Button btnSendComment;
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
    @BindView(R.id.ratingIconPR)
    ImageView imgRating;
    @BindView(R.id.txvRecipenPersPR)
    TextView txvRecipeNPers;
    @BindView(R.id.txvRecipeDescription)
    TextView txvRecipeDescrip;
    @BindView(R.id.txvRecipeNameUser)
    TextView txvRecipeUserName;
    @BindView(R.id.txvRecipeIngredients)
    TextView txvIngredients;
    @BindView(R.id.lvComments)
    RecyclerView lvComments;
    @BindView(R.id.txvRecipeSource)
    TextView txvSource;
    //@BindView(R.id.fabDeleteRecipe)
    //FloatingActionButton fabDelete;
    @BindView(R.id.fabEditRecipe)
    FloatingActionButton fabEdit;
    @BindView(R.id.fabSetFav)
    FloatingActionButton fabFav;

    static private RecipeFragment rfInstance;
    private RecipeFragmentListener mCallback;
    Recipe recetaActual;
    CommentsAdapter cAdapter;
    ArrayList<Comment> comments;
    int newRating;
    RecipesPresenter presenter;
    private CommentsAdapter adapterCommentFirebase;

    public void setRecipeData(Recipe r){


        try {
            if(r.getImage() != null && !r.getImage().equals("")) {

                try {
                    Picasso.with(getContext()).load(r.getImage()).into(imvRecipe);
                } catch (Exception e) {
                    imvRecipe.setImageDrawable(getResources().getDrawable(R.drawable.addrecipe));
                }

            }
            else
                imvRecipe.setImageDrawable(getResources().getDrawable(R.drawable.addrecipe));

            txvRecipeCategories.setText(r.getCategories());
            txvRecipeName.setText(r.getName());
            txvRecipeUserName.setText(r.getAuthorName());
            txvIngredients.setText(r.getIngredients());
            txvRecipeDescrip.setText(r.getElaboration());
            txvRecipeTime.setText(String.valueOf(r.getTime()) + " minutos");
            txvRecipeDifficulty.setText(r.getDifficulty());
            txvSource.setText(r.getSource());
            txvRecipeNPers.setText(String.valueOf(r.getnPers())+ " pers.");
            txvRecipeRating.setText(String.valueOf(r.getRating())+ "/5");
            setFavState(r);
        }
        catch (Exception e){}
    }

    @Override
    public void showRecipeInfo(Bundle recipe) {
        Recipe rtmp = recipe.getParcelable("r");
        setRecipeData(rtmp);
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

    @Override
    public void cancelSearch() {

    }

    @Override
    public void addNewComment(ArrayList<Comment> c) {
        comments.clear();
        comments.addAll(c);
        adapterCommentFirebase.notifyDataSetChanged();
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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
        comments = new ArrayList<>();
        if(rfInstance.getArguments() != null){
            if(rfInstance.getArguments().getParcelable("recipe") != null){
                recetaActual = rfInstance.getArguments().getParcelable("recipe");
            }
        }
        presenter = new RecipesPresenterImpl(this, recetaActual.getIdr());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        //fabDelete.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);

        //Personalización de rating bar
        ratingBar.setNameForSmile(BaseRating.TERRIBLE, "¿Esto qué es?");//R.string.bad);
        ratingBar.setNameForSmile(BaseRating.BAD, R.string.bad);
        ratingBar.setNameForSmile(BaseRating.OKAY, "No está mal");//R.string.bad);
        ratingBar.setNameForSmile(BaseRating.GOOD, "Me gusta");//R.string.bad);
        ratingBar.setNameForSmile(BaseRating.GREAT, "¡Me encanta!");//R.string.bad);

        adapterCommentFirebase = new CommentsAdapter(getContext(), comments, new CommentsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idUser) {
                Bundle b = new Bundle();
                b.putInt("id", idUser);
                mCallback.showProfile(b);
            }
        });

        lvComments.setHasFixedSize(true);
        lvComments.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    View mView;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mCallback.getUser().getId() == recetaActual.getAuthor()){
            //fabDelete.setEnabled(true);
            //fabDelete.setVisibility(View.VISIBLE);
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

        mView = view;
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtComment.getText().toString().equals("")){
                    String comment = edtComment.getText().toString();
                    presenter.sendComment(comment, recetaActual.getIdr(), mCallback.getUser().getId(), mCallback.getUser().getName());
                    edtComment.setText("");
                    Snackbar.make(mView, "Comentario publicado.", Snackbar.LENGTH_SHORT);
                }
            }
        });





        ratingBar.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                newRating = level;
            }
        });

        lvComments.setAdapter(adapterCommentFirebase);

       /*lvComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });*/

        fabFav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle b = new Bundle();
                b.putInt("id", recetaActual.getIdr());
                mCallback.showUsersList(b);
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
/*
            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.deleteRecipe(recetaActual.getIdr());
                }
            });
*/
        }
        else {
            fabFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recetaActual.getFav() == 0) {
                        presenter.setFavourite(mCallback.getUser().getId(), recetaActual.getIdr(), 1);
                    } else
                        presenter.setFavourite(mCallback.getUser().getId(), recetaActual.getIdr(), 0);

                }
            });
            imgRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ratingLayout.getVisibility() == View.VISIBLE)
                        ratingLayout.setVisibility(View.GONE);
                    else
                        ratingLayout.setVisibility(View.VISIBLE);
                }
            });
            btnSetRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(newRating > 0) {
                        presenter.setRating(recetaActual.getIdr(), mCallback.getUser().getId(), newRating);
                        ratingLayout.setVisibility(View.GONE);
                    }
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
    }


}
