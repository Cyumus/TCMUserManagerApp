package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.design.widget.Snackbar;

public class UserEdit extends AppCompatActivity {

    private final int RESULT_DELETE = 3;

    private EditText name;
    private EditText surname;
    private EditText phone;
    private EditText dni;
    private EditText grade;
    private EditText course;
    private Button buttonOK;
    private Button buttonCancel;
    private Button buttonDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);

        name=(EditText)this.findViewById(R.id.nameInput);
        surname=(EditText)findViewById(R.id.surnameInput);
        phone=(EditText)findViewById(R.id.phoneInput);
        dni=(EditText)findViewById(R.id.dniInput);
        grade=(EditText)findViewById(R.id.gradeInput);
        course=(EditText)findViewById(R.id.courseInput);

        Intent received=getIntent();
        String type=received.getStringExtra("type");
        if(type.equals("UPDATE")){
            name.setText(received.getStringExtra("name"));
            surname.setText(received.getStringExtra("surname"));
            phone.setText(received.getStringExtra("phone"));
            dni.setText(received.getStringExtra("dni"));
            grade.setText(received.getStringExtra("grade"));
            course.setText(received.getStringExtra("course"));
        }

        buttonOK=(Button)findViewById(R.id.saveButton);
        buttonCancel=(Button)findViewById(R.id.cancelButton);
        buttonDelete=(Button)findViewById(R.id.deleteButton);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean errorFound=false;
                String error="";
                Intent result=new Intent();
                result.putExtra("name",name.getText().toString());
                result.putExtra("surname",surname.getText().toString());
                if(checkValidNumber(phone.getText().toString())){
                result.putExtra("phone",phone.getText().toString());}
                else{
                    errorFound=true;
                    error+=" Phone is not a valid number; ";
                }
                result.putExtra("dni",dni.getText().toString());
                result.putExtra("grade",grade.getText().toString());
                if(checkValidNumber(course.getText().toString())){
                result.putExtra("course",course.getText().toString());}
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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent result= new Intent();
                result.putExtra("result","Delete");
                setResult(RESULT_DELETE,result);
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
}
