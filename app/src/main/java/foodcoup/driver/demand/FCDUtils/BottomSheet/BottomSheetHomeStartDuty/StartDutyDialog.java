package foodcoup.driver.demand.FCDUtils.BottomSheet.BottomSheetHomeStartDuty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import foodcoup.driver.demand.R;

public class StartDutyDialog extends BottomSheetDialogFragment {

    LinearLayout ll_gotIt;
    private DutyStarted dutyStarted;
    private int value = 0;

    public static StartDutyDialog newInstance() {
        return new StartDutyDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_bottom_sheet, container,
                false);

        // get the views and attach the listener

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        FindViewById(view);

        if (getActivity() instanceof DutyStarted)
            dutyStarted = (DutyStarted) getActivity();


        ll_gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                value = 1;
                if (dutyStarted != null)
                    dutyStarted.dutyStarted(value);
                dismiss();

            }
        });
    }

    private void FindViewById(View view) {

        ll_gotIt = view.findViewById(R.id.ll_gotIt);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public interface DutyStarted {
        void dutyStarted(int value);
    }
}