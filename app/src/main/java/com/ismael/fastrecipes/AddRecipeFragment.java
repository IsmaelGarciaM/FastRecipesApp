package com.ismael.fastrecipes;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;
import com.ismael.fastrecipes.provider.GenericFileProvider;
import com.ismael.fastrecipes.utils.Const;
import com.ismael.fastrecipes.utils.PhotoUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends Fragment implements RecipesPresenter.View{


    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.imvAddRecipeImage)
    ImageView imvImageRecipe;

    @BindView(R.id.btnAddImg)
    Button btnAddImage;

    @BindView(R.id.edtImageUrl)
    EditText edtImageUrl;
    @BindView(R.id.edtSourceRecipe)
    EditText edtSourceRecipe;
    @BindView(R.id.tilNameRecipe)
    TextInputLayout tilName;
    @BindView(R.id.edtNameRecipe)
    EditText edtNameRecipe;

    @BindView(R.id.btnAddCategories)
    Button btnAddCategories;

    @BindView(R.id.btnAddTime)
    Button btnAddTime;

    @BindView(R.id.btnAddDifficult)
    Button btnAddDifficulty;

    @BindView(R.id.btnAddIngredient)
    Button btnAddIng;

    @BindView(R.id.tilElaborationRecipe)
    TextInputLayout tilElaboration;
    @BindView(R.id.edtElaborationRecipe)
    EditText edtElaboration;

    @BindView(R.id.txvAddRecipeCat)
    TextView txvCats;

    @BindView(R.id.npNpers)
    NumberPicker npNPers;

    @BindView(R.id.fabSaveRecipe)
    FloatingActionButton fabSave;

    @BindView(R.id.txvAddIng)
    TextView txvIng;

    static private AddRecipeFragment arfInstance;
    private AddRecipeFragmentListener mCallback;
    Recipe recipeTmp;
    RecipesPresenter presenter;
    private StorageReference mStorageRef;
    private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040, ACTIVITY_SHARE = 1030;
    boolean imageChanged;
    private PhotoUtils photoUtils;
    StorageReference mStorageRefload ;

    interface AddRecipeFragmentListener{
        void showAddRecipe(Bundle recipe);
        User getUser();
        void showSocialFragment(boolean show);
        void showAddIngredients(Bundle b);
        void showSearchByCategories(Bundle b);
    }


    public static AddRecipeFragment getInstance(Bundle args){

        if(arfInstance == null) {
            arfInstance = new AddRecipeFragment();
        }
        arfInstance.setArguments(args);
        return  arfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this, -1);
        photoUtils = new PhotoUtils(getContext());
        imageChanged = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        ButterKnife.bind(this, rootView);
        npNPers.setMaxValue(10);

        if(arfInstance.getArguments()!= null) {
            recipeTmp = arfInstance.getArguments().getParcelable("recipe");
            if(recipeTmp.getIdr() > 0)
                mStorageRefload = FirebaseStorage.getInstance().getReference(Const.FIREBASE_IMAGE_RECIPE+"/"+String.valueOf(recipeTmp.getIdr()));

            setRecipeData();

        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe r = createRecipe();
                if(r!= null) {
                    if (r.getIdr() > 0) {
                        presenter.modifyRecipe(r, mImageUri);
                    } else
                        presenter.addRecipe(r, mImageUri);
                    mCallback.showSocialFragment(true);
                }
            }
        });

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog("");
            }
        });
        btnAddDifficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDifficultDialog();
            }
        });
        
        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putParcelable("recipe", getTemporalRecipeData());
                if(mImageUri != null)
                    b.putParcelable("uri", mImageUri);
                mCallback.showAddIngredients(b);
            }
        });
        
        btnAddCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putParcelable("recipe", getTemporalRecipeData());
                if(mImageUri != null)
                    b.putParcelable("uri", mImageUri);
                mCallback.showSearchByCategories(b);
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPhotoDialog();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (AddRecipeFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement AddRecipeFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    Recipe createRecipe(){


        Recipe newR = new Recipe();

        //imagen
        if(imageChanged) {
            //
                newR.setImage("add");

        }else if(recipeTmp != null && recipeTmp.getImage() != null && !recipeTmp.getImage().equals("")){
            newR.setImage(recipeTmp.getImage());
        } else if (!String.valueOf(edtImageUrl.getText()).equals("")) {
            newR.setImage(String.valueOf(edtImageUrl.getText()));
        } else{
            newR.setImage("");
        }
        String[] time;

        if(!edtImageUrl.getText().toString().equals("") && newR.getImage().equals(""))
            newR.setImage(edtImageUrl.getText().toString());



        tilName.setErrorEnabled(false);
        tilElaboration.setErrorEnabled(false);

        tilName.setError(null);
        tilElaboration.setError(null);

        //obtener valores
        String name = String.valueOf(edtNameRecipe.getText());
        String elaboration = String.valueOf(edtElaboration.getText());
        String ingredients = String.valueOf(txvIng.getText());
        String difficulty = btnAddDifficulty.getText().toString();
        time = btnAddTime.getText().toString().split(" ");
        String categories = String.valueOf(txvCats.getText());

        //si aparece un error se cancela el login; cancel = true
        boolean cancel = false;
        View focusView = null;

        //El presentador comprueba los datos
        try{
            presenter.validateField(time[0]);
            presenter.validateField(difficulty);
            presenter.validateField(ingredients);

        }catch (DataEntryException exc){
            showError(exc.getMessage());
            cancel = true;
            focusView = txvIng;
        }

        try {
            presenter.validateName(name);
        } catch (DataEntryException exc) {
            tilName.setErrorEnabled(true);
            tilName.setError(exc.getMessage());
            focusView = tilName;
            cancel = true;
        }

        try {
            presenter.validateEla(elaboration);
        } catch (DataEntryException exc) {
            tilElaboration.setErrorEnabled(true);
            tilElaboration.setError(exc.getMessage());
            focusView = tilElaboration;
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
            newR = null;
        }
        //El presentador realiza el login
        else{
            //showProgress(true);
            //presenter.logIn(mail, pass);
            newR.setName(name);
            newR.setElaboration(elaboration);
            newR.setIngredients(ingredients);
            newR.setDifficulty(difficulty);
            newR.setnPers(String.valueOf(npNPers.getValue()));
            newR.setSource(edtSourceRecipe.getText().toString());
            int t = Integer.parseInt(time[0]);
            newR.setTime(t);
            if(categories.equals("Sin categorías."))
                newR.setCategories("");
            else
                newR.setCategories(categories);
            newR.setAuthor(mCallback.getUser().getId());

            if(recipeTmp.getIdr() >0){
                newR.setIdr(recipeTmp.getIdr());
            }

        }

        //(mCallback.getUser().getIdr(), mCallback.getUser().getIdr(), mCallback.getUser().getName(), edtNameRecipe.getText().toString(), txvCats.getText().toString(),
                //txvIng.getText().toString(), edtElaboration.getText().toString(), time, difficulty, 2,"TODAY",
                //"null", "fastrecipes.com");

        return newR;
    }

    private void showError(String msg) {
        Snackbar.make(edtElaboration, msg, Snackbar.LENGTH_LONG).show();
    }

    void setRecipeData(){
        String t;
        if(recipeTmp.getTime() > 0)
         t = String.valueOf(recipeTmp.getTime())+" minutos";
        else
            t = "Tiempo";
        //imagen
        edtNameRecipe.setText(recipeTmp.getName());

        if(recipeTmp.getImage() != null && !recipeTmp.getImage().equals("") && !imageChanged) {
                    Picasso.with(getContext()).load(recipeTmp.getImage()).into(imvImageRecipe);

        }
        else if ( mImageUri != null) {
            getImage(mImageUri);
        }
        else if (recipeTmp.getImage() != null && !recipeTmp.getImage().equals("")){
                Picasso.with(getContext()).load(recipeTmp.getImage()).into(imvImageRecipe);
                edtImageUrl.setText(recipeTmp.getImage());
            }

        txvCats.setText(recipeTmp.getCategories());
        txvIng.setText(recipeTmp.getIngredients());
        btnAddTime.setText(t);
        btnAddDifficulty.setText(recipeTmp.getDifficulty());
        edtElaboration.setText(recipeTmp.getElaboration());
        if(recipeTmp.getnPers() != null && !recipeTmp.getnPers().equals(""))
            npNPers.setValue(Integer.parseInt(recipeTmp.getnPers()));
    }

    Recipe getTemporalRecipeData(){
        Recipe tmpRecipe = new Recipe();
        tmpRecipe.setName(String.valueOf(edtNameRecipe.getText()));
        tmpRecipe.setCategories(String.valueOf(txvCats.getText()));
        String s = String.valueOf(btnAddTime.getText());
        if(!String.valueOf(btnAddTime.getText()).equals("Tiempo"))
            tmpRecipe.setTime(Integer.parseInt(String.valueOf(btnAddTime.getText()).substring(0, String.valueOf(btnAddTime.getText()).length() - 8)));
        else
            tmpRecipe.setTime(0);
        tmpRecipe.setIngredients(String.valueOf(txvIng.getText()));
        tmpRecipe.setDifficulty(String.valueOf(btnAddDifficulty.getText()));
        tmpRecipe.setElaboration(String.valueOf(edtElaboration.getText()));
        tmpRecipe.setSource(String.valueOf(edtSourceRecipe.getText()));
        tmpRecipe.setnPers(String.valueOf(npNPers.getValue()));
        return tmpRecipe;

    }

    private void showTimeDialog(String time){
        final String[] t = new String[1];
        AlertDialog.Builder customDialog;
        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
        customDialog.setCancelable(false);
        customDialog.setView(R.layout.dialog_time_picker);

        customDialog.setNegativeButton("Atrás", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //add filtro de tiempo
                EditText edtTime = ((Dialog)dialogInterface).findViewById(R.id.edtTime);

                String tmp = edtTime.getText().toString();
                if(!tmp.equals("")) {
                    t[0] = tmp + " minutos";
                    btnAddTime.setText(t[0]);
                }
            }
        });


        customDialog.show();
    }


    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            imvImageRecipe.setImageBitmap(bounds);
        } else {
            Log.d("Error IMAGEN", "Error al cargar la imagen");

        }
    }
    private void getPhotoDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setTitle("Elige una acción");
        builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        "android.media.action.IMAGE_CAPTURE");
                File photo = null;
                try {
                    // place where to store camera taken picture
                    photo = PhotoUtils.createTemporaryFile("picture", ".jpg", getContext());
                    photo.delete();
                } catch (Exception e) {
                    Log.v(getClass().getSimpleName(),"Can't create file to take picture!");
                }
                //mImageUri = Uri.fromFile(photo);
                mImageUri = GenericFileProvider.getUriForFile(getContext(), "com.ismael.fastrecipes", photo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);

            }

        });
        builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
            }

        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            getImage(mImageUri);
            imageChanged = true;
        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == RESULT_OK) {
            getImage(mImageUri);
            imageChanged = true;
        }
    }

    private void showDifficultDialog(){
        AlertDialog.Builder builderDificult = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);

        builderDificult.setTitle("Dificultad")
                .setItems(R.array.diff_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = "";
                        switch (which) {
                            case 1:  content = "Fácil";
                                break;
                            case 2:  content = "Medio";
                                break;
                            case 3:  content = "Difícil";
                                break;
                        }

                        btnAddDifficulty.setText(content);
                        dialog.dismiss();
                    }
                });

        builderDificult.setCancelable(false).create().show();


    }

    @Override
    public void showRecipeInfo(Bundle recipe) {

    }

    @Override
    public void setFavState(Recipe recipe) {

    }

    @Override
    public void setListData(ArrayList<Recipe> recs) {

    }

    @Override
    public void cancelSearch() {

    }

    @Override
    public void addNewComment(ArrayList<Comment> newComment) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null)
            outState.putString("Uri", mImageUri.toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Uri")) {
                mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
            }
        }
    }

}
