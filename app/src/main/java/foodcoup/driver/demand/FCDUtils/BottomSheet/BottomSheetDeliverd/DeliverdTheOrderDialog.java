package foodcoup.driver.demand.FCDUtils.BottomSheet.BottomSheetDeliverd;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity;
import foodcoup.driver.demand.FCDFragment.FCD_LiveOrders;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

import static foodcoup.driver.demand.FCDActivity.FCDDashboardActivity.FCD_DashboardActivity.fragmentManager;

public class DeliverdTheOrderDialog extends BottomSheetDialogFragment {


    private AC_Textview txt_cancel ,txt_confirm ;

    public static DeliverdTheOrderDialog newInstance() {
        return new DeliverdTheOrderDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_deliver_complete, container,
                false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();
        FCD_User user = FCD_SharedPrefManager.getInstance(context).getUser();
        FCD_Common.id = String.valueOf(user.getid());
        FCD_Common.token_type = String.valueOf(user.gettoken_type());
        FCD_Common.access_token = String.valueOf(user.getaccess_token());
        Log.d("dfgdfgdfg","dfgdfgdfg"+FCD_Common.totalamt);
        Log.d("dfgdfgdfg","sdsdsf");
        FindViewById(view);

        txt_cancel.setOnClickListener(view12 -> dismiss());

        txt_confirm.setOnClickListener(view1 -> {
            ConfirmOrder();
        });

    }

    private void FindViewById(View view) {

        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_confirm = view.findViewById(R.id.txt_confirm);
    }


    private void ConfirmOrder() {
        Utils.playProgressBar(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_UPDATESTATUS,
                ServerResponse -> {

                    Log.d("ServerResponse", "PickedUp" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {

                            Utils.stopProgressBar();

                            dismiss();
                            FCD_Common.count=0;
                            FCD_Common.currentTask =  0;
                            FCD_LiveOrders liveOrderFragment = new FCD_LiveOrders();
                            FragmentTransaction fragmentTransactionLiveOrder = fragmentManager.beginTransaction();
                            fragmentTransactionLiveOrder.replace(R.id.content_frame, liveOrderFragment);
                            fragmentTransactionLiveOrder.commit();
                            FCD_DashboardActivity.txt_toolbar.setText(R.string.live_order);
                            FCD_DashboardActivity.txt_toolbarOrderNo.setVisibility(View.GONE);
                        }
                        else {
                            Utils.toast(getActivity(),getResources().getString(R.string.enable_location));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Log.d("dfgfdgfd","sgd"+value);
            // snackBar(value);
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", FCD_Common.orderid);
                params.put("order_status", "COMPLETED");
                params.put("latitude", String.valueOf(FCD_Common.latitude));
                params.put("longitude", String.valueOf(FCD_Common.longitude));
                params.put("total_pay", FCD_Common.totalamt);
                params.put("current_task", String.valueOf(FCD_Common.currentTask));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
