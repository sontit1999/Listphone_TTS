package com.example.listphone_tts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.listphone_tts.unity.Person;

public class Addactivity extends AppCompatActivity {
    ImageView ivexit;
    EditText edtname,edtphone;
    Button btnadd,btnupdate;
    String type ;
    String nameupdate;
    int idUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);
        anhxa();

        Intent intent = getIntent();
        if(intent!=null){
            type = intent.getStringExtra("type");
            idUpdate = intent.getIntExtra("id",999);
            nameupdate = intent.getStringExtra("name");
            edtname.setText(intent.getStringExtra("name"));
            edtphone.setText(intent.getStringExtra("phone"));
        }
        Action();
    }

    private void Action() {
        ivexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtphone.getText().toString().trim().isEmpty() || edtname.getText().toString().trim().isEmpty()){
                    Toast.makeText(Addactivity.this, "ko dc bỏ trống", Toast.LENGTH_SHORT).show();
                }else{
                    String name,phone;
                    name = edtname.getText().toString().trim();
                    phone = edtphone.getText().toString().trim();
                    MainActivity.mydatabase.addperson(name,phone);
                    Toast.makeText(Addactivity.this, "đã lưu", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "UPDATE listphone SET hoten = '"+ edtname.getText().toString().trim() + "'" + ", sodienthoai = '" + edtphone.getText().toString().trim() +"' WHERE id = '" + idUpdate + "'";
                MainActivity.mydatabase.queryData(sql);
                finish();
            }
        });
    }

    private void anhxa() {
        btnadd = (Button) findViewById(R.id.btn_add);
        ivexit =(ImageView) findViewById(R.id.iv_exit);
        edtname = (EditText) findViewById(R.id.edt_name);
        edtphone = (EditText) findViewById(R.id.edt_phone);
        btnupdate = (Button) findViewById(R.id.btn_update);
    }

}
