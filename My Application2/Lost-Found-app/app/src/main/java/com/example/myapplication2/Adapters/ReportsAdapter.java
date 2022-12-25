package com.example.myapplication2.Adapters;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import android.content.Context;
        import android.content.Intent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.LinearLayout;
        import android.widget.TextView;


        import com.example.myapplication2.ClassObject.ObjectReport;
        import com.example.myapplication2.R;
        import com.example.myapplication2.ViewPages.ViewForm;
        import com.example.myapplication2.ViewPages.ViewProfile;
        import com.example.myapplication2.ViewPages.ViewReport;
        import com.squareup.picasso.Picasso;

        import java.util.ArrayList;


public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {

    ArrayList<ObjectReport> reportsArrayList = new ArrayList<>();
    Context context;
    ArrayList<ObjectReport> reports;

    public ReportsAdapter(Context c , ArrayList<ObjectReport> p)
    {
        context = c;
        reports = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_card_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item.setText(reports.get(position).getReportType());
        holder.description.setText(reports.get(position).getDescription());
        //Picasso.get().load(reports.get(position).getImg()).into(holder.profilePic);
        //holder.btn.setVisibility(View.VISIBLE);
        holder.onClick(position);
        reportsArrayList.add(reports.get(position));
        //holder.infoWindowPopup.layout(0,30,0,0);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView item,description;
        //ImageView profilePic;
        Button ViewPostButton;
        Button ViewProfileButton;
        //LinearLayout infoWindowPopup;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.Item);
            description = (TextView) itemView.findViewById(R.id.Description);
            ViewPostButton = itemView.findViewById(R.id.viewpostbutton);
            ViewProfileButton=itemView.findViewById(R.id.viewprofilebutton);
            //infoWindowPopup = (LinearLayout) itemView.findViewById(R.id.linear);
        }
        public void onClick(final int position)
        {
            ViewPostButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, ViewReport.class);
                    ObjectReport report=new ObjectReport(reportsArrayList.get(position));
                    intent.putExtra("ObjectReport",report);
                    intent.putExtra("GeneratedKey", reportsArrayList.get(position).getGeneratedKey());
                    context.startActivity(intent);
                }
            });
            ViewProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(context, ViewProfile.class);
                    intent.putExtra("CALLED","VIEWREPORT");
                    intent.putExtra("UserObject",reportsArrayList.get(position).getUserID());
                    context.startActivity(intent);
                }
            });
        }
    }
}