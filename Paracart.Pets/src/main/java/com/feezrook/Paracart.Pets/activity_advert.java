package com.feezrook.Paracart.Pets;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import android.widget.ImageButton;
import android.widget.Toast;

import com.feezrook.Paracart.Pets.direct.HideBar;
import com.feezrook.Paracart.Pets.util.IabBroadcastReceiver;
import com.feezrook.Paracart.Pets.util.IabBroadcastReceiver.IabBroadcastListener;
import com.feezrook.Paracart.Pets.util.IabResult;
import com.feezrook.Paracart.Pets.util.IabHelper;
import com.feezrook.Paracart.Pets.util.Inventory;
import com.feezrook.Paracart.Pets.util.Purchase;
import com.google.android.gms.internal.ads;


public class activity_advert extends AppCompatActivity  {//implements BillingProcessor.IBillingHandler

    private ImageButton btnYes, btnNo;
    static final int RC_REQUEST = 10001;
    public Context context;

    private static final String TAG = "LoGs";
    IabHelper mHelper;
    static final String ITEM_SKU = "com.feezrook.livesunlimited";
    private String base64EncodedPublicKey =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAijdU5k8MHqTgdoiNnRrj+Kf3hcKDasF82fwkY8WIBBcwK7IPsnV/sv9iTqgrA3+EPCzfXJAlKO9Eq6x0jp7boCzwGelPiYt1OXh4rdvk/Vo8nxQiSlfhLKx/ILSiwb1A8zpEivJYRiigZJ6aSGlVV+UC38VJJnXSzem0YkOe8yQtTlIBJcw2hBRjDmgPmILhi97zcfS977trCcm5uhqPZoeB79mUAE/DW55fDhupXrADvH0wRJGgbM3yVt/It4T+d8pe/uf83ICeUpKszrjY83u3fGAEgbW1j6WxJz/AXAYiWQfWNhZeyzjOvdspLt8yYclFQ2R4eOX7dZzX1OoDNQIDAQAB";

    @Override
    protected void onResume() {
        super.onResume();
        HideBar.hideSystemUI(getWindow().getDecorView());
    }


    private void billingInit() {
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // включаем дебагинг (в релизной версии ОБЯЗАТЕЛЬНО выставьте в false)
        mHelper.enableDebugLogging(false);

        // инициализируем; запрос асинхронен
        // будет вызван, когда инициализация завершится
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result)  {
                if (!result.isSuccess()) {
                    return;
                }
                // чекаем уже купленное
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }

    // Слушатель для востановителя покупок.
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (result.isFailure()) {
                Log.d(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

			/*
			 * Проверяются покупки.
			 * Обратите внимание, что надо проверить каждую покупку, чтобы убедиться, что всё норм!
			 * см. verifyDeveloperPayload().
			 */

            Purchase purchase = inventory.getPurchase(ITEM_SKU);
            PreferencesHelper.savePurchase(
                    context,
                    PreferencesHelper.Purchase.DISABLE_ADS,
                    purchase != null && verifyDeveloperPayload(purchase));


            //ads.show(!PreferencesHelper.isAdsDisabled());

        }
    };
    //обработчик для покупки
    private void buy()  {

        if(!PreferencesHelper.isAdsDisabled()){
			/* для безопасности сгенерьте payload для верификации. В данном примере просто пустая строка юзается.
			 * Но в реальном приложение подходить к этому шагу с умом. */
            String payload = "";
            mHelper.launchPurchaseFlow(this, ITEM_SKU, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        }
    }

    // слушатель завершения покупки
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                return;
            }

            if (purchase.getSku().equals(ITEM_SKU)) {
                // сохраняем в настройках, что отключили рекламу
                PreferencesHelper.savePurchase(
                        context,
                        PreferencesHelper.Purchase.DISABLE_ADS,
                        true);
                // отключаем рекламу
               // ads.show(!PreferencesHelper.isAdsDisabled());
            }

        }
    };

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
		/*
		 * TODO: здесь необходимо свою верификацию реализовать
		 * Хорошо бы ещё с использованием собственного стороннего сервера.
		 */
        return true;
    }

    @Override
    public void onDestroy() {
        if (mHelper != null)  {
            mHelper.dispose();
        }
        mHelper = null;
        super.onDestroy();
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        billingInit();

        btnYes = findViewById(R.id.yesButton);
        btnNo = findViewById(R.id.noButton);

        context = this;

        btnNo.setImageResource(R.drawable.nopress);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                btnNo.setImageResource(R.drawable.no);
                btnNo.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                btnNo.setImageResource(R.drawable.nopress);
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        btnNo.setImageResource(R.drawable.no);
                                        Intent intent = new Intent("com.feezrook.Paracart.Pets.activity_game_hearts");
                                        startActivity(intent);
                                    }

                                };
                                Handler handler = new Handler();
                                handler.postDelayed(runnable, 50);

                            }
                        }
                );
            }

        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 5000);





        btnYes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btnYes.setImageResource(R.drawable.yespress);
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                btnYes.setImageResource(R.drawable.yes);
                                buy();
                            }

                        };
                        Handler handler = new Handler();
                        handler.postDelayed(runnable, 50);

                    }
                }
        );

        HideBar.hideSystemUI(getWindow().getDecorView());
        //ads.show(!PreferencesHelper.isAdsDisabled());
    }


}
