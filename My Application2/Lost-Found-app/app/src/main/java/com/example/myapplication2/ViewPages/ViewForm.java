package com.example.myapplication2.ViewPages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication2.AddPages.AddForm;
import com.example.myapplication2.ClassObject.ObjectForm;
import com.example.myapplication2.ClassObject.ObjectUser;
import com.example.myapplication2.MainPages.Login;
import com.example.myapplication2.MainPages.MainActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.SidePages.Credits;
import com.example.myapplication2.SidePages.Search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

public class ViewForm extends AppCompatActivity {
    TextView mObjectTitle,mLostFound,mCategory,mPlace,mDate,mDescription,mStatus;
    ImageView ViewPostImage;
    Button mMyOptions;
    String CurrUID;
    String userType;
    FirebaseAuth fAuth;
    FirebaseUser fBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        ObjectForm form= (ObjectForm) getIntent().getSerializableExtra("FormObject");

        mObjectTitle=findViewById(R.id.viewpostobject);
        mLostFound=findViewById(R.id.viewpostlostfound);
        mCategory=findViewById(R.id.viewpostcategory);
        mPlace=findViewById(R.id.viewpostplace);
        mDate=findViewById(R.id.viewpostdate);
        mDescription=findViewById(R.id.viewpostdescription);
        mStatus=findViewById(R.id.viewpoststatus);
        ViewPostImage=findViewById(R.id.PostimageView);
        mMyOptions=findViewById(R.id.MyOptionsButton);
        mMyOptions.setVisibility(View.GONE);

        mCategory.setText(form.getCategory());
        mLostFound.setText(form.getHappend());
        mDescription.setText(form.getDescription());
        mObjectTitle.setText(form.getObjectTitle());
        mDate.setText(form.getDate());
        mPlace.setText(form.getPlace());
        mStatus.setText(form.getStatus());
        Picasso.get().load(form.getImg()).into(ViewPostImage);
        fAuth = FirebaseAuth.getInstance();
        fBase = fAuth.getCurrentUser();
        assert fBase != null;
        userType= fBase.getUid();
        CurrUID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(CurrUID.equals(form.getUserID())){
            mMyOptions.setVisibility(View.VISIBLE);
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userType);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userAccess=dataSnapshot.child("type").getValue().toString();
                if(userAccess.equals("Inspector"))
                {
                    mMyOptions.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mMyOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewForm.this);
                alert.setTitle("선택");
                alert.setMessage("선택해주세요:");
                alert.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("forms/").child(form.getHappend()).child(form.getCategory()).child(form.getGeneratedKey()).removeValue();
                        FirebaseStorage.getInstance().getReference("forms/"+form.getGeneratedKey()+"/ObjectIMG.jpg").delete();
                    }
                });
                alert.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(ViewForm.this, AddForm.class);
                        intent.putExtra("CALㅁㄴㅇLED","ViewForm");
                        intent.putExtra("ObjectForm",form);
                        startActivity(intent);
                    }
                });
                alert.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.Search:
                Toast.makeText(this,"Search",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Search.class));
                return true;
            case R.id.refresh:
                Toast.makeText(this,"Refresh",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ViewForm.class));
                return true;
            case R.id.Credits:
                Toast.makeText(this,"Credits",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Credits.class));
                return true;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();//logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);

        }

    }
}
