package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities.UserEdit;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;

/**
 * Created by Carlos Bes on 05/05/2017.
 */
// todo comment
// todo clean code
public class MainActivity extends AppCompatActivity {
    private static final int CREATE_STUDENT = 0;
    private static final int EDIT_STUDENT = 1;

    FloatingActionButton mBtnCreate;
    Toolbar mMenu;
    private FragmentManager mFragmentManager;
    private StudentListFragment mStudentListFragment;


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

        // Adding Menu Toolbar
        mMenu = (Toolbar) findViewById(R.id.toolbarMenu);
        setSupportActionBar(mMenu);

        // Adding Student List Recycled View
        FragmentTransaction addStudentListTransaction = mFragmentManager.beginTransaction();
        mStudentListFragment = StudentListFragment.newInstance();
        addStudentListTransaction.replace(R.id.student_list, mStudentListFragment);
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
                    mStudentListFragment.fillData();
                }
                break;
            }
            case EDIT_STUDENT: {
                if (resultCode==RESULT_OK){
                    DbAdapter.getInstance(MainActivity.this).updateStudent(
                            mStudentListFragment.mItemsAdapter.getIdFromPosition(mStudentListFragment.mItemsAdapter.getSelectedItem()),
                            data.getStringExtra(DbAdapter.Student.KEY_NAME),
                            data.getStringExtra(DbAdapter.Student.KEY_SURNAME),
                            Integer.parseInt(data.getStringExtra(DbAdapter.Student.KEY_PHONE)),
                            data.getStringExtra(DbAdapter.Student.KEY_DNI),
                            data.getStringExtra(DbAdapter.Student.KEY_GRADE),
                            Integer.parseInt(data.getStringExtra(DbAdapter.Student.KEY_COURSE)));
                }
                refresh();
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.maintoolbar, menu);

        for (int i=0;i<menu.size();i++){
            switch (menu.getItem(i).getItemId()){
                case R.id.student_delete:
                    menu.getItem(i).setVisible(mStudentListFragment.mItemsAdapter.getSelectedItemCount()>0);
                    menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            deleteStudents();
                            return false;
                        }
                    });
                    break;
                case R.id.student_edit:
                    menu.getItem(i).setVisible(mStudentListFragment.mItemsAdapter.getSelectedItemCount()==1);
                    menu.getItem(i).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            editStudent();
                            return false;
                        }
                    });
            }
        }
        return true;
    }
    public void hideButton(){mBtnCreate.hide();}
    public void showButton(){mBtnCreate.show();}

    public void updateMenu(){
        invalidateOptionsMenu();
    }

    public void editStudent(){
        Cursor cuStudent = DbAdapter.getInstance(MainActivity.this).fetchStudent(
                mStudentListFragment.mItemsAdapter.getIdFromPosition(mStudentListFragment.mItemsAdapter.getSelectedItem()
                ));
        Intent intentEditStudent = new Intent(MainActivity.this, UserEdit.class);
        intentEditStudent.putExtra("type", "UPDATE");
        intentEditStudent.putExtra(DbAdapter.Student.KEY_NAME, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_NAME)));
        intentEditStudent.putExtra(DbAdapter.Student.KEY_SURNAME, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_SURNAME)));
        intentEditStudent.putExtra(DbAdapter.Student.KEY_PHONE, ""+cuStudent.getInt(cuStudent.getColumnIndex(DbAdapter.Student.KEY_PHONE)));
        intentEditStudent.putExtra(DbAdapter.Student.KEY_DNI, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_DNI)));
        intentEditStudent.putExtra(DbAdapter.Student.KEY_GRADE, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_GRADE)));
        intentEditStudent.putExtra(DbAdapter.Student.KEY_COURSE, ""+cuStudent.getInt(cuStudent.getColumnIndex(DbAdapter.Student.KEY_COURSE)));
        startActivityForResult(intentEditStudent, EDIT_STUDENT);
    }

    public void deleteStudents(){
        for (Integer student:mStudentListFragment.mItemsAdapter.getSelectedItems()){
            DbAdapter.getInstance(MainActivity.this).deleteStudent(mStudentListFragment.mItemsAdapter.getIdFromPosition(student));
        }
        refresh();
    }

    private void refresh(){
        mStudentListFragment.fillData();
        mStudentListFragment.mItemsAdapter.clearSelection();
        mStudentListFragment.mItemsAdapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }

}