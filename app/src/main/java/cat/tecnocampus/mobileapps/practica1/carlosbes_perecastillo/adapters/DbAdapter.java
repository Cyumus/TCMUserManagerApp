package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 */
public class DbAdapter {

    public static abstract class Student implements BaseColumns {
        public static final String KEY_NAME = "name";
        public static final String KEY_SURNAME="surname";
        public static final String KEY_PHONE="phone";
        public static final String KEY_DNI="dni";
        public static final String KEY_GRADE="grade";
        public static final String KEY_COURSE="course";
        public static final String KEY_ROW_ID = "_id";
        private static final String TABLE_NAME = "student";

        private static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + "( " +
                        KEY_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_NAME + " TEXT NOT NULL,"+
                        KEY_SURNAME+" TEXT NOT NULL,"+
                        KEY_PHONE+" INTEGER,"+
                        KEY_DNI+" TEXT NOT NULL,"+
                        KEY_GRADE+" TEXT NOT NULL,"+
                        KEY_COURSE+" INTEGER"+")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

        public static final String GET_COUNT = "SELECT count(*) FROM " + TABLE_NAME + ";";
    }

    private static DbAdapter instance = null;

    private static final String TAG = "StudentDbAdapter";
    private DatabaseHelper mDbHelper = null;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 4;

    private final Context mCtx;


    private static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * Main constructor of the DatabaseHelper class
         * @param aContext The context within which to work
         */
        DatabaseHelper(Context aContext) {
            super(aContext, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Function that is called when the database is created
         * @param aDb The SQLite database where will be executed the table creation query.
         */
        @Override
        public void onCreate(SQLiteDatabase aDb) {
            aDb.execSQL(Student.CREATE);
        }

        /**
         * Function that is called when the database is upgraded to newer versions.
         * @param aDb The SQLite database that has been upgraded
         * @param aOldVersion The old version of the database
         * @param aNewVersion The new version of the database
         */
        @Override
        public void onUpgrade(SQLiteDatabase aDb, int aOldVersion, int aNewVersion) {
            Log.w(TAG, "Upgrading database from version " + aOldVersion + " to "
                    + aNewVersion + ", which will destroy all old data");
            aDb.execSQL(Student.DELETE_TABLE);
            onCreate(aDb);
        }
    }

    /**
     * Singleton function to get the instance of the Database Adapter.
     * @param aCtx The context within which to work
     * @return The instance of the Database Adapter
     */
    public static DbAdapter getInstance(Context aCtx) {
        if (instance == null) {
            instance = new DbAdapter(aCtx);
        }
        return instance;
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param aCtx the Context within which to work
     */
    private DbAdapter(Context aCtx) {
        this.mCtx = aCtx;
        mDbHelper = null;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        if (mDbHelper == null) {
            mDbHelper = new DatabaseHelper(mCtx);
            mDb = mDbHelper.getWritableDatabase();
        }
        return this;
    }

    /**
     * Closes the Database Adapter
     */
    public void close() {
        mDbHelper.close();
        mDbHelper = null;
    }

    /**
     * Upgrades the Database Adapter.
     */
    public void upgrade() {
        mDbHelper.onUpgrade(mDb, 1, 2);
    }

    /**
     * Creates the Student on the Database.
     * @param aName The name of the Student
     * @param aSurname The surname of the Student
     * @param aPhone The telephone number of the Student
     * @param aDni The identification card number of the Student
     * @param aGrade The grade that the Student is studying
     * @param aCourse The course that the Student is coursing
     * @return The number of rows inserted.
     */
    public long createStudent(String aName, String aSurname, int aPhone, String aDni, String aGrade, int aCourse) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Student.KEY_NAME, aName);
        initialValues.put(Student.KEY_SURNAME, aSurname);
        initialValues.put(Student.KEY_PHONE, aPhone);
        initialValues.put(Student.KEY_DNI, aDni);
        initialValues.put(Student.KEY_GRADE, aGrade);
        initialValues.put(Student.KEY_COURSE, aCourse);

        return mDb.insert(Student.TABLE_NAME, null, initialValues);
    }

    /**
     * Deletes the Student of the given row.
     * @param aRowId The row where the Student is represented
     * @return The number of rows deleted.
     */
    public boolean deleteStudent(long aRowId) {
        return mDb.delete(Student.TABLE_NAME, Student.KEY_ROW_ID + "=" + aRowId, null) > 0;
    }

    /**
     * Returns all the Students in the Database.
     * @return All the Students in the Database.
     */
    public Cursor fetchAllStudents() {
        return mDb.query(Student.TABLE_NAME, new String[]{Student.KEY_ROW_ID, Student.KEY_NAME, Student.KEY_SURNAME, Student.KEY_PHONE, Student.KEY_DNI, Student.KEY_GRADE, Student.KEY_COURSE}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the Student that matches the given rowId
     *
     * @param aRowId id of Student to retrieve
     * @return Cursor positioned to matching Student, if found
     * @throws SQLException if the Student could not be found/retrieved
     */
    public Cursor fetchStudent(long aRowId) throws SQLException {
        Cursor cuStudent = mDb.query(true,Student.TABLE_NAME, new String []{Student.KEY_ROW_ID, Student.KEY_NAME, Student.KEY_SURNAME, Student.KEY_PHONE, Student.KEY_DNI, Student.KEY_GRADE, Student.KEY_COURSE}, Student.KEY_ROW_ID + " = ? ", new String [] {String.valueOf(aRowId+1)}, null, null, null, null);
        if (cuStudent != null && cuStudent.getCount()>0) {
            cuStudent.moveToFirst();
            Log.d("SwA", cuStudent.getString(0)+"");
        }
        return cuStudent;
    }

    /**
     * Returns whether the Database is empty or not.
     * @return true if the Database is empty.
     */
    public boolean isEmpty() {
        Cursor cuStudents = mDb.rawQuery(Student.GET_COUNT, null);
        cuStudents.moveToFirst();
        int icStudent = cuStudents.getInt(0);
        return icStudent <= 0;
    }
}