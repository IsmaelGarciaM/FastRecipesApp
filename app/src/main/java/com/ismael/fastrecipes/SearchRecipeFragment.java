package com.ismael.fastrecipes;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.ismael.fastrecipes.adapter.FilterAdapter;
import com.ismael.fastrecipes.model.Filter;
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
 */
public class SearchRecipeFragment extends Fragment{

    /**
     * Variables de clase
     */
    @BindView(R.id.fabSearch)
    FloatingActionButton fabSearch;

    @BindView(R.id.btnDelete)
    Button btnDeleteFilters;

    @BindView(R.id.txvfilterEmptyList)
    TextView txvEmpty;

    @BindView(R.id.rcvFilterList)
    ListView lvFilters;

    @BindView(R.id.btnAddFilter)
    Button btnAddFilter;

    private SearchFragmentListener mCallback;
    static SearchRecipeFragment srfInstance;
    AlertDialog.Builder customDialog = null;
    FilterAdapter filterAdapter;

    public static SearchRecipeFragment getInstance(Bundle args){

        if(srfInstance == null){
            srfInstance = new SearchRecipeFragment();
        }
        srfInstance.setArguments(args);
        return  srfInstance;
    }

    public SearchRecipeFragment() {
        // Required empty public constructor
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
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.showRecipesList(null);
            }
        });

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
                        Filter ingnot = mCallback.getFilterByName(f2);
                        ArrayList<Filter> tmp = new ArrayList<>();
                        tmp.add(filterAdapter.getItem(i));
                        tmp.add(ingnot);
                        b.putParcelableArrayList("filters", tmp);
                        mCallback.showSearchByIngredients(b);
                        break;
                    case f2:
                        Filter ingok = mCallback.getFilterByName(f1);
                        ArrayList<Filter> tmp2 = new ArrayList<>();
                        tmp2.add(filterAdapter.getItem(i));
                        tmp2.add(ingok);
                        b.putParcelableArrayList("filters", tmp2);
                        mCallback.showSearchByIngredients(b);
                        break;
                    case f3:
                        Filter ftmp3 = mCallback.getFilterByName(f3);
                        b.putParcelable("filter", ftmp3);
                        mCallback.showSearchByCategories(b);
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

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Muestra el cuadro de diálogo para añadir un nuevo filtro
     */
    public void showFilters(){
        // con este tema personalizado evitamos los bordes por defecto
        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
        customDialog.setTitle("Filtros");

        customDialog.setItems(new CharSequence[]{"Ingredientes", "Categorias", "Nombre", "Tiempo", "Dificultad"}, new DialogInterface.OnClickListener() {
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

        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
        // obligamos al usuario a pulsar los botones para cerrarlo
        //customDialog.setCancelable(false);
        //establecemos el contenido de nuestro dialog
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
                t[0] = edtTime.getText().toString() + getResources().getString(R.string.minutes);
                if (mCallback.getFilterByName(f5) != null) {
                    mCallback.getFilterByName(f5).setContent(t[0]);
                    filterAdapter.refreshList();
                }
                else {
                    Filter ftmp = new Filter(f5, t[0]);
                    mCallback.addFilter(ftmp);
                    filterAdapter.refreshList();
                    if(txvEmpty.getVisibility() == View.VISIBLE)
                        hideEmpty();

                }
            }
        });

      /*  if(!time.equals("")){
            EditText edttim = customDialog.create().findViewById(R.id.edtTime);
            edttim.setText(time);
        }*/

        customDialog.show();
    }

    /**
     * Muestra el cuadro de diálogo para añadir un nuevo filtro de dificultad
     */
    private void showDifficultDialog(){
        AlertDialog.Builder builderDificult = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);

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
                            case 3:  content = getResources().getStringArray(R.array.diff_array)[3];
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

        customDialog = new AlertDialog.Builder(this.getContext(), R.style.Theme_Dialog_Translucent);
        customDialog.setCancelable(false);
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
                if (mCallback.getFilterByName(f4) != null) {
                    mCallback.getFilterByName(f4).setContent(t[0]);
                    filterAdapter.refreshList();
                }
                else {
                    Filter ftmp = new Filter(f4, t[0]);
                    mCallback.addFilter(ftmp);
                    if(txvEmpty.getVisibility() == View.VISIBLE)
                        hideEmpty();
                    filterAdapter.refreshList();
                }
            }
        }).show();

    }

    private void hideEmpty(){
        txvEmpty.setVisibility(View.GONE);
    }

}
