package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.SparseLongArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.MainActivity;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.R;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.StudentListFragment;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities.StudentDetails;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities.UserEdit;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.domain.Student;

/**
 * Created by Carlos Bes on 07/05/2017.
 */

/**
 * RVAdapter is a simple Recycler View Adapter that implements the abstract classes Adapter and
 * ViewHolder using the MVC to easily show all the components of the StudentListFragment and manage
 * all its logic.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    /**
     * RVAdapter attributes
     * - mStudentCursor. This Cursor represents all the actual data of the Students that's going to
     * be shown on the list.
     * - mSelectedItems. This SparseBooleanArray represents all the items in the list of Students
     * that has been selected.
     * - mStudentListFragment. This StudentListFragment represents the view of the list of Students
     * as a Fragment.
     * - mStudentPositionIdList. This SparseLongArray represents a mapping to get the original id
     * from the relative position in this moment.
     */
    private Cursor mStudentCursor;
    private SparseBooleanArray mSelectedItems;
    private StudentListFragment mStudentListFragment;
    private SparseLongArray mStudentPositionIdList;

    /**
     * Main constructor
     * @param aStudentListFragment The Student List Fragment that represents the list of Students
     * @param aStudentCursor The data of the Students that's going to be shown on the list
     */
    public RVAdapter(StudentListFragment aStudentListFragment, Cursor aStudentCursor){
        mStudentListFragment = aStudentListFragment;
        mStudentCursor = aStudentCursor;
        mSelectedItems = new SparseBooleanArray();
        mStudentPositionIdList = new SparseLongArray();
    }


    /**
     * Function called when the ViewHolder, the visual representation of the RecyclerView, has been
     * created.
     * @param parent The root view of the RecyclerView
     * @param viewType The type of the view.
     * @return the resulting ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_info, parent, false));
    }

    /**
     * Function called when each item of the list of Students is bound to the created ViewHolder as
     * a root view.
     *
     * This function uses the given data and uses the position parameter to move to the actual
     * cursor position. Then, it stores the real row id of the item associated to that list position.
     * We do not use DNI as a PK to avoid problems in a future, when the account of a client may
     * has been corrupted and cannot be fixed and he's forced to create a new one. To avoid problems
     * with the same DNI, we use _id as the PK.
     *
     * When the program now understands which Student goes in each list row, the function creates
     * the click listeners of the Detail Icon (The triangle of the right) and for each item.
     * In this case, if the client has already started to select items, clicking on an item will
     * add it as a selected one, or deselect it if it has been selected already.
     *
     * Then, the program creates the Long Click Listener for each Student. Long pressing will either
     * select and start the selection, or deselect everything and finish it.
     *
     * After that, it creates the representation of the Student giving all its data as parameters.
     * The function gets the TextViews of the layout and sets all the data for each Student
     * list row.
     *
     * At last, it sets the selected rows as it: as selected.
     *
     * @param holder the root ViewHolder that's going to hold all the Student views.
     * @param position the current row position in the list of Students.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mStudentCursor.moveToPosition(position);
        mStudentPositionIdList.put(position,mStudentCursor.getInt(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_ROW_ID)));

        holder.mRootView.findViewById(R.id.student_details_icon).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor cuStudent = DbAdapter.getInstance(v.getContext()).fetchStudent(getIdFromPosition(position));
                Intent intentStudentDetails = new Intent(v.getContext(), StudentDetails.class);
                intentStudentDetails.putExtra(DbAdapter.Student.KEY_NAME, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_NAME)));
                intentStudentDetails.putExtra(DbAdapter.Student.KEY_SURNAME, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_SURNAME)));
                intentStudentDetails.putExtra(DbAdapter.Student.KEY_PHONE, ""+cuStudent.getInt(cuStudent.getColumnIndex(DbAdapter.Student.KEY_PHONE)));
                intentStudentDetails.putExtra(DbAdapter.Student.KEY_DNI, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_DNI)));
                intentStudentDetails.putExtra(DbAdapter.Student.KEY_GRADE, cuStudent.getString(cuStudent.getColumnIndex(DbAdapter.Student.KEY_GRADE)));
                intentStudentDetails.putExtra(DbAdapter.Student.KEY_COURSE, ""+cuStudent.getInt(cuStudent.getColumnIndex(DbAdapter.Student.KEY_COURSE)));
                v.getContext().startActivity(intentStudentDetails);
            }
        });

        holder.mRootView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (mSelectedItems.size()>0){
                    toggleSelection(position);
                    mStudentListFragment.updateMenu();
                    notifyItemChanged(position);
                }
            }
        });

        holder.mRootView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                if (mSelectedItems.size()>0){
                    clearSelection();
                    mStudentListFragment.updateMenu();
                    notifyDataSetChanged();
                }
                else{
                    toggleSelection(position);
                    mStudentListFragment.updateMenu();
                }
                return true;
            }
        });


        Student student = new Student(mStudentCursor.getString(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_NAME)),
                mStudentCursor.getString(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_SURNAME)),
                holder.mRootView.getResources().getString(R.string.course_tag)+": "+
                        mStudentCursor.getInt(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_COURSE)));

        TextView userName = (TextView) holder.mRootView.findViewById(R.id.student_name);
        TextView userSurname = (TextView) holder.mRootView.findViewById(R.id.student_surname);
        TextView userCourse = (TextView) holder.mRootView.findViewById(R.id.student_course);

        userName.setText(student.getName());
        userSurname.setText(student.getSurname());
        userCourse.setText(student.getCourse());

        holder.mRootView.setSelected(mSelectedItems.get(position, false));
    }

    /**
     * Function that returns the amount of Students in the list
     * @return The amount of Students in the list
     */
    @Override
    public int getItemCount() {
        return mStudentCursor.getCount();
    }

    /**
     * Function that sets the Student row associated to the given id as selected or deselected,
     * switching its states, and adding the row id to the mSelectedItems Sparse Array.
     * @param aRowId The list row id associated with the selected Student.
     */
    private void toggleSelection(int aRowId){
        if (mSelectedItems.get(aRowId,false)){
            mSelectedItems.delete(aRowId);
        }
        else{
            mSelectedItems.put(aRowId, true);
        }
        notifyDataSetChanged();
    }

    /**
     * Function that erases all the selected Students and sets them as deselected.
     */
    public void clearSelection(){
        mSelectedItems.clear();
    }

    /**
     * Function that returns the amount of selected Students.
     * @return the amount of selected Students.
     */
    public int getSelectedItemCount(){
        return mSelectedItems.size();
    }

    /**
     * Function that returns the first selected Student, if any.
     * @return the first selected Student
     */
    public int getSelectedItem(){
        return mSelectedItems.keyAt(0);
    }

    /**
     * Function that returns a list of the current selected Students.
     * @return the list of selected Students.
     */
    public List<Integer> getSelectedItems(){
        List<Integer> selectedItems = new ArrayList<Integer>();
        for (int i=0;i<mSelectedItems.size();i++)   selectedItems.add(mSelectedItems.keyAt(i));
        return selectedItems;
    }

    /**
     * Function that gets the actual row id of the Student based on the current list row position.
     * @param position The current position of the Student in the list.
     * @return The actual row id of the Student.
     */
    public long getIdFromPosition(int position){
        return mStudentPositionIdList.get(position);
    }

    /**
     * This class is used as the visual representation of the RecyclerView
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        View mRootView;

        ViewHolder(View v) {
            super(v);
            mRootView = v;
        }

    }
}
