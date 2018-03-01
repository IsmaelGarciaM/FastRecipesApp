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
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ismael.fastrecipes.FastRecipesApplication.getContext;

/**
 * Created by Ismael on 23/01/2018.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{

    Context context;
    View vista;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> com) {
        this.comments = com;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgCommentItem)
        ImageView imgUserComment;

        @BindView(R.id.txtCommentaristName)
        TextView txvNameUserComment;

        @BindView(R.id.txtComment)
        TextView txvComment;

        @BindView(R.id.txtCommentDate)
        TextView txvComDate;


        public ViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Comment cTmp = getItem(position);
        /*Picasso.with(context)
                .load("")
                .into(holder.imgUserComment);*/
        holder.txvNameUserComment.setText(cTmp.getNameAuthor());
        holder.txvComment.setText(cTmp.getContent());
        holder.txvComDate.setText(cTmp.getDate());



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(rootView);
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }

    public Comment getItem(int pos) {
        return comments.get(pos);
    }/*
    public CommentsAdapter(View itemView) {
        super(itemView);
        vista = itemView;
        context = itemView.getContext();
        //itemView.setOnClickListener(this);

    }




    public void bindComment(Comment comment) {

        TextView txvNameUser = (TextView)vista.findViewById(R.id.txtCommentaristName);
        TextView txvComment = (TextView)vista.findViewById(R.id.txtComment);
        TextView txvCommentDate = (TextView)vista.findViewById(R.id.txtCommentDate);
        CircleImageView civUser = (CircleImageView)vista.findViewById(R.id.imgCommentItem);

        // Set their text
        String s = comment.getNameAuthor();
        txvNameUser.setText(s);
        txvComment.setText(comment.getContent());
        txvCommentDate.setText(comment.getDate());

        if(comment.getImage() != null && !comment.getImage().equals(""))
            Picasso.with(getContext())
                    .load(comment.getImage())
                    .centerCrop()
                    .into(civUser);
        else
            civUser.setImageDrawable(getContext().getResources().getDrawable(R.drawable.user_icon));
    }

/*

    @Override
    public void onClick(View v){
        //SHOW COMMENTS AUTHOR PROFILE
    }
*/




   /* List<Comment> comList;
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
        ch.nameAuthor.setText(getItem(position).getNameAuthor());
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
