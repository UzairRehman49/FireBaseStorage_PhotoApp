package com.example.demo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageHolder> {
    private Context c;
    private List<UploadData> uploads;
    public RecyclerViewAdapter(Context c,List<UploadData>uploads)
    {
        this.c=c;
        this.uploads=uploads;
    }
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.image_item,parent,false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, final int position) {
    final UploadData upload = uploads.get(position);

        Picasso.get()
                .load(upload.mImageUri)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
               .centerInside()
                .into(holder.image);
        holder.c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c,uploads.get(position).getName(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder
 {
     public ImageView image;
     public CardView c;
     public ImageHolder(@NonNull View itemView) {
         super(itemView);
         image=itemView.findViewById(R.id.imageView);
         c = itemView.findViewById(R.id.cardview);

     }
 }
}
