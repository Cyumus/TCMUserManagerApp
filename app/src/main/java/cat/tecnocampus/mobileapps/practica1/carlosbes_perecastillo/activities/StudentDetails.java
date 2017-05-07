package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.R;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;


public class StudentDetails extends AppCompatActivity {

    private TextView name;
    private TextView surname;
    private TextView phone;
    private TextView dni;
    private TextView grade;
    private TextView course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        name=(TextView)findViewById(R.id.studentName);
        surname=(TextView)findViewById(R.id.studentSurname);
        phone=(TextView)findViewById(R.id.studentPhone);
        dni=(TextView)findViewById(R.id.studentDni);
        grade=(TextView) findViewById(R.id.studentGrade);
        course=(TextView) findViewById(R.id.studentCourse);

        Intent received=getIntent();
        name.setText(received.getStringExtra(DbAdapter.Student.KEY_NAME));
        surname.setText(received.getStringExtra(DbAdapter.Student.KEY_SURNAME));
        phone.setText(received.getStringExtra(DbAdapter.Student.KEY_SURNAME));
        dni.setText(received.getStringExtra(DbAdapter.Student.KEY_DNI));
        grade.setText(received.getStringExtra(DbAdapter.Student.KEY_GRADE));
        course.setText(received.getStringExtra(DbAdapter.Student.KEY_COURSE));
    }
}
