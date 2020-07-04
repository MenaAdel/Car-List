package com.example.softxperttask.presentation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.softxperttask.R;

public class CarsActivity extends AppCompatActivity {

    private CarsViewModel viewModel;
    private ProgressBar progressBar;
    private ProgressBar progressBarTop;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private CarsAdapter carsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initViewModel();
        observeViewModelData();
        viewModel.fetchCars(1);
    }

    private void initView() {
        progressBar = findViewById(R.id.progress);
        refreshLayout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.recycler_car);
        progressBarTop = findViewById(R.id.my_progressBar);
        carsAdapter = new CarsAdapter(this);

        setUpRecycler();
        setupSwipeRefresher();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders
                .of(this)
                .get(CarsViewModel.class);
    }

    private void observeViewModelData() {
        viewModel.loading.observe(this, loading -> {
            if (loading) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        });

        viewModel.error.observe(this, error -> {
            if (error) {
                Toast.makeText(this, "Unexpected error", Toast.LENGTH_LONG).show();
            }
            progressBarTop.setVisibility(View.GONE);
        });

        viewModel.data.observe(this, data -> {
            progressBar.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            carsAdapter.appendToList(data);

        });

        viewModel.newData.observe(this, data -> {
            progressBarTop.setVisibility(View.GONE);
            refreshLayout.setRefreshing(false);
            carsAdapter.appendToList(data);
        });
    }

    private void setUpRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(carsAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (limitReached(manager)){
                    if (viewModel.isLoadingData())
                        progressBarTop.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private boolean limitReached(LinearLayoutManager manager) {
        int visibleItemCount = manager.getChildCount();
        int totalItemCount = manager.getItemCount();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

        return (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0;

    }

    private void setupSwipeRefresher() {
        refreshLayout.setOnRefreshListener(() -> {
            progressBar.setVisibility(View.VISIBLE);
            progressBarTop.setVisibility(View.GONE);
            carsAdapter.clearList();
            viewModel.reloadReviewData();
        });
    }
}

