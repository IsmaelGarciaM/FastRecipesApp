package com.ismael.fastrecipes.presenter;

import android.content.Context;

import com.ismael.fastrecipes.interfaces.SearchPresenter;
import com.ismael.fastrecipes.model.ShopList;

import java.util.List;

/**
 * Created by Ismael on 18/01/2018.
 */

public class SearchPresenterImpl implements SearchPresenter{
    private Context context;
    private SearchPresenter.View view;

    public SearchPresenterImpl(SearchPresenter.View vista){
        this.view = vista;
        this.context = vista.getContext();
    }

    @Override
    public List<ShopList> getIngredients(String str) {
        return null;
    }
}
