package foodcoup.driver.demand.FCDFragment.FCDWeeklyEarnings;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import foodcoup.driver.demand.FCDPojo.FCDWeeklyEarnings.EarningParticularWeekObject;
import foodcoup.driver.demand.FCDPojo.FCDWeeklyEarnings.IncentivesParticularWeekObject;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FCD_ParticularWeekEarnings extends Fragment {


    RecyclerView rv_earnings, rv_incentives;
    AC_Textview txt_earnings, txt_incentives;

    EarningsAdapter earningsAdapter;
    IncentivesAdapter incentivesAdapter;

    ArrayList<EarningParticularWeekObject> earningParticularWeekObjects = new ArrayList<>();
    ArrayList<IncentivesParticularWeekObject> incentivesParticularWeekObjects = new ArrayList<>();

    public FCD_ParticularWeekEarnings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fcd__particular_week_earnings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FindViewById(view);

        earningsAdapter = new EarningsAdapter(getActivity(), earningParticularWeekObjects);
        rv_earnings.setAdapter(earningsAdapter);
        rv_earnings.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        incentivesAdapter = new IncentivesAdapter(getActivity(), incentivesParticularWeekObjects);
        rv_incentives.setAdapter(incentivesAdapter);
        rv_incentives.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        txt_earnings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_earnings.setVisibility(View.VISIBLE);
                earningsAdapter.loadData();
                txt_earnings.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.white));
                txt_earnings.setBackground(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.rec_bg_green_layout));
                txt_incentives.setTextColor(getActivity().getResources().getColor(R.color.colordarkgrey));
                txt_incentives.setBackground(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.rec_bg_stroke_green_layout));
                rv_incentives.setVisibility(View.GONE);
            }
        });


        txt_incentives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rv_earnings.setVisibility(View.GONE);
                incentivesAdapter.loadData();
                txt_incentives.setTextColor(Objects.requireNonNull(getActivity()).getResources().getColor(R.color.white));
                txt_incentives.setBackground(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.rec_bg_green_layout));
                txt_earnings.setTextColor(getActivity().getResources().getColor(R.color.colordarkgrey));
                txt_earnings.setBackground(Objects.requireNonNull(getActivity()).getResources().getDrawable(R.drawable.rec_bg_stroke_green_layout));
                rv_incentives.setVisibility(View.VISIBLE);
            }
        });
    }

    private void FindViewById(View view) {

        txt_earnings = view.findViewById(R.id.txt_earnings);
        txt_incentives = view.findViewById(R.id.txt_incentives);

        rv_earnings = view.findViewById(R.id.rv_earnings);
        rv_incentives = view.findViewById(R.id.rv_incentives);


        EarningParticularWeekObject earnparticularWeekObject = new EarningParticularWeekObject();
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);
        earnparticularWeekObject.setD_image("");
        earnparticularWeekObject.setD_name("");
        earningParticularWeekObjects.add(earnparticularWeekObject);


        IncentivesParticularWeekObject inParticularWeekObject = new IncentivesParticularWeekObject();
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
        inParticularWeekObject.setD_image("");
        inParticularWeekObject.setD_name("");
        incentivesParticularWeekObjects.add(inParticularWeekObject);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class EarningsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_ITEM = 1;

        private final FragmentActivity activity;
        private final ArrayList<EarningParticularWeekObject> earningParticularWeekObjects;

        EarningsAdapter(FragmentActivity activity, ArrayList<EarningParticularWeekObject> earningParticularWeekObjects) {
            this.activity = activity;
            this.earningParticularWeekObjects = earningParticularWeekObjects;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            if (viewType == TYPE_HEADER) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header_earings_item, parent, false);
                return new HeaderViewHolder(layoutView);
            } else if (viewType == TYPE_ITEM) {
                View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_earings_item, parent, false);
                return new ItemViewHolder(layoutView);
            }
            throw new RuntimeException("No match for " + viewType + ".");
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return earningParticularWeekObjects.size() + 1;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position))
                return TYPE_HEADER;
            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }

        void loadData() {
            notifyDataSetChanged();
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            ItemViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private class IncentivesAdapter extends RecyclerView.Adapter<IncentivesAdapter.ViewHolder> {
        private final ArrayList<IncentivesParticularWeekObject> incentivesParticularWeekObjects;
        private final FragmentActivity activity;

        IncentivesAdapter(FragmentActivity activity, ArrayList<IncentivesParticularWeekObject> incentivesParticularWeekObjects) {
            this.activity = activity;
            this.incentivesParticularWeekObjects = incentivesParticularWeekObjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.layout_incentives_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return incentivesParticularWeekObjects.size();
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        void loadData() {

            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
