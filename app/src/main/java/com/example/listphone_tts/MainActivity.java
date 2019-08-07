package com.example.listphone_tts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.listphone_tts.adapter.PhoneAdapter;
import com.example.listphone_tts.unity.Mydatabase;
import com.example.listphone_tts.unity.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements PhoneAdapter.PhoneListener{
    private static final int REQUEST_CODE_UPDATE  = 123 ;
    private Realm realm;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerview)  RecyclerView recyclerView;
    @BindView(R.id.iv_add) ImageView ivadd;
    @BindView (R.id.edttimkiem) EditText edtsearch;
    ArrayList<Person> arrayList, arrsearch;
    int indexitemupdate = 0;
    PhoneAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //// khởi tạo realm
        realm = Realm.getDefaultInstance();
        // lấy data
        initdata();
        setuprecyclerview();
        Action();
        Actionbar();
    }
    // lấy dư liệu từ Realm add vào arraylist
    private void setupdata() {
        arrayList.clear();
        RealmResults<Person> listResult = realm.where(Person.class).findAll();
        arrayList.addAll(realm.copyFromRealm(listResult));
        adapter.notifyDataSetChanged();
    }
    @OnClick(R.id.iv_add)
    public void OnImageviewaddClick(View view){
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.setTitle("Thêm liên hệ");
            dialog.setContentView(R.layout.dialog_add);
            Button btnadd = (Button) dialog.findViewById(R.id.btn_add);
            Button btnexit = (Button) dialog.findViewById(R.id.btn_huy);
            EditText edt_name = (EditText) dialog.findViewById(R.id.edt_name);
            EditText edt_phone = (EditText) dialog.findViewById(R.id.edt_phone);
            btnexit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "add", Toast.LENGTH_SHORT).show();
                        if(edt_phone.getText().toString().trim().isEmpty() || edt_name.getText().toString().trim().isEmpty()){
                            Toast.makeText(MainActivity.this, "ko dc bỏ trống", Toast.LENGTH_SHORT).show();
                        }else{
                            String name,phone;
                            name = edt_name.getText().toString().trim();
                            phone = edt_phone.getText().toString().trim();
                            Person personadd = new Person(5,"link anh",name,phone);
                            addpersonintorealm(personadd);
                            arrayList.add(personadd);
                            adapter.notifyItemInserted(arrayList.size());
                            dialog.dismiss();
                        }
                    }
                });
            dialog.show();



    }
    // các sự kiện
    private void Action() {
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
                    setupdata();
                }else{
                    String text = "*" + edtsearch.getText().toString().trim() +"*";
                    arrayList.clear();
                    RealmResults<Person> listResult = realm.where(Person.class).like("name", text, Case.INSENSITIVE).findAll();
                    arrayList.addAll(realm.copyFromRealm(listResult));
                    adapter.notifyDataSetChanged();
                }


            }
        });

    }
   // ánh xạ
    private void initdata() {
        arrayList = new ArrayList<>();
        adapter = new PhoneAdapter(this,arrayList,R.layout.item_phone,this);
        setupdata();
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
                deletePerson(person);
                arrayList.remove(viewHolder.getAdapterPosition());
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView);
    }

    private void deletePerson(Person person) {
        realm.beginTransaction();
        RealmResults<Person> rows= realm.where(Person.class).equalTo("id",person.getId()).findAll();
        rows.deleteAllFromRealm();
        realm.commitTransaction();
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

    // hàm show dialog xác nhận xóa all
    public void showdialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete item sellected ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                  realm.beginTransaction();
                                  RealmResults results = realm.where(Person.class).findAll();
                                  results.deleteAllFromRealm();
                                  realm.commitTransaction();
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

    @Override
    public void onClickItem(Person person,int i) {
                 indexitemupdate = i;
                 Intent intent = new Intent(this, Addactivity.class);
                 intent.putExtra("person",person);
                 startActivityForResult(intent,REQUEST_CODE_UPDATE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK && data!= null){
            // get data check
             Person person = (Person) data.getSerializableExtra("person");
             arrayList.set(indexitemupdate,person);
             adapter.notifyItemChanged(indexitemupdate);
//           arrayList.add((Person) data.getSerializableExtra("person"));
//           int size = arrayList.size();
//           adapter.notifyItemRangeInserted(size,1);
        }
    }
    // thêm 1 person
    public  void addpersonintorealm(Person person){
        realm.beginTransaction();

        // lấy id max để tìm ra id object thêm vào
        Number currentIdNum = realm.where(Person.class).max("id");
        int nextId;
        if(currentIdNum == null) {
            nextId = 1;
        } else {
            nextId = currentIdNum.intValue() + 1;
        }

        Person person1 = new Person();
        person1.setId(nextId);
        person1.setLinkanh(person.getLinkanh());
        person1.setName(person.getName());
        person1.setPhonenumber(person.getPhonenumber());
        person1.setIscheck(person.getIscheck());
        realm.insertOrUpdate(person1); // using insert API
        realm.commitTransaction();
    }
}

