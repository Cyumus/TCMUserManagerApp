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

/**
 * This is the Main Activity of the Application
 */
public class MainActivity extends AppCompatActivity {
    /**
     * Class constants
     */
    private static final int CREATE_STUDENT = 0;
    private static final int EDIT_STUDENT = 1;

    /**
     * Main Activity Attributes
     * - mBtnCreate. The Floating Action Button on the bottom-right corner of the view. It creates
     * new Students.
     * - mMenu. The Toolbar of the Application. It has the edit and delete options when some
     * items are selected.
     * - mStudentListFragment. The visual and logic representation of the Student list as a Fragment
     */
    FloatingActionButton mBtnCreate;
    Toolbar mMenu;
    private StudentListFragment mStudentListFragment;


    /**
     * This function is called when the Main Activity is created.
     * First, it gets the current Fragment Manager to manage the future transactions.
     * Then, it gets the Floating Action Button btnCreate and sets its click listener to start the
     * activity UserEdit and waits for a result.
     *
     * Sets the Toolbar mMenu as the Action Bar of the Application.
     *
     * And begins the transaction of the mStudentListFragment in order to add it to the current view
     * by replacing it.
     * @param savedInstanceState The saved instance state before destroying the old activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

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
        FragmentTransaction addStudentListTransaction = fragmentManager.beginTransaction();
        mStudentListFragment = StudentListFragment.newInstance();
        addStudentListTransaction.replace(R.id.student_list, mStudentListFragment);
        addStudentListTransaction.commit();
    }

    /**
     * This function is called whe the Activity was waiting for a result and actually it gets it.
     *
     * If the Main Activity was waiting for a CREATE_STUDENT result, it will create the Student
     * using the received data.
     *
     * If the Main Activity was waiting for a EDIT_STUDENT result, it will update the Student
     * using the received data.
     *
     * @param requestCode The code that the caller Activity has made
     * @param resultCode The code that the called Activity has sent as a result
     * @param data All the data that the called Activity has sent as a result
     */
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

    /**
     * This function is called when the menu is created.
     * In this case, we filter the visibility of the menu items based on the amount of selected
     * Students.
     *
     * (I know that a flag pattern could be easier, but I didn't know how to implement that pattern
     * in this environment and I managed to implement it this way.)
     *
     * @param menu
     * @return
     */
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

    /**
     * This function hides the Floating Action Button
     */
    public void hideButton(){mBtnCreate.hide();}

    /**
     * This function shows the Floating Action Button
     */
    public void showButton(){mBtnCreate.show();}

    /**
     * This function refreshes the menu options in order to recreate them.
     */
    public void updateMenu(){
        invalidateOptionsMenu();
    }

    /**
     * This function is called when the Edit Button is pressed.
     *
     * It gets the selected Student and calls for result the UserEdit Activity.
     */
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

    /**
     * This function is called when the Delete Button is pressed.
     *
     * It gets all the selected Students and iterate the list deleting all of them.
     */
    public void deleteStudents(){
        for (Integer student:mStudentListFragment.mItemsAdapter.getSelectedItems()){
            DbAdapter.getInstance(MainActivity.this).deleteStudent(mStudentListFragment.mItemsAdapter.getIdFromPosition(student));
        }
        refresh();
    }

    /**
     * This function refreshes the list. Recreating the list Student row items, clearing the
     * selected Student and refreshing the Menu Options.
     */
    private void refresh(){
        mStudentListFragment.fillData();
        mStudentListFragment.mItemsAdapter.clearSelection();
        mStudentListFragment.mItemsAdapter.notifyDataSetChanged();
        invalidateOptionsMenu();
    }

}