package com.ismael.fastrecipes;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.ismael.fastrecipes.adapter.CategoryAdapter;
import com.ismael.fastrecipes.interfaces.CategoriesPresenter;
import com.ismael.fastrecipes.model.Category;
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.presenter.CategoriesPresenterImpl;
import com.ismael.fastrecipes.utils.Const;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchByCategoriesFragment extends Fragment implements CategoriesPresenter.View{

    public SearchByCategoriesFragment() {

        // Required empty public constructor
    }

    @BindView(R.id.rcvCategories)
    RecyclerView rcvCategories;

    @BindView(R.id.btnSaveCategories)
    FloatingActionButton fabSave;
    CategoryAdapter adapter;




    static private SearchByCategoriesFragment sbcfInstance;
    private SearchCategoriesListener mCallback;
    CategoriesPresenter presenter;
    ArrayList<Category> catList;
    String[] cats;
    boolean adding = false;
    Recipe tmp;

    interface SearchCategoriesListener{
        void showSearchByCategories(Bundle data);
        void showSearchFragment(Bundle data);

        Filter getFilter(int pos);
        void addFilter(Filter f);
        void removeFilter(int pos);
        int getNFilters();

        Filter getFilterByName(String s);

        void showAddRecipe(Bundle b);
    }

    public static SearchByCategoriesFragment getInstance(Bundle args){

        if(sbcfInstance == null) {
            sbcfInstance = new SearchByCategoriesFragment();
        }
        try{
            sbcfInstance.setArguments(args);
        }catch (NullPointerException npe){}
        return  sbcfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CategoriesPresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_by_categories, container, false);
        ButterKnife.bind(this, rootView);
        RecyclerView.LayoutManager lm = new GridLayoutManager(this.getContext(), 3);

        if(sbcfInstance.getArguments() != null && sbcfInstance.getArguments().getParcelable("recipe") != null){
            tmp = sbcfInstance.getArguments().getParcelable("recipe");
            adding = true;
            if(tmp!=null && tmp.getCategories() != null && !tmp.getCategories().equals("Sin categorías."))
                cats = tmp.getCategories().split(", ");
        }
        else {
            if(mCallback.getFilterByName(Const.f3) != null)
                cats = mCallback.getFilterByName(Const.f3).getContent().split(", ");
        }
        rcvCategories.setLayoutManager(lm);
        catList = new ArrayList<>();
        catList.addAll(presenter.getCategories(false));
        if(cats != null){
            for (String cat : cats) {
                for (int j = 0; j < catList.size(); j++)
                    if(cat.equals(catList.get(j).getName()))
                        catList.get(j).setState(true);
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        adapter = new CategoryAdapter(this.getContext(), catList);
        rcvCategories.setAdapter(adapter);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!adding) {
                    save();
                    mCallback.showSearchFragment(null);
                }
                else{
                    mCallback.showAddRecipe(addCats());
                }
                //rcvCategories.setAdapter(new CategoryAdapter(getParentFragment().getContext(), presenter.getCategories()));
            }


        });


    }

    Bundle addCats(){
        Bundle response = new Bundle();
        String content = "";
        for (int i = 0; i < adapter.getCats().size(); i++) {
            if (adapter.getCats().get(i).isState()) {
                content += adapter.getItem(i).getName() + ", ";
            }
        }

        if(content.length()>0)
            tmp.setCategories(content.substring(0, content.length() - 2));
        else
            tmp.setCategories("");

        response.putParcelable("recipe", tmp);
        return response;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (SearchCategoriesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement SearchCategoriesListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    private void save() {
        String content = "";
        for (int i = 0; i < adapter.getCats().size(); i++) {
            if (adapter.getCats().get(i).isState()) {
                content += adapter.getItem(i).getName() + ", ";
            }
        }

        if(content.length()>0) {
            if (mCallback.getFilterByName(Const.f3) != null)
                mCallback.getFilterByName(Const.f3).setContent(content.substring(0, content.length() - 2));

            else {
                Filter ftmp = new Filter(Const.f3, content.substring(0, content.length() - 2));
                mCallback.addFilter(ftmp);
            }
        }


    }
}
