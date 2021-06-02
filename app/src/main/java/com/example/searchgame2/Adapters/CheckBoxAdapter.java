package com.example.searchgame2.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.R;
import com.example.searchgame2.Translaytor;
import java.util.ArrayList;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.ExampleViewHolder> {
    private ArrayList<String> List;
    private ArrayList<Integer> Index;
    private String between = null;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

    public CheckBoxAdapter (ArrayList<String> exampleList, ArrayList<Integer> index) {
        List = exampleList;
        Index = index;
    }

    public CheckBoxAdapter (ArrayList<String> exampleList, ArrayList<Integer> index, String b) {
        List = exampleList;
        Index = index;
        between=b;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.checks, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        Translaytor t = new Translaytor();
        String currentItem = null;
        if(between==null) {
            currentItem = t.gup(List.get(position));
        }else{
            currentItem = t.Breaker(List.get(position),between);
        }
        holder.checkBox.setText(currentItem);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Index.contains((Object)position)){
                    Index.remove((Object)position);
                }else{
                    Index.add(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public ArrayList Click (ArrayList arrayList){
        arrayList = Index;
        return arrayList;
    }

}
