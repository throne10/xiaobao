package com.xiaobao.good.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaobao.good.ClientActivity;
import com.xiaobao.good.R;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.ui.recycler.model.ItemClient;
import com.xiaobao.good.ui.recycler.provider.ItemClientProvider;
import com.xiaobao.good.widget.recyclerview.LoadMoreFooterModel;
import com.xiaobao.good.widget.recyclerview.LoadMoreFooterViewHolderProvider;
import com.xiaobao.good.widget.recyclerview.OnClickByViewIdListener;
import com.xiaobao.good.widget.recyclerview.RecyclerAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
                SwipeRefreshLayout.OnRefreshListener {

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
    RecyclerView rvRecyclerView;

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
        super.onViewCreated(view, savedInstanceState);

        initViews();
        registerModel();
        initEvent();
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
    }

    private void getClients() {
        Call<Clients> mResponseBody = RetrofitUtils.getService().getClients("4");
        mResponseBody.enqueue(
                new Callback<Clients>() {
                    // 请求成功时回调
                    @Override
                    public void onResponse(Call<Clients> call, Response<Clients> response) {
                        mAdapter.clearData();
                        dataBean = response.body().getData();
                        dataFilter();
                        initData();
                        // 请求处理,输出结果
                        LogUtil.i(TAG, response.body().getData() + "");
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
}