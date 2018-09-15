package com.onekey.takeaway;

import java.util.List;
import java.util.Map;

import mythware.http.CloudUpdateVersionServer.CloudInterface;
import mythware.http.CloudUpdateVersionServer.CloudResponseStatus;
import android.app.Fragment;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.onekey.common.LogUtils;
import com.onekey.http.CloudManager;
import com.onekey.takeaway.bean.DeviceFoodBean;
import com.onekey.takeaway.bean.DeviceFoodBean.InnerDeviceBean;
import com.onekey.takeaway.bean.DeviceFoodBean.InnerDeviceFoodBean;
import com.onekey.takeaway.ux.DataExpanableListAdapter;
import com.onekey.takeaway.ux.DataExpanableListAdapter.ChildListAdapterInterface;
import com.onekey.takeaway.ux.DataExpanableListAdapter.ParentListAdapterInterface;
import com.onekey.takeaway.ux.DataExpanableListAdapter.ViewHolder;

public class FragmentDevices extends Fragment {
	ExpandableListView mExpandableListView;
	private DataExpanableListAdapter<InnerDeviceFoodBean> mDataExpanableListAdapter;
	String mName = "智能配餐柜";
	public static final String CFJ = FragmentStore.tab1;
	public static final String STJ = FragmentStore.tab2;
	private String mCurrentType = CFJ;
	
	private DeviceFoodBean mDeviceFoodBean;
	
	public static FragmentDevices newInstance(String text) {
		FragmentDevices fragmentCommon = new FragmentDevices();
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
//		List<InnerDeviceBean> list = new ArrayList<InnerDeviceBean>();
//		for (int i = 0; i < 10; i++) {
//			InnerDeviceBean bean = new InnerDeviceBean("张三", "大饭煲", "黄瓜", "西红柿蛋汤", 17, "running", 100, "黄焖鸡米饭", 18);
//			list.add(bean);
//		}
//		mDataListAdapter.setShowDataList(list);
		CloudManager.getInstance().requestDeviceFoodList(new CloudInterface() {
			
			@Override
			public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
				if (arg0 == CloudResponseStatus.Succ) {
					DeviceFoodBean DeviceFoodBean = (DeviceFoodBean) arg1;
					mDeviceFoodBean = DeviceFoodBean;
					Map<String, List<InnerDeviceFoodBean>> cList = new ArrayMap<String, List<InnerDeviceFoodBean>> ();
					for (InnerDeviceBean item : DeviceFoodBean.getDevicelist()) {
						if (mCurrentType.equals(CFJ)) {
							if (item.getDeviceType() == 1) {
								// caofanji
								cList.put(item.getDeviceID(), item.getSlots());
							}
						} else {
							if (item.getDeviceType() == 2) {
								// soutangji 
								cList.put(item.getDeviceID(), item.getSlots());
							}
						}
						
					}
					LogUtils.i("ll1 cList:" + cList);
					LogUtils.i("ll1 mDataExpanableListAdapter:" + mDataExpanableListAdapter);
					mDataExpanableListAdapter.setShowList(cList);

				}
			}
		});
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_device, container, false);
		mCurrentType = getArguments().getString("text");
		mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandable_list);
		mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				ImageView iv = (ImageView) v.findViewById(R.id.iv_arrow);
				if ( parent.isGroupExpanded( groupPosition ) ){
					iv.setImageDrawable(getResources().getDrawable(R.drawable.right_arrow));
				} else {
					iv.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
				}
				return false;
			}
		});
		
		ParentListAdapterInterface parentManager = new ParentListAdapterInterface() {

			@Override
			public int getParentLayoutId() {
				return R.layout.expandlist_group;
			}

			@Override
			public void regesterParentListeners(ViewHolder viewHolder,
					int position) {
			}

			@Override
			public void initParentListViewItem(View convertView,
					ViewHolder holder, DataExpanableListAdapter<?> adapter,
					int groupPosition) {
				LogUtils.i("wzw groupPosition=" + groupPosition);
				String name = adapter.getGroup(groupPosition);
				if (mDeviceFoodBean != null && mDeviceFoodBean.getDevicelist() != null) {
					for (InnerDeviceBean item : mDeviceFoodBean.getDevicelist()) {
						if (item.getDeviceID().equals(name)) {
							name += "(" + item.getStatusName() + ")";
						}
					}
				}
				holder.tvs[0].setText(name);
			}

			@Override
			public void initParentLayout(View convertView, ViewHolder holder) {
				holder.tvs = new TextView[1];
				holder.tvs[0] = (TextView)convertView.findViewById(R.id.tv_group_name);
				holder.ivs = new ImageView[1];
				holder.ivs[0] = (ImageView) convertView.findViewById(R.id.iv_arrow);
			}
			
		};
		ChildListAdapterInterface childManager = new ChildListAdapterInterface<InnerDeviceFoodBean>() {

			@Override
			public int getChildLayoutId() {
				return R.layout.expandlist_item;
			}

			@Override
			public View getChildHeaderView() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void regesterChildListeners(final ViewHolder viewHolder,
					final int position) {
				viewHolder.ivs[0].setOnClickListener(new OnClickListener() {
					
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
								int stock = mDataExpanableListAdapter.getItem(position).getStock();
								
								if (number == stock) {
									return;
								}
								if (number > stock) {
									number -= stock;
								} else {
									type = 2;
									number = stock - number;
								}
								CloudManager.getInstance().modifyDeviceFood(new CloudInterface() {
									
									@Override
									public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
										if (arg0 == CloudResponseStatus.Succ) {
//											bean.setTotal(EditText.getText().toString());
											viewHolder.tvs[2].setText(EditText.getText().toString());
											int num = Integer.parseInt(EditText.getText().toString());
											mDataExpanableListAdapter.getItem(position).setStock(num);
										}
									}
								}, mDataExpanableListAdapter.getItem(position).getSlotCode(), type, number);
							}
							
							@Override
							public void onNegativeClick() {
								AbDialogUtil.removeDialog(getActivity());
							}
						});
					}
				});
			}

			@Override
			public void initChildListViewItem(View convertView,
					ViewHolder holder,
					DataExpanableListAdapter<InnerDeviceFoodBean> adapter,
					int position) {

				LogUtils.i("wzw position=" + position);
				holder.tvs[0].setText(adapter.getItem(position).getSlotCode());
				holder.tvs[1].setText(adapter.getItem(position).getFoodName());
				holder.tvs[2].setText(adapter.getItem(position).getStock() + "");
			}

			@Override
			public void initChildLayout(View convertView, ViewHolder holder) {

				holder.tvs = new TextView[3];
				holder.tvs[0] = (TextView)convertView.findViewById(R.id.tv1);			
				holder.tvs[1] = (TextView)convertView.findViewById(R.id.tv2);			
				holder.tvs[2] = (TextView)convertView.findViewById(R.id.tv3);
				holder.ivs = new ImageView[1];	
				holder.ivs[0] = (ImageView)convertView.findViewById(R.id.iv);				
			}

			@Override
			public List<InnerDeviceFoodBean> findByCondition(Object... condition) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isSameObject(InnerDeviceFoodBean c1, InnerDeviceFoodBean c2) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		mDataExpanableListAdapter = new DataExpanableListAdapter<InnerDeviceFoodBean>(getActivity(), parentManager, childManager, mExpandableListView);
		
		mExpandableListView.setAdapter(mDataExpanableListAdapter);
		
		initData();
		return view;
	}
}
