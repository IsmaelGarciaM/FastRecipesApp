package com.ismael.fastrecipes;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ismael.fastrecipes.adapter.UsersAdapter;
import com.ismael.fastrecipes.interfaces.ProfilePresenter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.ProfilePresenterImpl;

import java.util.ArrayList;

import javax.xml.transform.Templates;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author Ismael Garcia
 * */
public class UsersListFragment extends Fragment implements ProfilePresenter.View {

    @BindView(R.id.lvUsers)
    ListView lvUsers;
    @BindView(R.id.txvTitleUsers)
    TextView txvTitle;
    @BindView(R.id.txvEmptyUsers)
    TextView txvEmpty;
    int idRecipe;
    ArrayList<User> users;

    static private UsersListFragment ulfInstance;
    private UsersListListener mCallback;
    ProfilePresenter presenter;
    UsersAdapter adapter;

    public UsersListFragment() {
        // Required empty public constructor
    }


    @Override
    public void setUserData(User u) {

    }

    @Override
    public void setUserRecipesData(ArrayList<Recipe> recs) {

    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showNetworkError(String msg) {
        Toast t = new Toast(FastRecipesApplication.getContext());
        t.setText(msg);
        t.setDuration(Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    @Override
    public void updateCurrentUser(User u) {

    }

    @Override
    public void setUserListData(ArrayList<User> u) {
        adapter.addAll(u);
        adapter.notifyDataSetChanged();
        txvEmpty.setVisibility(View.GONE);
        lvUsers.setVisibility(View.VISIBLE);
    }

    @Override
    public void cancelSearch() {
        txvEmpty.setVisibility(View.VISIBLE);
        lvUsers.setVisibility(View.GONE);
    }

    interface UsersListListener{
        void showProfile(Bundle b);
        void showUsersList(Bundle args);
    }


    public static UsersListFragment getInstance(Bundle args){

        if(ulfInstance == null) {
            ulfInstance = new UsersListFragment();
            ulfInstance.setArguments(new Bundle());
        }
        if(args!=null) {
            ulfInstance.getArguments().putAll(args);
        }
        return  ulfInstance;
    }


    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try{
            mCallback = (UsersListListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e.getMessage() + " activity must implement UsersListListener interface");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getUsersFav(idRecipe);// (idRecipe);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenterImpl(this);
        if(ulfInstance.getArguments()!= null && ulfInstance.getArguments().getInt("id") != 0){
            idRecipe = ulfInstance.getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_users_list, container, false);
        ButterKnife.bind(this, root);
        users = new ArrayList<>();
        adapter = new UsersAdapter(getContext(), R.layout.user_item, users);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvUsers.setAdapter(adapter);
    }
}
