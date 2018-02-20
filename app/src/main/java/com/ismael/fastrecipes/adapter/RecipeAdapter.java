package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.Recipe;
import com.squareup.picasso.Picasso;

import java.security.spec.ECField;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ismael on 17/05/17.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Context context;
    private Cursor items;
    OnItemClickListener rListener;


    public interface OnItemClickListener {
        void onItemClick(int idRecipe);
    }

    public RecipeAdapter(Context context, OnItemClickListener rListener) {
        this.context = context;
        this.rListener = rListener;

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imvRecipeImg)
        CircleImageView imgRecipe;
        @BindView(R.id.txvRecipeName)
        TextView txvName;
        @BindView(R.id.txvRecipeCategories)
        TextView txvCategories;
        @BindView(R.id.txvTimeRecipe)
        TextView txvRecipeTime;
        @BindView(R.id.txvRecipeDifficult)
        TextView txvRecipeDifficulty;
        @BindView(R.id.txvRecipeRating)
        TextView txvRecipeRating;
        @BindView(R.id.txvRecipeLikes)
        TextView txvRecipeLikes;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        int id;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(final Recipe r, final OnItemClickListener listener){

            String rating = String.valueOf(r.getRating())+ "/5";
            this.txvName.setText(r.getName());
            this.txvRecipeTime.setText(String.valueOf(r.getTime()));
            this.txvRecipeDifficulty.setText(r.getDifficulty());
            this.txvRecipeRating.setText(rating);
            try {
                Picasso.with(context)
                        .load(r.getImage())
                        .into(this.imgRecipe);
            }catch (Exception e){}


            if(r.getCategories() != null)
                txvCategories.setText(r.getCategories()) ;

            setId(r.getId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(r.getId());
                }
            });
        };

    }
/*
        @Override
        public void onClick(View view) {
            Bundle arg = new Bundle();
            arg.putInt("id", id);
           // listener.showRecipe(arg);
        }
    */


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipes_list, parent, false);
        final ViewHolder holder = new ViewHolder(rootView);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Recipe rTmp = getItem(position);
        holder.bind(rTmp, rListener);
    }

    @Override
    public int getItemCount() {
        if(items != null)
            return items.getCount();
        else return 0;
    }

    Cursor getCursor(){
        return  items;
    }

    private int getRecipeId(int position) {
        if (items != null) {
            if (items.moveToPosition(position)) {
                return items.getInt(0);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    private Recipe getItem(int position){
        getCursor().moveToPosition(position);
        Recipe pro = new Recipe(getCursor().getInt(0), getCursor().getInt(1), getCursor().getString(2), getCursor().getString(3),
                getCursor().getString(4), getCursor().getString(5), getCursor().getString(6), getCursor().getInt(7),
                getCursor().getString(8), getCursor().getInt(9),    getCursor().getString(10),getCursor().getString(11),
                getCursor().getString(12),getCursor().getFloat(13), getCursor().getInt(14));
        return pro;
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor != null) {
            items = newCursor;
            notifyDataSetChanged();
        }
    }

}
