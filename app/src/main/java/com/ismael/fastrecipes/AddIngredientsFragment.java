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
 * A simple {@link Fragment} subclass.
 */
public class AddIngredientsFragment extends Fragment{

    @BindView(R.id.actxvIng)
    AutoCompleteTextView searchI;

    @BindView(R.id.btnAddIng)
    Button btnAddIng;

    @BindView(R.id.lvAddIngredients)
    ListView ingList;

    @BindView(R.id.fabSaveNewIngredients)
    FloatingActionButton fabsave;
    boolean cont = false;

    static AddIngredientsFragment aifInstance;
    ArrayAdapter<String> adapter;
    TextWatcher twIng;

    AddIngredientsListener mCallback;
    ArrayAdapter<String> listAdapter;
    Recipe tmpRecipe;
    String[] allIng;

    public static AddIngredientsFragment getInstance(Bundle args){

        if(aifInstance == null)
            aifInstance = new AddIngredientsFragment();
        aifInstance.setArguments(args);
        return  aifInstance;
    }

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
    public void onStart() {
        super.onStart();

    }


    public AddIngredientsFragment() {
        // Required empty public constructor
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
        /*else {
            adapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, allIng);
        }*/

        listAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        if(tmpRecipe.getIngredients() != null && !tmpRecipe.getIngredients().equals("")){
            String[] a = tmpRecipe.getIngredients().split("\r\n");
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

        /*twIng = new TextWatcher() {
            String value = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (String.valueOf(charSequence).length() != 0 && String.valueOf(charSequence).length() > value.length()){
                        //if(!adapterOk.getCursor().isClosed())

                    }
                    value = String.valueOf(charSequence);
                }catch (Exception e){
                    Log.d("EXCTIONNOCONTROL", e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };

*/
        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle b = new Bundle();
                String in = "";
                for (int i = 0; i < listAdapter.getCount(); i++){
                    in += listAdapter.getItem(i) + "\r\n";
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
                listAdapter.add(searchI.getText().toString());
                searchI.setText("");
                listAdapter.notifyDataSetChanged();
            }
        });

       // searchI.addTextChangedListener(twIng);


    }

    private void showSafeDelete(String ing, final int pos){
        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
        customDialog.setCancelable(false);
        customDialog.setTitle("¿Borrar "+ing+"?");

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
                listAdapter.remove(listAdapter.getItem(pos));
                listAdapter.notifyDataSetChanged();
            }
        }).show();

    }

    private void showAddQuantity(final String ing, final int pos){
        final String[] t = new String[1];

        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
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
                listAdapter.remove(listAdapter.getItem(pos));
                listAdapter.add(t[0] + " " + ing);
            }
        }).show();

    }
    private void showAddQuantity(final String ing){
        final String[] t = new String[1];

        AlertDialog.Builder customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
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
                listAdapter.add(t[0] + " " + ing);
                listAdapter.notifyDataSetChanged();
                searchI.setText("");

            }
        }).show();

    }


}
