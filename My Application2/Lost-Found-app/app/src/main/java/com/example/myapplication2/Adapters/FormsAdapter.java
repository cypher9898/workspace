package com.example.myapplication2.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.myapplication2.ClassObject.ObjectForm;
import com.example.myapplication2.ClassObject.ObjectUser;
import com.example.myapplication2.R;
import com.example.myapplication2.ViewPages.ViewForm;
import com.example.myapplication2.ViewPages.ViewProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FormsAdapter extends RecyclerView.Adapter<FormsAdapter.MyViewHolder> {

        ArrayList<ObjectForm> DataArrayList= new ArrayList<>();
        Context context;
        ArrayList<ObjectForm> items;

        public FormsAdapter(Context c , ArrayList<ObjectForm> p)
        {
            context = c;
            items = p;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_card_view,parent,false));
        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.item.setText(items.get(position).getObjectTitle());
            holder.description.setText(items.get(position).getDescription());
            Picasso.get().load(items.get(position).getImg()).into(holder.profilePic);
            holder.ViewPostButton.setVisibility(View.VISIBLE);



        holder.onClick(position);


        DataArrayList.add(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {
            TextView item,description;
            ImageView profilePic;
            Button ViewPostButton;
            Button ViewProfileButton;

            public MyViewHolder(View itemView) {
                super(itemView);
                item = (TextView) itemView.findViewById(R.id.Item);
                description = (TextView) itemView.findViewById(R.id.Description);
                profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
                ViewPostButton = (Button) itemView.findViewById(R.id.viewpostbutton);
                ViewProfileButton = (Button) itemView.findViewById(R.id.viewprofilebutton);

            }
            public void onClick(final int position)
            {
                ViewPostButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(context, ViewForm.class);
                        ObjectForm form=new ObjectForm(DataArrayList.get(position));
                        intent.putExtra("FormObject",form);

                        context.startActivity(intent);
                    }
                });
                ViewProfileButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent= new Intent(context, ViewProfile.class);
                        intent.putExtra("CALLED","ALLPOSTS");
                        intent.putExtra("UserObject", DataArrayList.get(position).getUserID());

                        context.startActivity(intent);
                    }
                });
            }
        }
    }