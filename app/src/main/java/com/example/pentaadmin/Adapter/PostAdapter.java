package com.example.pentaadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pentaadmin.Model.PostModel;
import com.example.pentaadmin.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    List<PostModel> userList;

    public PostAdapter(Context context, List<PostModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.show_posts,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        PostModel user=userList.get(position);
        holder.description.setText(user.getdescription());
        holder.title.setText(user.gettitle());

        Glide.with(context).load(user.getphoto()).into(holder.photo);


    }



    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView photo;
        TextView title,description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            photo=itemView.findViewById(R.id.photo);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);


        }
    }

    public void removeItem(int position){
        userList.remove(position);


        notifyItemRemoved(position);


    }





}

