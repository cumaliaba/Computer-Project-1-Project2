package com.example.abalik.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;





public class SendFile extends AppCompatActivity implements View.OnClickListener {

    FilePickerDialog dialog;

    private EditText alici;
    private Button kaydet,gor, file_but;
    private TextView mesajlar;

    String path="";
    String dosya_ici ="";



    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_file);
        baslangic();
    }
    private void baslangic(){

        db=FirebaseDatabase.getInstance();
        //kullanici_adi=(EditText)findViewById(R.id.userEt);
        alici=(EditText)findViewById(R.id.aliciEt);
        kaydet=(Button)findViewById(R.id.kaydetBtn);
        gor=(Button)findViewById(R.id.button2);
        file_but=(Button)findViewById(R.id.fileEt);
        mesajlar=(TextView)findViewById(R.id.MesajTv);

        kaydet.setOnClickListener(this);
        gor.setOnClickListener(this);
        file_but.setOnClickListener(this);

        /*
        // kullanıcıyı alabilmek için
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String kullanici_str_yeni = extras.getString("kullanici_str");
        }
        */

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fileEt:
                File_select();
                break;
            case R.id.kaydetBtn:
                String mesaj_str,alici_str,gonderen_str;
                //gonderen_str=kullanici_adi.getText().toString().trim();
                //burada Huffman encode yapilmalı
                alici_str=alici.getText().toString().trim();
                MesajKaydet(alici_str);
                break;
            case R.id.button2:
                kayitlariGetir();
                break;
        }
    }

    private void File_select() {

        DialogProperties properties = new DialogProperties();
        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.root = new File(DialogConfigs.DEFAULT_DIR);
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;

        dialog = new FilePickerDialog(this, properties);
        dialog.setTitle("Select files to share");



        dialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if (null == files || files.length == 0) {
                    Toast.makeText(SendFile.this, "Select at least one file to start Share Mode", Toast.LENGTH_SHORT).show();
                    return;
                }

                path = files[0].toString();

                try {
                    File file = new File(path);
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                        stringBuffer.append("\n");
                    }
                    fileReader.close();
                    System.out.println("Contents of file:");
                    System.out.println(stringBuffer.toString());
                    dosya_ici=stringBuffer.toString();


                } catch (IOException e) {
                    e.printStackTrace();
                }

       }
        });
        dialog.show();

    }


    private void MesajKaydet(String alici) {

        DatabaseReference dbRef = db.getReference("Mesajlar");
        String key = dbRef.push().getKey();
        DatabaseReference dbRefKeyli = db.getReference("Mesajlar/" + key);

        // kullanıcıyı giriş sayfasından alabilmek için
        Bundle extras = getIntent().getExtras();
        String kullanici_str_yeni = null;
        if (extras != null) {
            kullanici_str_yeni = extras.getString("kullanici_str");
        }
        //String newtext = huffman.compress(dosya_ici);

        dbRefKeyli.setValue(new Mesaj(kullanici_str_yeni, dosya_ici, alici));
        Toast.makeText(SendFile.this, "Dosya server'a gönderildi!", Toast.LENGTH_SHORT).show();

    }

    private void kayitlariGetir(){
        mesajlar.setText("");

        DatabaseReference dbGelenler = db.getReference("Mesajlar");
        dbGelenler.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mesajlar.append("Gonderen   |   Mesaj\n");
                mesajlar.append("---------------------------\n");

                for (DataSnapshot gelenler: dataSnapshot.getChildren()) {
                    //String kullanici_adi_str=kullanici_adi.getText().toString().trim();

                    // kullanıcıyı giriş sayfasından alabilmek için
                    Bundle extras = getIntent().getExtras();
                    String kullanici_str_yeni = null;
                    if (extras != null) {
                        kullanici_str_yeni = extras.getString("kullanici_str");
                    }



                    String gelen_huffman = gelenler.getValue(Mesaj.class).getMesaj();
                    String gonderen_adi = gelenler.getValue(Mesaj.class).getGonderen();
                    String alici_adi = gelenler.getValue(Mesaj.class).getAlici();
                    //burada Huffman decode
                    if(alici_adi.equals(kullanici_str_yeni)){

                        mesajlar.append(gonderen_adi+" -> "+gelen_huffman+"\n");
                        mesajlar.append("---------------------------\n");

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
