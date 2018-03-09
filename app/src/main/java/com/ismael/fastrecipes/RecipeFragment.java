package com.ismael.fastrecipes;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
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
 * RecipeFragment.class - Contiene la vista de los datos de una receta
 * @author Ismael García
 */
public class RecipeFragment extends Fragment implements RecipesPresenter.View{

    public RecipeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.txvIng)
    TextView txvIngTitle;
    @BindView(R.id.txvDesc)
    TextView txvElaTitle;
    @BindView(R.id.nameAuthorRecipeTitle)
    TextView txvAuthTitle;
    @BindView(R.id.txvtitlecomments)
    TextView txvCommentTitle;
    @BindView(R.id.txvRecipeSourceTitle)
    TextView txvSourceTitle;


    @BindView(R.id.crvRecipeUser)
    CardView recipeAuthor;
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
    @BindView(R.id.fabDeleteRecipe)
    FloatingActionButton fabDelete;
    @BindView(R.id.fabEditRecipe)
    FloatingActionButton fabEdit;
    @BindView(R.id.fabSetFav)
    FloatingActionButton fabFav;

    static private RecipeFragment rfInstance;
    private RecipeFragmentListener mCallback;
    Recipe recetaActual;
    ArrayList<Comment> comments;
    int newRating;
    RecipesPresenter presenter;
    private CommentsAdapter adapterCommentFirebase;

    interface RecipeFragmentListener{
        void showRecipe(Bundle recipe);
        User getUser();
        void showAddRecipe(Bundle b);
        void showProfile(Bundle b);
        void showUsersList(Bundle b);
        void showSocialFragment(String msg);

    }

    public static RecipeFragment getInstance(Bundle args){

        if(rfInstance == null) {
            rfInstance = new RecipeFragment();
            rfInstance.setArguments(new Bundle());
        }
        if(args != null) {
            rfInstance.getArguments().putAll(args);
        }
        return  rfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        fabDelete.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        txvRecipeName.setTypeface(font);
        txvAuthTitle.setTypeface(font);
        txvCommentTitle.setTypeface(font);
        txvElaTitle.setTypeface(font);
        txvIngTitle.setTypeface(font);
        txvSourceTitle.setTypeface(font);
        txvRecipeUserName.setTypeface(font);

        //Personalización de rating bar
        ratingBar.setNameForSmile(BaseRating.TERRIBLE, R.string.terrible);//R.string.bad);
        ratingBar.setNameForSmile(BaseRating.BAD, R.string.bad);
        ratingBar.setNameForSmile(BaseRating.OKAY,  R.string.okay);//R.string.bad);
        ratingBar.setNameForSmile(BaseRating.GOOD, R.string.like);//R.string.bad);
        ratingBar.setNameForSmile(BaseRating.GREAT, R.string.love);//R.string.bad);

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

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtComment.getText().toString().equals("")){
                    String comment = edtComment.getText().toString();
                    String image = "" ;
                    if(!mCallback.getUser().getImage().equals(""))
                        image = mCallback.getUser().getImage();
                    Comment c = new Comment( mCallback.getUser().getId(), mCallback.getUser().getName(), recetaActual.getIdr(), comment, image);
                    presenter.sendComment(c);
                    edtComment.setText("");
                    Snackbar.make(btnSendComment, "Comentario publicado.", Snackbar.LENGTH_SHORT);
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

        /*fabFav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bundle b = new Bundle();
                b.putInt("id", recetaActual.getIdr());
                mCallback.showUsersList(b);
                return true;
            }
        });*/


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
                    showSafeDelete(recetaActual.getName());
                }
            });

        }
        else {
            fabFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recetaActual.getFav() == 0) {
                        Log.d("ADDINFAV", "1");
                        presenter.setFavourite(mCallback.getUser().getId(), recetaActual.getIdr(), 1);
                    } else{
                        Log.d("DELETINFAV", "0");
                        presenter.setFavourite(mCallback.getUser().getId(), recetaActual.getIdr(), 0);
                    }

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

            recipeAuthor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putInt("id", recetaActual.getAuthor());
                    mCallback.showProfile(b);
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


    /**
     * Realiza la carga de los datos de la receta en sus respectivas vistas
     * @param r Receta a mostrar
     */
    public void setRecipeData(Recipe r){
        try {
            if(r.getImage() != null && !r.getImage().equals("")) {
                try {
                    Picasso.with(getContext()).load(r.getImage()).resize(300, 200).onlyScaleDown().into(imvRecipe);
                } catch (Exception e) {
                    imvRecipe.setImageDrawable(getResources().getDrawable(R.drawable.addrecipe));
                }
            }
            else
                imvRecipe.setImageDrawable(getResources().getDrawable(R.drawable.addrecipe));


            try{
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(Const.FIREBASE_IMAGE_USER+"/"+String.valueOf(r.getAuthor()));
                mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        try {
                            Picasso.with(getContext()).load(task.getResult()).error(R.drawable.user_icon).into(imvRecipeUser);
                        }catch (RuntimeExecutionException ree){
                            imvRecipeUser.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        imvRecipeUser.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
                    }
                });
            }catch (Exception e){}
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

    /**
     * Actualiza los datos de la receta actualmente mostrada
     * @param recipe Nuevos datos de la receta
     */
    @Override
    public void showRecipeInfo(Bundle recipe) {
        Recipe rtmp = recipe.getParcelable("r");
        setRecipeData(rtmp);
    }

    /**
     * Cambia el estado de favorito de la receta tras la respuesta del servidor
     * @param recipe Receta con el nuevo estado de favorito
     */
    @Override
    public void setFavState(Recipe recipe) {
        if (recipe.getFav() == 0){
            recetaActual.setFav(0);
            fabFav.setImageResource(R.drawable.heart_outline);
        }else{
            recetaActual.setFav(1);
            fabFav.setImageResource(R.drawable.heart);
        }
    }

    @Override
    public void setListData(ArrayList<Recipe> recs) {

    }

    /**
     * Muestra un Snackbar al eliminar una receta
     */
    @Override
    public void cancelSearch() {
        Toast.makeText(getContext(), "La receta se ha eliminado.", Toast.LENGTH_SHORT).show();
        mCallback.showSocialFragment("");
    }

    /**
     * Carga un nuevo comentario realizado en la receta
     * @param c Comentarios de la receta
     */
    @Override
    public void addNewComment(ArrayList<Comment> c) {
        comments.clear();
        comments.addAll(c);
        adapterCommentFirebase.notifyDataSetChanged();
    }

    /**
     * Muestra un mensaje tras realizar ciertas acciones o al producirse un error
     * @param msg Mensaje a mostrar
     */
    @Override
    public void showNetworkError(String msg) {
        try {
            Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }catch (Exception e){}
    }

    /**
     * Muestra un cuadro de diálogo para confirmar la eliminación de una receta
     * @param name Nombre de la receta a eliminar
     */
    private void showSafeDelete(String name){
        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setTitle(getResources().getString(R.string.delete));
        customDialog.setMessage("¿Estás seguro de que quieres eliminar la receta '"+name+"'?\n No podrás deshacer esta acción");
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });
        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.deleteRecipe(recetaActual.getIdr());
            }
        }).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
    }
}
