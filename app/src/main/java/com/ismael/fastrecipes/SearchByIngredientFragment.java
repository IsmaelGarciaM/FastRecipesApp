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
import com.ismael.fastrecipes.utils.Const;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByIngredientFragment extends Fragment{

    @BindView(R.id.sacsi)
    AutoCompleteTextView sacIng;

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
    ArrayAdapter<String> adapterGrvIngOk;
    ArrayAdapter<String> adapterGrvIngNot;
    ArrayList<String> ingredientsOk;
    ArrayList<String> ingredientsNot;
    ArrayAdapter<String> adapterOk;
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
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //filtersPos = new int[]{10,10};
        if(mCallback.getFilterByName(Const.f1) != null){
                    Collections.addAll(ingredientsOk, mCallback.getFilterByName(Const.f1).getContent().split(", "));
        }
        else
            ingredientsOk = new ArrayList<>();
        if(mCallback.getFilterByName(Const.f2) != null) {
                    Collections.addAll(ingredientsNot, mCallback.getFilterByName(Const.f2).getContent().split(", "));
        }
        else
            ingredientsNot = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_by_ingredient, container, false);
        ButterKnife.bind(this, rootView);


        adapterOk = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.ingredients));

        adapterGrvIngOk = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, ingredientsOk);
        adapterGrvIngNot = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, ingredientsNot);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sacIng.setThreshold(1);
        sacIng.setAdapter(adapterOk);
        grvIngOk.setAdapter(adapterGrvIngOk);
        grvIngNot.setAdapter(adapterGrvIngNot);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                mCallback.showSearchFragment(null);
            }
        });

        sacIng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!swtIn.isChecked()) {
                        if(!contains(adapterGrvIngOk, adapterOk.getItem(i))) {
                            adapterGrvIngOk.add(adapterOk.getItem(i));
                            adapterGrvIngOk.notifyDataSetChanged();
                        }
                    } else {
                        if(!contains(adapterGrvIngNot, adapterOk.getItem(i))) {
                            adapterGrvIngNot.add(adapterOk.getItem(i));
                            adapterGrvIngNot.notifyDataSetChanged();
                        }
                    }
                    sacIng.setText("");
               }

        });

        grvIngOk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterGrvIngOk.remove(adapterGrvIngOk.getItem(i));
                adapterGrvIngOk.notifyDataSetChanged();
            }
        });

        grvIngNot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterGrvIngNot.remove(adapterGrvIngNot.getItem(i));
                adapterGrvIngNot.notifyDataSetChanged();
            }
        });
    }

    boolean contains(ArrayAdapter adapter, String value){
        boolean exists = false;
        for(int i = 0; i < adapter.getCount(); i++){
            if (adapter.getItem(i).equals(value))
                exists = true;
            break;
        }
        return exists;
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
            if (mCallback.getFilterByName(Const.f1) != null)
                mCallback.getFilterByName(Const.f1).setContent(ingOk);
            else {
                Filter ingOkFilter = new Filter(Const.f1, ingOk);
                mCallback.addFilter(ingOkFilter);
            }
        }

        for (int i = 0; i < adapterGrvIngNot.getCount(); i++)
            ingNot += adapterGrvIngNot.getItem(i) + " ";

        if (!ingNot.equals("")) {
            if (mCallback.getFilterByName(Const.f2) != null)
                mCallback.getFilterByName(Const.f2).setContent(ingNot);
            else {
                Filter ingNotFilter = new Filter(Const.f2, ingNot);
                mCallback.addFilter(ingNotFilter);
            }
        }
    }

}
