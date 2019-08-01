package com.example.listphone_tts.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    PhoneListener listener;

    public PhoneAdapter(Context context, ArrayList<Person> arrayList,int layout,PhoneListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.layout = layout;
        this.listener = listener;
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
    public void onBindViewHolder(@NonNull final Myviewhoder myviewhoder, final int i) {
         final Person person = arrayList.get(i);
         myviewhoder.tvname.setText(person.getName());
         myviewhoder.tvphone.setText(person.getPhonenumber());
        // lắng nghe

        myviewhoder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                person.setIscheck(isChecked);
            }
        });
         myviewhoder.checkBox.setChecked(person.getIscheck());
         myviewhoder.container.setOnClickListener(new View.OnClickListener() {
              @Override
             public void onClick(View v) {
                   listener.onClickItem(person);
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
    }
    public interface PhoneListener{
        void onClickItem(Person person);
    }
}
