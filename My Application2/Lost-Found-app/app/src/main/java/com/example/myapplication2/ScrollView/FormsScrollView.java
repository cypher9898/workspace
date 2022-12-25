package com.example.myapplication2.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication2.Adapters.FormsAdapter;
import com.example.myapplication2.ClassObject.ObjectForm;
import com.example.myapplication2.MainPages.Login;
import com.example.myapplication2.MainPages.MainActivity;
import com.example.myapplication2.R;
import com.example.myapplication2.SidePages.Credits;
import com.example.myapplication2.SidePages.Search;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FormsScrollView extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<ObjectForm> list;
    FormsAdapter adapter;
    Spinner Happened_spinner;
    TextView headline;
    Intent intent;
    private  String actObject[]={"핸드폰","귀금속","옷","전자기기","자전거","가방","etc"};
    private  String happend[]={"분실","습득"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view);
        Happened_spinner = findViewById(R.id.spinner6);
        ArrayAdapter<CharSequence> HappendAdapter= ArrayAdapter.createFromResource(this, R.array.Whathappened, R.layout.support_simple_spinner_dropdown_item);
        Happened_spinner.setAdapter(HappendAdapter);
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        list = new ArrayList<ObjectForm>();
        headline=findViewById(R.id.textView9);
        reference = FirebaseDatabase.getInstance().getReference().child("forms").child("Lost");
        intent=getIntent();
        if(intent.getStringExtra("CALLED").equals("Search")){
            Happened_spinner.setVisibility(View.GONE);
            headline.setVisibility(View.GONE);
            loop(intent.getStringExtra("LostFound"));
        }
/*        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //loop("Lost");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FormsScrollView.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
            }
        });*/


        recyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {

            @Override
            public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
              //  Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                Toast.makeText(FormsScrollView.this,"변경완료", Toast.LENGTH_SHORT).show();

            }

        });

        Happened_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                list = new ArrayList<ObjectForm>();
                if(Happened_spinner.getSelectedItem().equals("분실")){
                  //  list.clear();
                    loop("분실");
                }
                else if(Happened_spinner.getSelectedItem().equals("습득")){
                 //   list.clear();
                    loop("습득");
                }
                else{
                  //  list.clear();
                    loop("분실");
                    loop("습득");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here

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
                startActivity(new Intent(getApplicationContext(), FormsScrollView.class));
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


    public void loop(String Look){
       // list.clear();
        for (String object : actObject) {
            //in this section check from where avctivity you came from (search or ALL POSTS) and give the reference בהתאם. check that actObject is set to the category that gave you
            reference = FirebaseDatabase.getInstance().getReference("forms").child(Look).child(object);
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    if(intent.getStringExtra("CALLED").equals("Search")){
                        String searchField=intent.getStringExtra("SearchField");
                        String searchFieldValue=dataSnapshot.child(searchField).getValue().toString();
                        if(searchFieldValue.equals(intent.getStringExtra("FreeSearch"))){
                            ObjectForm p = dataSnapshot.getValue(ObjectForm.class);
                            p.setCategory(FirebaseDatabase.getInstance().getReference("forms").child(Look).child(object).getKey());
                            p.setHappend(FirebaseDatabase.getInstance().getReference("forms").child(Look).getKey());
                            p.setGeneratedKey(dataSnapshot.getKey());
                            list.add(p);
                        }
                    }
                    else {
                        ObjectForm p = dataSnapshot.getValue(ObjectForm.class);
                        p.setCategory(FirebaseDatabase.getInstance().getReference("forms").child(Look).child(object).getKey());
                        p.setHappend(FirebaseDatabase.getInstance().getReference("forms").child(Look).getKey());
                        p.setGeneratedKey(dataSnapshot.getKey());
                        list.add(p);
                    }

                    adapter = new FormsAdapter(FormsScrollView.this,list);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }



}