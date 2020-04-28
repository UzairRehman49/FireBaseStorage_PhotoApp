package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class show extends AppCompatActivity {
private RecyclerView recyclerView;
private RecyclerViewAdapter mAdapter;
private DatabaseReference mDatabseRef;
List<UploadData> uploads;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads=new ArrayList<UploadData>();
        mDatabseRef= FirebaseDatabase.getInstance().getReference().child("uploads");
        mDatabseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Toast.makeText(getApplicationContext(), "Fetching uploads from Firebase", Toast.LENGTH_SHORT).show();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    UploadData upload = snapshot.getValue(UploadData.class);
                    uploads.add(upload);
                }
                mAdapter=new RecyclerViewAdapter(show.this,uploads);
                recyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
