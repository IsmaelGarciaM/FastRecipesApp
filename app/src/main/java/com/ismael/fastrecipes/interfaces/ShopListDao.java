package com.ismael.fastrecipes.interfaces;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ismael.fastrecipes.model.ShopList;

import java.util.List;

/**
 * Created by Ismael on 19/01/2018.
 */

@Dao
public interface ShopListDao {

    @Query("SELECT name FROM shoplist")
    List<ShopList> getShopList();

}
