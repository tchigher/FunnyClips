package com.victor.clips.fragment

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.victor.clips.R
import com.victor.clips.MainActivity
import android.support.v7.widget.RecyclerView
import android.graphics.Color
import com.victor.clips.util.Constant
import kotlinx.android.synthetic.main.content_main.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.victor.clips.VideoDetailActivity
import com.victor.clips.adapter.RankingAdapter
import com.victor.clips.data.HomeItemInfo
import com.victor.clips.data.TrendingReq
import com.victor.clips.presenter.RankingPresenterImpl
import com.victor.clips.util.DeviceUtils
import com.victor.clips.util.WebConfig
import com.victor.clips.view.RankingView
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.fragment_monthly_ranking.*
import kotlinx.android.synthetic.main.fragment_total_ranking.*
import kotlinx.android.synthetic.main.fragment_weekly_ranking.*


/*
 * -----------------------------------------------------------------
 * Copyright (C) 2018-2028, by Victor, All rights reserved.
 * -----------------------------------------------------------------
 * File: TotalRankingFragment.java
 * Author: Victor
 * Date: 2018/8/30 15:40
 * Description: 
 * -----------------------------------------------------------------
 */
class TotalRankingFragment : BaseFragment(),AdapterView.OnItemClickListener,RankingView {

    var rankingAdapter: RankingAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    var rankingPresenter: RankingPresenterImpl? = null

    companion object {
        fun newInstance(): TotalRankingFragment {
            return newInstance(0)
        }
        fun newInstance(id: Int): TotalRankingFragment {
            val fragment = TotalRankingFragment()
            val bundle = Bundle()
            bundle.putInt(ID_KEY, id)
            fragment.setArguments(bundle)
            return fragment
        }
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_total_ranking
    }
    override fun handleBackEvent(): Boolean {
        return false
    }
    override fun freshFragData() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialize()
        initData()
    }

    fun initialize () {
        (activity as MainActivity).toolbar.setTitle("Total ranking")

        rankingPresenter = RankingPresenterImpl(this)

        linearLayoutManager = mRvTotalRanking.layoutManager as LinearLayoutManager

        mRvTotalRanking.setHasFixedSize(true)

        rankingAdapter = RankingAdapter(activity!!,this)
        rankingAdapter?.setHeaderVisible(false)
        rankingAdapter?.setFooterVisible(false)
        mRvTotalRanking.adapter = rankingAdapter


        mRvTotalRanking.addOnScrollListener((activity as MainActivity).OnScrollListener())

    }

    fun initData () {
        sendMonthlyRankingRequest()
    }

    fun sendMonthlyRankingRequest () {
        rankingPresenter?.sendRequest(String.format(WebConfig.getRequestUrl(WebConfig.HOT_TOTAL_RANKING_URL),
                DeviceUtils.getPhoneModel()),null,null)
    }

    override fun OnRanking(data: Any?, msg: String) {
        var totalRankingReq = data!! as TrendingReq
        rankingAdapter?.add(totalRankingReq.itemList)
        rankingAdapter?.notifyDataSetChanged()
    }

    override fun onItemClick(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
        VideoDetailActivity.intentStart(activity as AppCompatActivity,
                rankingAdapter?.getItem(position) as HomeItemInfo,
                view?.findViewById(R.id.mIvRankingPoster) as View,
                getString(R.string.transition_video_img))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rankingPresenter?.detachView()
        rankingPresenter = null
    }

}