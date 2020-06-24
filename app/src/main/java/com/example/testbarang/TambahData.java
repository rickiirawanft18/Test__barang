package com.example.testbarang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class TambahData extends AppCompatActivity {
    private DatabaseReference database;

    private Button btSubmit;
    private EditText etKode;
    private EditText etNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        etKode = (EditText) findViewById(R.id.editNo);
        etNama = (EditText) findViewById(R.id.editNama);
        btSubmit = (Button) findViewById(R.id.btnOk);

        database = FirebaseDatabase.getInstance().getReference();

        final Barang barang = (Barang) getIntent().getSerializableExtra("data");

        if (barang != null) {
            etKode.setText(barang.getKode());
            etNama.setText(barang.getNama());
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    barang.setKode(etKode.getText().toString());
                    barang.setNama(etNama.getText().toString());


                    updateBarang(barang);
                }
            });
        } else {
            btSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isEmpty(etKode.getText().toString()) && !isEmpty(etNama.getText().toString()))
                        submitBrg(new Barang(etKode.getText().toString(), etNama.getText().toString()));
                    else
                        Snackbar.make(findViewById(R.id.btnTambah), "Data barang tidak boleh kosong", Snackbar.LENGTH_LONG).show();

                    InputMethodManager imm = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(etKode.getWindowToken(), 0);

                }
            });
        }
    }

    private boolean isEmpty(String s){
        // Cek apakah ada fields yang kosong, sebelum disubmit
        return TextUtils.isEmpty(s);
    }

    public void submitBrg(Barang brg){
        /*Berikut ini adalah kode yang digunakan untuk mengirimkan data
         * ke firebase RealTime Database dan juga kita set onSuccessListener
         * yang berisi kode yang akan dijalankan ketika data berhasil ditambahkan*/
        database.child("Barang").push().setValue(brg).addOnSuccessListener(this, new OnSuccessListener<Void>(){
            @Override
            public void onSuccess(Void aVoid){
                etKode.setText("");
                etNama.setText("");
                Toast.makeText(getApplicationContext(), "Data berhasil ditambahkan", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void updateBarang(Barang barang) {
        /**
         * Baris kode yang digunakan untuk mengupdate data barang
         * yang sudah dimasukkan di Firebase Realtime Database
         */
        database.child("Barang") //akses parent index, ibaratnya seperti nama tabel
                .child(barang.getKode()) //select barang berdasarkan kode
                .setValue(barang) //set value barang yang baru
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        /**
                         * Baris kode yang akan dipanggil apabila proses update barang sukses
                         */
                        Snackbar.make(findViewById(R.id.btnOk), "Data berhasil di update", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });
    }

    public static Intent getActIntent(Activity activity){
        return new Intent(activity, TambahData.class);
    }
}
