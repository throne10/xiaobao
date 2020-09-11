package com.xiaobao.good.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xiaobao.good.ClientActivity;
import com.xiaobao.good.R;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.schedule.ScheduleActivity;
import com.xiaobao.good.ui.recycler.model.ItemClient;
import com.xiaobao.good.ui.recycler.provider.ItemClientProvider;
import com.xiaobao.good.widget.SwipeRecyclerView;
import com.xiaobao.good.widget.recyclerview.LoadMoreFooterModel;
import com.xiaobao.good.widget.recyclerview.LoadMoreFooterViewHolderProvider;
import com.xiaobao.good.widget.recyclerview.OnClickByViewIdListener;
import com.xiaobao.good.widget.recyclerview.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientFragment extends Fragment
        implements LoadMoreFooterModel.LoadMoreListener,
                OnClickByViewIdListener,
                SwipeRefreshLayout.OnRefreshListener,
                SwipeRecyclerView.OnRightClickListener,
                SwipeRecyclerView.OnItemClickListener {

    private static final String TAG = "fragment_CF";

    @OnClick(R.id.iv_add)
    public void clickAddClient() {
        Intent intent = new Intent(getActivity(), ClientActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @BindView(R.id.tv_visit_count)
    TextView tvVisitCount;

    @BindView(R.id.tv_m_visit_count)
    TextView tvMouthVisitCount;

    @BindView(R.id.tv_last_visit_client)
    TextView tvLastVisitClient;

    @BindView(R.id.tv_last_visit_time)
    TextView tvLastVisitTime;

    @BindView(R.id.tv_visit_target)
    TextView tvVisitTarget;

    @BindView(R.id.tv_visit_address)
    TextView tvVisitAddress;

    @BindView(R.id.refresh)
    SwipeRefreshLayout srlRefresh;

    @BindView(R.id.recyclerView)
    SwipeRecyclerView rvRecyclerView;

    private Unbinder unbinder;
    private RecyclerAdapter mAdapter;
    private LinearLayoutManager layoutManager;
    private Handler handler = new Handler();
    private LoadMoreFooterModel mLoadMoreFooterModel;
    private Clients.DataBean dataBean;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.client_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated");
        initViews();
        registerModel();
        initEvent();
        getClients();
    }

    private void initViews() {
        mAdapter = new RecyclerAdapter(getActivity());
        srlRefresh.setColorSchemeResources(
                R.color.color_blue, R.color.color_red, R.color.color_green, R.color.color_orange);
        layoutManager = new LinearLayoutManager(getActivity());
        rvRecyclerView.setLayoutManager(layoutManager);
        rvRecyclerView.setAdapter(mAdapter);
    }

    private void registerModel() {
        mAdapter.register(ItemClient.class, new ItemClientProvider(getActivity()));
        mAdapter.register(LoadMoreFooterModel.class, new LoadMoreFooterViewHolderProvider());
    }

    private void initEvent() {
        mLoadMoreFooterModel = new LoadMoreFooterModel();
        mLoadMoreFooterModel.setLoadMoreListener(this);
        mAdapter.setOnClickByViewIdListener(this);
        srlRefresh.setOnRefreshListener(this);
        rvRecyclerView.setRightClickListener(this);
        rvRecyclerView.setItemListener(this);
    }

    private void getClients() {
        Call<Clients> mResponseBody = RetrofitUtils.getService().getClients("4");
        mResponseBody.enqueue(
                new Callback<Clients>() {
                    // 请求成功时回调
                    @Override
                    public void onResponse(Call<Clients> call, Response<Clients> response) {
                        LogUtil.i(TAG, response.body().getData() + "");
                        mAdapter.clearData();
                        itemList = null;
                        itemList = new ArrayList<>();
                        dataBean = response.body().getData();
                        dataFilter();
                        initData();
                    }

                    // 请求失败时候的回调
                    @Override
                    public void onFailure(Call<Clients> call, Throwable throwable) {
                        LogUtil.i(TAG, "连接失败");
                    }
                });
    }

    private void initData() {
        Clients.DataBean.LatestRecordsBean client = dataBean.getLatestRecords();
        tvVisitCount.setText(String.valueOf(dataBean.getTotalNum()));
        tvMouthVisitCount.setText(String.valueOf(dataBean.getCurMonthNum()));
        tvLastVisitClient.setText(client.getClient().getClient_name());
        tvLastVisitTime.setText(client.getVisit_time());
        tvVisitTarget.setText(client.getPurpose());
        tvVisitAddress.setText(client.getSign_address());
    }

    private List<ItemClient> itemList;

    private void dataFilter() {
        for (Clients.DataBean.ClientsBean bean : dataBean.getClients()) {
            ItemClient model = new ItemClient();
            model.clientsBean = bean;
            itemList.add(model);
        }
        updateData();
    }

    private boolean hasMore;

    private void updateData() {
        getActivity()
                .runOnUiThread(
                        () -> {
                            if (itemList.size() < 10) {
                                hasMore = false;
                                mAdapter.addData(itemList);
                                mAdapter.removeFooter(mLoadMoreFooterModel);
                            } else if (itemList.size() >= 10) {
                                hasMore = true;
                                mAdapter.addData(itemList);
                                mAdapter.addFooter(mLoadMoreFooterModel);
                            }
                            srlRefresh.setRefreshing(false);
                        });
    }

    @Override
    public void onLoadMore() {
        LogUtil.d(TAG, "onLoadMore hasMore : " + hasMore);
        if (hasMore) {
            mLoadMoreFooterModel.canLoadMore();
            getClients();
        }
    }

    @Override
    public void clickByViewId(View view, Object o, int position) {}

    @Override
    public void onRefresh() {
        getClients();
    }

    @Override
    public void onRightClick(int position, String id) {
        deleteItem(dataBean.getClients().get(position));
    }

    public void deleteItem(Clients.DataBean.ClientsBean bean) {
        Call<Clients> mResponseBody = RetrofitUtils.getService().deleteClient(bean.getClient_id());
        mResponseBody.enqueue(
                new Callback<Clients>() {
                    // 请求成功时回调
                    @Override
                    public void onResponse(Call<Clients> call, Response<Clients> response) {
                        try {
                            LogUtil.d(TAG, response.body().getData().toString() + "");
                        } catch (Exception e) {
                            LogUtil.e(TAG, "deleteClient Exception : " + e.toString());
                        }
                        if (response.isSuccessful()) {
                            getClients();
                        } else {
                            Toast.makeText(
                                            getActivity(),
                                            "删除失败：" + response.message(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }

                    // 请求失败时候的回调
                    @Override
                    public void onFailure(Call<Clients> call, Throwable throwable) {
                        LogUtil.i(TAG, "连接失败");
                    }
                });
    }

    @Override
    public void onItemClick(int position, String id) {
        LogUtil.d(TAG, "onItemClick:" + position);
        Intent intent = new Intent(getActivity(), ScheduleActivity.class);
        Gson gson = new Gson();
        intent.putExtra("ClientBean", gson.toJson(dataBean.getClients().get(position)));
        getActivity().startActivity(intent);
    }
}
