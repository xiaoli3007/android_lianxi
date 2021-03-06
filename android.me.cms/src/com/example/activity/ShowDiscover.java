package com.example.activity;

import org.json.JSONArray;
import org.json.JSONException;

import com.example.adapter.ShowDiscoverAdapter;
import com.example.utils.Utils;
import com.example.cms.BaseFragment;
import com.example.cms.R;
import com.example.cms.ShangXiang;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ShowDiscover extends BaseFragment {
	private Button buttonBack;
	private RelativeLayout layoutLoading;
	private Button buttonCreateOrder;
	private ListView viewList;
	private ShowDiscoverAdapter adapterShowDiscoverList;
	private boolean showLoading = false;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sinha) {
		View view = inflater.inflate(R.layout.show_discover, null);

		this.buttonBack = (Button) view.findViewById(R.id.show_discover_title_back_button);
		this.buttonBack.setOnClickListener(this);
		this.layoutLoading = (RelativeLayout) view.findViewById(R.id.loading);
		this.buttonCreateOrder = (Button) view.findViewById(R.id.show_discover_create_order_button);
		this.buttonCreateOrder.setOnClickListener(this);
		this.viewList = (ListView) view.findViewById(R.id.show_discover_container);

		return view;
	}

	public void onActivityCreated(Bundle sinha) {
		super.onActivityCreated(sinha);
		this.adapterShowDiscoverList = new ShowDiscoverAdapter(getActivity());
		this.viewList.setAdapter(this.adapterShowDiscoverList);
		loadDiscover();
	}

	private void loadDiscover() {
		if (Utils.CheckNetwork()) {
			showLoading();
			try {
				this.adapterShowDiscoverList.data = new JSONArray("[{\"name\":\"寂静的风\", \"date\":\"刚刚在加持祝福\"}, {\"name\":\"机器猫\", \"date\":\"1天前加持祝福\"}, {\"name\":\"机器猫\", \"date\":\"1天前在五台山还愿\"}, {\"name\":\"机器猫\", \"date\":\"1天前在五台山还愿\"}, {\"name\":\"机器猫\", \"date\":\"1天前在五台山还愿\"}]");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.adapterShowDiscoverList.notifyDataSetChanged();
			showLoading();
		} else {
			Utils.ShowToast(getActivity(), R.string.dialog_network_check_content);
		}
	}

	private void showLoading() {
		Utils.animView(this.layoutLoading, !this.showLoading);
		this.showLoading = !this.showLoading;
	}

	@Override
	public void onClick(View v) {
		if (v == this.buttonBack) {
			getActivity().onBackPressed();
		}
		if (v == this.buttonCreateOrder) {
		}
	}
}