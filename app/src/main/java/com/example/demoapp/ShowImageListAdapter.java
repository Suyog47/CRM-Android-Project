package com.example.demoapp;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.SqlliteDBClasses.ShowImageDbService;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class ShowImageListAdapter extends RecyclerView.Adapter<ShowImageListAdapter.ViewHolder>  {
    ViewHolder viewHolder;
    Context context;
    ArrayList imgList, imgDate, imgDesc;

    public ShowImageListAdapter(Context context, ArrayList imgList, ArrayList imgDate, ArrayList imgDesc){
        this.context = context;
        this.imgList = imgList;
        this.imgDate = imgDate;
        this.imgDesc = imgDesc;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.show_image_fragment, parent, false);
        viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
     holder.img.setImageBitmap((Bitmap) imgList.get(position));
     holder.imgdate.setText(imgDate.get(position).toString());
     holder.imgdesc.setText(imgDesc.get(position).toString());
     holder.dbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             deleteImage(holder, position);
         }
     });
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, expandImg;
        ImageButton dbtn;
        TextView imgdate, imgdesc;
        LinearLayout imglayout;

        public ViewHolder(View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img);
            imgdate = itemView.findViewById(R.id.imgdate);
            imgdesc = itemView.findViewById(R.id.imgdesc);
            dbtn = itemView.findViewById(R.id.delBtn);
            imglayout = itemView.findViewById(R.id.imgLayout);
            expandImg = itemView.findViewById(R.id.expandImg);
        }
    }

    public void deleteImage(ViewHolder holder, int position){
        int res = new ShowImageDbService(context).delPhoto(imgDesc.get(position).toString());
        if(res > 0){
           holder.imglayout.setVisibility(View.GONE);
        }
        else{
            Toast.makeText(context, "Something went Wrong", Toast.LENGTH_SHORT).show();
        }
    }

}
