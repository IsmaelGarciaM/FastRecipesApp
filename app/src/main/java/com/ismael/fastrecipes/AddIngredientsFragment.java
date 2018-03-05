package com.ismael.fastrecipes;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ismael.fastrecipes.db.DatabaseContract;
import com.ismael.fastrecipes.interfaces.IngredientPresenter;
import com.ismael.fastrecipes.model.Ingredient;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.presenter.IngredientsPresenterImpl;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * AddIngredientsFragment.class - Fragment con la vista para añadir ingredientes a ula publicación de una receta.
 * @author Ismael García
 */
public class AddIngredientsFragment extends Fragment{

    //Cuadro de texto para la búsqueda
    @BindView(R.id.actxvIng)
    AutoCompleteTextView searchI;

    @BindView(R.id.btnAddIng)
    Button btnAddIng;

    @BindView(R.id.lvAddIngredients)
    ListView ingList;

    @BindView(R.id.fabSaveNewIngredients)
    FloatingActionButton fabsave;

    private static AddIngredientsFragment aifInstance;   //Instancia singleton del fragment
    ArrayAdapter<String> adapter;                        //Adapter para el listado
    AddIngredientsListener mCallback;
    ArrayAdapter<String> listAdapter;
    Recipe tmpRecipe;
    String[] allIng;


    public AddIngredientsFragment() {
        // Required empty public constructor
    }

    /**
     * Método para instanciar el frament
     * @param args Bundle con la receta que se está editando o creando, para reconstruir al volver a mostrar
     * @see AddRecipeFragment
     * @return Devuelve la instancia del fragment, siempre única
     */
    public static AddIngredientsFragment getInstance(Bundle args){
        if(aifInstance == null) {
            aifInstance = new AddIngredientsFragment();
            aifInstance.setArguments(new Bundle());
        }
        if(args != null) {
            aifInstance.getArguments().putAll(args);
        }
        return  aifInstance;
    }

    /**
     * Interfaz para gestión del fragment desde HomeActivity
     */
    interface AddIngredientsListener{
        void showAddIngredients(Bundle b);
        void showAddRecipe(Bundle b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (AddIngredientsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement AddIngredientsListener interface");
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_ingredients, container, false);
        ButterKnife.bind(this, root);
        if(aifInstance.getArguments() != null){
                tmpRecipe = aifInstance.getArguments().getParcelable("recipe");
        }


        allIng = getResources().getStringArray(R.array.ingredients);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, allIng);
        listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line);
        if(tmpRecipe.getIngredients() != null && !tmpRecipe.getIngredients().equals("")){
            String[] a = tmpRecipe.getIngredients().split("\n");
            for(int i=0; i<+a.length;i++) {
                listAdapter.add(a[i]);
            }
        }
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchI.setThreshold(2);
        searchI.setAdapter(adapter);
        ingList.setAdapter(listAdapter);
        ingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //add quantity
                showAddQuantity(String.valueOf(listAdapter.getItem(i)), i);
            }
        });
        ingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //seguro que quieres borrar?
                showSafeDelete(String.valueOf(listAdapter.getItem(i)), i);
                return true;
            }
        });

        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                String in = "";
                for (int i = 0; i < listAdapter.getCount(); i++){
                    in += listAdapter.getItem(i) + "\n";
                }

                tmpRecipe.setIngredients(in);
                b.putParcelable("recipe", tmpRecipe);
                mCallback.showAddRecipe(b);
            }
        });

        searchI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAddQuantity(adapter.getItem(i));
            }
        });

        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchI.getText().toString().equals("")) {
                    listAdapter.add(searchI.getText().toString());
                    searchI.setText("");
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        if(aifInstance.getArguments().getParcelable("recipe") != null){
            listAdapter.clear();
            String[] ings = new String[]{};
            Recipe tmp = aifInstance.getArguments().getParcelable("recipe");
            if(tmp.getIngredients() != null && !tmp.getIngredients().equals("")) {
                ings = tmp.getIngredients().split("\n");
                listAdapter.addAll(ings);
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Muestra un cuadro de diálogo para confirmar el borrado de un elemento
     * @param ing Nombre del ingrediente a añadir
     * @param pos Posición que ocupa en el adaptador
     */
    private void showSafeDelete(String ing, final int pos){
        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setCancelable(false);
        customDialog.setTitle("¿Borrar "+ing+"?");
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listAdapter.remove(listAdapter.getItem(pos));
                listAdapter.notifyDataSetChanged();
            }
        }).show();

    }

    /**
     * Muestra un cuadro de diálogo para añadir una cantidad a un ingrediente
     * @param ing Ingrediente seleccionado
     * @param pos Posición en el adaptador
     */
    private void showAddQuantity(final String ing, final int pos){
        final String[] t = new String[1];

        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setCancelable(true);
        customDialog.setTitle(getResources().getString(R.string.add_quantity));

        customDialog.setView(R.layout.item_search);
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText edtTime = ((Dialog)dialogInterface).findViewById(R.id.edtNameRecSearch);
                t[0] = edtTime.getText().toString();
                if(!t[0].equals("")) {
                    listAdapter.remove(listAdapter.getItem(pos));
                    listAdapter.add(t[0] + " " + ing);
                }
            }
        }).show();

    }

    /**
     * Muestra un cuadro de diálogo para añadir una cantidad a un ingrediente
     * @param ing Ingrediente seleccionado
     */
    private void showAddQuantity(final String ing){
        final String[] t = new String[1];

        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setCancelable(false);
        customDialog.setTitle(getResources().getString(R.string.add_quantity));

        customDialog.setView(R.layout.item_search);
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText edtTime = ((Dialog)dialogInterface).findViewById(R.id.edtNameRecSearch);
                t[0] = edtTime.getText().toString();
                if(!t[0].equals("")) {
                    listAdapter.add(t[0] + " " + ing);
                    listAdapter.notifyDataSetChanged();
                }
                else{
                    listAdapter.add(ing);
                    listAdapter.notifyDataSetChanged();

                }
                searchI.setText("");

            }
        }).show();

    }
}
