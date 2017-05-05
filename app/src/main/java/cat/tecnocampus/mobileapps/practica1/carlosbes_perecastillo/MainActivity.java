package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    private ListView mList;
    private EditText editText;
    private SimpleCursorAdapter mItemsAdapter;
    private Button mBtnCreate;
    private DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mList = (ListView) findViewById(R.id.todolist);
        editText = (EditText) findViewById(R.id.editTextTodo);
        mBtnCreate = (Button) findViewById(R.id.buttonAdd);

        dbAdapter = DbAdapter.getInstance(getApplicationContext());
        dbAdapter.open();
        dbAdapter.upgrade();

        fillData();

        mBtnCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String current = editText.getText().toString();
                dbAdapter.createStudent(current,current,0,current,current,0);
                fillData();
                editText.setText("");
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
    public void fillData(){
        Cursor cuStudents = dbAdapter.fetchAllStudents();

        String [] from = new String[]{DbAdapter.Student.KEY_NAME};
        int [] to = new int[]{android.R.id.text1};
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
