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

/**
 * This class is the virtual representation of a Student.
 */
public class Student {

    /**
     * Student Attributes
     * These Spannable String Builders are used to set some fancy styles programmatically
     * instead of hardcoding it in the layout. This give us some flexibility when trying to
     * implement a Settings functionality, where the user can change the way that these Strings
     * are shown in the view.
     * - mName. The name of the Student.
     * - mSurname. The surname of the Student.
     * - mCourse. The course that the Student is studying.
     */
    private SpannableStringBuilder mName;
    private SpannableStringBuilder mSurname;
    private SpannableStringBuilder mCourse;

    /**
     * Main constructor. It gets all the Basic String parameters and sets fancy styles to them.
     * @param aName The name of the Student.
     * @param aSurname The surname of the Student.
     * @param aCourse The course that the Student is studying.
     */
    public Student(String aName, String aSurname, String aCourse){
        mName = new SpannableStringBuilder(aName);
        mName.setSpan(new StyleSpan(Typeface.BOLD),0,aName.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        mSurname = new SpannableStringBuilder(aSurname);

        mCourse = new SpannableStringBuilder(aCourse);
        mCourse.setSpan(new UnderlineSpan(), 0, mCourse.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mCourse.setSpan(new StyleSpan(Typeface.ITALIC), 0, mCourse.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

    /**
     * Name getter
     * @return the name of the Student
     */
    public SpannableStringBuilder getName() {return mName;}

    /**
     * Surname getter
     * @return the surname of the Student
     */
    public SpannableStringBuilder getSurname() {return mSurname;}

    /**
     * Course getter
     * @return the course that the Student is studying.
     */
    public SpannableStringBuilder getCourse() {return mCourse;}

}
