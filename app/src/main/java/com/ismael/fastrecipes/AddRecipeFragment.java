package com.ismael.fastrecipes;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;
import com.ismael.fastrecipes.provider.GenericFileProvider;
import com.ismael.fastrecipes.utils.PhotoUtils;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import static android.app.Activity.RESULT_OK;

/**
 * AddRecipeFragment.class - Fragment que contiene la vista para publicar recetas nuevas o editar publicadas
 * @author Ismael García
 */
public class AddRecipeFragment extends Fragment implements RecipesPresenter.View{

    @BindView(R.id.imvAddRecipeImage)
    ImageView imvImageRecipe;

    @BindView(R.id.rldataRecipe)
    RelativeLayout dataRecipe;

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

    @BindView(R.id.pbRecipe)
    ProgressBar pbRecipe;

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

    private static AddRecipeFragment arfInstance;
    private AddRecipeFragmentListener mCallback;
    Recipe recipeTmp;
    RecipesPresenter presenter;
    private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020;
    private static final int ACTIVITY_SELECT_FROM_CAMERA = 1040;
    boolean imageChanged;
    private PhotoUtils photoUtils;
    String returnQuery;

    /**
     * Interfaz para la gestión con HomeActivity
     */
    interface AddRecipeFragmentListener{
        void showAddRecipe(Bundle recipe);
        User getUser();
        void showSocialFragment(String msg);
        void showAddIngredients(Bundle b);
        void showSearchByCategories(Bundle b);
        void showRecipe(Bundle recipe);
    }

    /**
     * Instanciador de la clase con patrón singleton
     * @param args Bundle con los datos de la receta si es para editar
     * @return Instancia del fragment
     */
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        ButterKnife.bind(this, rootView);
        npNPers.setMaxValue(10);
        imageChanged = false;
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clear();

        if(arfInstance.getArguments() != null && arfInstance.getArguments().getParcelable("recipe") != null) {
            recipeTmp = arfInstance.getArguments().getParcelable("recipe");

        }
        else{
            recipeTmp = new Recipe(-1,-1,"","","","","",-1, "", "","","","");

        }
        setRecipeData();

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe r = createRecipe();
                if(r!= null) {
                    if (r.getIdr() > 0) {
                        showProgress(true);
                        presenter.modifyRecipe(r, mImageUri);
                        returnQuery = getResources().getString(R.string.updatedrecipe);
                        clear();

                    } else {
                        showProgress(true);
                        presenter.addRecipe(r, mImageUri);
                        returnQuery = getResources().getString(R.string.recipepublished);
                        clear();
                    }

                }
            }
        });

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
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


    /**
     * Reestablece todos los campos a valores vacíos
     */
    void clear(){
        imvImageRecipe.setImageDrawable(getResources().getDrawable(R.drawable.addrecipe));
        edtImageUrl.setText("");
        edtNameRecipe.setText("");
        txvCats.setText("");
        npNPers.setValue(0);
        btnAddTime.setText(getResources().getString(R.string.time));
        btnAddDifficulty.setText(getResources().getString(R.string.difficulty));
        txvIng.setText("");
        edtElaboration.setText("");
        edtSourceRecipe.setText("");
    }

    /**
     * Comprueba los campos mínimos necesarios y crea el objeto necesario para la publicación de una receta
     * @return La nueva receta con los datos introducidos
     */
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

    @Override
    public void showNetworkError(String msg){
        showProgress(false);
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Muestra un error en caso de fallo de comprobación de alguno de los campos
     * @param msg Mensaje de error
     */
    private void showError(String msg) {
        showProgress(false);
        Snackbar.make(edtElaboration, msg, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Establece los datos en la pantalla de la receta nueva o a editar
     */
    void setRecipeData(){
        String t;
        if(recipeTmp.getTime() > 0)
         t = String.valueOf(recipeTmp.getTime())+" minutos";
        else
            t = "Tiempo";
        //imagen

        if(recipeTmp.getImage() != null && !recipeTmp.getImage().equals("") && mImageUri ==null) {
            Log.d("ADDRECIPE", "CARGANDO IMAGEN DESDE DATOS DE LA RECETA");
            Picasso.with(getContext()).load(recipeTmp.getImage()).into(imvImageRecipe);
            edtImageUrl.postDelayed(new Runnable() {
                @Override
                public void run() {
                    edtImageUrl.setText(recipeTmp.getImage());
                }
            }, 400);
        }
        else if ( mImageUri != null) {
            getImage(mImageUri);
        }
        else if (recipeTmp.getImage() != null && !recipeTmp.getImage().equals("")){
                Picasso.with(getContext()).load(recipeTmp.getImage()).into(imvImageRecipe);
                edtImageUrl.setText(recipeTmp.getImage());
        }

        edtNameRecipe.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtNameRecipe.setText(recipeTmp.getName());
            }
        },250);
        txvCats.setText(recipeTmp.getCategories());
        txvIng.setText(recipeTmp.getIngredients());
        btnAddTime.setText(t);
        edtSourceRecipe.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtSourceRecipe.setText(recipeTmp.getSource());
            }
        }, 500);
        if(recipeTmp.getDifficulty() == null || recipeTmp.getDifficulty().equals(""))
            btnAddDifficulty.setText("Dificultad");
        else
            btnAddDifficulty.setText(recipeTmp.getDifficulty());
        edtElaboration.postDelayed(new Runnable() {
            @Override
            public void run() {
                edtElaboration.setText(recipeTmp.getElaboration());
            }
        }, 750);
        if(recipeTmp.getnPers() != null && !recipeTmp.getnPers().equals(""))
            if(recipeTmp.getnPers().length()>2)
                npNPers.setValue(Integer.parseInt(recipeTmp.getnPers().substring(0, recipeTmp.getnPers().length() - 6)));
            else
                npNPers.setValue(Integer.parseInt(recipeTmp.getnPers()));

    }

    /**
     * Obtiene los datos de la receta que se va a crear para guardarlos mientras se accede a otras vistas para añadir
     * ingredientes o categorías
     * @return Nueva receta con los datos temporales de la receta a crear
     */
    Recipe getTemporalRecipeData(){
        Recipe tmpRecipe = new Recipe();
        if(recipeTmp.getIdr() > 0)
            tmpRecipe.setIdr(recipeTmp.getIdr());
        if(recipeTmp.getImage()!= null && !recipeTmp.getImage().equals("") && mImageUri == null)
            tmpRecipe.setImage(recipeTmp.getImage());
        else if(mImageUri != null){}
        else
            tmpRecipe.setImage(edtImageUrl.getText().toString());
        tmpRecipe.setName(String.valueOf(edtNameRecipe.getText()));
        tmpRecipe.setCategories(String.valueOf(txvCats.getText()));
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

    /**
     * Muestra un cuadro de diálogo para añadir un tiempo de receta
     */
    private void showTimeDialog(){
        final String[] t = new String[1];
        AlertDialog.Builder customDialog;
        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
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


    /**
     * Carga la imagen añadida en la vista obteniendo un Bitmap desde la URI de la imagen
     * @param uri Ruta de acceso a la imagen guardada en la memoria del dispositivo
     */
    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            imvImageRecipe.setImageBitmap(bounds);
        } else {
            Log.d("Error IMAGEN", "Error al cargar la imagen");

        }
    }

    /**
     * Muestra un cuadro de diálogo para añadir una imagen a la receta
     */
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
                //Obtenemos la URI de la imagen temporal creada desde la cámara
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

    /**
     * Recoge los datos tras la selección de una imagen
     * @param requestCode Indica cómo se ha cargado la imagen, cámara o galería
     * @param resultCode Código del resultado de la selección de la imagen
     * @param data Intent con los datos de la imagen
     */
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

    /**
     * Muestra un cuadro de diálogo para cambiar la dificultad de la receta
     */
    private void showDifficultDialog(){
        AlertDialog.Builder builderDificult = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        builderDificult.setTitle("Dificultad")
                .setItems(R.array.diff_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = "";
                        switch (which) {
                            case 0:  content =  getResources().getStringArray(R.array.diff_array)[0];
                                break;
                            case 1:  content =  getResources().getStringArray(R.array.diff_array)[1];
                                break;
                            case 2:  content = getResources().getStringArray(R.array.diff_array)[2];
                                break;
                        }

                        btnAddDifficulty.setText(content);
                        dialog.dismiss();
                    }
                });

        builderDificult.setCancelable(false).create().show();
    }

    /**
     * Guarda el estado del fragment
     * @param outState Datos de salida
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null)
            outState.putString("Uri", mImageUri.toString());
    }

    /**
     * Restituye el estado del fragment
     * @param savedInstanceState Datos de entrada
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Uri")) {
                mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
            }
        }
    }


    /**
     * Métodos innecesarios
     */
    @Override
    public void showRecipeInfo(Bundle recipe) {
        showProgress(false);
        Toast.makeText(getContext(), returnQuery, Toast.LENGTH_SHORT).show();
        mCallback.showRecipe(recipe);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter = null;
        photoUtils = null;
    }

    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        dataRecipe.setVisibility(show ? View.GONE : View.VISIBLE);
        dataRecipe.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dataRecipe.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        pbRecipe.setVisibility(show ? View.VISIBLE : View.GONE);
        pbRecipe.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                pbRecipe.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
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

}
