package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.utils.Const;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ismael on 19/02/2018.
 */

public class FilteredRecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    List<Recipe> relist;
    public FilteredRecipeAdapter(@NonNull Context context, int resource, @NonNull List<Recipe> filters) {
        super(context, R.layout.item_filter_list, filters);
        this.relist = filters;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(relist != null)
            return relist.size();
        else return 0;
    }

    @Nullable
    @Override
    public Recipe getItem(int position) {
        return relist.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        final FiltRecipeHolder frh;
        View item = view;
        if (item == null) {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_recipes_list, parent, false);
            frh = new FiltRecipeHolder(item);
            item.setTag(frh);
        }
        else
            frh = (FiltRecipeHolder)item.getTag();


        if(getItem(position).getImage() != null && !getItem(position).getImage().equals("")) {
            try {
                Picasso.with(context)
                        .load(getItem(position).getImage()).resize(300, 200)
                        .error(R.drawable.addrecipe)
                        .into(frh.imgRecipe);

            } catch (Exception e) {
                frh.imgRecipe.setImageDrawable(context.getResources().getDrawable(R.drawable.addrecipe));
            }
        }else
            frh.imgRecipe.setImageDrawable(context.getResources().getDrawable(R.drawable.addrecipe));

        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        frh.txvName.setTypeface(font);
        frh.txvCategories.setTypeface(font);


        frh.txvName.setText(getItem(position).getName());
        frh.txvCategories.setText(getItem(position).getCategories());
        frh.txvRecipeTime.setText(String.valueOf(getItem(position).getTime()) + " min.");
        frh.txvRecipeDifficulty.setText(getItem(position).getDifficulty());
        frh.txvRecipeRating.setText(String.valueOf(getItem(position).getRating())+ " /5");
        frh.txvRecipeNPers.setText(String.valueOf(getItem(position).getnPers()));

        return item;
    }

    public void refreshList(){
        notifyDataSetChanged();
    }

    class FiltRecipeHolder{
        CircleImageView imgRecipe;
        TextView txvName;
        TextView txvCategories;
        TextView txvRecipeTime;

        TextView txvRecipeDifficulty;
        TextView txvRecipeRating;
        TextView txvRecipeNPers;

        public FiltRecipeHolder(View v){
            imgRecipe = (CircleImageView) v.findViewById(R.id.imvRecipeImg);
            txvName =(TextView) v.findViewById(R.id.txvRecipeName);
            txvCategories = (TextView) v.findViewById(R.id.txvRecipeCategories);
            txvRecipeTime = (TextView) v.findViewById(R.id.txvTimeRecipe);
            txvRecipeDifficulty = (TextView) v.findViewById(R.id.txvRecipeDifficult);
            txvRecipeRating = (TextView) v.findViewById(R.id.txvRecipeRating);
            txvRecipeNPers = (TextView) v.findViewById(R.id.txvRecipeNper);

        }
    }

    @Override
    public void add(@Nullable Recipe object) {
        super.add(object);
        relist.add(object);
    }

    @Override
    public void remove(@Nullable Recipe object) {
        super.remove(object);
        relist.remove(object);
    }

    public Recipe getRecipeById(int id){
        Recipe frtmp = null;
        for (int i = 0; i < relist.size(); i++){
            if(relist.get(i).getIdr() == id){
                frtmp = relist.get(i);
                break;
            }
        }
        return frtmp;
    }
}
