package com.example.demoapp.DatesClasses;

import android.content.Context;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.CommonFunctionsClass.CommonFunctions;
import com.example.demoapp.R;
import com.example.demoapp.SqlliteDBClasses.ImpDatesSqlliteDbService;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ImpDatesListAdapter extends RecyclerView.Adapter<ImpDatesListAdapter.ViewHolder> {

    ViewHolder viewHolder;
    ArrayList Dates, Events;
    Context context;
    ImpDatesSqlliteDbService db;
    CommonFunctions cf = new CommonFunctions();

    // RecyclerView recyclerView;

    //Constructor to set Contexts
    public ImpDatesListAdapter(Context context, ArrayList Dates, ArrayList Events) {
        this.context = context;
        this.Dates = Dates;
        this.Events = Events;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.impdate_fragment, parent, false);
            viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        SpannableString content = new SpannableString(Dates.get(position).toString()+" :-");
        content.setSpan( new UnderlineSpan() , 0 , content.length(),0);

        holder.date.setText(content);
        holder.event.setText(Events.get(position).toString());
        final ViewHolder hold = holder;
        holder.del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                deleteDate(hold, Dates.get(pos).toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return Events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, event;
        public ImageButton del;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.event = itemView.findViewById(R.id.event);
            this.event.setMovementMethod(new ScrollingMovementMethod());
            this.del = itemView.findViewById(R.id.delBtn);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }

    //Function to delete Date
    public void deleteDate(final ViewHolder holder, final String Date){
        cf.setVibration(context);
        confirmDelete(holder, Date);
    }

    public void confirmDelete(final ViewHolder holder, String Date){
        Animation animation = AnimationUtils.loadAnimation(holder.relativeLayout.getContext(),R.anim.noteanim);
        holder.relativeLayout.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.relativeLayout.setVisibility(View.GONE);
            }
        }, 500);

        db = new ImpDatesSqlliteDbService(context);
        int res = db.deleteDate(Date);
        if(res > 0){
            Toast.makeText(context,"Date Deleted",Toast.LENGTH_SHORT).show();
        }
        else{
            new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops")
                    .setContentText("Date cannot be  Deleted!")
                    .show();
        }
    }
}
