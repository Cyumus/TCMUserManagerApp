package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.R;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;

/**
 * Created by Pere Castillo on 07/05/2017.
 */

public class StudentDetails extends AppCompatActivity {

    private TextView name;
    private TextView surname;
    private TextView phone;
    private TextView dni;
    private TextView grade;
    private TextView course;
    private  boolean isLandscape;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLandscape=getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE;
        if(isLandscape){
            setContentView(R.layout.activity_student_details_landscape);

            name=(TextView)findViewById(R.id.studentNamelandscape);
            surname=(TextView)findViewById(R.id.studentSurnameLandscape);
            phone=(TextView)findViewById(R.id.studentPhoneLandscape);
            dni=(TextView)findViewById(R.id.studentDniLandscape);
            grade=(TextView) findViewById(R.id.studentGradeLandscape);
            course=(TextView) findViewById(R.id.studentCourseLandscape);

        }
        else{
            setContentView(R.layout.activity_student_details);
            name=(TextView)findViewById(R.id.studentName);
            surname=(TextView)findViewById(R.id.studentSurname);
            phone=(TextView)findViewById(R.id.studentPhone);
            dni=(TextView)findViewById(R.id.studentDni);
            grade=(TextView) findViewById(R.id.studentGrade);
            course=(TextView) findViewById(R.id.studentCourse);

        }


        Intent received=getIntent();
        name.setText(received.getStringExtra(DbAdapter.Student.KEY_NAME));
        surname.setText(received.getStringExtra(DbAdapter.Student.KEY_SURNAME));
        phone.setText(received.getStringExtra(DbAdapter.Student.KEY_PHONE));
        dni.setText(received.getStringExtra(DbAdapter.Student.KEY_DNI));
        grade.setText(received.getStringExtra(DbAdapter.Student.KEY_GRADE));
        course.setText(received.getStringExtra(DbAdapter.Student.KEY_COURSE));
    }
}
