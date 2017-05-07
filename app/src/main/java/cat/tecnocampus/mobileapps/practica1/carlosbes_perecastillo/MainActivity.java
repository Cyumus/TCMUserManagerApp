package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity {
    private ListView mList;

    private final int CREATE_CODE=1;
    private final int UPDATE_CODE=2;
    private final int DELETE_RESULT_CODE=3;

    private SimpleCursorAdapter mItemsAdapter;
    private Button mBtnCreate;
    private DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = (ListView) findViewById(R.id.todolist);

        mBtnCreate = (Button) findViewById(R.id.buttonCreate);

        dbAdapter = DbAdapter.getInstance(getApplicationContext());
        dbAdapter.open();
        dbAdapter.upgrade();

        fillData();

        mBtnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent calling=new Intent(MainActivity.this,UserEdit.class);
                calling.putExtra("type","CREATE");
                startActivityForResult(calling,CREATE_CODE);
            }
        });

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean deleted = dbAdapter.deleteStudent(l);
                fillData();
                return deleted;
            }
        });
    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        Log.d("[LIVE_CYCLE]","onActivityResult");
        if(requestCode== CREATE_CODE){
            if(resultCode==RESULT_OK){
                dbAdapter.createStudent(data.getStringExtra("name"),data.getStringExtra("surname"),Integer.parseInt(data.getStringExtra("phone"))
                        ,data.getStringExtra("dni"),data.getStringExtra("grade"),Integer.parseInt(data.getStringExtra("course")));
                Log.d("ResultOk",data.getStringExtra("name"));
                fillData();
            }
            else{
                Log.d("ResultCancel",data.getStringExtra("result"));
            }
        }
        else{
            if(requestCode==UPDATE_CODE){
                if(resultCode==RESULT_OK){
                    dbAdapter.updateStudent(data.getStringExtra("name"),data.getStringExtra("surname"),Integer.parseInt(data.getStringExtra("phone"))
                            ,data.getStringExtra("dni"),data.getStringExtra("grade"),Integer.parseInt(data.getStringExtra("course")));
                    Log.d("ResultOk",data.getStringExtra("name"));
                }
            }
            else{
                if(resultCode==DELETE_RESULT_CODE){
                    dbAdapter.deleteStudentByDni(data.getStringExtra("dni"));
                    Log.d("StudentDeleted","Deleted student with dni: "+data.getStringExtra("dni"));
                }
                else{
                    Log.d("ResultCancel",data.getStringExtra("result"));
                }
            }
        }
    }

    public void fillData(){
        Cursor cuStudents = dbAdapter.fetchAllStudents();
        String [] from = new String[]{DbAdapter.Student.KEY_NAME,DbAdapter.Student.KEY_SURNAME,DbAdapter.Student.KEY_GRADE};
        int [] to = new int[]{android.R.id.text1,android.R.id.text2,android.R.id.text2};
        mItemsAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                cuStudents,from,to,0);
        mList.setAdapter(mItemsAdapter);
    }

    @Override
    public void onDestroy(){
        dbAdapter.close();
        super.onDestroy();
    }
}
