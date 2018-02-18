package com.ismael.fastrecipes;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ismael.fastrecipes.adapter.PagerAdapter;
import com.ismael.fastrecipes.interfaces.CommentPresenter;
import com.ismael.fastrecipes.presenter.CommentPresenterImpl;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SocialActivityFragment extends Fragment{

  //  @BindView(R.id.cd_civUserProfile)
    CircleImageView userImage;
/*
    @BindView(R.id.cd_txvUserName)
    TextView userName;

    @BindView(R.id.navigation_head)
    NavigationView nav;*/

    private SocialActivityFragmentListener mCallback;
    static SocialActivityFragment safInstance;
    TabLayout tabLayout;
    FrameLayout frame;
    PagerAdapter adapter;

    public SocialActivityFragment() {
        // Required empty public constructor
    }



    public interface SocialActivityFragmentListener{
        void showSocialFragment();

        String getUserName();

        void showProfile(Bundle b);
    }

    public static SocialActivityFragment getInstance(Bundle args){

        if(safInstance == null){
            safInstance = new SocialActivityFragment();
        }
        safInstance.setArguments(args);
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
        //ButterKnife.bind(rootView);
        NavigationView nav = (NavigationView) rootView.findViewById(R.id.navigation_head);
        nav.inflateHeaderView(R.layout.header_navigation_view);
        View nav_header = LayoutInflater.from(getContext()).inflate(R.layout.header_navigation_view, null);
        ((TextView) nav_header.findViewById(R.id.cd_txvUserName)).setText(mCallback.getUserName());

        Picasso.with(getContext())
                .load("https://images.vexels.com/media/users/3/137047/isolated/preview/5831a17a290077c646a48c4db78a81bb-user-profile-blue-icon-by-vexels.png")
                .into((CircleImageView) nav_header.findViewById(R.id.cd_civUserProfile));

        ((CircleImageView) nav_header.findViewById(R.id.cd_civUserProfile)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCallback.showProfile(null);
            }
        });;
        nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.showProfile(null);

            }
        });
        nav.addHeaderView(nav_header);
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

        tabLayout.addTab(tabLayout.newTab().setText("Mis Recetas"));
        tabLayout.addTab(tabLayout.newTab().setText("Comentarios"));
        //viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0)
                    showMyRecipes();
                else if(tab.getPosition() == 1)
                    showMyComments();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        

    }

    void showMyRecipes(){
        MyRecipesFragment mrF = MyRecipesFragment.getInstance(null);
        getChildFragmentManager().beginTransaction().replace(R.id.vpSocial, mrF).commit();
    }
    void showMyComments(){
        MyCommentsFragment mcF = MyCommentsFragment.getInstance(null);
        getChildFragmentManager().beginTransaction().replace(R.id.vpSocial, mcF).commit();
    }
}
