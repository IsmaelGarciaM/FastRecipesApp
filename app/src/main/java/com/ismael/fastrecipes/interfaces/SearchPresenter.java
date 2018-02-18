package com.ismael.fastrecipes.interfaces;

import android.content.Context;

/**
 * Created by Ismael on 18/01/2018.
 */

public interface SearchPresenter {
    interface View {
        Context getContext();

    }

    void addFilter();
    void deleteFilter();

}
