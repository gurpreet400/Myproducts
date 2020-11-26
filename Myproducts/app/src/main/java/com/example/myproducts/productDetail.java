package com.example.myproducts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class productDetail extends AppCompatActivity {

    Button ProductSignOut;
    TextView txtProductName,txtProductPrice,txtProductDescription;
    String productId="";
    ImageView imgProductDetails;
    FirebaseUser user;
    DatabaseReference reference;
    String fullName;
    String userID;
    private String ImageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Admin profile = snapshot.getValue(Admin.class);
                if(profile != null){
                    fullName = profile.name;
                    String email = profile.email;
                    String age = profile.age;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(productDetail.this, "Something went wrong. Restart the app.", Toast.LENGTH_SHORT).show();

            }
        });



        ProductSignOut = (Button) findViewById(R.id.btnProductSignOut);



        productId = getIntent().getStringExtra("pid");
        imgProductDetails = (ImageView) findViewById(R.id.imgProductDetails);
        txtProductName = (TextView) findViewById(R.id.txtProductName);
        txtProductPrice = (TextView) findViewById(R.id.txtProductPrice);
        txtProductDescription = (TextView) findViewById(R.id.txtProductDescription);

        getProductDetails(productId);

        ProductSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(productDetail.this, MainActivity.class));
                finish();
            }
        });

    }

    public void getProductDetails(final String productId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");
        reference.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Product product = snapshot.getValue(Product.class);
                    if(product!=null) {

                        txtProductName.setText(product.getPname());
                        txtProductPrice.setText(product.getPrice());
                        txtProductDescription.setText(product.getDescription());
                        Picasso.get().load(product.getImage()).into(imgProductDetails);
                        ImageID=product.getImage();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}