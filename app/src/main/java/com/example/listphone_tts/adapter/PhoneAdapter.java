package com.example.listphone_tts.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.listphone_tts.Addactivity;
import com.example.listphone_tts.R;
import com.example.listphone_tts.unity.Person;

import java.util.ArrayList;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.Myviewhoder> {
   // SparseBooleanArray itemStateArray= new SparseBooleanArray();
    Context context;
    ArrayList<Person> arrayList;
    int layout;

    public PhoneAdapter(Context context, ArrayList<Person> arrayList,int layout) {
        this.context = context;
        this.arrayList = arrayList;
        this.layout = layout;
    }

    public PhoneAdapter() {
    }

    @NonNull
    @Override
    public Myviewhoder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout,null);
        return new Myviewhoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Myviewhoder myviewhoder, int i) {
         final Person person = arrayList.get(i);
         myviewhoder.tvname.setText(person.getName());
         myviewhoder.tvphone.setText(person.getPhonenumber());
         //  myviewhoder.bind(i);
         myviewhoder.container.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(context, Addactivity.class);
                 intent.putExtra("type","update");
                 intent.putExtra("name",person.getName());
                 intent.putExtra("phone",person.getPhonenumber());
                 context.startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Myviewhoder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvname,tvphone;
        CheckBox checkBox;
        RelativeLayout container;

        public Myviewhoder(@NonNull final View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.contain);
            img = (ImageView) itemView.findViewById(R.id.iv);
            tvname = (TextView) itemView.findViewById(R.id.tv_name);
            tvphone = (TextView) itemView.findViewById(R.id.tv_phonenumber);
            checkBox = (CheckBox) itemView.findViewById(R.id.check);

        }
        /*
        void bind(int position) {

            // use the sparse boolean array to check
            if (!itemStateArray.get(position, false)) {
                checkBox.setChecked(false);}
            else {
                checkBox.setChecked(true);
            }
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            if (!itemStateArray.get(adapterPosition, false)) {
                checkBox.setChecked(true);
                itemStateArray.put(adapterPosition, true);
            }
            else  {
                checkBox.setChecked(false);
                itemStateArray.put(adapterPosition, false);
            }

        }
       */
    }
}
