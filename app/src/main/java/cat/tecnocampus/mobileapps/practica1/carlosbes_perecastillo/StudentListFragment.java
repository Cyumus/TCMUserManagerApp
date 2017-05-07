package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.RVAdapter;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.domain.Student;

/**
 * Created by carlo on 06/05/2017.
 */

public class StudentListFragment extends Fragment {
    private RecyclerView mStudentListView;
    private RVAdapter mItemsAdapter;

    private GridLayoutManager mLayoutManager;
    private DbAdapter dbAdapter;

    public StudentListFragment() {
    }

    public static StudentListFragment newInstance() {
        return new StudentListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.student_list, container, false);


        mLayoutManager = new GridLayoutManager(getActivity(),
                view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE? 2:1);

        mStudentListView = (RecyclerView) view.findViewById(R.id.student_list);
        mStudentListView.setLayoutManager(mLayoutManager);

        mStudentListView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            private int x=0,y=0;
            @Override
            public void onScrollStateChanged(RecyclerView aRecyclerView, int aNewState){
                super.onScrollStateChanged(aRecyclerView, aNewState);
            }

            @Override
            public void onScrolled(RecyclerView aRecyclerView, int dx, int dy){
                super.onScrolled(aRecyclerView, dx, dy);
                x+=dx;  y+=dy;
                if (y>200){ ((MainActivity) getActivity()).hideButton(); }
                else if (y<=200){ ((MainActivity) getActivity()).showButton(); }
            }
        });

        dbAdapter = DbAdapter.getInstance(getContext());
        dbAdapter.open();
        dbAdapter.upgrade(); // <<< Database is being deleted because of this, use this JUST for debug purposes

        dbAdapter.createStudent("A", "A", 0, "A","A",0);
        dbAdapter.createStudent("B", "B", 0, "B","B",0);
        dbAdapter.createStudent("C", "C", 0, "C","C",0);
        dbAdapter.createStudent("D", "D", 0, "D","D",0);
        dbAdapter.createStudent("E", "E", 0, "E","E",0);
        dbAdapter.createStudent("F", "F", 0, "F","F",0);
        dbAdapter.createStudent("G", "G", 0, "G","G",0);
        dbAdapter.createStudent("H", "H", 0, "H","H",0);
        dbAdapter.createStudent("I", "I", 0, "I","I",0);
        dbAdapter.createStudent("J", "I", 0, "I","I",0);
        dbAdapter.createStudent("K", "I", 0, "I","I",0);
        dbAdapter.createStudent("L", "I", 0, "I","I",0);
        dbAdapter.createStudent("M", "I", 0, "I","I",0);
        dbAdapter.createStudent("N", "I", 0, "I","I",0);
        dbAdapter.createStudent("I", "I", 0, "I","I",0);
        dbAdapter.createStudent("I", "I", 0, "I","I",0);
        dbAdapter.createStudent("I", "I", 0, "I","I",0);
        dbAdapter.createStudent("I", "I", 0, "I","I",0);


        fillData();

        return view;
    }

    public void fillData() {
        Cursor cuStudents = dbAdapter.fetchAllStudents();

        mItemsAdapter = new RVAdapter(cuStudents);

        mStudentListView.setAdapter(mItemsAdapter);
    }

    @Override
    public void onDestroy(){
        dbAdapter.close();
        super.onDestroy();
    }
}
