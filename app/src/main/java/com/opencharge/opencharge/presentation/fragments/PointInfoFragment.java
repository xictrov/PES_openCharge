package com.opencharge.opencharge.presentation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.opencharge.opencharge.R;
import com.opencharge.opencharge.domain.Entities.Point;
import com.opencharge.opencharge.domain.use_cases.PointByIdUseCase;
import com.opencharge.opencharge.presentation.adapters.ItemDecoration;
import com.opencharge.opencharge.presentation.adapters.PointsAdapter;
import com.opencharge.opencharge.presentation.locators.UseCasesLocator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PointInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PointInfoFragment extends Fragment {

    private PointsAdapter pointsAdapter;
    private RecyclerView recyclerView;
    private FloatingActionButton horari;

    private static final String ARG_POINT_ID = "point_id";
    private String pointId;

    public PointInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pointId String
     * @return A new instance of fragment PointInfoFragment.
     */
    public static PointInfoFragment newInstance(String pointId) {
        PointInfoFragment fragment = new PointInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POINT_ID, pointId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.pointId = getArguments().getString(ARG_POINT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_point_info, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        horari = (FloatingActionButton) view.findViewById(R.id.horari);

        RelativeLayout datePickerButton = (RelativeLayout) getActivity().findViewById(R.id.date_picker_button);
        datePickerButton.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UseCasesLocator useCasesLocator = UseCasesLocator.getInstance();
        PointByIdUseCase getPointUseCase = useCasesLocator.getPointByIdUseCase(new PointByIdUseCase.Callback() {
            @Override
            public void onPointRetrieved(Point point) {
                pointsAdapter = new PointsAdapter(getActivity(), point);

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(pointsAdapter);
                recyclerView.addItemDecoration(new ItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });

        getPointUseCase.setPointId(pointId);
        getPointUseCase.execute();

        horari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    DaysPagerFragment fragment = DaysPagerFragment.newInstance(pointId);
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
