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
// todo comment
// todo clean code
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private Cursor mStudentCursor;
    private SparseBooleanArray mSelectedItems;
    private StudentListFragment mStudentListFragment;
    private SparseLongArray mStudentPositionIdList;

    /**
     * Main constructor
     * @param aStudentCursor
     */
    public RVAdapter(StudentListFragment aStudentListFragment, Cursor aStudentCursor){
        mStudentListFragment = aStudentListFragment;
        mStudentCursor = aStudentCursor;
        mSelectedItems = new SparseBooleanArray();
        mStudentPositionIdList = new SparseLongArray();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_info, parent, false));
    }

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
                "Course: "+mStudentCursor.getInt(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_COURSE)));

        TextView userName = (TextView) holder.mRootView.findViewById(R.id.student_name);
        TextView userSurname = (TextView) holder.mRootView.findViewById(R.id.student_surname);
        TextView userCourse = (TextView) holder.mRootView.findViewById(R.id.student_course);

        userName.setText(student.getName());
        userSurname.setText(student.getSurname());
        userCourse.setText(student.getCourse());

        holder.mRootView.setSelected(mSelectedItems.get(position, false));

    }

    @Override
    public int getItemCount() {
        return mStudentCursor.getCount();
    }

    public void toggleSelection(int aRowId){
        if (mSelectedItems.get(aRowId,false)){
            mSelectedItems.delete(aRowId);
        }
        else{
            mSelectedItems.put(aRowId, true);
        }
        notifyDataSetChanged();
    }

    public void clearSelection(){
        mSelectedItems.clear();
    }
    public int getSelectedItemCount(){
        return mSelectedItems.size();
    }
    public int getSelectedItem(){
        return mSelectedItems.keyAt(0);
    }
    public List<Integer> getSelectedItems(){
        List<Integer> selectedItems = new ArrayList<Integer>();
        for (int i=0;i<mSelectedItems.size();i++)   selectedItems.add(mSelectedItems.keyAt(i));
        return selectedItems;
    }

    public long getIdFromPosition(int position){
        return mStudentPositionIdList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mRootView;

        public ViewHolder(View v) {
            super(v);
            mRootView = v;
        }

    }
}
