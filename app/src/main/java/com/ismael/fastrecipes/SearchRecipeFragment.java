package com.ismael.fastrecipes;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ismael.fastrecipes.adapter.FilterAdapter;
import com.ismael.fastrecipes.interfaces.RecipesPresenter;
import com.ismael.fastrecipes.model.Comment;
import com.ismael.fastrecipes.model.Filter;
import com.ismael.fastrecipes.model.Recipe;
import com.ismael.fastrecipes.presenter.RecipesPresenterImpl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ismael.fastrecipes.utils.Const.f1;
import static com.ismael.fastrecipes.utils.Const.f2;
import static com.ismael.fastrecipes.utils.Const.f3;
import static com.ismael.fastrecipes.utils.Const.f4;
import static com.ismael.fastrecipes.utils.Const.f5;
import static com.ismael.fastrecipes.utils.Const.f6;

/**
 * SearchRecipeFragment.class - Vista del buscador de recetas. Gestiona los filtros por los que se va a buscar una receta.
 * @author Ismael Garcia
 */
public class SearchRecipeFragment extends Fragment implements RecipesPresenter.View{

    /**
     * Variables de clase
     */
    @BindView(R.id.crvRecipeOfDay)
    CardView crvRecipeDay;

    @BindView(R.id.txvSerchSubtitle)
    TextView txvSubtitle;

    @BindView(R.id.txvRecipeOfDayTitle)
    TextView title2;

    @BindView(R.id.txvConfirmEmail)
    TextView txvConfirmEmail;

    @BindView(R.id.imvRecipeOfDay)
    ImageView imvRecipeDay;

    @BindView(R.id.imvInfo)
    ImageView imvInfo;

    @BindView(R.id.txvRecipeOfDayName)
    TextView txvRecipeDayName;

    @BindView(R.id.txvRecipeOfDayTime)
    TextView txvRecipeDayTime;

    @BindView(R.id.txvRecipeOfDayDifficulty)
    TextView txvRecipeDayDifficulty;

    @BindView(R.id.txvRecipeOfDayNPers)
    TextView txvRecipeDayNPers;

    @BindView(R.id.fabSearch)
    FloatingActionButton fabSearch;

    @BindView(R.id.btnDelete)
    Button btnDeleteFilters;

    @BindView(R.id.txvfilterEmptyList)
    TextView txvEmpty;

    @BindView(R.id.txvQqc)
    TextView title;

    @BindView(R.id.rcvFilterList)
    ListView lvFilters;

    @BindView(R.id.btnAddFilter)
    Button btnAddFilter;
    FirebaseAuth fa;

    private SearchFragmentListener mCallback;
    static SearchRecipeFragment srfInstance;
    AlertDialog.Builder customDialog = null;
    FilterAdapter filterAdapter;
    RecipesPresenter presenter;
    ArrayList<Recipe> recipeOfDay;

    public static SearchRecipeFragment getInstance(Bundle args){

        if(srfInstance == null){
            srfInstance = new SearchRecipeFragment();
            srfInstance.setArguments(new Bundle());
        }
        if(args != null) {
            srfInstance.getArguments().putAll(args);
        }
        return  srfInstance;
    }

    public SearchRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public void showRecipeInfo(Bundle recipe) {

    }

    @Override
    public void setFavState(Recipe recipe) {

    }

    @Override
    public void setListData(ArrayList<Recipe> rDay) {
        if(recipeOfDay == null){
            recipeOfDay = new ArrayList<Recipe>();
            recipeOfDay.add(rDay.get(0));
        }
        txvRecipeDayName.setText(recipeOfDay.get(0).getName());
        txvRecipeDayTime.setText(recipeOfDay.get(0).getTime() + " min.");
        txvRecipeDayDifficulty.setText(recipeOfDay.get(0).getDifficulty());
        txvRecipeDayNPers.setText(String.valueOf(recipeOfDay.get(0).getnPers()) + " pers.");
        if(recipeOfDay.get(0).getImage() != null && !recipeOfDay.get(0).getImage().equals("")){
            try{
                Picasso.with(getContext()).load(recipeOfDay.get(0).getImage())
                        .error(R.drawable.addrecipe).noFade()
                        .resize(128,128)
                        .into(imvRecipeDay);
            }catch(Exception e){}
        }
    }

    @Override
    public void cancelSearch() {

    }

    @Override
    public void addNewComment(ArrayList<Comment> newComment) {

    }

    @Override
    public void showNetworkError(String msg) {
        try {
            Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
        }catch(Exception e){}
    }

    //Interfaz para HomeActivity
    public interface SearchFragmentListener{
        void showSearchFragment(Bundle data);
        void showSearchByIngredients(Bundle data);
        void showSearchByCategories(Bundle data);
        Filter getFilter(int pos);
        void addFilter(Filter f);
        void removeFilter(int pos);
        int getPos(String type);
        Filter getFilterByName(String name);
        ArrayList<Filter> getFilters();
        void showRecipesList(Bundle b);
        void deleteFilters();
        void showRecipe(Bundle b);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (SearchFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.getMessage() + " activity must implement SearchFragmentListener interface");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RecipesPresenterImpl(this, 0);
    }



    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        android.view.View rootView = inflater.inflate(R.layout.fragment_search_recipe, container, false);
        ButterKnife.bind(this, rootView);
        filterAdapter = new FilterAdapter(this.getContext(), 0, mCallback.getFilters());
        if(mCallback.getFilters().size() == 0){
            txvEmpty.setVisibility(View.VISIBLE);
        }
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "yummycupcakes.ttf");
        title.setTypeface(font);
        title2.setTypeface(font);
        txvEmpty.setTypeface(font);
        txvRecipeDayName.setTypeface(font);
        txvSubtitle.setTypeface(font);
        return rootView;
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
        fa = FirebaseAuth.getInstance();
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fa.getCurrentUser().isEmailVerified())
                    mCallback.showRecipesList(null);
                else
                    showVerifyEmail();
            }
        });

        if(!fa.getCurrentUser().isEmailVerified()) {
            txvConfirmEmail.setVisibility(View.VISIBLE);
            imvInfo.setVisibility(View.VISIBLE);
        }

        btnAddFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilters();
            }
        });

        lvFilters.setAdapter(filterAdapter);
        lvFilters.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle b = new Bundle();
                String type = (filterAdapter.getItem(i)).getType();

                switch (type){
                    case f1:
                        mCallback.showSearchByIngredients(null);
                        break;
                    case f2:
                        mCallback.showSearchByIngredients(null);
                        break;
                    case f3:
                        mCallback.showSearchByCategories(null);
                        break;
                    case f4:
                        showNameDialog();
                        break;
                    case f5:
                        showTimeDialog("");
                        break;
                    case f6:
                        showDifficultDialog();
                        break;
                }
            }});
        lvFilters.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                filterAdapter.remove(filterAdapter.getItem(i));
                filterAdapter.refreshList();
                if(mCallback.getFilters().size() == 0)
                    txvEmpty.setVisibility(View.VISIBLE);
                return true;
            }
        });
        btnDeleteFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.deleteFilters();
                filterAdapter.refreshList();
                txvEmpty.setVisibility(View.VISIBLE);
            }
        });

        if(recipeOfDay != null){
            setListData(recipeOfDay);
        }

        crvRecipeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putParcelable("recipe", recipeOfDay.get(0));
                mCallback.showRecipe(b);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recipeOfDay == null){
            presenter.getRecipeOfDay();
        }
    }

    /**
     * Muestra el cuadro de diálogo para añadir un nuevo filtro
     */
    public void showFilters(){
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setTitle("Filtros");

        customDialog.setItems(new CharSequence[]{"Ingredientes", "Categorías", "Nombre", "Tiempo", "Dificultad"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0: mCallback.showSearchByIngredients(null);
                        break;
                    case 1: mCallback.showSearchByCategories(null);
                        break;
                    case 2: showNameDialog();
                        break;
                    case 3: showTimeDialog("");
                        break;
                    case 4:showDifficultDialog();
                        break;

                }
            }
        });

        customDialog.show();
    }

    /**
     * Muestra el cuadro de diálogo para añadir un nuevo filtro de tiempo máximo
     * @param time Tiempo máximo
     */
    private void showTimeDialog(String time){
        final String[] t = new String[1];

        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setTitle("Tiempo máximo en minutos.");
        customDialog.setCancelable(false);
        customDialog.setView(R.layout.dialog_time_picker);
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //add filtro de tiempo
                EditText edtTime = ((Dialog)dialogInterface).findViewById(R.id.edtTime);
                String tmp = edtTime.getText().toString();
                if(!tmp.equals("")) {
                    t[0] = tmp + " " + getResources().getString(R.string.minutes);
                    if (mCallback.getFilterByName(f5) != null) {
                        mCallback.getFilterByName(f5).setContent(t[0]);
                        filterAdapter.refreshList();
                    } else {
                        Filter ftmp = new Filter(f5, t[0]);
                        mCallback.addFilter(ftmp);
                        filterAdapter.refreshList();
                        if (txvEmpty.getVisibility() == View.VISIBLE)
                            hideEmpty();

                    }
                }
            }
        });

        customDialog.show();
    }

    /**
     * Muestra el cuadro de diálogo para añadir un nuevo filtro de dificultad
     */
    private void showDifficultDialog(){
        AlertDialog.Builder builderDificult = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);

        builderDificult.setTitle(f6)
                .setItems(R.array.diff_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String content = "";
                        switch (which) {
                            case 0:  content = getResources().getStringArray(R.array.diff_array)[0];
                             break;
                            case 1: content = getResources().getStringArray(R.array.diff_array)[1];
                                break;
                            case 2:  content = getResources().getStringArray(R.array.diff_array)[2];
                                break;
                        }

                        int p = mCallback.getPos(f6);
                        if (p != 10) {
                            mCallback.getFilter(p).setContent(content);
                            filterAdapter.refreshList();
                        }
                        else {
                            Filter ft = new Filter(f6, content);
                            mCallback.addFilter(ft);
                            if(txvEmpty.getVisibility() == View.VISIBLE)
                                hideEmpty();
                            filterAdapter.refreshList();
                        }
                        dialog.dismiss();
                    }
                });

        builderDificult.setCancelable(false).create().show();


    }

    /**
     * Muestra el cuadro de diálogo para añadir un nuevo filtro de nombre
     */
    private void showNameDialog(){
        final String[] t = new String[1];

        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        customDialog.setCancelable(false);
        customDialog.setTitle("Busca por el nombre de la receta:");

        customDialog.setView(R.layout.item_search);
        customDialog.setNegativeButton(getResources().getString(R.string.back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        customDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText edtTime = ((Dialog)dialogInterface).findViewById(R.id.edtNameRecSearch);
                t[0] = edtTime.getText().toString();
                if(!t[0].equals("")) {
                    if (mCallback.getFilterByName(f4) != null) {
                        mCallback.getFilterByName(f4).setContent(t[0]);
                        filterAdapter.refreshList();
                    } else {
                        Filter ftmp = new Filter(f4, t[0]);
                        mCallback.addFilter(ftmp);
                        if (txvEmpty.getVisibility() == View.VISIBLE)
                            hideEmpty();
                        filterAdapter.refreshList();
                    }
                }
            }
        }).show();

    }

    private void hideEmpty(){
        txvEmpty.setVisibility(View.GONE);
    }

    /**
     * Muestra un cuadro de diálogo desde el que se puede solicitar un nuevo correo de verificación
     */
    public void showVerifyEmail(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.setTitle("Verificar e-mail");
        dialog.setCancelable(false);
        dialog.setMessage("Verifica tu email para poder acceder a las funcionalidades de FastRecipes. Recuerda que debes reiniciar sesión tras la verificación.");
        dialog.setNegativeButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //cancel
            }
        });

        dialog.setPositiveButton(getResources().getString(R.string.sendEmailVerification), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                fa.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Email enviado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "No ha sido posible. Inténtalo en unos minutos.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        dialog.show();
    }
}
