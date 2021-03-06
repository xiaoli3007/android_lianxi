package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.pipe.SinhaPipeClient;
import com.example.pipe.SinhaPipeMethod;
import com.example.spinnerwheel.AbstractWheel;
import com.example.spinnerwheel.AbstractWheelTextAdapter;
import com.example.spinnerwheel.OnWheelChangedListener;
import com.example.spinnerwheel.OnWheelScrollListener;
import com.example.utils.Utils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.example.cms.BaseFragment;
import com.example.cms.Consts;
import com.example.cms.R;
import com.example.cms.ShangXiang;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class CreateOrder extends BaseFragment implements OnTouchListener {
	private SinhaPipeClient httpClient;
	private SinhaPipeMethod httpMethod;
	private RelativeLayout layoutLoading;
	private ScrollView viewMain;
	private Button buttonBack;
	private ImageButton viewHallThumb;
	private ProgressBar viewHallThumbLoading;
	private ImageButton viewBuddhistThumb;
	private ProgressBar viewBuddhistThumbLoading;
	private TextView viewHallName;
	private TextView viewBuddhistName;
	private Button viewDesireType;
	private EditText viewDesirer;
	private EditText viewMobile;
	private boolean showLoading = false;
	private JSONObject temple = new JSONObject();
	private Bundle bundle;
	private int desireType = 0;

	private LinearLayout layoutSelectContent;
	private TextView buttonSelectContentShow;
	private Button buttonSelectContentCancel;
	private Button buttonSelectContentOK;
	private EditText viewDesireContent;
	private LinearLayout layoutSelectJSC;
	private TextView buttonSelectJSCShow;
	private Button buttonSelectJSCCancel;
	private Button buttonSelectJSCOK;
	private LinearLayout layoutSelectRegion;
	private TextView buttonSelectRegionShow;
	private Button buttonSelectRegionCancel;
	private Button buttonSelectRegionOK;
	private LinearLayout layoutSelectDate;
	private TextView buttonSelectDateShow;
	private Button buttonSelectDateCancel;
	private Button buttonSelectDateOK;

	private AbstractWheel selectContent;
	private ContentAdapter contentAdapter;
	private String[] contentData = new String[] {};
	private AbstractWheel selectJSC;
	private JSCAdapter JSCAdapter;
	private JSONArray jsonJSCs;
	private String[] JSCData = new String[] {};
	private AbstractWheel selectRegionProv;
	private RegionProvAdapter regionProvAdapter;
	private AbstractWheel selectRegionCity;
	private RegionCityAdapter regionCityAdapter;
	private JSONArray jsonRegions;
	private AbstractWheel selectDateYear;
	private DateYearAdapter dateYearAdapter;
	private AbstractWheel selectDateMonth;
	private DateMonthAdapter dateMonthAdapter;
	private AbstractWheel selectDateDay;
	private DateDayAdapter dateDayAdapter;
	private Calendar calendar;
	private int daySelected = 0;
	private SimpleDateFormat formatter;

	private Button buttonSubmit;

	private boolean isSubmiting = false;
	private boolean scrolling = false;

	@SuppressLint({ "InflateParams", "SimpleDateFormat" })
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle sinha) {
		View view = inflater.inflate(R.layout.create_order, null);

		this.bundle = getArguments();
		this.httpClient = new SinhaPipeClient();

		this.layoutLoading = (RelativeLayout) view.findViewById(R.id.loading);
		this.viewMain = (ScrollView) view.findViewById(R.id.create_order_main_layout);
		this.viewMain.setOnTouchListener(this);
		this.buttonBack = (Button) view.findViewById(R.id.create_order_title_back_button);
		this.buttonBack.setOnClickListener(this);
		this.viewHallThumb = (ImageButton) view.findViewById(R.id.create_order_hall_thumb_button);
		this.viewHallThumbLoading = (ProgressBar) view.findViewById(R.id.create_order_hall_thumb_loading);
		this.viewBuddhistThumb = (ImageButton) view.findViewById(R.id.create_order_buddhist_thumb_button);
		this.viewBuddhistThumbLoading = (ProgressBar) view.findViewById(R.id.create_order_buddhist_thumb_loading);
		this.viewHallName = (TextView) view.findViewById(R.id.create_order_hall_name_text);
		this.viewBuddhistName = (TextView) view.findViewById(R.id.create_order_buddhist_name_text);
		this.viewDesireType = (Button) view.findViewById(R.id.create_order_desire_type_button);
		this.viewDesireContent = (EditText) view.findViewById(R.id.create_order_content_text);
		this.viewDesirer = (EditText) view.findViewById(R.id.create_order_other_desirer_text);
		this.viewMobile = (EditText) view.findViewById(R.id.create_order_other_mobile_text);

		this.layoutSelectContent = (LinearLayout) view.findViewById(R.id.create_order_select_content_layout);
		this.layoutSelectContent.setOnClickListener(this);
		this.buttonSelectContentShow = (TextView) view.findViewById(R.id.create_order_select_content_show_button);
		this.buttonSelectContentShow.setOnClickListener(this);
		this.buttonSelectContentCancel = (Button) view.findViewById(R.id.create_order_select_content_cancel_button);
		this.buttonSelectContentCancel.setOnClickListener(this);
		this.buttonSelectContentOK = (Button) view.findViewById(R.id.create_order_select_content_ok_button);
		this.buttonSelectContentOK.setOnClickListener(this);

		this.layoutSelectJSC = (LinearLayout) view.findViewById(R.id.create_order_select_JSC_layout);
		this.layoutSelectJSC.setOnClickListener(this);
		this.buttonSelectJSCShow = (TextView) view.findViewById(R.id.create_order_select_JSC_show_button);
		this.buttonSelectJSCShow.setOnClickListener(this);
		this.buttonSelectJSCCancel = (Button) view.findViewById(R.id.create_order_select_JSC_cancel_button);
		this.buttonSelectJSCCancel.setOnClickListener(this);
		this.buttonSelectJSCOK = (Button) view.findViewById(R.id.create_order_select_JSC_ok_button);
		this.buttonSelectJSCOK.setOnClickListener(this);

		this.layoutSelectRegion = (LinearLayout) view.findViewById(R.id.create_order_select_region_layout);
		this.layoutSelectRegion.setOnClickListener(this);
		this.buttonSelectRegionShow = (TextView) view.findViewById(R.id.create_order_select_region_show_button);
		this.buttonSelectRegionShow.setOnClickListener(this);
		this.buttonSelectRegionCancel = (Button) view.findViewById(R.id.create_order_select_region_cancel_button);
		this.buttonSelectRegionCancel.setOnClickListener(this);
		this.buttonSelectRegionOK = (Button) view.findViewById(R.id.create_order_select_region_ok_button);
		this.buttonSelectRegionOK.setOnClickListener(this);

		this.layoutSelectDate = (LinearLayout) view.findViewById(R.id.create_order_select_date_layout);
		this.layoutSelectDate.setOnClickListener(this);
		this.buttonSelectDateShow = (TextView) view.findViewById(R.id.create_order_select_date_show_button);
		this.buttonSelectDateShow.setOnClickListener(this);
		this.buttonSelectDateCancel = (Button) view.findViewById(R.id.create_order_select_date_cancel_button);
		this.buttonSelectDateCancel.setOnClickListener(this);
		this.buttonSelectDateOK = (Button) view.findViewById(R.id.create_order_select_date_ok_button);
		this.buttonSelectDateOK.setOnClickListener(this);

		try {
			this.jsonRegions = new JSONArray(Utils.getRegions());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.calendar = Calendar.getInstance();
		this.daySelected = this.calendar.get(Calendar.DAY_OF_MONTH) - 1;

		if (null != this.bundle) {
			this.desireType = this.bundle.getInt("desire_type");
			switch (this.desireType) {
			case 1:
				this.viewDesireType.setText(R.string.desire_1);
				break;
			case 2:
				this.viewDesireType.setText(R.string.desire_2);
				break;
			case 3:
				this.viewDesireType.setText(R.string.desire_3);
				break;
			case 4:
				this.viewDesireType.setText(R.string.desire_4);
				break;
			case 5:
				this.viewDesireType.setText(R.string.desire_5);
				break;
			case 6:
				this.viewDesireType.setText(R.string.desire_6);
				break;
			case 7:
				this.viewDesireType.setText(R.string.desire_7);
				break;
			default:
				break;
			}
		}
		if (null != this.bundle && "" != this.bundle.getString("temple")) {
			try {
				this.temple = new JSONObject(bundle.getString("temple"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ShangXiang.imageLoader.displayImage(this.temple.optString("pic_tmb_path", ""), this.viewHallThumb, ShangXiang.imageLoaderOptions, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					viewHallThumbLoading.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					viewHallThumbLoading.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					viewHallThumbLoading.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					viewHallThumbLoading.setVisibility(View.GONE);
				}
			});
			ShangXiang.imageLoader.displayImage(this.temple.optString("tmb_headface", ""), this.viewBuddhistThumb, ShangXiang.imageLoaderOptions, new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					viewBuddhistThumbLoading.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					viewBuddhistThumbLoading.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					viewBuddhistThumbLoading.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					viewBuddhistThumbLoading.setVisibility(View.GONE);
				}
			});
			this.viewHallName.setText(this.temple.optString("templename", ""));
			this.viewBuddhistName.setText(this.temple.optString("buddhistname", ""));
		}
		this.viewDesirer.setText(ShangXiang.memberInfo.optString("truename", ""));
		this.buttonSelectRegionShow.setText(ShangXiang.memberInfo.optString("area", ""));
		this.viewMobile.setText(ShangXiang.APP.getMobile());
		this.formatter = new SimpleDateFormat("yyyy-MM-dd");
		this.buttonSelectDateShow.setText(this.formatter.format(this.calendar.getTime()));

		this.buttonSubmit = (Button) view.findViewById(R.id.create_order_submit_button);
		this.buttonSubmit.setOnClickListener(this);

		return view;
	}

	public void onActivityCreated(Bundle sinha) {
		super.onActivityCreated(sinha);

		this.selectContent = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_content_view);
		this.contentAdapter = new ContentAdapter(getActivity());
		this.contentAdapter.contents = new String[] { "请选择" };
		this.selectContent.setViewAdapter(this.contentAdapter);

		this.selectJSC = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_JSC_view);
		this.JSCAdapter = new JSCAdapter(getActivity());
		this.JSCAdapter.JSCs = new String[] { "请选择" };
		this.selectJSC.setViewAdapter(this.JSCAdapter);

		this.selectRegionCity = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_region_city_view);
		this.regionCityAdapter = new RegionCityAdapter(getActivity());
		try {
			this.regionCityAdapter.regions = new JSONArray("[{\"name\":\"请选择\"}]");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.selectRegionCity.setViewAdapter(this.regionCityAdapter);

		this.selectRegionProv = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_region_prov_view);
		this.regionProvAdapter = new RegionProvAdapter(getActivity());
		this.regionProvAdapter.regions = this.jsonRegions;
		this.selectRegionProv.setViewAdapter(this.regionProvAdapter);
		this.selectRegionProv.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				regionCityAdapter = new RegionCityAdapter(getActivity());
				regionCityAdapter.regions = regionProvAdapter.regions.optJSONObject(selectRegionProv.getCurrentItem()).optJSONArray("sub");
				selectRegionCity.setViewAdapter(regionCityAdapter);
			}
		});
		this.selectRegionProv.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					regionCityAdapter = new RegionCityAdapter(getActivity());
					regionCityAdapter.regions = regionProvAdapter.regions.optJSONObject(newValue).optJSONArray("sub");
					selectRegionCity.setViewAdapter(regionCityAdapter);
				}
			}
		});

		this.selectDateYear = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_date_year_view);
		this.selectDateMonth = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_date_month_view);
		this.selectDateDay = (AbstractWheel) getActivity().findViewById(R.id.create_order_select_date_day_view);

		this.dateYearAdapter = new DateYearAdapter(getActivity());
		this.selectDateYear.setViewAdapter(this.dateYearAdapter);
		this.selectDateYear.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				calendar.set(calendar.get(Calendar.YEAR) + selectDateYear.getCurrentItem(), selectDateMonth.getCurrentItem(), 1);
				dateDayAdapter = new DateDayAdapter(getActivity());
				dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				selectDateDay.setViewAdapter(dateDayAdapter);
				selectDateDay.setCurrentItem(daySelected);
			}
		});
		this.selectDateYear.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					calendar.set(calendar.get(Calendar.YEAR) + selectDateYear.getCurrentItem(), selectDateMonth.getCurrentItem(), 1);
					dateDayAdapter = new DateDayAdapter(getActivity());
					dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					selectDateDay.setViewAdapter(dateDayAdapter);
					selectDateDay.setCurrentItem(daySelected);
				}
			}
		});

		this.dateMonthAdapter = new DateMonthAdapter(getActivity());
		this.selectDateMonth.setViewAdapter(this.dateMonthAdapter);
		this.selectDateMonth.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				calendar.set(calendar.get(Calendar.YEAR) + selectDateYear.getCurrentItem(), selectDateMonth.getCurrentItem(), 1);
				dateDayAdapter = new DateDayAdapter(getActivity());
				dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				selectDateDay.setViewAdapter(dateDayAdapter);
				selectDateDay.setCurrentItem(daySelected);
			}
		});
		this.selectDateMonth.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					calendar.set(calendar.get(Calendar.YEAR) + selectDateYear.getCurrentItem(), selectDateMonth.getCurrentItem(), 1);
					dateDayAdapter = new DateDayAdapter(getActivity());
					dateDayAdapter.dayCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					selectDateDay.setViewAdapter(dateDayAdapter);
					selectDateDay.setCurrentItem(daySelected);
				}
			}
		});

		this.dateDayAdapter = new DateDayAdapter(getActivity());
		this.dateDayAdapter.dayCount = this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		this.selectDateDay.setViewAdapter(this.dateDayAdapter);
		this.selectDateDay.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(AbstractWheel wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(AbstractWheel wheel) {
				scrolling = false;
				daySelected = selectDateDay.getCurrentItem();
				calendar.set(calendar.get(Calendar.YEAR) + selectDateYear.getCurrentItem(), selectDateMonth.getCurrentItem(), daySelected + 1);
			}
		});
		this.selectDateDay.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
				if (!scrolling) {
					daySelected = selectDateDay.getCurrentItem();
					calendar.set(calendar.get(Calendar.YEAR) + selectDateYear.getCurrentItem(), selectDateMonth.getCurrentItem(), daySelected + 1);
				}
			}
		});

		this.selectDateMonth.setCurrentItem(this.calendar.get(Calendar.MONTH));
		this.selectDateDay.setCurrentItem(this.daySelected);

		loadSelectContent();
	}

	private class ContentAdapter extends AbstractWheelTextAdapter {
		String[] contents = new String[] {};

		protected ContentAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return this.contents.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView JSCView = (TextView) view.findViewById(R.id.select_custom_text);
			JSCView.setText(this.contents[index]);
			return view;
		}
	}

	private class JSCAdapter extends AbstractWheelTextAdapter {
		String[] JSCs = new String[] {};

		protected JSCAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return this.JSCs.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView JSCView = (TextView) view.findViewById(R.id.select_custom_text);
			JSCView.setText(this.JSCs[index]);
			return view;
		}
	}

	private class RegionProvAdapter extends AbstractWheelTextAdapter {
		JSONArray regions = new JSONArray();

		protected RegionProvAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return this.regions.length();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			JSONObject prov = this.regions.optJSONObject(index);
			TextView provView = (TextView) view.findViewById(R.id.select_custom_text);
			provView.setText(prov.optString("name", ""));
			return view;
		}
	}

	private class RegionCityAdapter extends AbstractWheelTextAdapter {
		JSONArray regions = new JSONArray();

		protected RegionCityAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return this.regions.length();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			JSONObject city = this.regions.optJSONObject(index);
			TextView cityView = (TextView) view.findViewById(R.id.select_custom_text);
			cityView.setText(city.optString("name", ""));
			return view;
		}
	}

	private class DateYearAdapter extends AbstractWheelTextAdapter {
		protected DateYearAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return 10;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView yearView = (TextView) view.findViewById(R.id.select_custom_text);
			yearView.setText((calendar.get(Calendar.YEAR) + index) + "年");
			return view;
		}
	}

	private class DateMonthAdapter extends AbstractWheelTextAdapter {
		protected DateMonthAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return 12;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView monthView = (TextView) view.findViewById(R.id.select_custom_text);
			monthView.setText((index + 1) + "月");
			return view;
		}
	}

	private class DateDayAdapter extends AbstractWheelTextAdapter {
		int dayCount = 30;

		protected DateDayAdapter(Context context) {
			super(context, R.layout.select_custom_text, NO_RESOURCE);
		}

		@Override
		public int getItemsCount() {
			return dayCount;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return "";
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			TextView monthView = (TextView) view.findViewById(R.id.select_custom_text);
			monthView.setText((index + 1) + "日");
			return view;
		}
	}

	private void showLoading() {
		Utils.animView(this.layoutLoading, !this.showLoading);
		this.showLoading = !this.showLoading;
	}

	private void loadSelectContent() {
		if (Utils.CheckNetwork()) {
			showLoading();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("wishtype", String.valueOf(this.desireType)));

			this.httpClient.Config("get", Consts.URI_SELECT_CONTENT, params, true);
			this.httpMethod = new SinhaPipeMethod(this.httpClient, new SinhaPipeMethod.MethodCallback() {
				public void CallFinished(String error, Object result) {
					showLoading();
					if (null == error) {
						loadSelectContent((String) result);
					} else {
						int err = R.string.dialog_system_error_content;
						if (error == httpClient.ERR_TIME_OUT) {
							err = R.string.dialog_network_error_timeout;
						}
						if (error == httpClient.ERR_GET_ERR) {
							err = R.string.dialog_network_error_getdata;
						}
						Utils.ShowToast(getActivity(), err);
					}
				}
			});
			this.httpMethod.start();
		} else {
			Utils.ShowToast(getActivity(), R.string.dialog_network_check_content);
		}
	}

	private void loadSelectContent(String s) {
		if (null != s) {
			try {
				JSONObject result = new JSONObject(s);
				if (result.optString("code", "").equals("succeed")) {
					JSONArray jsonContent = result.optJSONArray("wishtextchoice");
					if (null != jsonContent) {
						String[] arr = new String[jsonContent.length()];
						for (int i = 0; i < jsonContent.length(); i++) {
							arr[i] = jsonContent.optString(i, "");
						}
						this.contentData = arr;
						this.contentAdapter = new ContentAdapter(getActivity());
						this.contentAdapter.contents = arr;
						this.selectContent.setViewAdapter(this.contentAdapter);
						this.selectContent.setCurrentItem(0);
					}
				} else {
					Utils.Dialog(getActivity(), getString(R.string.dialog_normal_title), result.optString("msg", ""));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		loadJSC();
	}

	private void loadJSC() {
		if (Utils.CheckNetwork()) {
			showLoading();

			this.httpClient.Config("get", Consts.URI_JSC, null, true);
			this.httpMethod = new SinhaPipeMethod(this.httpClient, new SinhaPipeMethod.MethodCallback() {
				public void CallFinished(String error, Object result) {
					showLoading();
					if (null == error) {
						loadJSC((String) result);
					} else {
						int err = R.string.dialog_system_error_content;
						if (error == httpClient.ERR_TIME_OUT) {
							err = R.string.dialog_network_error_timeout;
						}
						if (error == httpClient.ERR_GET_ERR) {
							err = R.string.dialog_network_error_getdata;
						}
						Utils.ShowToast(getActivity(), err);
					}
				}
			});
			this.httpMethod.start();
		} else {
			Utils.ShowToast(getActivity(), R.string.dialog_network_check_content);
		}
	}

	private void loadJSC(String s) {
		if (null != s) {
			try {
				JSONObject result = new JSONObject(s);
				if (result.optString("code", "").equals("succeed")) {
					this.jsonJSCs = result.optJSONArray("wishgradeinfo");
					if (null != this.jsonJSCs) {
						String[] arr = new String[this.jsonJSCs.length()];
						for (int i = 0; i < this.jsonJSCs.length(); i++) {
							JSONObject jsonJSC = this.jsonJSCs.optJSONObject(i);
							arr[i] = jsonJSC.optString("name", "") + "  ￥" + jsonJSC.optString("price", "");
						}
						this.JSCData = arr;
						this.JSCAdapter = new JSCAdapter(getActivity());
						this.JSCAdapter.JSCs = arr;
						this.selectJSC.setViewAdapter(this.JSCAdapter);
						this.selectJSC.setCurrentItem(3);
					}
				} else {
					Utils.Dialog(getActivity(), getString(R.string.dialog_normal_title), result.optString("msg", ""));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void submitOrder() {
		if (Utils.CheckNetwork()) {
			if (checkForm()) {
				this.isSubmiting = true;
				showLoading();

				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("mid", ShangXiang.APP.getMemberId()));
				params.add(new BasicNameValuePair("tid", this.temple.optString("templeid", "")));
				params.add(new BasicNameValuePair("aid", this.temple.optString("attacheid", "")));
				params.add(new BasicNameValuePair("wishtype", String.valueOf(this.desireType)));
				params.add(new BasicNameValuePair("wishtext", this.viewDesireContent.getText().toString()));
				params.add(new BasicNameValuePair("wishname", this.viewDesirer.getText().toString()));
				params.add(new BasicNameValuePair("mobile", this.viewMobile.getText().toString()));
				params.add(new BasicNameValuePair("wishgrade", (String) this.buttonSelectJSCShow.getTag()));
				params.add(new BasicNameValuePair("buddhadate", this.buttonSelectDateShow.getText().toString()));
				params.add(new BasicNameValuePair("wishplace", this.buttonSelectRegionShow.getText().toString()));

				this.httpClient.Config("post", Consts.URI_CREATE_ORDER, params, true);
				this.httpMethod = new SinhaPipeMethod(this.httpClient, new SinhaPipeMethod.MethodCallback() {
					public void CallFinished(String error, Object result) {
						showLoading();
						isSubmiting = false;
						if (null == error) {
							submitOrder((String) result);
						} else {
							int err = R.string.dialog_system_error_content;
							if (error == httpClient.ERR_TIME_OUT) {
								err = R.string.dialog_network_error_timeout;
							}
							if (error == httpClient.ERR_GET_ERR) {
								err = R.string.dialog_network_error_getdata;
							}
							Utils.ShowToast(getActivity(), err);
						}
					}
				});
				this.httpMethod.start();
			}
		} else {
			Utils.ShowToast(getActivity(), R.string.dialog_network_check_content);
		}
	}

	private void submitOrder(String s) {
		if (null != s) {
			try {
				JSONObject result = new JSONObject(s);
				if (result.optString("code", "").equals("succeed")) {
					Utils.Dialog(getActivity(), R.string.dialog_normal_title, R.string.dialog_create_order_success, new Utils.Callback() {
						@Override
						public void callFinished() {
							bundle.putString("order_no", "");
							bundle.putString("desirer", viewDesirer.getText().toString());
							bundle.putString("JSC", JSCData[selectJSC.getCurrentItem()].replace(" ", ""));
							bundle.putString("date", buttonSelectDateShow.getText().toString());
							bundle.putString("desire_content", viewDesireContent.getText().toString());
							goFragment(new ShowOrderRecord(), bundle);
						}
					});
				} else {
					Utils.Dialog(getActivity(), getString(R.string.dialog_normal_title), result.optString("msg", ""));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkForm() {
		boolean bolAlertPop = false;
		boolean bolCheckResult = true;
		if (!this.viewDesireContent.getText().toString().matches(".{1,100}")) {
			bolAlertPop = true;
			bolCheckResult = false;
			Utils.Dialog(getActivity(), getString(R.string.dialog_form_check_title), getString(R.string.dialog_form_check_err_create_order_desire_content));
		}
		if (!bolAlertPop && !this.viewDesirer.getText().toString().matches(".{1,50}")) {
			bolAlertPop = true;
			bolCheckResult = false;
			Utils.Dialog(getActivity(), getString(R.string.dialog_form_check_title), getString(R.string.dialog_form_check_err_create_order_desirer));
		}
		if (!bolAlertPop && !this.viewMobile.getText().toString().matches(".{11,11}")) {
			bolAlertPop = true;
			bolCheckResult = false;
			Utils.Dialog(getActivity(), getString(R.string.dialog_form_check_title), getString(R.string.dialog_form_check_err_create_order_mobile));
		}
		if (!bolAlertPop && !this.buttonSelectJSCShow.getText().toString().matches(".{1,}")) {
			bolAlertPop = true;
			bolCheckResult = false;
			Utils.Dialog(getActivity(), getString(R.string.dialog_form_check_title), getString(R.string.dialog_form_check_err_create_order_JSC));
		}
		if (!bolAlertPop && !this.buttonSelectRegionShow.getText().toString().matches(".{1,50}")) {
			bolAlertPop = true;
			bolCheckResult = false;
			Utils.Dialog(getActivity(), getString(R.string.dialog_form_check_title), getString(R.string.dialog_form_check_err_create_order_region));
		}
		return bolCheckResult;
	}

	@Override
	public void onClick(View v) {
		Utils.hideKeyboard(getActivity());
		if (v == this.buttonBack) {
			getActivity().onBackPressed();
		}
		if (v == this.buttonSelectContentShow) {
			this.layoutSelectContent.setVisibility(View.VISIBLE);
		}
		if (v == this.buttonSelectContentOK) {
			if (this.selectContent.getCurrentItem() < this.contentData.length) {
				this.viewDesireContent.setText(this.contentData[this.selectContent.getCurrentItem()]);
			}
		}
		if (v == this.layoutSelectContent || v == this.buttonSelectContentOK || v == this.buttonSelectContentCancel) {
			this.layoutSelectContent.setVisibility(View.GONE);
		}
		if (v == this.buttonSelectJSCShow) {
			this.layoutSelectJSC.setVisibility(View.VISIBLE);
		}
		if (v == this.buttonSelectJSCOK) {
			if (this.selectJSC.getCurrentItem() < this.JSCData.length) {
				JSONObject jsonJSC = this.jsonJSCs.optJSONObject(this.selectJSC.getCurrentItem());
				this.buttonSelectJSCShow.setTag(jsonJSC.optString("val", ""));
				this.buttonSelectJSCShow.setText(this.JSCData[this.selectJSC.getCurrentItem()]);
			}
		}
		if (v == this.layoutSelectJSC || v == this.buttonSelectJSCOK || v == this.buttonSelectJSCCancel) {
			this.layoutSelectJSC.setVisibility(View.GONE);
		}
		if (v == this.buttonSelectRegionShow) {
			this.layoutSelectRegion.setVisibility(View.VISIBLE);
		}
		if (v == this.buttonSelectRegionOK) {
			String strRegion = "";
			if (0 != this.selectRegionProv.getCurrentItem() && 0 != this.selectRegionCity.getCurrentItem()) {
				JSONObject jsonRegionProv = this.jsonRegions.optJSONObject(this.selectRegionProv.getCurrentItem());
				if (null != jsonRegionProv) {
					strRegion = jsonRegionProv.optString("name", "");
					JSONArray jsonRegionCities = jsonRegionProv.optJSONArray("sub");
					if (null != jsonRegionProv) {
						JSONObject jsonRegionCity = jsonRegionCities.optJSONObject(this.selectRegionCity.getCurrentItem());
						if (null != jsonRegionCity) {
							strRegion += "-" + jsonRegionCity.optString("name", "");
						}
					}
				}
			}
			if (!TextUtils.isEmpty(strRegion)) {
				this.buttonSelectRegionShow.setText(strRegion);
			}
		}
		if (v == this.layoutSelectRegion || v == this.buttonSelectRegionOK || v == this.buttonSelectRegionCancel) {
			this.layoutSelectRegion.setVisibility(View.GONE);
		}
		if (v == this.buttonSelectDateShow) {
			this.layoutSelectDate.setVisibility(View.VISIBLE);
		}
		if (v == this.buttonSelectDateOK) {
			this.buttonSelectDateShow.setText(this.formatter.format(this.calendar.getTime()));
		}
		if (v == this.layoutSelectDate || v == this.buttonSelectDateOK || v == this.buttonSelectDateCancel) {
			this.layoutSelectDate.setVisibility(View.GONE);
		}
		if (v == this.buttonSubmit) {
			if (this.isSubmiting) {
				Utils.Dialog(getActivity(), getString(R.string.dialog_tip), getString(R.string.dialog_submiting_content));
			} else {
				submitOrder();
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		onClick(v);
		return false;
	}
}