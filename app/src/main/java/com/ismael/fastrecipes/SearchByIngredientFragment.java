package com.ismael.fastrecipes;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.utils.Const;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * SearchByIngredientFragment -> Vista de la pantalla de selección de ingredientes
 * @author Ismael Garcia
 */
public class SearchByIngredientFragment extends Fragment{

    @BindView(R.id.sacsi)
    AutoCompleteTextView sacIng;


    @BindView(R.id.txvquecontenga)
    TextView txvYes;

    @BindView(R.id.sacno)
    TextView txvNo;


    @BindView(R.id.txvIngIn)
    TextView txvIngIn;

    @BindView(R.id.txvIngOut)
    TextView txvIngOut;

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
    Typeface font;


    public static SearchByIngredientFragment getInstance(Bundle args){

        if(sbyfInstance == null) {
            sbyfInstance = new SearchByIngredientFragment();
            sbyfInstance.setArguments(new Bundle());
        }
        if(args!=null){
            sbyfInstance.getArguments().putAll(args);
        }
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
        ingredientsOk = new ArrayList<>();
        ingredientsNot = new ArrayList<>();

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

        font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        txvIngIn.setTypeface(font);
        txvIngOut.setTypeface(font);
        adapterOk = new ArrayAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.ingredients));
        txvIngIn.setTextColor(getResources().getColor(R.color.colorPrimary));
        adapterGrvIngOk = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, ingredientsOk);
        adapterGrvIngNot = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, ingredientsNot);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(adapterGrvIngOk.getCount() > 0)
            txvYes.setVisibility(View.VISIBLE);

        if(adapterGrvIngNot.getCount() > 0)
            txvNo.setVisibility(View.VISIBLE);

        txvIngIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swtIn.setChecked(false);
            }
        });
        txvIngOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swtIn.setChecked(true);
            }
        });
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

        swtIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(swtIn.isChecked()) {
                    txvIngIn.setTextColor(getResources().getColor(R.color.textColorPrimary));
                    txvIngOut.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                else{
                    txvIngIn.setTextColor(getResources().getColor(R.color.colorPrimary));
                    txvIngOut.setTextColor(getResources().getColor(R.color.textColorPrimary));

                }

            }
        });

        sacIng.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!swtIn.isChecked()) {
                        if(!contains(adapterGrvIngOk, adapterOk.getItem(i))) {
                            if(adapterGrvIngOk.getCount() == 0) {
                                txvYes.setVisibility(View.VISIBLE);
                                txvYes.setTypeface(font);
                            }
                            adapterGrvIngOk.add(adapterOk.getItem(i));
                            adapterGrvIngOk.notifyDataSetChanged();
                        }
                    } else {
                        if(!contains(adapterGrvIngNot, adapterOk.getItem(i))) {
                            if(adapterGrvIngNot.getCount() == 0){
                                txvNo.setVisibility(View.VISIBLE);
                                txvNo.setTypeface(font);
                            }
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
                if(adapterGrvIngOk.getCount() == 0)
                    txvYes.setVisibility(View.GONE);
                adapterGrvIngOk.notifyDataSetChanged();
            }
        });

        grvIngNot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterGrvIngNot.remove(adapterGrvIngNot.getItem(i));
                adapterGrvIngNot.notifyDataSetChanged();
                if(adapterGrvIngNot.getCount() == 0)
                    txvNo.setVisibility(View.GONE);
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
            ingOk += adapterGrvIngOk.getItem(i) + ", ";

        if (!ingOk.equals("")) {
            if (mCallback.getFilterByName(Const.f1) != null)
                mCallback.getFilterByName(Const.f1).setContent(ingOk.substring(0, ingOk.length()-2));
            else {
                Filter ingOkFilter = new Filter(Const.f1, ingOk.substring(0, ingOk.length()-2));
                mCallback.addFilter(ingOkFilter);
            }
        }

        for (int i = 0; i < adapterGrvIngNot.getCount(); i++)
            ingNot += adapterGrvIngNot.getItem(i) + ", ";

        if (!ingNot.equals("")) {
            if (mCallback.getFilterByName(Const.f2) != null)
                mCallback.getFilterByName(Const.f2).setContent(ingNot.substring(0, ingNot.length()-2));
            else {
                Filter ingNotFilter = new Filter(Const.f2, ingNot.substring(0, ingNot.length()-2));
                mCallback.addFilter(ingNotFilter);
            }
        }
    }

}
