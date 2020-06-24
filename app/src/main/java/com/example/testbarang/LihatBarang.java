package com.example.testbarang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class LihatBarang extends AppCompatActivity implements AdapterLihatBarang.FirebaseDataListener{
    //Mendefinisikan variabel yang akan dipakai
    private DatabaseReference database;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Barang> daftarBarang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_barang);

        //Inisialisasi RecyclerView & komponennya
        recyclerView = findViewById(R.id.rv_main);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Inisialisasi dan mengambil firebase database reference
        database = FirebaseDatabase.getInstance().getReference();

        //Mengambil data barang dari firebase realtime DB
        database.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Memasukkan data baru ke arrayList
                daftarBarang = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()){
                    /*Mapping data pada DataSnapshot ke dalam object barang
                     * dan juga menyimpan primary key pada object barang untuk
                     * keperluan edit dan delete data.*/
                    Barang barang = noteDataSnapshot.getValue(Barang.class);
                    barang.setKode(noteDataSnapshot.getKey());
                    /*Menambahkan objek barang yang sudah dimapping ke dalam arraylist*/
                    daftarBarang.add(barang);
                }
                /*Inisialisasi adapter dan data barang dalam bentuk arrayList
                 * dan mengeset adapter ke dalam recyclerView*/
                adapter = new AdapterLihatBarang(daftarBarang, LihatBarang.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /*Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint errornya
                 * ke Logcat*/
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, LihatBarang.class);
    }

    @Override
    public void onDeleteData(Barang barang, int position) {
        /**
         * Kode ini akan dipanggil ketika method onDeleteData
         * dipanggil dari adapter lewat interface.
         * Yang kemudian akan mendelete data di Firebase Realtime DB
         * berdasarkan key barang.
         * Jika sukses akan memunculkan Toast
         */
        if(database!=null){
            database.child("Barang")
                    .child(barang.getKode())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(LihatBarang.this,"Data berhasil di hapus", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}