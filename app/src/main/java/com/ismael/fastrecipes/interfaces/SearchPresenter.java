package com.ismael.fastrecipes.interfaces;

import android.content.Context;

import com.ismael.fastrecipes.model.ShopList;

import java.util.List;

/**
 * Created by Ismael on 18/01/2018.
 */

public interface SearchPresenter {
    interface View {
        Context getContext();
    }

    List<ShopList> getIngredients(String str);
}
