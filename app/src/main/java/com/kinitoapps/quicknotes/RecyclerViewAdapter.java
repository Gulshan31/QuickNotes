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



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private RecyclerView recyclerView;
    private Context context;
   private  String className;
    private ArrayList<String> items;

    public void update(String filename, String class_name){
        className = class_name;
        items.add(filename);
        notifyDataSetChanged(); // this refreshes the recyclerview automatically so that the latest item populated
    }


    public RecyclerViewAdapter(RecyclerView recyclerView, Context context, ArrayList<String> items) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // used to create views for list items or recyclerviews items
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_view_items,viewGroup,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
            // intialize the elements of individual items.
        viewHolder.indexName.setText(items.get(i));
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {

                    Log.v("Long Press", "Yes" + items.get(position));

                    Intent intent = new Intent(context,DownloadTask.class);
                    intent.putExtra("index_name", items.get(position));
                    intent.putExtra("class_name",className);
                    context.startActivity(intent);



                }
                else

                    Log.v("Long Press","No"+items.get(position));
                Intent intent = new Intent(context,DownloadTask.class);
                intent.putExtra("index_name", items.get(position));
                intent.putExtra("class_name",className);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {

        // will be usde to return the number of items
        // the size of array list

        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private ItemClickListener itemClickListener;
        private TextView indexName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            indexName = itemView.findViewById(R.id.index_name);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);


        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);

            return true;
        }
    }

}
