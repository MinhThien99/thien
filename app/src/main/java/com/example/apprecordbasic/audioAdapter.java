package com.example.apprecordbasic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class audioAdapter extends RecyclerView.Adapter<audioAdapter.AudioViewHolder> {
    private File[] allFiles;
    timeAgo timeago;
    private onItemListClick onitemListClick;

    public audioAdapter(File[] allFiles , onItemListClick onitemListClick){
        this.allFiles = allFiles;
        this.onitemListClick = onitemListClick;
    }

    @NonNull
    @Override
    public audioAdapter.AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item,parent,false);
        timeago = new timeAgo();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final audioAdapter.AudioViewHolder holder, final int position) {

        holder.title.setText(allFiles[position].getName());
        holder.date.setText(timeago.getTimeAgo(allFiles[position].lastModified()));

        holder.imgMenuAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopup(v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        private TextView title;
        private TextView date;
        private ImageView imgMenuAction;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            imgMenuAction = itemView.findViewById(R.id.btn_menu_action);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onitemListClick.onClicklistener(allFiles[getAdapterPosition()], getAdapterPosition());
        }

        public void showPopup(View view) {
            PopupMenu popup = new PopupMenu(itemView.getContext(), view);
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
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.act_play:
                    onClick(itemView);
                    return true;
                case R.id.act_rename:
//                    Log.d("ADAPTER POS:", Integer.toString(getAdapterPosition()));
                    rename(title, itemView.getContext(), getAdapterPosition());
                    return true;
                case R.id.act_delete:
//                    File file = new File(allFiles[getAdapterPosition()].getAbsolutePath());
//                    File file = itemView.getContext().getFileStreamPath(allFiles[getAdapterPosition()].getAbsolutePath());
                    Log.d("FILES BEFORE DELETED", Integer.toString(allFiles.length));
                    boolean success = allFiles[getAdapterPosition()].delete();
                    String path = itemView.getContext().getExternalFilesDir("/").getAbsolutePath();
                    allFiles = (new File(path)).listFiles();
                    Log.d("DELETE FILE", Boolean.toString(success));
//                    notifyItemChanged(getAdapterPosition()+1);
                    AudioListActivity.recyclerView.setAdapter(new audioAdapter(allFiles, onitemListClick));
//                    notifyItemRangeChanged(getAdapterPosition(), AudioListActivity.recyclerView.getChildCount());
//                    notifyDataSetChanged();
                    Log.d("FILES AFTER DELETED", Integer.toString(allFiles.length));
                    return true;
                default:
                    return true;
            }
        }
    }

    public interface onItemListClick{
        void onClicklistener(File file , int position);
    }

    private void rename(final View view, final Context context, final int pos){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Rename");

        final EditText input = new EditText(context);
        input.setText(((TextView)view).getText());
        alert.setView(input);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title_edited = input.getEditableText().toString();
                ((TextView)view).setText(title_edited);
                MediaPlayerFragment.renameFile(allFiles[pos], title_edited);
            }
        });

        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}
