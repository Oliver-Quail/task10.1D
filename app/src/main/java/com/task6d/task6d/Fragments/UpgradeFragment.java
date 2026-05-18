package com.task6d.task6d.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.task6d.task6d.R;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class UpgradeFragment extends Fragment {


    Button purchaseStarter;
    Button purchaseIntermediate;
    Button purchaseAdvanced;

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

        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

            }
        };

        PendingPurchasesParams pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build();
        billingClient = BillingClient.newBuilder(requireContext())
                .setListener(purchasesUpdatedListener)
                .enableAutoServiceReconnection()
                .enablePendingPurchases(pendingPurchasesParams)
                .build();




        purchaseStarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
                    .setProductList(
                            ImmutableList.of(
                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId("android.test.purchased")
                                            .setProductType(BillingClient.ProductType.INAPP)
                                            .build()
                            )
                    ).build();

                ArrayList<ProductDetails> productDetailsList = new ArrayList<ProductDetails>();
                billingClient.queryProductDetailsAsync(queryProductDetailsParams, new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull QueryProductDetailsResult queryProductDetailsResult) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                            for(ProductDetails productDetails : queryProductDetailsResult.getProductDetailsList()) {
                                Activity activity = getActivity();
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams = ImmutableList.of(
                                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                                .setProductDetails(productDetails)
                                                .build()
                                );

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                                        .setProductDetailsParamsList(productDetailsParams)
                                        .build();
                                BillingResult billingResult1 = billingClient.launchBillingFlow(activity, billingFlowParams);
                            }

                        }
                    }
                });






                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingServiceDisconnected() {

                    }

                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {

                        }
                    }
                });
            }
        });

    }



}