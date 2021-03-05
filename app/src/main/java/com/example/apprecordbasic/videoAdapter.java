package com.example.apprecordbasic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.viewHolder> {

    Context context;
    ArrayList<videoModel> videoArrayList;
    public OnItemClickListener onItemClickListener;
    int index = 0;
    PopupMenu popup;

    public videoAdapter(Context context, ArrayList<videoModel> videoArrayList){
        this.context = context;
        this.videoArrayList = videoArrayList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position) {

        holder.title.setText(videoArrayList.get(position).getVideoTitle());
        holder.duration.setText(videoArrayList.get(position).getVideoDuration());


        holder.img_menu_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoArrayList.remove(position);
                Toast.makeText(context, "Delete file successful",Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });
       // popup.setOnMenuItemClickListener(this);



    }


    @Override
    public int getItemCount() {
        return videoArrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder  {

        TextView title , duration;
        ImageView img_menu_video;
            public viewHolder(View view){
                super(view);
                title = view.findViewById(R.id.title_video);
                duration = view.findViewById(R.id.duration_video);
                img_menu_video = view.findViewById(R.id.btn_menu_video);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(getAdapterPosition(),v);
                    }
                });

                /*
                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onItemClick(getAdapterPosition(),v);
                        notifyDataSetChanged();
                        return true;
                    }
                });

                 */
            }


/*
        public void showPopup( View view) {
            popup = new PopupMenu(itemView.getContext(), view);
            try {
                // Reflection apis to enforce show icon
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equals("mPopup")) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            popup.getMenuInflater().inflate(R.menu.item_actions, popup.getMenu());
            popup.show();
        }

 */


    }



    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener {
        void onItemClick(int pos, View v);
    }

}


















