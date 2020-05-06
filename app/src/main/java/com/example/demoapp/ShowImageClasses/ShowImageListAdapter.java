package com.example.demoapp.ShowImageClasses;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.ShowImageDbService;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

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
        SpannableString content = new SpannableString(imgDate.get(position).toString());
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);
     holder.imgdate.setText(content);
     holder.imgdesc.setText(imgDesc.get(position).toString());

     holder.dbtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             deleteImage(holder, position);
         }
     });

     holder.savebtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             BitmapDrawable draw = (BitmapDrawable) holder.img.getDrawable();
             saveImage(draw, position);
         }
     });

     holder.img.setOnClickListener(new View.OnClickListener(){
         @Override
         public void onClick(View v) {
             Intent a1 = new Intent(context, ShowExpandedImage.class);
             a1.putExtra("imgDesc", imgDesc.get(position).toString());
             context.startActivity(a1);
         }
     });
    }


    @Override
    public int getItemCount() {
        return imgList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageButton dbtn, savebtn;
        TextView imgdate, imgdesc;
        LinearLayout imglayout;

        public ViewHolder(View itemView){
            super(itemView);
            img = itemView.findViewById(R.id.img);
            imgdate = itemView.findViewById(R.id.imgdate);
            imgdesc = itemView.findViewById(R.id.imgdesc);
            dbtn = itemView.findViewById(R.id.delBtn);
            savebtn = itemView.findViewById(R.id.saveBtn);
            imglayout = itemView.findViewById(R.id.imgLayout);
        }
    }


    //Function to save Image on phone galary
    private void saveImage(BitmapDrawable draw, int position){
        Bitmap bitmap = draw.getBitmap();

        FileOutputStream outStream;
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + "/Download");
        String fileName = String.format(imgDesc.get(position).toString()+".jpg", System.currentTimeMillis());
        File outFile = new File(dir, fileName);
        try {
            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            Toast.makeText(context,"Image Downloaded", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){ e.printStackTrace();}
    }


    //Function to delete Image from db
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
