package com.task6d.task6d.Fragments;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryProductDetailsResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.button.ButtonOptions;
import com.google.android.gms.wallet.button.PayButton;
import com.google.android.gms.wallet.contract.TaskResultContracts;
import com.task6d.task6d.R;
import com.google.common.collect.ImmutableList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class UpgradeFragment extends Fragment {


    PayButton purchaseStarter;
    PayButton purchaseIntermediate;
    PayButton purchaseAdvanced;

    private PurchasesUpdatedListener purchasesUpdatedListener;

    private BillingClient billingClient;

    public UpgradeFragment() {
        // Required empty public constructor
    }

    public static UpgradeFragment newInstance() {
        UpgradeFragment fragment = new UpgradeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upgrade, container, false);
    }





    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        purchaseStarter = view.findViewById(R.id.purchase_starter);
        purchaseIntermediate = view.findViewById(R.id.purchase_intermediate);
        purchaseAdvanced = view.findViewById(R.id.purchase_advanced);

        try {
            JSONObject baseCardPaymentMethod = new JSONObject()
                    .put("type", "CARD")
                    .put(
                            "parameters",
                            new JSONObject()
                                    .put(
                                            "allowedCardNetworks",
                                            new JSONArray(Arrays.asList("VISA", "MASTERCARD"))
                                    )
                                    .put(
                                            "allowedAuthMethods",
                                            new JSONArray(Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS"))
                                    )
                    );

            ButtonOptions buttonOptions = ButtonOptions.newBuilder()
                    .setAllowedPaymentMethods(getAllowedPaymentMethods())
                    .build();
            purchaseStarter.initialize(buttonOptions);
            purchaseAdvanced.initialize(buttonOptions);
            purchaseIntermediate.initialize(buttonOptions);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }




        ActivityResultLauncher<Task<PaymentData>> paymentDataLauncher =
                registerForActivityResult(
                        new TaskResultContracts.GetPaymentDataResult(),
                        taskResult -> {
                            switch (taskResult.getStatus().getStatusCode()) {

                                case CommonStatusCodes.SUCCESS:
                                    PaymentData paymentData = taskResult.getResult();

                                    if (paymentData != null) {
                                        Log.i("Google Pay result:", paymentData.toJson());

                                        // Handle the result
                                    }
                                    break;

                                case CommonStatusCodes.CANCELED:
                                    // The user canceled
                                    break;

                                case AutoResolveHelper.RESULT_ERROR:
                                    // The API returned an error
                                    // Status status = taskResult.getStatus();
                                    break;

                                case CommonStatusCodes.INTERNAL_ERROR:
                                    // Handle other unexpected errors
                                    break;
                            }
                        }
                );


        purchaseStarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build();

                PaymentsClient paymentsClient = Wallet.getPaymentsClient(getActivity(), walletOptions);

                try {
                    JSONObject merchantInfo = new JSONObject()
                            .put("merchantName", "Example Merchant")
                            .put("merchantId", "01234567890123456789");

                    JSONObject transactionInfo = new JSONObject()
                            .put("totalPrice", "123.45")
                            .put("totalPriceStatus", "FINAL")
                            .put("currencyCode", "USD");

                    JSONObject tokenizationSpecification = new JSONObject()
                            .put("type", "PAYMENT_GATEWAY")
                            .put(
                                    "parameters",
                                    new JSONObject(
                                            new HashMap<String, Object>() {{
                                                put("gateway", "Adyen");
                                                put("gatewayMerchantId", "adyen");
                                            }}
                                    )
                            );

                    JSONObject baseCardPaymentMethod = new JSONObject()
                            .put("type", "CARD")
                            .put("tokenizationSpecification", tokenizationSpecification)
                            .put(
                                    "parameters",
                                    new JSONObject()
                                            .put(
                                                    "allowedCardNetworks",
                                                    new JSONArray(Arrays.asList("VISA", "MASTERCARD"))
                                            )
                                            .put(
                                                    "allowedAuthMethods",
                                                    new JSONArray(Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS"))
                                            )
                            );
                    JSONObject googlePayBaseConfiguration = new JSONObject()
                            .put("apiVersion", 2)
                            .put("apiVersionMinor", 0)
                            .put(
                                    "allowedPaymentMethods",
                                    new JSONArray(Arrays.asList(baseCardPaymentMethod))
                            );

                    JSONObject paymentDataRequestJson = new JSONObject(googlePayBaseConfiguration.toString())
                            .put(
                                    "allowedPaymentMethods",
                                    new JSONArray().put(baseCardPaymentMethod)
                            )
                            .put("transactionInfo", transactionInfo)
                            .put("merchantInfo", merchantInfo);



                    PaymentDataRequest paymentDataRequest = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());

                    Task<PaymentData> task = paymentsClient.loadPaymentData(paymentDataRequest);

                    task.addOnCompleteListener(result -> {
                        paymentDataLauncher.launch(result);
                    });

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });



    }

    private String getAllowedPaymentMethods() {
        try {
            JSONObject baseCardPaymentMethod = new JSONObject()
                    .put("type", "CARD")
                    .put(
                            "parameters",
                            new JSONObject()
                                    .put(
                                            "allowedCardNetworks",
                                            new JSONArray(Arrays.asList("VISA", "MASTERCARD"))
                                    )
                                    .put(
                                            "allowedAuthMethods",
                                            new JSONArray(Arrays.asList("PAN_ONLY", "CRYPTOGRAM_3DS"))
                                    )
                    );

            JSONArray allowedPaymentMethodsArray = new JSONArray();
            allowedPaymentMethodsArray.put(baseCardPaymentMethod);

            return allowedPaymentMethodsArray.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "[]";
        }
    }



}