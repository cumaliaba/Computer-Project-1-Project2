package com.example.abalik.start;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;
        import android.app.Activity;


        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.io.File;
        import java.util.ArrayList;
        import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText kullanici_adi;
    private Button kaydet,gor;
    private TextView mesajlar;
    private Button kullaniciKayit;



    FirebaseDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baslangic();
    }
    private void baslangic(){



        db=FirebaseDatabase.getInstance();
        kullanici_adi=(EditText)findViewById(R.id.userEt);
        kaydet=(Button)findViewById(R.id.kaydetBtn);
        gor=(Button)findViewById(R.id.button2);
        mesajlar=(TextView)findViewById(R.id.MesajTv);

        //kullaniciKayit=(Button)findViewById(R.id.button4);


        kaydet.setOnClickListener(this);
        gor.setOnClickListener(this);

        //kullaniciKayit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.kaydetBtn:
                String kullanici_str;
                kullanici_str=kullanici_adi.getText().toString().trim();
                KullaniciKaydet(kullanici_str);


                // diğer sayfaya geçiş için
                Intent intent = new Intent(getApplicationContext(),SendFile.class);
                intent.putExtra("kullanici_str",kullanici_str);
                startActivity(intent);

                break;
            case R.id.button2:
                Kullanicilari_getir();
                break;
            /*
            case R.id.button4:
                kullaniciAktivitesiniAc();
                break;
            */
        }
    }


    private void KullaniciKaydet(final String kullanici){

        DatabaseReference dbRef = db.getReference("Kullanicilar");
        String key = dbRef.push().getKey();
        DatabaseReference dbRefKeyli = db.getReference("Kullanicilar/"+key);
        dbRefKeyli.setValue(kullanici);


    }


    private void Kullanicilari_getir(){


        DatabaseReference okuma = db.getReference("Kullanicilar");
        okuma.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mesajlar.setText("");
                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key: keys) {

                    mesajlar.append(key.getValue().toString()+"\n");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    /*
    private void kullaniciAktivitesiniAc(){
        Intent intent = new Intent(getApplicationContext(),SendFile.class);
        startActivity(intent);
    }
    */



}
