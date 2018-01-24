package com.phduo.hiit;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class OverviewFragment extends ListFragment {

    public static final String LOG_TAG = OverviewFragment.class.getName();
    private static final MyAppApplication GLOBALVARS = new MyAppApplication();

    private Regime regime;
    private ExerciseAdapter aAdapter;

    static OverviewFragment newInstance(Regime regime) {
        OverviewFragment oFrag = new OverviewFragment();
        Bundle b = new Bundle();
        b.putParcelable(GLOBALVARS.REGIME_KEY, regime);
        oFrag.setArguments(b);

        return oFrag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        regime = (getArguments() != null) ? (Regime)getArguments().getParcelable(GLOBALVARS.REGIME_KEY) : null;

        View fragment = inflater.inflate(R.layout.fragment_overview, container, false);

        TextView idView = (TextView) fragment.findViewById(R.id.regime_id);
        TextView nameView = (TextView) fragment.findViewById(R.id.regime_name);
        TextView timeView = (TextView) fragment.findViewById(R.id.regime_time);
        TextView descriptionView = (TextView) fragment.findViewById(R.id.regime_description);

        idView.setText(regime.getId() + "");
        nameView.setText(regime.getName());
        timeView.setText(regime.getSetTime() + "");
        descriptionView.setText(regime.getDescription());

        Log.v(LOG_TAG, "Fragment View created for Regime ID: " + regime.getId());

        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        aAdapter = new ExerciseAdapter(OverviewFragment.this.getActivity(), regime.getExercises());
        OverviewFragment.this.setListAdapter(aAdapter);

        Log.v(LOG_TAG, "List Adapter setted for Regime ID: " + regime.getId());
    }
}
