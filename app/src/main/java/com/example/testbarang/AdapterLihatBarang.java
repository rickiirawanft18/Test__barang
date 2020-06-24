package com.example.testbarang;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterLihatBarang extends RecyclerView.Adapter<AdapterLihatBarang.ViewHolder>{

    private ArrayList<Barang> daftarBarang;
    private Context context;
    FirebaseDataListener listener;

    public AdapterLihatBarang(ArrayList<Barang> barangs, Context c){
        //Inisialisasi data dan variabel yang akan digunakan
        daftarBarang = barangs;
        context = c;
        listener = (FirebaseDataListener) c;
    }

    @NonNull
    @Override
    public AdapterLihatBarang.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inisialisasi ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_barang, parent, false);
        // Mengeset ukuran view, margin, padding dan parameter layout lainnya
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterLihatBarang.ViewHolder holder, final int position) {
        //Menampilkan data pada view
        final String name = daftarBarang.get(position).getNama();
        final String kode = daftarBarang.get(position).getKode();
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Untuk latihan selanjutnya, jika ingin membaca detail data
            }
        });
        holder.tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.activity_update_data);
                dialog.setTitle("Pilih Aksi");
                dialog.show();

                Button updateButton = (Button) dialog.findViewById(R.id.bt_edit_data);
                Button deleteButton = (Button) dialog.findViewById(R.id.bt_delete_data);

                //apabila tombol edit diklik
                updateButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                context.startActivity(TambahData.getActIntent((Activity) context).putExtra("data", daftarBarang.get(position)));
                            }
                        }
                );

                //apabila tombol delete diklik
                deleteButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                listener.onDeleteData(daftarBarang.get(position), position);
                            }
                        }
                );
                return true;
            }
        });
        holder.tvTitle.setText(name);
    }

    @Override
    public int getItemCount() {
        //Mengembalikan jumlah item pada barang
        return daftarBarang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        /*Inisialisasi View
         * Disini kita hanya menggunakan data string untuk tiap item
         * dan juga viewnya hanyalah satu TextView*/
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_namabarang);
        }
    }

    public interface FirebaseDataListener{
        void onDeleteData(Barang barang, int position);
    }
}