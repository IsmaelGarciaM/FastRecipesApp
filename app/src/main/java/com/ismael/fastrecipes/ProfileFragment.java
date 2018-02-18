package com.ismael.fastrecipes;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ismael.fastrecipes.interfaces.ProfilePresenter;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.ProfilePresenterImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.ProfileListener} interface
 * to handle interaction events.
 */
public class ProfileFragment extends Fragment implements ProfilePresenter.View{

    private ProfileListener mCallback;
    private boolean edit;
    int idUser;
    ProfilePresenter presenter;

    private static ProfileFragment instance;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.imgProfileFragm)
    CircleImageView imgUser;

    @BindView(R.id.edtNameProfile)
    TextInputEditText edtName;

    @BindView(R.id.edtLocationProfile)
    TextInputEditText edtLocation;


    @BindView(R.id.edtRegdateProfile)
    TextInputEditText edtRegdate;

    @BindView(R.id.edtDetailsProfile)
    TextInputEditText edtDetails;



    @BindView(R.id.fabEditProfile)
    FloatingActionButton fabEditProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenterImpl(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        edit = false;
        try{
            instance.getArguments().getInt("id");
        }catch (NullPointerException npe){
            fabEditProfile.setEnabled(false);
            fabEditProfile.setVisibility(View.INVISIBLE);
            idUser = -1;
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try  {
            mCallback = (ProfileListener) activity;
        } catch (Exception e){
            throw new ClassCastException(activity.getClass()
                    + " must implement ProfileListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ProfileListener {
        void showProfile(Bundle b);
        User getUser();
    }


    public static ProfileFragment getInstance(Bundle b){
        if(instance == null)
            instance = new ProfileFragment();
        instance.setArguments(b);
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(idUser == -1){
            String n = mCallback.getUser().getName() + " " + mCallback.getUser().getSurname();
            edtName.setText(n);
            edtLocation.setText(mCallback.getUser().getLocation());
            edtRegdate.setText(mCallback.getUser().getRegdate());
        }
        else {
            presenter.getUser(idUser);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit){
                    edtName.setEnabled(false);
                    edtLocation.setEnabled(false);
                    //presenter.editUser(getUserData());
                    fabEditProfile.setImageResource(R.drawable.ic_edit);
                }else{
                    edtName.setEnabled(true);
                    edtLocation.setEnabled(true);
                    fabEditProfile.setImageResource(R.drawable.ic_save);
                }
            }
        });
    }

    @Override
    public void setUserData(ArrayList<User> us){
        User u = us.get(0);
        String n = u.getName() + " " + u.getSurname();
        edtName.setText(n);
        edtLocation.setText(u.getLocation());
        edtRegdate.setText(u.getRegdate());
        Picasso.with(this.getContext()).load(u.getImage()).into(imgUser);
    }

    User getUserData(){
        String[] names = edtName.getText().toString().split(" ");
        if(names.length > 2)
            names[1]+= " " + names[2];
        User tmp = new User(mCallback.getUser().getId(), mCallback.getUser().getEmail(), names[0], names[1], edtLocation.getText().toString(), mCallback.getUser().getRegdate(), mCallback.getUser().getNrecipes(), mCallback.getUser().getImage(), mCallback.getUser().getUid());
        return tmp;
    }
}
