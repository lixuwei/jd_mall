package com.aries.main.ui.coordinator

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.airbnb.mvrx.MavericksView
import com.airbnb.mvrx.viewModel
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.util.PixelUtil
import com.aries.main.R
import com.aries.main.ui.coordinator.adapter.BannerAdapter
import com.aries.main.ui.coordinator.fragment.WaterfallFragment
import com.aries.main.ui.coordinator.state.ScrollTabState
import com.aries.main.ui.coordinator.viewmodel.ScrollTabViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.AlphaPageTransformer
import kotlinx.android.synthetic.main.layout_tab_waterfall.*

@Route(path = RouterPaths.TAB_WATERFALL_ACTIVITY)
class ScrollTabActivity: BaseActivity(R.layout.layout_tab_waterfall), MavericksView {
    private val viewModel: ScrollTabViewModel by viewModel()

    override fun initView() {
        addStateChangeListener()
    }

    override fun initData() {
        viewModel.queryBanner()
        viewModel.queryScrollTab()
    }

    override fun invalidate() {}

    private fun addStateChangeListener() {
        viewModel.onEach(
            ScrollTabState::bannerList,
            ScrollTabState::tabs
        ) { bannerList, tabs ->
            run {
                if (bannerList.isNotEmpty()) {
                    showBannerView(bannerList)
                }
                if (bannerList.isNotEmpty()) {
                    showBannerView(bannerList)
                }
                if (tabs.isNotEmpty()) {
                    showTabLayout(tabs)
                }
            }
        }
    }

    private fun showBannerView(data: List<BannerBean>) {
        tabWaterfallBanner.run {
            adapter = BannerAdapter(data)
            setBannerRound(PixelUtil.toPixelFromDIP(3f).toFloat())
            setIndicator(CircleIndicator(this.context))
            setPageTransformer(AlphaPageTransformer())
            setIndicatorSelectedColorRes(com.aries.home.R.color.indicator_selected_color)
            setLoopTime(6000L)
            isAutoLoop(true)
        }
    }

    private fun showTabLayout(list: List<String>) {
        viewPager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount(): Int = list.size
            override fun createFragment(position: Int): Fragment {
                return WaterfallFragment()
            }
        }
        viewPager.offscreenPageLimit = list.size
        TabLayoutMediator(tabLayout, viewPager) {
            tab, position -> tab.text = list[position]
        }.attach()
    }
}