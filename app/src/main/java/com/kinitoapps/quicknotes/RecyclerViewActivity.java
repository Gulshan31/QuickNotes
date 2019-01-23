package com.kinitoapps.quicknotes;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kinitoapps.quicknotes.R;

import java.util.ArrayList;

public  class RecyclerViewActivity extends AppCompatActivity {


    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index_recycler_view);
        final String class_name = getIntent().getStringExtra("class");
        String c_name = getIntent().getStringExtra("c_name");
        recyclerView = findViewById(R.id.recycler_view);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(c_name.trim()).child(class_name.trim());
        databaseReference.keepSynced(true);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // for individual items  the databse reference
                String filename = dataSnapshot.getKey(); // will return key
                ((RecyclerViewAdapter)recyclerView.getAdapter()).update(filename,class_name);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mAdapter = new RecyclerViewAdapter(recyclerView, RecyclerViewActivity.this, new ArrayList<String>());
        recyclerView.setAdapter(mAdapter);
        }






    }

