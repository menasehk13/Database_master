package com.example.database_master;

import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.Viewholder> {
 List<DisplayHotel> displayDetails;
Context c;
    public DisplayAdapter(Context context, List<DisplayHotel> list) {
        displayDetails=list;
        context=c;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.image_detail_view,parent,false);
        return  new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
    holder.textView.setText(displayDetails.get(position).getName());
    holder.rating.setText(displayDetails.get(position).getRating());
        Picasso.get().load(displayDetails.get(position).getImageurl()).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (displayDetails!=null){
            return displayDetails.size();
        }
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textView;
        Rating rating;
        ImageView imageView;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.hotelname);
            imageView=itemView.findViewById(R.id.imageholder);
            rating=itemView.findViewById(R.id.rating);
        }
    }
}
