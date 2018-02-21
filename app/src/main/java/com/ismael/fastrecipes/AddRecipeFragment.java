package com.ismael.fastrecipes;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddRecipeFragment extends Fragment {


    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.imvAddRecipeImage)
    ImageView imvImageRecipe;

    @BindView(R.id.btnAddImg)
    Button btnAddImage;

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

    @BindView(R.id.edtDetailsRecipe)
    EditText edtElaboration;

    @BindView(R.id.txvAddRecipeCat)
    TextView txvCats;

    @BindView(R.id.fabSaveRecipe)
    FloatingActionButton fabSave;

    @BindView(R.id.tilNameRecipe)
    TextInputLayout tilName;

    @BindView(R.id.txvAddIngredients)
    TextView txvIng;



    static private AddRecipeFragment arfInstance;
    private AddRecipeFragmentListener mCallback;
    Recipe recipeTmp;
    //SearchIngredientsPresenter presenter;

    interface AddRecipeFragmentListener{
        void showAddRecipe(Bundle recipe);

        User getUser();

        void showSocialFragment();

        void onBackPressed();

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
        //presenter = new SearchIngredientsPresenterImpl(this);
        if(arfInstance.getArguments()!= null){
            recipeTmp = arfInstance.getArguments().getParcelable("recipe");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_recipe, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(arfInstance.getArguments() == null)
                    //presenter.addRecipe(createRecipe());

                else
                    //presenter.updateRecipe(createRecipe());
*/
                mCallback.onBackPressed();
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
                //b.putParcelable("recipe", recipeTmp);
                mCallback.showAddIngredients(b);
            }
        });
        
        btnAddCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                //b.putParcelable("recipe", recipeTmp);
               // mCallback.showSearchByCategories(b);
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

    @Override
    public void onStart() {
        super.onStart();
        if(recipeTmp != null){
            setRecipeData();
        }
    }

    Recipe createRecipe(){

        //presenter.comprobardatos
        int time = Integer.parseInt(btnAddTime.getText().toString().substring(0, 2));
        String difficulty = btnAddDifficulty.getText().toString();

        Recipe newR = new Recipe(mCallback.getUser().getId(), mCallback.getUser().getId(), mCallback.getUser().getName(), edtNameRecipe.getText().toString(), txvCats.getText().toString(),
                txvIng.getText().toString(), edtElaboration.getText().toString(), time, difficulty, 2,"TODAY",
                "null", "fastrecipes.com");

        return newR;
    }

    void setRecipeData(){
        String t = String.valueOf(recipeTmp.getTime())+" minutos";
        //imagen
        edtNameRecipe.setText(recipeTmp.getName());
        txvCats.setText(recipeTmp.getCategories());
        txvIng.setText(recipeTmp.getIngredients());
        btnAddTime.setText(t);
        btnAddDifficulty.setText(recipeTmp.getDifficulty());
        edtElaboration.setText(recipeTmp.getElaboration());
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
                t[0] = edtTime.getText().toString() + "   minutos";
                btnAddTime.setText(t[0]);
            }
        });


        customDialog.show();
    }

    private void showDifficultDialog(){
        AlertDialog.Builder builderDificult = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);

        builderDificult.setTitle("Dificultad")
                .setItems(R.array.diff_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = "";
                        switch (which) {
                            case 0:  content = "Tirado";
                                break;
                            case 1:  content = "Facil";
                                break;
                            case 2:  content = "Medio";
                                break;
                            case 3:  content = "Dificil";
                                break;
                        }

                        btnAddDifficulty.setText(content);
                        dialog.dismiss();
                    }
                });

        builderDificult.setCancelable(false).create().show();


    }
}
