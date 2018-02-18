package com.ismael.fastrecipes;


import android.app.Activity;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
public class AddIngredientsFragment extends Fragment implements IngredientPresenter.View{

    @BindView(R.id.actxvIng)
    AutoCompleteTextView searchI;

    @BindView(R.id.btnAddIng)
    Button btnAddIng;

    @BindView(R.id.lvAddIngredients)
    ListView ingList;

    @BindView(R.id.fabSaveNewIngredients)
    FloatingActionButton fabsave;

    static AddIngredientsFragment aifInstance;
    SimpleCursorAdapter adapter;
    TextWatcher twIng;
    IngredientPresenter presenter;
    AddIngredientsListener mCallback;
    String addedIngredients;
     ArrayAdapter listAdapter;

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
        if(aifInstance.getArguments() != null){
            try{
                addedIngredients = aifInstance.getArguments().getString("ingredients");
            }catch (NullPointerException npe){}
        }
        presenter = new IngredientsPresenterImpl(this);
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
        if(addedIngredients != null){
            String[] a = addedIngredients.split("\r\n");
            for(int i=0; i<+a.length;i++) {
                listAdapter.add(a[i]);
            }
        }
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
        String[] columns = new String[] {DatabaseContract.IngredientEntry.COLUMN_NAME};
        int[] to = new int[] { android.R.id.text1 };
        adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, null, columns, to, 0);
        listAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchI.setAdapter(adapter);
        ingList.setAdapter(listAdapter);
        ingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //add quantity
            }
        });
        ingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //seguro que quieres borrar?
                listAdapter.remove(listAdapter.getItem(i));
                listAdapter.notifyDataSetChanged();
                return true;
            }
        });
        twIng = new TextWatcher() {
            String value = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (String.valueOf(charSequence).length() != 0 && String.valueOf(charSequence).length() > value.length()){
                        //if(!adapterOk.getCursor().isClosed())
                        presenter.getIngredients(String.valueOf(charSequence));
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


        fabsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Bundle b = new Bundle();
                Recipe rt = aifInstance.getArguments().getParcelable("newR");

                String in = "";
                for (int i = 0; i < listAdapter.getCount(); i++){
                    in += listAdapter.getItem(i) + "\r\n";
                }

                rt.setIngredients(in);
                b.putParcelable("recipe", rt);*/
                mCallback.showAddRecipe(null);
            }
        });

        searchI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listAdapter.add(adapter.getCursor().getString(1));
                searchI.setText("");
                listAdapter.notifyDataSetChanged();
            }
        });

        btnAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAdapter.add(searchI.getText().toString());
                listAdapter.notifyDataSetChanged();
            }
        });

        searchI.addTextChangedListener(twIng);
    }

    @Override
    public void addIngredient(int list) {

    }

    @Override
    public void setIngCursorData(Cursor data) {
        adapter.swapCursor(data);
    }
}
