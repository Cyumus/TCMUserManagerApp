package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.domain;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by Carlos Bes on 07/05/2017.
 */
// todo comment
// todo clean code
public class Student {
    private SpannableStringBuilder mName;
    private SpannableStringBuilder mSurname;
    private SpannableStringBuilder mCourse;

    public Student(String aName, String aSurname, String aCourse){
        mName = new SpannableStringBuilder(aName);
        mName.setSpan(new StyleSpan(Typeface.BOLD),0,aName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        mSurname = new SpannableStringBuilder(aSurname);

        mCourse = new SpannableStringBuilder(aCourse);
        mCourse.setSpan(new UnderlineSpan(), 0, mCourse.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mCourse.setSpan(new StyleSpan(Typeface.ITALIC), 0, mCourse.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    public SpannableStringBuilder getName() {return mName;}
    public SpannableStringBuilder getSurname() {return mSurname;}
    public SpannableStringBuilder getCourse() {return mCourse;}

}
