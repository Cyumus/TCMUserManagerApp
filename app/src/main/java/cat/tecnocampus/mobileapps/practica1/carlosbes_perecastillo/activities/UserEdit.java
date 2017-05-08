package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.Snackbar;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.R;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;

/**
 * Created by Pere Castillo on 07/05/2017.
 */

public class UserEdit extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText dni;
    private EditText grade;
    private EditText course;
    private Button buttonOK;
    private Button buttonCancel;

    /**
     * Student attributes
     */
    private String mName;
    private String mSurname;
    private String mPhone;
    private String mDni;
    private String mGrade;
    private String mCourse;
    private boolean isLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLandscape=getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE;
        if(isLandscape){
        setContentView(R.layout.activity_user_edit_landscape);

            name=(EditText)this.findViewById(R.id.nameInputLandscape);
            surname=(EditText)findViewById(R.id.surnameInputLandscape);
            phone=(EditText)findViewById(R.id.phoneInputLandscape);
            dni=(EditText)findViewById(R.id.dniInputLandscape);
            grade=(EditText)findViewById(R.id.gradeInputLandscape);
            course=(EditText)findViewById(R.id.courseInputLandscape);

            buttonOK=(Button)findViewById(R.id.saveButtonLandscape);
            buttonCancel=(Button)findViewById(R.id.cancelButtonLandscape);
        }
        else{
            setContentView(R.layout.activity_user_edit);
            name=(EditText)this.findViewById(R.id.nameInput);
            surname=(EditText)findViewById(R.id.surnameInput);
            phone=(EditText)findViewById(R.id.phoneInput);
            dni=(EditText)findViewById(R.id.dniInput);
            grade=(EditText)findViewById(R.id.gradeInput);
            course=(EditText)findViewById(R.id.courseInput);

            buttonOK=(Button)findViewById(R.id.saveButton);
            buttonCancel=(Button)findViewById(R.id.cancelButton);
        }


        Intent received=getIntent();
        String type=received.getStringExtra("type");
        if(received.hasExtra("type")&&type.equals("UPDATE")){
            mName = received.getStringExtra(DbAdapter.Student.KEY_NAME);
            mSurname = received.getStringExtra(DbAdapter.Student.KEY_SURNAME);
            mPhone = received.getStringExtra(DbAdapter.Student.KEY_PHONE);
            mDni = received.getStringExtra(DbAdapter.Student.KEY_DNI);
            mGrade = received.getStringExtra(DbAdapter.Student.KEY_GRADE);
            mCourse = received.getStringExtra(DbAdapter.Student.KEY_COURSE);
        }

        if (savedInstanceState!=null){
            savedInstanceState.get(mName);
            savedInstanceState.get(mSurname);
            savedInstanceState.get(mPhone);
            savedInstanceState.get(mDni);
            savedInstanceState.get(mGrade);
            savedInstanceState.get(mCourse);
        }

        setAttrsToTextViews();



        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorFound=false;
                String error="";
                Intent result=new Intent();
                result.putExtra(DbAdapter.Student.KEY_NAME,name.getText().toString());
                result.putExtra(DbAdapter.Student.KEY_SURNAME,surname.getText().toString());
                if(checkValidNumber(phone.getText().toString())){
                    result.putExtra(DbAdapter.Student.KEY_PHONE,phone.getText().toString());}
                else{
                    errorFound=true;
                    error+=" Phone is not a valid number; ";
                }
                result.putExtra(DbAdapter.Student.KEY_DNI,dni.getText().toString());
                result.putExtra(DbAdapter.Student.KEY_GRADE,grade.getText().toString());
                if(checkValidNumber(course.getText().toString())){
                    result.putExtra(DbAdapter.Student.KEY_COURSE,course.getText().toString());}
                else{
                    errorFound=true;
                    error+=" Course is not a valid number; ";
                }
                if(errorFound){
                    Snackbar.make(findViewById(R.id.myCoordinatorLayout),error,Snackbar.LENGTH_SHORT).show();
                }else{
                    setResult(RESULT_OK,result);
                    finish();}

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result= new Intent();
                result.putExtra("result","Cancel");
                setResult(RESULT_CANCELED,result);
                finish();
            }
        });
    }
    private boolean checkValidNumber(String number){
        try{
            Integer.parseInt(number);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public void setAttrsToTextViews(){
        name.setText(mName);
        surname.setText(mSurname);
        phone.setText(mPhone);
        dni.setText(mDni);
        grade.setText(mGrade);
        course.setText(mCourse);
    }
}
