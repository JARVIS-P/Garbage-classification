package com.example.distinguish.Adapter;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distinguish.R;
import com.example.distinguish.db.Waste;

import java.util.List;

public class DefaultAdapter extends RecyclerView.Adapter<DefaultAdapter.ViewHolder> {

    private List<Waste> wasteList;

    public DefaultAdapter(List<Waste> wasteList) {
        this.wasteList = wasteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Waste waste = wasteList.get(position);
        holder.name.setText(waste.getName());
        holder.sort.setText(waste.getSort());
        if(waste.getSort().equals("厨余垃圾")){
            holder.imageTwo.setImageResource(R.drawable.kitchen);
        }
        if(waste.getSort().equals("其他垃圾")){
            holder.imageTwo.setImageResource(R.drawable.other);
        }
        if(waste.getSort().equals("有害垃圾")){
            holder.imageTwo.setImageResource(R.drawable.nonrecyclable);
        }
        if(waste.getSort().equals("可回收垃圾")){
            holder.imageTwo.setImageResource(R.drawable.recyclable);
        }
        //holder.imageTwo.setImageResource(R.drawable.image_two);
    }

    @Override
    public int getItemCount() {
        return wasteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView sort;
        ImageView imageTwo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.waste_name);
            sort = itemView.findViewById(R.id.waste_sort);
            imageTwo = itemView.findViewById(R.id.image_two);
        }
    }
}
