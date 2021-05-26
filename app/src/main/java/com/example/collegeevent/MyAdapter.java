package com.example.collegeevent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

    private Context context;
    private List<Upload> uploads;
    FirebaseStorage storage;

    public MyAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_images, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder,final int position) {

        final Upload upload = uploads.get(position);
        holder.textViewEvent.setText(upload.getName());
        holder.textViewVenue.setText(upload.getVenue());
        holder.textViewDate.setText(upload.getDate());
        holder.textViewTime.setText(upload.getStart_time());
        holder.textViewDept.setText(upload.getDept());
        Picasso.get().load(upload.getUrl()).into(holder.imageViewpost);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(v.getContext(),Preview.class);
               intent.putExtra("name", upload);
               v.getContext().startActivity(intent);
                Toast.makeText(context,""+upload.getName() ,Toast.LENGTH_SHORT).show();
            }
        });
        holder.menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(context,v);
                popupMenu.inflate(R.menu.menu);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final AlertDialog.Builder dailog = new AlertDialog.Builder(v.getRootView().getContext());
                        dailog.setMessage("Are you Surely wants to delete?");
                        dailog.setTitle("Delete");
                        dailog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String key =upload.getKey();
                                FirebaseDatabase.getInstance().getReference().child("CSE").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Deleted Successfully"+upload.getName() ,Toast.LENGTH_SHORT).show();
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("ECE").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Deleted Successfully"+upload.getName() ,Toast.LENGTH_SHORT).show();
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("CIVIL").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Deleted Successfully"+upload.getName() ,Toast.LENGTH_SHORT).show();
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("MECH").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Deleted Successfully"+upload.getName() ,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = dailog.create();
                        alertDialog.show();
                        return false;
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return uploads.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder{


        public TextView textViewEvent, textViewVenue, textViewDate, textViewTime, textViewDept,link;
        public ImageView imageViewpost, menu_btn;


        public ViewHolder(View itemView) {
            super(itemView);
            menu_btn = itemView.findViewById(R.id.menu_btn);
            textViewVenue = itemView.findViewById(R.id.venue);
            textViewEvent = itemView.findViewById(R.id.textViewName);
            textViewDept = itemView.findViewById(R.id.dept);
            textViewTime = itemView.findViewById(R.id.Time);
            textViewDate = itemView.findViewById(R.id.Calender);
            imageViewpost = itemView.findViewById(R.id.post);
        }

    }


}