package com.example.activity;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShowOrderRecord extends BaseFragment {
	private RelativeLayout layoutLoading;
	private Button buttonBack;
	private TextView viewOrderNo;
	private TextView viewOrderTitle;
	private TextView viewOrderHall;
	private TextView viewOrderBuddhist;
	private TextView viewOrderJSC;
	private TextView viewOrderDate;
	private TextView viewDesireContent;
	private Button buttonAlipay;
	private Button buttonUnionpay;
	private Button buttonMobilepay;
	private boolean showLoading = false;
	private JSONObject temple = new JSONObject();
	private Bundle bundle;

	@SuppressLint("InflateParams")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sinha) {
		View view = inflater.inflate(R.layout.show_order_record, null);
		
		this.bundle = getArguments();

		this.layoutLoading = (RelativeLayout) view.findViewById(R.id.loading);
		this.buttonBack = (Button) view.findViewById(R.id.show_order_record_title_back_button);
		this.buttonBack.setOnClickListener(this);
		this.viewOrderNo = (TextView) view.findViewById(R.id.show_order_record_order_no_text);
		this.viewOrderTitle = (TextView) view.findViewById(R.id.show_order_record_order_title_text);
		this.viewOrderHall = (TextView) view.findViewById(R.id.show_order_record_order_hall_text);
		this.viewOrderBuddhist = (TextView) view.findViewById(R.id.show_order_record_order_buddhist_text);
		this.viewOrderJSC = (TextView) view.findViewById(R.id.show_order_record_order_JSC_text);
		this.viewOrderDate = (TextView) view.findViewById(R.id.show_order_record_order_date_text);
		this.viewDesireContent = (TextView) view.findViewById(R.id.show_order_record_order_content_text);
		this.buttonAlipay = (Button) view.findViewById(R.id.show_order_record_pay_alipay_button);
		this.buttonAlipay.setOnClickListener(this);
		this.buttonUnionpay = (Button) view.findViewById(R.id.show_order_record_pay_unionpay_button);
		this.buttonUnionpay.setOnClickListener(this);
		this.buttonMobilepay = (Button) view.findViewById(R.id.show_order_record_pay_mobilepay_button);
		this.buttonMobilepay.setOnClickListener(this);

		if (null != this.bundle) {
			this.viewOrderNo.setText("订单编号：" + this.bundle.getString("order_no"));
			String orderTitle = this.bundle.getString("desirer");
			switch (this.bundle.getInt("desire_type")) {
			case 1:
				orderTitle += " 祈求" + getString(R.string.desire_1);
				break;
			case 2:
				orderTitle += " 祈求" + getString(R.string.desire_2);
				break;
			case 3:
				orderTitle += " 祈求" + getString(R.string.desire_3);
				break;
			case 4:
				orderTitle += " 祈求" + getString(R.string.desire_4);
				break;
			case 5:
				orderTitle += " 祈求" + getString(R.string.desire_5);
				break;
			case 6:
				orderTitle += " 祈求" + getString(R.string.desire_6);
				break;
			case 7:
				orderTitle += " 祈求" + getString(R.string.desire_7);
				break;
			default:
				break;
			}
			this.viewOrderTitle.setText(orderTitle);
			try {
				this.temple = new JSONObject(bundle.getString("temple"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			this.viewOrderHall.setText(this.temple.optString("templename", ""));
			this.viewOrderBuddhist.setText(this.temple.optString("buddhistname", ""));
			this.viewOrderJSC.setText(this.bundle.getString("JSC"));
			this.viewOrderDate.setText(this.bundle.getString("date"));
			this.viewDesireContent.setText(this.bundle.getString("desire_content"));
		}
		
		return view;
	}

	public void onActivityCreated(Bundle sinha) {
		super.onActivityCreated(sinha);
		loadTemple();
	}

	private void loadTemple() {
		if (Utils.CheckNetwork()) {
			showLoading();
			showLoading();
		} else {
			Utils.ShowToast(getActivity(), R.string.dialog_network_check_content);
		}
	}

	private void showLoading() {
		Utils.animView(this.layoutLoading, !this.showLoading);
		this.showLoading = !this.showLoading;
	}

	
}