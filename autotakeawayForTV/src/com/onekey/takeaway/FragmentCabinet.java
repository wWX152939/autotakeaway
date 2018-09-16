package com.onekey.takeaway;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import mythware.http.CloudUpdateVersionServer.CloudInterface;
import mythware.http.CloudUpdateVersionServer.CloudResponseStatus;
import android.app.Fragment;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import com.onekey.common.Common;
import com.onekey.common.LogUtils;
import com.onekey.http.CloudManager;
import com.onekey.takeaway.bean.CabinetBean;
import com.onekey.takeaway.bean.CabinetBean.DoorListBean;
import com.onekey.takeaway.bean.CabinetBean.InnerCabinetBean;
import com.onekey.takeaway.ux.DataExpanableListAdapter;
import com.onekey.takeaway.ux.DataExpanableListAdapter.ChildListAdapterInterface;
import com.onekey.takeaway.ux.DataExpanableListAdapter.ParentListAdapterInterface;
import com.onekey.takeaway.ux.DataExpanableListAdapter.ViewHolder;
import com.onekey.takeawayForTV.R;

public class FragmentCabinet extends Fragment {
	ExpandableListView mExpandableListView;
	private DataExpanableListAdapter<DoorListBean> mDataExpanableListAdapter;
	String mName = "智能配餐柜";

	public static FragmentCabinet newInstance(String text) {
		FragmentCabinet fragmentCommon = new FragmentCabinet();
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
    
    private Timer mTimer;
	private void initTimer() {
		mTimer = new Timer();
		TimerTask task = new RefreshDataTask();

		mTimer.schedule(task, 5000, 5000);
	}
	
	private class RefreshDataTask extends TimerTask {

		@Override
		public void run() {
			LogUtils.d("ll1" + isHidden() + " mCurrentType=");

			if (!isHidden()) {
				initData();
			}
			
		}
		
	}
	
	void initData() {
//		List<InnerDeviceBean> list = new ArrayList<InnerDeviceBean>();
//		for (int i = 0; i < 10; i++) {
//			InnerDeviceBean bean = new InnerDeviceBean("张三", "大饭煲", "黄瓜", "西红柿蛋汤", 17, "running", 100, "黄焖鸡米饭", 18);
//			list.add(bean);
//		}
//		mDataListAdapter.setShowDataList(list);
		CloudManager.getInstance().requestCabinetList(new CloudInterface() {
			
			@Override
			public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
				if (arg0 == CloudResponseStatus.Succ) {
					CabinetBean CabinetBean = (CabinetBean) arg1;
					Map<String, List<DoorListBean>> cList = new ArrayMap<String, List<DoorListBean>> ();
					for (InnerCabinetBean item : CabinetBean.getDevicelist()) {
						cList.put(item.getDeviceID(), item.getSlots());
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
				.inflate(R.layout.fragment_cabinet, container, false);
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void initParentListViewItem(View convertView,
					ViewHolder holder, DataExpanableListAdapter<?> adapter,
					int groupPosition) {
				LogUtils.i("wzw groupPosition=" + groupPosition);
				holder.tvs[0].setText(adapter.getGroup(groupPosition));
			}

			@Override
			public void initParentLayout(View convertView, ViewHolder holder) {
				holder.tvs = new TextView[1];
				holder.tvs[0] = (TextView)convertView.findViewById(R.id.tv_group_name);
			}
			
		};
		ChildListAdapterInterface childManager = new ChildListAdapterInterface<DoorListBean>() {

			@Override
			public int getChildLayoutId() {
				return R.layout.expandlist_item_cabinet;
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
						String status = mDataExpanableListAdapter.getItem(position).getFrontDoor();
						if (status == null) {
							return;
						}
						if (status.equals("1")) {
							status = "0";
						} else {
							status = "1";
						}
						final String finalStatus = status;
						CloudManager.getInstance().modifyCabinet(new CloudInterface() {
							
							@Override
							public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
								if (arg0 == CloudResponseStatus.Succ) {
									String s = finalStatus.equals("1") ? "打开" : "关闭";
									viewHolder.tvs[2].setText(s);
									mDataExpanableListAdapter.getItem(position).setFrontDoor(finalStatus);
									Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
								}
							}
						}, mDataExpanableListAdapter.getItem(position).getDoorCode(), true, status);
					}
				});
				viewHolder.ivs[1].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String status = mDataExpanableListAdapter.getItem(position).getBackDoor();
						if (status == null) {
							return;
						}
						if (status.equals("1")) {
							status = "0";
						} else {
							status = "1";
						}
						final String finalStatus = status;
						CloudManager.getInstance().modifyCabinet(new CloudInterface() {
							
							@Override
							public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
								if (arg0 == CloudResponseStatus.Succ) {
									Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
									String s = finalStatus.equals("1") ? "打开" : "关闭";
									viewHolder.tvs[3].setText(s);
									mDataExpanableListAdapter.getItem(position).setBackDoor(finalStatus);
								}
							}
						}, mDataExpanableListAdapter.getItem(position).getDoorCode(), false, status);
					}
				});
			}

			@Override
			public void initChildListViewItem(View convertView,
					ViewHolder holder,
					DataExpanableListAdapter<DoorListBean> adapter,
					int position) {

				LogUtils.i("wzw position=" + position);
				holder.tvs[0].setText(Common.getDoorCodeCustom(adapter.getItem(position).getDoorCode()));
				if (adapter.getItem(position).getContainFood() != null) {
					holder.tvs[1].setText(adapter.getItem(position).getContainFood().equals("0") ? "否" : "是");
				} else {
					holder.tvs[1].setText("null");
				}
				if (adapter.getItem(position).getFrontDoor() != null) {
					holder.tvs[2].setText(adapter.getItem(position).getFrontDoor().equals("1") ? "打开" : "关闭");
				} else {
					holder.tvs[2].setText("null");
				}
				if (adapter.getItem(position).getBackDoor() != null) {
					holder.tvs[3].setText(adapter.getItem(position).getBackDoor().equals("1") ? "打开" : "关闭");
				} else {
					holder.tvs[3].setText("null");
				}
			}

			@Override
			public void initChildLayout(View convertView, ViewHolder holder) {

				holder.tvs = new TextView[4];
				holder.tvs[0] = (TextView)convertView.findViewById(R.id.tv1);			
				holder.tvs[1] = (TextView)convertView.findViewById(R.id.tv2);			
				holder.tvs[2] = (TextView)convertView.findViewById(R.id.tv3);		
				holder.tvs[3] = (TextView)convertView.findViewById(R.id.tv4);
				holder.ivs = new ImageView[2];	
				holder.ivs[0] = (ImageView)convertView.findViewById(R.id.iv);	
				holder.ivs[1] = (ImageView)convertView.findViewById(R.id.iv2);				
			}

			@Override
			public List<DoorListBean> findByCondition(Object... condition) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isSameObject(DoorListBean c1, DoorListBean c2) {
				// TODO Auto-generated method stub
				return false;
			}
		};
		mDataExpanableListAdapter = new DataExpanableListAdapter<DoorListBean>(getActivity(), parentManager, childManager, mExpandableListView);
		
		mExpandableListView.setAdapter(mDataExpanableListAdapter);
		
		initData();
		initTimer();
		return view;
	}
}
