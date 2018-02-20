package com.ismael.fastrecipes;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ismael.fastrecipes.adapter.CommentsAdapter;
import com.ismael.fastrecipes.interfaces.CommentPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.CommentPresenterImpl;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCommentsFragment extends Fragment implements CommentPresenter.View {


    public MyCommentsFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.lvMyComments)
    ListView lvMyComments;

    static private MyCommentsFragment mcfInstance;
    private MyCommentsFragmentListener mCallback;
    CommentPresenter presenter;
    CommentsAdapter adapter;
    ArrayList<Comment> myCom;

    @Override
    public void setCursorData() {
    }

    @Override
    public void setCommentsData(ArrayList<Comment> comments) {
        adapter.addAll(comments);
        adapter.notifyDataSetChanged();
    }

    interface MyCommentsFragmentListener{
        void showMyComments();
        User getUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        //presenter.showMyComments(1);//mCallback.getUser().getId());
        adapter.add(new Comment(0, "Ismael García", 1, "Excelente receta, la recomiendo :)", "18/02/18", 0, ""));
    }

    public static MyCommentsFragment getInstance(Bundle args){

        if(mcfInstance == null) {
            mcfInstance = new MyCommentsFragment();
        }
        mcfInstance.setArguments(args);
        return  mcfInstance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CommentPresenterImpl(this);
        myCom = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_comments, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new CommentsAdapter(this.getContext(),R.layout.item_comment, myCom);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvMyComments.setAdapter(adapter);
        lvMyComments.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.deleteComment(adapter.getItem(i).getId());
                return false;
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (MyCommentsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement MyCommentsFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }



}
