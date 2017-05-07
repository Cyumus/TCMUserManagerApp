package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities.UserEdit;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;

public class MainActivity extends AppCompatActivity {
    private static final int CREATE_STUDENT = 0;
    private static final int EDIT_STUDENT = 1;
    private static final int DELETE_STUDENT = 2;

    FloatingActionButton mBtnCreate;
    private FragmentManager mFragmentManager;
    private StudentListFragment studentListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        // Create Student Floating Action Button
        mBtnCreate = (FloatingActionButton) findViewById(R.id.createStudentButton);
        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, UserEdit.class), CREATE_STUDENT);
            }
        });

        // Adding Student List Recycled View
        FragmentTransaction addStudentListTransaction = mFragmentManager.beginTransaction();
        studentListFragment = StudentListFragment.newInstance();
        addStudentListTransaction.replace(R.id.student_list, studentListFragment);
        addStudentListTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CREATE_STUDENT: {
                if (resultCode == RESULT_OK){
                    DbAdapter.getInstance(MainActivity.this)
                                .createStudent(
                                    data.getStringExtra(DbAdapter.Student.KEY_NAME),
                                    data.getStringExtra(DbAdapter.Student.KEY_SURNAME),
                                    Integer.parseInt(data.getStringExtra(DbAdapter.Student.KEY_PHONE)),
                                    data.getStringExtra(DbAdapter.Student.KEY_DNI),
                                    data.getStringExtra(DbAdapter.Student.KEY_GRADE),
                                    Integer.parseInt(data.getStringExtra(DbAdapter.Student.KEY_COURSE))
                                );
                    studentListFragment.fillData();
                }
                break;
            }
            case EDIT_STUDENT: {
                break;
            }
            case DELETE_STUDENT: {
                break;
            }
            default: {

            }
        }
    }
    public void hideButton(){mBtnCreate.hide();}
    public void showButton(){mBtnCreate.show();}

}