package com.ismael.fastrecipes;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FilterQueryProvider;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Switch;

import com.ismael.fastrecipes.db.DatabaseContract;
import com.ismael.fastrecipes.interfaces.IngredientPresenter;
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.presenter.IngredientsPresenterImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByIngredientFragment extends Fragment implements IngredientPresenter.View{

    @BindView(R.id.sacsi)
    AutoCompleteTextView sacIngOk;

    @BindView(R.id.grvsi)
    GridView grvIngOk;

    @BindView(R.id.swtIngr)
    Switch swtIn;

    @BindView(R.id.grvno)
    GridView grvIngNot;

    @BindView(R.id.fabSaveIngredients)
    FloatingActionButton fabSave;

    static private SearchByIngredientFragment sbyfInstance;
    private SearchIngredientsListener mCallback;
    IngredientPresenter presenter;
    ArrayAdapter<String> adapterGrvIngOk;
    ArrayAdapter<String> adapterGrvIngNot;
    ArrayList<String> ingredientsOk;
    ArrayList<String> ingredientsNot;
    SimpleCursorAdapter adapterOk;
    TextWatcher tw;
    ArrayList<Filter> backup;
    int[] filtersPos;


    public static SearchByIngredientFragment getInstance(Bundle args){

        if(sbyfInstance == null)
            sbyfInstance = new SearchByIngredientFragment();
        sbyfInstance.setArguments(args);
        return  sbyfInstance;
    }

    public SearchByIngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public void addIngredient(int list) {

    }

    @Override
    public void setIngCursorData(Cursor data) {
            adapterOk.swapCursor(data);
    }

    interface SearchIngredientsListener{
        void showSearchByIngredients(Bundle data);
        void showSearchFragment(Bundle b);
        Filter getFilter(int pos);
        void addFilter(Filter f);
        void removeFilter(int pos);
        int getNFilters();

        Filter getFilterByName(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (SearchIngredientsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement SearchIngredientsListener interface");
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if(backup != null){
            adapterGrvIngOk.addAll(backup.get(0).getContent().split(","));
            adapterGrvIngOk.notifyDataSetChanged();

            adapterGrvIngNot.addAll(backup.get(1).getContent().split(","));
            adapterGrvIngNot.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new IngredientsPresenterImpl(this);
        //filtersPos = new int[]{10,10};
        try {
            backup = sbyfInstance.getArguments().getParcelableArrayList("filters");

        }catch (NullPointerException npe){
           // backup = new ArrayList<Filter>(){};
        }
        /*if (filters.size() > 0){
            for (int i = 0; i < mCallback.getNFilters() ; i++){
                if(mCallback.getFilter(i).getType().equals("Que contenga")) {
                    Collections.addAll(ingredientsOk, mCallback.getFilter(i).getContent().split(", "));
                    filtersPos[0] = i;
                }
                else if(mCallback.getFilter(i).getType().equals("Que no contenga")) {
                    Collections.addAll(ingredientsNot, mCallback.getFilter(i).getContent().split(", "));
                    filtersPos[0] = i;
                }
            }
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_by_ingredient, container, false);
        ButterKnife.bind(this, rootView);

        ingredientsOk = new ArrayList<>();
        ingredientsNot = new ArrayList<>();

        String[] columns = new String[] {DatabaseContract.IngredientEntry.COLUMN_NAME};
        int[] to = new int[] { android.R.id.text1 };
        adapterOk = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, null, columns, to);

        adapterGrvIngOk = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, ingredientsOk);
        adapterGrvIngNot = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, ingredientsNot);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sacIngOk.setThreshold(1);
        grvIngOk.setAdapter(adapterGrvIngOk);
        grvIngNot.setAdapter(adapterGrvIngNot);





        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                mCallback.showSearchFragment(null);
            }
        });

        tw = new TextWatcher() {
            String value = "";
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (String.valueOf(charSequence).length() != 0 && String.valueOf(charSequence).length() > value.length()){
                        presenter.getIngredients(String.valueOf(charSequence));
                    }
                    value = String.valueOf(charSequence);
                }catch (Exception e){
                    Log.d("EXCTIONNOCONTROL", e.getMessage());
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        };


        sacIngOk.addTextChangedListener(tw);
        sacIngOk.setAdapter(adapterOk);
        sacIngOk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterOk.getCursor() != null) {
                    if (!swtIn.isChecked()) {
                        adapterGrvIngOk.add(adapterOk.getCursor().getString(1));
                        adapterGrvIngOk.notifyDataSetChanged();
                    } else {
                        adapterGrvIngNot.add(adapterOk.getCursor().getString(1));
                        adapterGrvIngNot.notifyDataSetChanged();
                    }
                    adapterOk.changeCursor(null);
                    sacIngOk.setText("");
               }

            }
        });
/*
        sacIngOk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                adapterGrvIngOk.add(adapterOk.getItem(i).toString());
                adapterGrvIngOk.notifyDataSetChanged();
                //presenter.addIngredientToList(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //sacIngOk.getText();
            }
        });*/
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void save(){
        String ingOk = "";
        String ingNot = "";

        for (int i = 0; i < adapterGrvIngOk.getCount(); i++)
            ingOk += adapterGrvIngOk.getItem(i) + " ";

        if (!ingOk.equals("")) {
            if (mCallback.getFilterByName("Que contenga") != null)
                mCallback.getFilterByName("Que contenga").setContent(ingOk);
            else {
                Filter ingOkFilter = new Filter("Que contenga", ingOk);
                mCallback.addFilter(ingOkFilter);
            }
        }

        for (int i = 0; i < adapterGrvIngNot.getCount(); i++)
            ingNot += adapterGrvIngNot.getItem(i) + " ";

        if (!ingNot.equals("")) {
            if (mCallback.getFilterByName("Que no lleve:") != null)
                mCallback.getFilterByName("Que no lleve:").setContent(ingNot);
            else {
                Filter ingNotFilter = new Filter("Que no lleve:", ingNot);
                mCallback.addFilter(ingNotFilter);
            }
        }
    }

}
