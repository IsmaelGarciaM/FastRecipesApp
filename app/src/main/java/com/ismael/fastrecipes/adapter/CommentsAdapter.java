package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Ismael on 23/01/2018.
 */

public class CommentsAdapter extends ArrayAdapter<Comment>{

    Context context;
    List<Comment> comList;
    int itemLayout;

    public CommentsAdapter(@NonNull Context context, int resource, List<Comment> comlist) {
        super(context, resource, comlist);
        this.context = context;
        this.itemLayout = resource;
        this.comList = comlist;
    }

    @Override
    public int getCount() {
        return comList.size();
    }

    @Nullable
    @Override
    public Comment getItem(int position) {
        Comment c = comList.get(position);
        return c;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        CommentHolder ch;
        View item = view;
        if (item == null) {
            item = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
            ch = new CommentHolder(item);
            item.setTag(ch);
        }
        else
            ch = (CommentHolder) item.getTag();


        //cargar imagen
        ch.nameAuthor.setText(getItem(position).getIdAuthor());
        ch.comment.setText(getItem(position).getContent());
        ch.date.setText(getItem(position).getDate());

        return item;
    }

    class CommentHolder{
        CircleImageView userImage;
        TextView nameAuthor;
        TextView comment;
        TextView date;

        public CommentHolder(View v){
            userImage = (CircleImageView) v.findViewById(R.id.imgCommentItem);
            nameAuthor = (TextView) v.findViewById(R.id.txtCommentaristName);
            comment = (TextView) v.findViewById(R.id.txtComment);
            date = (TextView) v.findViewById(R.id.txtCommentDate);
        }
    }

/*
    private Context context;
    private Cursor items;

    public CommentsAdapter(Context context) {
        this.context = context;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgCommentItem)
        ImageView imgUserComment;

        @BindView(R.id.txtCommentaristName)
        TextView txvNameUserComment;

        @BindView(R.id.txtComment)
        TextView txvComment;


        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            Comment cTmp = getItem(position);
            Picasso.with(context)
                    .load("https://cdn.pixabay.com/photo/2014/12/21/23/28/recipe-575434_960_720.png")
                    .into(holder.imgUserComment);
            holder.txvNameUserComment.setText("");
            //holder.cbxCatState.setText(rTmp.getTime());


        }

        @Override
        public int getItemCount() {
            return items.getCount();
        }

        Cursor getCursor(){
            return  items;
        }


        private Comment getItem(int position){

        }
*/
}
