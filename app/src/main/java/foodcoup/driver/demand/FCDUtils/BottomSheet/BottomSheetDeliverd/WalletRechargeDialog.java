package foodcoup.driver.demand.FCDUtils.BottomSheet.BottomSheetDeliverd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import foodcoup.driver.demand.FCDViews.AC_Edittext;
import foodcoup.driver.demand.FCDViews.AC_Textview;
import foodcoup.driver.demand.FCDViews.FCD_Common;
import foodcoup.driver.demand.FCDViews.FCD_SharedPrefManager;
import foodcoup.driver.demand.FCDViews.FCD_URL;
import foodcoup.driver.demand.FCDViews.FCD_User;
import foodcoup.driver.demand.FCDViews.Utils;
import foodcoup.driver.demand.R;

public class WalletRechargeDialog extends BottomSheetDialogFragment {
    private Button payButton;
    private CardInputWidget cardInputWidget;
    private Stripe stripe;

    public static WalletRechargeDialog newInstance() {
        return new WalletRechargeDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_checkout, container,
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

        FindViewById(view);

        AccessCheck();

        payButton.setOnClickListener(v -> {
            payButton.setEnabled(false);
            pay();
        });

    }
    private void FindViewById(View view) {
        payButton = view.findViewById(R.id.payButton);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);

    }

    private void pay() {

        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();

        if (params == null) {
            return;
        }
        stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
            @Override
            public void onSuccess(@NonNull PaymentMethod result) {
                // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                //pay(result.id, null);
                String paymentid=result.id;
                Payment(paymentid);
                Log.d("gfhdfgdfg","dfgdfg"+result.id);
                Log.d("gfhdfgdfg","dfgdfg"+paymentid);
            }

            @Override
            public void onError(@NonNull Exception e) {
                e.printStackTrace();
                Log.d("dfghfdgdfg","dgsdf"+e);
            }
        });
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    private void AccessCheck() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, FCD_URL.ROOTSTRIPE,
                ServerResponse -> {

                    Log.d("ServerResponse", "Splash" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        FCD_Common.stripe_publickey = jsonResponse1.getString("stripe_publickey");

                        final Context applicationContext = getActivity().getApplicationContext();
                        PaymentConfiguration.init(applicationContext, FCD_Common.stripe_publickey);
                        stripe = new Stripe(applicationContext, FCD_Common.stripe_publickey);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            Log.d("dfgfdgfd","dfgsdfd"+value);

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                Log.d("fdgdfgfdg", "sdfgsdgs" + FCD_Common.token_type + " " + FCD_Common.access_token);
                params.put("Authorization", FCD_Common.token_type + " " + FCD_Common.access_token);
                // params.put("X-Requested-With", FC_Common.XMLCODE);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        requestQueue.getCache().clear();
    }

    private void Payment(String paymentid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FCD_URL.URL_WALLETPAYMENT,
                ServerResponse -> {

                    Log.d("ServerResponse", "dashboard12" + ServerResponse);
                    try {

                        JSONObject jsonResponse1 = new JSONObject(ServerResponse);
                        FCD_Common.status = jsonResponse1.getString("success");
                        if (FCD_Common.status.equalsIgnoreCase("1")) {
                            dismiss();
                            payButton.setEnabled(true);
                        }
                        else {
                            payButton.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        payButton.setEnabled(true);
                        Log.d("xcgsdgsdgsd", "dfhdf" + e);
                    }
                }, volleyError -> {
            String value = volleyError.toString();
            payButton.setEnabled(true);
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("paymentmethodid", paymentid);
                params.put("amount", "5000");
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
