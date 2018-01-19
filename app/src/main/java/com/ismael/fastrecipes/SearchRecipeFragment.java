package com.ismael.fastrecipes;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ismael.fastrecipes.interfaces.SearchPresenter;
import com.ismael.fastrecipes.presenter.SearchPresenterImpl;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchRecipeFragment extends Fragment implements SearchPresenter.View {

    private SearchFragmentListener mCallback;
    static SearchRecipeFragment srfInstance;
    SearchPresenterImpl presenter;

    public SearchRecipeFragment() {
        // Required empty public constructor
    }

    public interface SearchFragmentListener{
        void showSearchFragment();
    }

    public static SearchRecipeFragment getInstance(Bundle args){

        if(srfInstance == null){
            srfInstance = new SearchRecipeFragment();
            srfInstance.setArguments(args);}
        return  srfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SearchPresenterImpl(this);
    }

    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.view.View rootView = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (SearchFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement ListChemistListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
