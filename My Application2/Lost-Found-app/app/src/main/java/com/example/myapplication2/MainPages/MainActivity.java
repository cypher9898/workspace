package com.example.myapplication2.MainPages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication2.AddPages.AddForm;
import com.example.myapplication2.AddPages.AddReport;
import com.example.myapplication2.R;
import com.example.myapplication2.ScrollView.FormsScrollView;
import com.example.myapplication2.ScrollView.MyPostsScrollView;
import com.example.myapplication2.ScrollView.ReportsScrollView;
import com.example.myapplication2.SidePages.Credits;
import com.example.myapplication2.SidePages.Search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    String userType;
    FirebaseAuth fAuth;
    FirebaseUser fBase;
    Button inspector;
    Button report;
    String userAccess ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        fBase = fAuth.getCurrentUser();
        assert fBase != null;
        userType= fBase.getUid();
        inspector=  findViewById(R.id.inspector);
        report=  findViewById(R.id.report);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userType);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userAccess=dataSnapshot.child("type").getValue().toString();
                if(userAccess.equals("Inspector"))
                {
                    inspector.setVisibility(View.VISIBLE);
                    report.setVisibility(View.INVISIBLE);

                }
                else
                {
                    report.setVisibility(View.VISIBLE);
                    inspector.setVisibility(View.INVISIBLE);

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
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

    public void allPosts(View view) {
        Intent intent=new Intent(MainActivity.this, FormsScrollView.class);
        intent.putExtra("CALLED","Main");
        startActivity(intent);
    }

    public void addform(View view) {
        Intent intent=new Intent(MainActivity.this, AddForm.class);
        intent.putExtra("CALLED","Main");
        startActivity(intent);
    }
    public void settings(View view) {
        startActivity(new Intent(getApplicationContext(), Settings.class));

    }
    public void report(View view) {
        startActivity(new Intent(getApplicationContext(), AddReport.class));
    }
    public void myPosts(View view) {
        startActivity(new Intent(getApplicationContext(), MyPostsScrollView.class));

    }
    public void inspector(View view) {
        startActivity(new Intent(getApplicationContext(), ReportsScrollView.class));

    }

}