package com.kinitoapps.quicknotes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinitoapps.quicknotes.R;

import java.util.ArrayList;

public class DataRecordAdapter extends RecyclerView.Adapter<DataRecordAdapter.ViewHolder> {

    private RecyclerView recyclerView;
    private Context context;
    private  String className;
    private String  c_name;
    private ArrayList<String> items;

    public void update( String class_name,String name){
        className = class_name;
        c_name= name;
        Log.v("gulshan",class_name);
        items.add(class_name);
        notifyDataSetChanged(); // this refreshes the recyclerview automatically so that the latest item populated
    }
    public DataRecordAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view_items,viewGroup,false);
        return new DataRecordAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.indexName.setText(items.get(i));
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                    Intent intent = new Intent(context,RecyclerViewActivity.class);
                    intent.putExtra("class", items.get(position));
                    intent.putExtra("c_name",c_name);
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, RecyclerViewActivity.class);
                    intent.putExtra("class", items.get(position));
                    intent.putExtra("c_name", c_name);
                    context.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        private ItemClickListener itemClickListener;
        private TextView indexName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            indexName = itemView.findViewById(R.id.index_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);

            return true;
        }
    }
}
