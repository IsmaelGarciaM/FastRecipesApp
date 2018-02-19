package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ismael on 18/02/2018.
 */

public class UsersAdapter extends ArrayAdapter {

    Context context;
    List<User> usList;
    int itemLayout;

    public UsersAdapter(@NonNull Context context, int resource, List<User> uslist) {
        super(context, resource, uslist);
        this.context = context;
        this.itemLayout = resource;
        this.usList = uslist;
    }

    @Override
    public int getCount() {
        return usList.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return usList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        UserHolder uh;
        View item = view;
        if (item == null) {
            item = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            uh = new UserHolder(item);
            item.setTag(uh);
        }
        else
            uh = (UserHolder) item.getTag();


        //cargar imagen
        uh.name.setText(getItem(position).getName());

        return item;
    }

    class UserHolder{
        CircleImageView userImage;
        TextView name;

        public UserHolder(View v){
            userImage = (CircleImageView) v.findViewById(R.id.imgUserFav);
            name = (TextView) v.findViewById(R.id.txvNameUserFav);
        }
    }
}