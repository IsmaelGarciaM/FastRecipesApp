package com.ismael.fastrecipes;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismael.fastrecipes.adapter.PagerAdapter;
import com.ismael.fastrecipes.model.User;
import com.ismael.fastrecipes.utils.Const;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * SocialActivityFragment.class - Fragment con la vista de actividad social del usuario
 * Contiene un tabLayout para visualizar las recetas del usuario logueado o sus comentarios
 * También el acceso al perfil.
 */
public class SocialActivityFragment extends Fragment{

    @BindView(R.id.cd_civUserProfile)
    CircleImageView imvUser;

    @BindView(R.id.cd_txvUserName)
    TextView txvUserName;


    private SocialActivityFragmentListener mCallback;
    static SocialActivityFragment safInstance;
    TabLayout tabLayout;
    FrameLayout frame;
    PagerAdapter adapter;

    public SocialActivityFragment() {
        // Required empty public constructor
    }


    /**
     * Interfaz de escucha de la clase HomeAcivity
     */
    public interface SocialActivityFragmentListener{
        void showSocialFragment(String msg);
        String getUserName();
        void showProfile(Bundle b);
        User getUser();
    }

    /**
     * Instanciador del fragment que asegura una única instancia
     * @param args Datos previos para la carga del fragment
     * @return Devuelve la instancia de la vista
     */
    public static SocialActivityFragment getInstance(Bundle args){
        if(safInstance == null){
            safInstance = new SocialActivityFragment();
            safInstance.setArguments(new Bundle());
        }
        if(args!=null){
            safInstance.getArguments().putAll(args);
        }
        return  safInstance;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_social_activity, container, false);
        ButterKnife.bind(this, rootView);

        if(mCallback.getUser().getImage() != null && !mCallback.getUser().getImage().equals("")) {

            try {
                Picasso.with(getContext())
                        .load(mCallback.getUser().getImage())
                        .resize(110, 145).onlyScaleDown()
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .noFade()
                        .error(R.drawable.user_icon)
                        .into(imvUser);
            }catch (Exception e){
                imvUser.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
            }
        }
        else{
            imvUser.setImageDrawable(getResources().getDrawable(R.drawable.user_icon));
        }
        txvUserName.setText(mCallback.getUserName());
        tabLayout = (TabLayout) rootView.findViewById(R.id.tlSocialMenu);
        frame = (FrameLayout) rootView.findViewById(R.id.vpSocial);

        showMyRecipes();
        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mCallback = (SocialActivityFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement SocialActivityFragmentListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(android.view.View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //viewPager.setAdapter(adapter);
        //tabLayout.setupWithViewPager(viewPager);

        tabLayout.addTab(tabLayout.newTab().setText(getContext().getResources().getString(R.string.myrecipes)));
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0)
                    showMyRecipes();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        imvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putInt("id", mCallback.getUser().getId());
                mCallback.showProfile(b);
            }
        });

        if(safInstance.getArguments().getString("message") != null && !safInstance.getArguments().getString("message").equals("") ) {
            String msg = safInstance.getArguments().getString("message");
            Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
        }


    }

    /**
     * Carga la vista de las recetas del usuario en el TabLayout
     */
    void showMyRecipes(){
        MyRecipesFragment mrF = MyRecipesFragment.getInstance(null);
        getChildFragmentManager().beginTransaction().replace(R.id.vpSocial, mrF).commit();
    }

    /**
     * Carga la vista de los comentarios del usuario en el TabLayout
     */
    void showMyComments(){
        MyCommentsFragment mcF = MyCommentsFragment.getInstance(null);
        getChildFragmentManager().beginTransaction().replace(R.id.vpSocial, mcF).commit();
    }
}
