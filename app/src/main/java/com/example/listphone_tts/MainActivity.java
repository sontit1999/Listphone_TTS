package com.example.listphone_tts;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.listphone_tts.adapter.PhoneAdapter;
import com.example.listphone_tts.unity.Mydatabase;
import com.example.listphone_tts.unity.Person;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{
    public static Mydatabase mydatabase;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<Person> arrayList, arrsearch;
    PhoneAdapter adapter;
    ImageView ivadd;
    EditText edtsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        // tạo bảng danh bạ
        mydatabase.queryData("CREATE TABLE IF NOT EXISTS listphone(hoten varchar(50),sodienthoai varchar(20))");
        setupdata();
        arrsearch = new ArrayList<>(arrayList);
        setuprecyclerview();
        Action();
        Actionbar();
    }
    // lấy dư liệu từ SQlite add vào arraylist
    private void setupdata() {
        arrayList.clear();
        Cursor cursor = mydatabase.getData("SELECT * FROM listphone");
        while (cursor.moveToNext()){
            String hoten,sdt;
            hoten = cursor.getString(0);
            sdt = cursor.getString(1);
            arrayList.add(new Person("ffffff",hoten,sdt));
            adapter.notifyDataSetChanged();
        }
    }
    // các sự kiện
    private void Action() {
        ivadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Addactivity.class);
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });
        // lắng nghe sự thay đổi edittext
        edtsearch.addTextChangedListener(new TextWatcher() {

            //Đang thay đổi
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            //Trước khi thay đổi
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }
            //Sau khi thay đổi
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                // nếu edt trống thì load all list ngược lại kiểm tra xem trong list có danh bạ này ko r .nếu có show ra
                if(edtsearch.getText().toString().trim().equals("")){
                    arrayList.addAll(arrsearch);
                    adapter.notifyDataSetChanged();
                }else{
                    arrayList.clear();
                    for(Person i : arrsearch){
                        if(i.getName().equalsIgnoreCase(edtsearch.getText().toString().trim())){
                            arrayList.add(i);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }


            }
        });

    }
   // ánh xạ
    private void anhxa() {
        edtsearch = (EditText) findViewById(R.id.edttimkiem) ;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mydatabase = new Mydatabase(this,"danhba.sqlite",null,1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivadd = (ImageView) findViewById(R.id.iv_add);
        arrayList = new ArrayList<>();
        adapter = new PhoneAdapter(this,arrayList,R.layout.item_phone);
    }


    // setup recyclerview
    public void setuprecyclerview(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        // sự kiện swipe 1 item trong recyclerview
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Person person = arrayList.get(viewHolder.getAdapterPosition());
                String name = person.getName();
                mydatabase.deleteonerecord(name,"listphone");
                arrayList.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);
    }

    // tạo menu
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    // sự kiện click menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_delete:
                showdialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // set up toolbar
    private void Actionbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("vongdoi","onstart");
        setupdata();
    }
    // hàm show dialog xác nhận xóa all
    public void showdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydatabase.Deleteallrecord("listphone");
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
    }
}

