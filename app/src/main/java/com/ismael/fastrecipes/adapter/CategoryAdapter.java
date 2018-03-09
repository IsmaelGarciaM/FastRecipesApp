package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ismael.fastrecipes.FastRecipesApplication;
import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.Category;
import com.ismael.fastrecipes.utils.ErrorMapUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * CategoryAdapter.class -> Adaptador para las categorías
 * @author Ismael Garcia
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Category> checkedCats;


    public CategoryAdapter(Context context, ArrayList<Category> checkedCats) {
        this.context = context;
        this.checkedCats = checkedCats;
    }


class ViewHolder extends RecyclerView.ViewHolder{
    @BindView(R.id.catIcon)
    ImageView imgCat;

    @BindView(R.id.cbxCatState)
    CheckBox cbxCatState;


    public ViewHolder(View v){
        super(v);
        ButterKnife.bind(this, v);
    }
}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final int p = position;
        holder.imgCat.setImageDrawable(checkedCats.get(holder.getAdapterPosition()).getImg());
        holder.cbxCatState.setText(checkedCats.get(holder.getAdapterPosition()).getName());
        holder.cbxCatState.setChecked(checkedCats.get(holder.getAdapterPosition()).isState());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cbxCatState.isChecked()) {
                    holder.cbxCatState.setChecked(false);
                    checkedCats.get(p).setState(false);
                }

                else {
                    holder.cbxCatState.setChecked(true);
                    checkedCats.get(p).setState(true);
                }

            }
        });
        //holder.cbxCatState.setText(rTmp.getTime());

        /*holder.cbxCatState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(isCatChecked(position)){
                    checkedCats.set(position, false);
                }
                else
                    checkedCats.set(position, true);

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return checkedCats.size();
    }



    public Category getItem(int position){
        return checkedCats.get(position);
    }

    public ArrayList<Category> getCats(){
        return checkedCats;
    }


}
