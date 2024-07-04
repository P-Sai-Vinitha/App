package com.example.autism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ParentAdapter extends BaseAdapter {
    private Context context;
    private List<Parent> parentList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ParentAdapter(Context context, List<Parent> parents) {
        this.context = context;
        this.parentList = parents;
    }

    @Override
    public int getCount() {
        return parentList.size();
    }

    @Override
    public Object getItem(int position) {
        return parentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.parent_item, parent, false);
        }

        TextView nameTextView = convertView.findViewById(R.id.nameTextView);
        ImageView profileImageView = convertView.findViewById(R.id.imageView14);

        Parent parent1 = parentList.get(position);
        nameTextView.setText(parent1.getName());

        // Set item click listener
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        // Set profile image using Base64
        String base64Image = parent1.getProfileImage();
        byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        profileImageView.setImageBitmap(decodedByte);

        return convertView;
    }

    public void setFilteredList(List<Parent> filteredList) {
        parentList = filteredList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
