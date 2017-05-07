package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.MainActivity;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.R;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities.StudentDetails;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities.UserEdit;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.domain.Student;

/**
 * Created by carlo on 07/05/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder>{
    private Cursor mStudentCursor;

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(int position);
    }

    /**
     * Main constructor
     * @param aStudentCursor
     */
    public RVAdapter(Cursor aStudentCursor){mStudentCursor = aStudentCursor;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.student_info, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mRootView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor cuStudent = DbAdapter.getInstance(v.getContext()).fetchStudent(position);
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

        mStudentCursor.moveToPosition(position);

        Student student = new Student(mStudentCursor.getString(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_NAME)),
                mStudentCursor.getString(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_SURNAME)),
                "Course: "+mStudentCursor.getInt(mStudentCursor.getColumnIndex(DbAdapter.Student.KEY_COURSE)));

        TextView userName = (TextView) holder.mRootView.findViewById(R.id.student_name);
        TextView userSurname = (TextView) holder.mRootView.findViewById(R.id.student_surname);
        TextView userCourse = (TextView) holder.mRootView.findViewById(R.id.student_course);

        userName.setText(student.getName());
        userSurname.setText(student.getSurname());
        userCourse.setText(student.getCourse());
    }

    @Override
    public int getItemCount() {
        return mStudentCursor.getCount();
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
