package com.electrofire.playpkm.ui.Components

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdReward(
    private val context: Context
) {
    private var rewardedAd: RewardedAd? = null

    fun loadAd(onLoaded: () -> Unit = {}) {
        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    onLoaded()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    rewardedAd = null
                }
            }
        )
    }

    fun showAd(
        activity: Activity?,
        onReward: () -> Unit
    ) {

        if (activity == null || rewardedAd == null) return

        rewardedAd?.show(activity) { rewardItem ->
            onReward()
            rewardedAd = null
            loadAd()
        }
    }

    fun isLoaded(): Boolean = rewardedAd != null
}
