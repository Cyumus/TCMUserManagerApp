package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


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
        name.setText(received.getStringExtra("name"));
        surname.setText(received.getStringExtra("surname"));
        phone.setText(received.getStringExtra("phone"));
        dni.setText(received.getStringExtra("dni"));
        grade.setText(received.getStringExtra("grade"));
        course.setText(received.getStringExtra("course"));
    }
}
