package cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.DbAdapter;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.adapters.RVAdapter;
import cat.tecnocampus.mobileapps.practica1.carlosbes_perecastillo.layouts.DividerItemDecoration;

/**
 * Created by Carlos Bes on 06/05/2017.
 */
// todo comment
// todo clean code
public class StudentListFragment extends Fragment {
    private RecyclerView mStudentListView;
    RVAdapter mItemsAdapter;
    private GridLayoutManager mLayoutManager;
    private DbAdapter mDbAdapter;

    public StudentListFragment() {
    }

    public static StudentListFragment newInstance() {
        return new StudentListFragment();
    }

    @Override
    public void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater aInflater, final ViewGroup aContainer,
                             Bundle aSavedInstanceState) {
        View view = aInflater.inflate(R.layout.student_list, aContainer, false);


        mLayoutManager = new GridLayoutManager(getActivity(),
                view.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE? 2:1);

        mStudentListView = (RecyclerView) view.findViewById(R.id.student_list);
        mStudentListView.setLayoutManager(mLayoutManager);

        mStudentListView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));

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

        mStudentListView.setItemAnimator(new DefaultItemAnimator());

        mDbAdapter = DbAdapter.getInstance(getContext());
        mDbAdapter.open();

        fillData();

        return view;
    }

    public void fillData() {
        Cursor cuStudents = mDbAdapter.fetchAllStudents();

        mItemsAdapter = new RVAdapter(this, cuStudents);

        mStudentListView.setAdapter(mItemsAdapter);
    }

    public void updateMenu(){
        ((MainActivity) getActivity()).updateMenu();
    }

    @Override
    public void onDestroy(){
        mDbAdapter.close();
        super.onDestroy();
    }
}
