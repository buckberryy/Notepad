package com.olcayesir.handlingnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotListAdapter extends RecyclerView.Adapter<NotListAdapter.MyViewHolder> {
    private Context mContext;
    int mResource;
    private List<Not> mData;
    SQLiteDatabase database;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.row,parent,false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.tvTitle.setText(mData.get(position).getTitle());
            holder.tvDate.setText(mData.get(position).getDate());
            holder.tvNote.setText(mData.get(position).getNote());
            holder.tvTime.setText(mData.get(position).getTime());

            if (position% 5 ==1)
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.color2));
            else if(position%5 == 2)
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.color3));
            else if(position%5==3)
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.color4));
            else if(position%5==4)
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.color5));
            else
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.color1));


            holder.cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(mContext,notDuzelt.class);
                    intent.putExtra("Baslik",mData.get(position).getTitle());
                    intent.putExtra("Not",mData.get(position).getNote());
                    intent.putExtra("Tarih",mData.get(position).getDate());
                    intent.putExtra("Saat",mData.get(position).getTime());
                    mContext.startActivity(intent);



                }



            });

            holder.cardView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog.Builder adb=new AlertDialog.Builder(mContext);
                    adb.setTitle("SİL");
                    adb.setMessage("Silmek istediğinize emin misiniz ? ");
                    final int positionToRemove = position;

                    adb.setPositiveButton("Evet", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(mContext,MainActivity.class);
                            intent.putExtra("bas",mData.get(position).getTitle());
                            mContext.startActivity(intent);
                        }});
                    adb.setNegativeButton("Hayır", null);
                    adb.show();

                    return true;
                }



            });
        }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        TextView tvNote;
        TextView tvTime;
        TextView tvDate;
        View view;
        CardView cardView;
        public MyViewHolder(View itemView){
            super(itemView);

            tvTitle = itemView.findViewById(R.id.textBaslik);
            tvNote = itemView.findViewById(R.id.textNot);
            tvTime = itemView.findViewById(R.id.textSaat);
            tvDate = itemView.findViewById(R.id.textTarih);
            view = itemView.findViewById(R.id.view1);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }


    public NotListAdapter(Context mContext,  List<Not> mData){

        this.mContext = mContext;
        this.mData = mData;
    }

}
