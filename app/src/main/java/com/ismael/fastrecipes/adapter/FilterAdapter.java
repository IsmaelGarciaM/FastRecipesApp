package com.ismael.fastrecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ismael.fastrecipes.R;
import com.ismael.fastrecipes.model.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ismael on 29/01/2018.
 */

public class FilterAdapter extends ArrayAdapter<Filter> {

    private Context context;
    List<Filter> filist;
    public FilterAdapter(@NonNull Context context, int resource, @NonNull List<Filter> filters) {
        super(context, R.layout.item_filter_list, filters);
        this.filist = filters;
        this.context = context;
    }

    @Override
    public int getCount() {
        if(filist != null)
         return filist.size();
        else return 0;
    }

    @Nullable
    @Override
    public Filter getItem(int position) {
        return filist.get(position);
    }

    @NonNull
    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {
        FilterHolder fh;
        View item = view;
        if (item == null) {
            item = LayoutInflater.from(getContext()).inflate(R.layout.item_filter_list, parent, false);
            fh = new FilterHolder(item);
            item.setTag(fh);
        }
        else
            fh = (FilterHolder)item.getTag();


        fh.txvType.setText(getItem(position).getType());

        fh.txvContent.setText(getItem(position).getContent());

        return item;
    }

    public void refreshList(){
        notifyDataSetChanged();
    }

    class FilterHolder{
        TextView txvType ;
        TextView txvContent ;

        public FilterHolder(View v){
            txvType = (TextView) v.findViewById(R.id.filterTitle);
            txvContent =(TextView) v.findViewById(R.id.txvfilterDescription);

        }
    }

    @Override
    public void add(@Nullable Filter object) {
        super.add(object);
        filist.add(object);
    }

    @Override
    public void remove(@Nullable Filter object) {
        super.remove(object);
        filist.remove(object);
    }

    public Filter getFilterByName(String type){
        Filter ftmp = null;
        for (int i = 0; i < filist.size(); i++){
            if(filist.get(i).getType().equals(type)){
                ftmp = filist.get(i);
                break;
            }
        }
        return ftmp;
    }
}
