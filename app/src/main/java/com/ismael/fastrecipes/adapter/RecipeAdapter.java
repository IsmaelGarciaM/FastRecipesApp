package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.utils.Const;
import com.squareup.picasso.Picasso;

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
        @BindView(R.id.txvRecipeNper)
        TextView txvNPers;


        public int getIdr() {
            return idr;
        }

        public void setIdr(int id) {
            this.idr = id;
        }

        int idr;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public void bind(final Recipe r, final OnItemClickListener listener){

            this.txvName.setText(r.getName());
            this.txvRecipeTime.setText(String.valueOf(r.getTime()));
            this.txvRecipeDifficulty.setText(r.getDifficulty());
            this.txvNPers.setText(r.getnPers());
            this.txvRecipeRating.setText("-");

            if(r.getImage().startsWith("gs")) {

                StorageReference mStorageRefloadrec = FirebaseStorage.getInstance().getReference(Const.FIREBASE_IMAGE_RECIPE + "/" + String.valueOf(r.getIdr()));

                mStorageRefloadrec.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        try {
                            Picasso.with(context).load(task.getResult()).into(imgRecipe);
                        } catch (Exception e) {
                            imgRecipe.setImageDrawable(context.getResources().getDrawable(R.drawable.addrecipe));
                        }
                    }
                });
            } else if(r.getImage() != null && !r.getImage().equals("")) {
                try {
                    Picasso.with(context)
                            .load(r.getImage())
                            .into(this.imgRecipe);
                } catch (Exception e) {
                    this.imgRecipe.setImageDrawable(context.getResources().getDrawable(R.drawable.addrecipe));
                }
            }else
                this.imgRecipe.setImageDrawable(context.getResources().getDrawable(R.drawable.addrecipe));


            if(r.getCategories() != null)
                txvCategories.setText(r.getCategories()) ;

            this.setIdr(r.getIdr());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(r.getIdr());
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
                getCursor().getString(8), getCursor().getString(9),    getCursor().getString(10),getCursor().getString(11),
                getCursor().getString(12));
        try {
            int fav = getCursor().getInt(13);
            pro.setFav(fav);
        }catch (Exception e){}
        return pro;
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor != null) {
            items = newCursor;
            notifyDataSetChanged();
        }
    }

}
