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

/**
 * This class is the visual and logic representation of the Student List.
 */
public class StudentListFragment extends Fragment {
    /**
     * StudentListFragment Attributes
     * - mStudentListView. The RecyclerView with the actual Student List.
     * - mItemsAdapter. The RecyclerView Adapter that manages this Fragment.
     * - mLayoutManager. The GridLayoutManager used to set 1 column when it's in portrait and 2
     * columns when it's in landscape.
     * - mDbAdapter. The Database Adapter used to get all the data.
     */
    private RecyclerView mStudentListView;
    RVAdapter mItemsAdapter;
    private GridLayoutManager mLayoutManager;
    private DbAdapter mDbAdapter;

    /**
     * Empty main constructor
     */
    public StudentListFragment() {
    }

    /**
     * This function creates a new instance of the fragment.
     * @return a new instance of the fragment.
     */
    public static StudentListFragment newInstance() {
        return new StudentListFragment();
    }

    /**
     * This function is called when the StudentListFragment is created.
     * @param aSavedInstanceState The old instance state before the old instance was destroyed
     */
    @Override
    public void onCreate(Bundle aSavedInstanceState) {
        super.onCreate(aSavedInstanceState);
    }

    /**
     * This function is called when the View of the Fragment is created.
     *
     * It inflates the given LayoutInflater, adding it to the main view.
     *
     * The function gets the orientation of the device and sets 2 columns if it's in landscape and 1
     * if it's in portrait, using the GridLayoutManager.
     *
     * The function adds then a DividerItemDecoration, just for having a fancy styling.
     *
     * The function sets a Scroll Listener to hide or show the Floating Action Button if the user
     * scrolls the list down to avoid having an annoying button floating in front of the list.
     *
     * It sets the item animator.
     *
     * At last, it gets the Database, opens a new connection to it and fills the list with data.
     *
     * @param aInflater The LayoutInflater that inflates the view aContainer
     * @param aContainer The root View that is inflated
     * @param aSavedInstanceState The old instance state before the old instance was destroyed
     * @return The resulting view of all this process.
     */
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

    /**
     * This function gets all the students in the Database and sets the list to the RecyclerView
     * adapter in order to make it to represent it in the view.
     */
    public void fillData() {
        Cursor cuStudents = mDbAdapter.fetchAllStudents();

        mItemsAdapter = new RVAdapter(this, cuStudents);

        mStudentListView.setAdapter(mItemsAdapter);
    }

    /**
     * This function updates the Main Activity menu.
     */
    public void updateMenu(){
        ((MainActivity) getActivity()).updateMenu();
    }

    /**
     * This function is called when the StudentListFragment is destroyed.
     *
     * It closes the connection with the database.
     */
    @Override
    public void onDestroy(){
        mDbAdapter.close();
        super.onDestroy();
    }
}
