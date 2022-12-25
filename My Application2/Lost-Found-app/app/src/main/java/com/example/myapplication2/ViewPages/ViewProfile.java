package com.example.myapplication2.ViewPages;

import android.content.Intent;
import android.net.Uri;
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

import com.example.myapplication2.ClassObject.ObjectUser;
import com.example.myapplication2.MainPages.Login;
import com.example.myapplication2.MainPages.Settings;
import com.example.myapplication2.R;
import com.example.myapplication2.ScrollView.ReportsScrollView;
import com.example.myapplication2.SidePages.Credits;
import com.example.myapplication2.SidePages.Search;
import com.example.myapplication2.SidePages.Warning_Mail;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ViewProfile extends AppCompatActivity {
    TextView mName,mEmail,mPhone,namehint,phonehint,mailhint,NoUsersFound;
    ImageView mViewProfileImage;
    Button mInspectorButton;
    ObjectUser CurrUser;
    String UserID;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile_activity);
        fAuth = FirebaseAuth.getInstance();
        namehint = findViewById(R.id.textView11);
        mailhint = findViewById(R.id.textView12);
        phonehint = findViewById(R.id.textView13);
        mName = findViewById(R.id.viewprofilename);
        mEmail = findViewById(R.id.viewprofileemail);
        mPhone = findViewById(R.id.viewprofilephone);
        mViewProfileImage = findViewById(R.id.ViewProfileImage);
        mInspectorButton = findViewById(R.id.RemovalOptions);
        NoUsersFound = findViewById(R.id.NoUsersFound);
        NoUsersFound.setVisibility(View.GONE);
        Intent intent = getIntent();
        String Called = intent.getStringExtra("CALLED");
        if (Called.equals("Search")) {
            FirebaseDatabase.getInstance().getReference().child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String EmailField = intent.getStringExtra("FreeSearch");
                    for (DataSnapshot childes : dataSnapshot.getChildren()) {
                        if (childes.child("email").getValue().equals(EmailField)) {
                            UserID = childes.getKey();
                        }
                    }
                    if (UserID == null) {
                        namehint.setVisibility(View.GONE);
                        phonehint.setVisibility(View.GONE);
                        mailhint.setVisibility(View.GONE);
                        mInspectorButton.setVisibility(View.GONE);
                        NoUsersFound.setVisibility(View.VISIBLE);
                        return;
                    }
                    else{
                        func();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            UserID = intent.getStringExtra("UserObject");
            func();
        }


        mInspectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ViewProfile.this, Warning_Mail.class);
                intent.putExtra("email", mEmail.getText().toString());
                startActivity(intent);
               // startActivity(new Intent(getApplicationContext(), ReportsScrollView.class));
            }
        });


    }

    public void func(){
        FirebaseStorage.getInstance().getReference().child("users/" + UserID + "/profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mViewProfileImage);
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CurrUser = new ObjectUser(
                        dataSnapshot.child(UserID).child("email").getValue().toString(),
                        dataSnapshot.child(UserID).child("fName").getValue().toString(),
                        dataSnapshot.child(UserID).child("phone").getValue().toString(),
                        dataSnapshot.child(UserID).child("type").getValue().toString());

                mPhone.setText(CurrUser.getPhone());
                mName.setText(CurrUser.getFullName());
                mEmail.setText(CurrUser.getEmail());
                if (!dataSnapshot.child(fAuth.getCurrentUser().getUid()).child("type").getValue().toString().equals("Inspector")) {
                    mInspectorButton.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                startActivity(new Intent(getApplicationContext(), ViewProfile.class));
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
