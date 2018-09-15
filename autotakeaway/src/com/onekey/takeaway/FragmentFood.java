package com.onekey.takeaway;

import mythware.http.CloudUpdateVersionServer.CloudInterface;
import mythware.http.CloudUpdateVersionServer.CloudResponseStatus;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.onekey.common.LogUtils;
import com.onekey.http.CloudManager;
import com.onekey.takeaway.bean.FoodBean;
import com.onekey.takeaway.bean.FoodBean.InnerFoodBean;
import com.onekey.takeaway.ux.DataListAdapter;
import com.onekey.takeaway.ux.DataListAdapter.ViewHolder;

public class FragmentFood extends Fragment {
	GridView mGridView;
	private ImageLoader mLoader;
	private DataListAdapter<InnerFoodBean> mDataListAdapter;

	public static FragmentFood newInstance(String text) {
		FragmentFood fragmentCommon = new FragmentFood();
		Bundle bundle = new Bundle();
		bundle.putString("text", text);
		fragmentCommon.setArguments(bundle);
		return fragmentCommon;
	}

    public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		LogUtils.d("ll1 hidden=" + hidden);
		if (!hidden) {
			initData();
		}
	}
	
	void initData() {
//		List<InnerFoodBean> list = new ArrayList<InnerFoodBean>();
//		for (int i = 0; i < 10; i++) {
//			InnerFoodBean bean = new InnerFoodBean("张三", "大饭煲", "黄瓜", "西红柿蛋汤", 17, "running", 100, "黄焖鸡米饭", 18);
//			list.add(bean);
//		}
//		mDataListAdapter.setShowDataList(list);
		CloudManager.getInstance().requestFoodList(new CloudInterface() {
			
			@Override
			public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
				if (arg0 == CloudResponseStatus.Succ) {
					FoodBean FoodBean = (com.onekey.takeaway.bean.FoodBean) arg1;
					LogUtils.d("ll1 size=" + FoodBean.getFoodlist().size());
					mDataListAdapter.setShowDataList(FoodBean.getFoodlist());
				}
			}
		});
	}
	

	DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_food, container, false);

		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getActivity()).build();
		// Initialize ImageLoader with configuration.
		mLoader = ImageLoader.getInstance();
		mLoader.init(configuration);
		
		mGridView = (GridView) view.findViewById(R.id.gridView);
		mDataListAdapter = new DataListAdapter<InnerFoodBean>(getActivity(), new DataListAdapter.ListAdapterInterface<InnerFoodBean>() {

			@Override
			public int getLayoutId() {
				return R.layout.item_fragment_food;
			}

			@Override
			public void regesterListeners(ViewHolder viewHolder, int position) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void initListViewItem(View convertView, final ViewHolder holder,
					DataListAdapter<InnerFoodBean> adapter, final int position) {
				// TODO Auto-generated method stub
				final InnerFoodBean bean = adapter.getItem(position);
				
				holder.tvs[0].setText(bean.getFoodName());
				holder.tvs[1].setText(bean.getStock() + "");
				holder.tvs[1].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final View view = inflater.inflate(R.layout.edittext, null);
						AbDialogUtil.showAlertDialog("请输入数量", view, new AbDialogOnClickListener() {
							
							@Override
							public void onPositiveClick() {
								final EditText EditText = (EditText)view.findViewById(R.id.et);
								if (EditText.getText().toString().isEmpty()) {
									AbToastUtil.showToast(getActivity(), "请输入修改数量");
									return;
								}
								int number = Integer.parseInt(EditText.getText().toString());
								int type = 1;
								int stock = mDataListAdapter.getItem(position).getStock();
								
								if (number == stock) {
									return;
								}
								if (number > stock) {
									number -= stock;
								} else {
									type = 2;
									number = stock - number;
								}
								CloudManager.getInstance().modifyFood(new CloudInterface() {
									
									@Override
									public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
										if (arg0 == CloudResponseStatus.Succ) {
//											bean.setTotal(EditText.getText().toString());
											holder.tvs[2].setText(EditText.getText().toString());
											int num = Integer.parseInt(EditText.getText().toString());
											mDataListAdapter.getItem(position).setStock(num);
										}
									}
								}, bean.getFoodId(), type, number);
							}
							
							@Override
							public void onNegativeClick() {
								AbDialogUtil.removeDialog(getActivity());
							}
						});
					}
				});

//				mLoader.displayImage(CloudManager.URL_CLOUD + bean.getPicURL(), holder.ivs[0], options);
			}

			@Override
			public void initLayout(View convertView, ViewHolder holder) {
				// TODO Auto-generated method stub
				convertView.setBackgroundColor(getResources().getColor(R.color.title));
				holder.tvs = new TextView[2];
				holder.tvs[0] = (TextView)convertView.findViewById(R.id.tv1);
				holder.tvs[1] = (TextView)convertView.findViewById(R.id.tv2);
//				holder.tvs[2] = (TextView)convertView.findViewById(R.id.tv3);
				holder.ivs = new ImageView[1];
				holder.ivs[0] = (ImageView)convertView.findViewById(R.id.iv);
				
			}

			@Override
			public boolean isSameObject(InnerFoodBean t1, InnerFoodBean t2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		mGridView.setAdapter(mDataListAdapter);
		
		initData();
		return view;
	}
}
