package com.ismael.fastrecipes;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.ismael.fastrecipes.adapter.FilteredRecipeAdapter;
import com.ismael.fastrecipes.exceptions.DataEntryException;
import com.ismael.fastrecipes.interfaces.ProfilePresenter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.presenter.ProfilePresenterImpl;
import com.ismael.fastrecipes.utils.PhotoUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

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
    FilteredRecipeAdapter adapter;
    ArrayList<Recipe> userRecipes;
    String urlImageEditTmp;
    private AlertDialog photoDialog;
    private Uri mImageUri;
    private static final int ACTIVITY_SELECT_IMAGE = 1020,
            ACTIVITY_SELECT_FROM_CAMERA = 1040, ACTIVITY_SHARE = 1030;
    private PhotoUtils photoUtils;

    private static ProfileFragment instance;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.imgProfileFragm)
    CircleImageView imgUser;

    @BindView(R.id.edtNameProfile)
    TextInputEditText edtName;

    @BindView(R.id.lvUserRecipes)
    ListView lvUserRecipes;

    @BindView(R.id.edtLocationProfile)
    TextInputEditText edtLocation;


   @BindView(R.id.edtRegdateProfile)
    TextInputEditText edtRegdate;

     @BindView(R.id.tilEmailProfile)
    TextInputLayout tilEmailProfile;

    @BindView(R.id.tilNameProfile)
    TextInputLayout tilNameProfile;

    @BindView(R.id.edtEmailProfile)
    TextInputEditText edtEmailProfile;



    @BindView(R.id.fabEditProfile)
    FloatingActionButton fabEditProfile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ProfilePresenterImpl(this);
        edit = false;
        userRecipes = new ArrayList<>();
        photoUtils = new PhotoUtils(getContext());

        try{
            instance.getArguments().getInt("id");
        }catch (NullPointerException npe){
            fabEditProfile.setEnabled(false);
            fabEditProfile.setVisibility(View.INVISIBLE);
            idUser = -1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new FilteredRecipeAdapter(getContext(), R.layout.item_recipes_list, userRecipes);
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
        if(idUser != -1) {
            presenter.getUser(idUser);
            presenter.getUserRecipes(idUser);
        }
        {
            presenter.getUserRecipes(mCallback.getUser().getId());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fabEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                edit=!edit;

                if (edit){
                    edtName.setEnabled(false);
                    edtLocation.setEnabled(false);
                    edtEmailProfile.setEnabled(false);
                    view.setBackgroundColor(getResources().getColor(R.color.backgroundEdit));
                    fabEditProfile.setImageResource(R.drawable.ic_edit);
                }else{
                    edtName.setEnabled(true);
                    edtEmailProfile.setEnabled(true);
                    view.setBackgroundColor(getResources().getColor(R.color.backgroundEdit));
                    edtLocation.setEnabled(true);
                    fabEditProfile.setImageResource(R.drawable.ic_save);
                    presenter.editProfile(getUserData());
                }
            }
        });
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit){
                    if(!getPhotoDialog().isShowing() && getActivity().isFinishing())
                        getPhotoDialog().show();
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

    @Override
    public void setUserRecipesData(ArrayList<Recipe> recs) {
        userRecipes.addAll(recs);
        adapter.notifyDataSetChanged();
    }

    User getUserData(){



        tilNameProfile.setErrorEnabled(false);
        tilNameProfile.setError(null);

        tilEmailProfile.setErrorEnabled(false);
        tilEmailProfile.setError(null);

        String mail = String.valueOf(edtEmailProfile.getText());
        String name = String.valueOf(edtName.getText());

        //si aparece un error se cancela el login; cancel = true
        boolean cancel = false;
        View focusView = null;

        //El presentador comprueba los datos
        try {
            presenter.validateMail(mail);
        } catch (DataEntryException exc) {
            tilEmailProfile.setErrorEnabled(true);
            tilEmailProfile.setError(exc.getMessage());
            focusView = tilEmailProfile;
            cancel = true;
        }

        try {
            presenter.validateName(name);
        } catch (DataEntryException exc) {
            tilNameProfile.setErrorEnabled(true);
            tilNameProfile.setError(exc.getMessage());
            focusView = tilNameProfile;
            cancel = true;
        }

        User tmp = new User(mCallback.getUser().getId(), mCallback.getUser().getEmail(), name, "", edtLocation.getText().toString(), mCallback.getUser().getRegdate(), adapter.getCount(), mCallback.getUser().getImage(), mCallback.getUser().getUid());
        return tmp;
    }

    void loadImage(){
       // Intent i = new Intent(Intent.ACTION_PICK, android.provider.)
    }

    private AlertDialog getPhotoDialog() {
        if (photoDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("/");
            builder.setPositiveButton(R.string.camera, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    File photo = null;
                    try {
                        // place where to store camera taken picture
                        photo = PhotoUtils.createTemporaryFile("picture", ".jpg", getContext());
                        photo.delete();
                    } catch (Exception e) {
                        Log.v(getClass().getSimpleName(),"Can't create file to take picture!");
                    }
                    mImageUri = Uri.fromFile(photo);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    startActivityForResult(intent, ACTIVITY_SELECT_FROM_CAMERA);

                }

            });
            builder.setNegativeButton(R.string.gallery, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
                }

            });
            photoDialog = builder.create();

        }
        return photoDialog;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null)
            outState.putString("Uri", mImageUri.toString());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState.containsKey("Uri")) {
            mImageUri = Uri.parse(savedInstanceState.getString("Uri"));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            getImage(mImageUri);
        } else if (requestCode == ACTIVITY_SELECT_FROM_CAMERA
                && resultCode == RESULT_OK) {
            getImage(mImageUri);
        }
    }

    public void getImage(Uri uri) {
        Bitmap bounds = photoUtils.getImage(uri);
        if (bounds != null) {
            imgUser.setImageBitmap(bounds);
        } else {
            Log.d("Error IMAGEN", "Error al cargar la imagen");

        }
    }
}
