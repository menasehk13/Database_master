package com.example.database_master;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayImage extends AppCompatActivity {
    RecyclerView recyclerView;
    DisplayAdapter adapter;
    List<DisplayDetail> displayDetails =new ArrayList<>();
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Pictures");
        progressDialog.show();
        DatabaseReference reference=database.getReference().child("Hotels");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                for (DataSnapshot file:snapshot.getChildren()){
                    DisplayDetail detail=file.getValue(DisplayDetail.class);
                    displayDetails.add(detail);
                }
                adapter =new DisplayAdapter(getApplicationContext(),displayDetails);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               progressDialog.dismiss();
            }
        });

    }
}