package com.onekey.takeaway;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mythware.common.LogUtils;
import mythware.http.CloudUpdateVersionServer.CloudInterface;
import mythware.http.CloudUpdateVersionServer.CloudResponseStatus;
import android.app.Fragment;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbDialogUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.onekey.common.Common;
import com.onekey.http.CloudManager;
import com.onekey.takeaway.bean.CabinetBean;
import com.onekey.takeaway.bean.CabinetBean.DoorListBean;
import com.onekey.takeaway.bean.CabinetBean.InnerCabinetBean;
import com.onekey.takeaway.bean.DoorBean;
import com.onekey.takeaway.bean.MsgBean;
import com.onekey.takeaway.bean.OrderBean;
import com.onekey.takeaway.bean.OrderBean.InnerOrderBean;
import com.onekey.takeaway.bean.ResponseFoodStoreBean;
import com.onekey.takeaway.ux.DataListAdapter;
import com.onekey.takeaway.ux.DataListAdapter.ViewHolder;
import com.onekey.takeawayForTV.R;

public class FragmentOrder extends Fragment {
	GridView mGridView;
	private AbPullToRefreshView mAbPullToRefreshView;
//	private TextView mTv1, mTv2;
	
	private DataListAdapter<InnerOrderBean> mDataListAdapter;
	private int mCurrentPage = 1;
	private String mStatus;
	
	private String mCurrentType = FragmentOperation.tab4;
	private int mCurrentDevType = 1;

	public static FragmentOrder newInstance(String text) {
		FragmentOrder fragmentCommon = new FragmentOrder();
		Bundle bundle = new Bundle();
		bundle.putString("text", text);
		fragmentCommon.setArguments(bundle);
		return fragmentCommon;
	}
	
	void initData() {
		List<InnerOrderBean> list = new ArrayList<InnerOrderBean>();
		for (int i = 0; i < 10; i++) {
			InnerOrderBean bean = new InnerOrderBean("张三", "西红柿蛋汤", "303031", "B0B0", 5, "2018-09-06 17:22:06");
			list.add(bean);
		}
		mDataListAdapter.addShowDataList(list);
		
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
			LogUtils.d("ll1" + isHidden() + " mCurrentType=" + mCurrentType + " act=" + getActivity());
			if (getActivity() == null) {
				return;
			}

			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					if (!isHidden() || mCurrentType.equals(FragmentOperation.tab4)) {
						CloudManager.getInstance().requestUnTakenOrderList(new CloudInterface() {
							
							@Override
							public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
								LogUtils.i("ll1 mCurrentType:" + mCurrentType + " arg0=" + arg0 + " info.getErrorcode()=");
								if (arg0 == CloudResponseStatus.Succ) {
									OrderBean bean = (OrderBean) arg1;

									synchronized (this) {
										
										if (bean.getOrderList() != null) {
											List<InnerOrderBean> list = getOrderBeanList(bean.getOrderList());
											LogUtils.d("ll1 list=" + list);
											if (!bean.getOrderList().isEmpty()) {
												
												mDataListAdapter.setDataList(list);
												
												requestCabinetList();
												
											} else {
												mDataListAdapter.setDataList(null);
											}
//										mDataListAdapter.setDataList(bean.getOrderList());
										}
									}
								} 
								mAbPullToRefreshView.onHeaderRefreshFinish();
							}
						}, mCurrentDevType, "", mStatus);
						
						
					}
				}
			});
		}
		
	}
	
	private void requestCabinetList() {

		CloudManager.getInstance().requestCabinetList(new CloudInterface() {

			@Override
			public void cloudCallback(CloudResponseStatus arg0,
					Object arg1) {
				if (arg0 == CloudResponseStatus.Succ) {
					CabinetBean bean = (CabinetBean) arg1;
					LogUtils.d("ll1 bean=" + bean);
					if (bean != null && bean.getDevicelist() != null && !bean.getDevicelist().isEmpty()) {
						synchronized (this) {
						try {
								
							for (InnerOrderBean item : mDataListAdapter.getDataList()) {
								LogUtils.d("ll1 item.getDevId()=" + item.getDevId() + " item.getDoorId()=" + item.getDoorId());
								if (item.getDevId() != null && item.getDoorId() != null) {
									for (InnerCabinetBean deviceItem : bean.getDevicelist()) {
										LogUtils.d("ll1 item.getDevId()=" + item.getDevId() + " deviceItem.getDeviceID()=" + deviceItem.getDeviceID());
										if (item.getDevId().equals(deviceItem.getDeviceID()) && deviceItem.getSlots() != null) {
											for (DoorListBean doorItem : deviceItem.getSlots()) {
												LogUtils.d("ll1 doorItem.getDoorCode()=" + doorItem.getDoorCode() + " item.getDoorId()=" + item.getDoorId());
												if (doorItem.getDoorCode().equals(item.getDoorId())) {
													LogUtils.d("ll1 ===================set================");
													item.setBackDoorClose(doorItem.getBackDoor().equals("0"));
													item.setContainFood(doorItem.getContainFood().equals("0"));
													break;
												}
											}
										}
									}
								}
							}
							
							if (!mDataListAdapter.getDataList().isEmpty()) {
								
								if (mDataListAdapter.getDataShowList().isEmpty()) {
									
									if (mCurrentType.equals(FragmentOperation.tab4)) {
										playSoundScan();
									}
								} else {
									if (mDataListAdapter.getDataList().get(0).getOrderId() != mDataListAdapter.getDataShowList().get(0).getOrderId()) {
										LogUtils.d("ll1 not equal");
										if (mCurrentType.equals(FragmentOperation.tab4)) {
											playSoundScan();
										}
									} else {
										LogUtils.d("ll1 equal");
									}
									
								}
							}
							
							// remove
							List<InnerOrderBean> newList = new ArrayList<InnerOrderBean>();
							for (InnerOrderBean item : mDataListAdapter.getDataList()) {
								if (item.isBackDoorClose() && item.isContainFood()) {
									newList.add(item);
								}
							}
							if (!newList.isEmpty()) {
								mDataListAdapter.setShowDataList(newList);
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
						}
					}
				}
			}
			
		});
	}
	
	public void refreshData() {
			
		if (mDataListAdapter == null || !mDataListAdapter.getDataShowList().isEmpty()) {
			return;
		}
		if (mCurrentType.equals(FragmentOperation.tab1)) {
			mCurrentDevType = 1;
		} else if (mCurrentType.equals(FragmentOperation.tab2)){
			mCurrentDevType = 2;
		} else {
			mCurrentDevType = 17;
		}
		String lastOrderId = "";
		if (mDataListAdapter.getDataShowList().size() != 0) {
			lastOrderId = mDataListAdapter.getDataShowList().get(mDataListAdapter.getDataShowList().size() -1).getOrderId() + "";
		}
		CloudManager.getInstance().requestUnTakenOrderList(new CloudInterface() {
			
			@Override
			public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
				if (arg0 == CloudResponseStatus.Succ) {
					LogUtils.i("ll1 mCurrentType:" + mCurrentType);
					OrderBean bean = (OrderBean) arg1;
					List<InnerOrderBean> cList = new ArrayList<InnerOrderBean>();
					for (InnerOrderBean item : bean.getOrderList()) {
						if (mCurrentType.equals(FragmentOperation.tab1)) {
							if (item.getDevType() == 1) {
								// caofanji
								cList.add(item);
							}
						} else if (mCurrentType.equals(FragmentOperation.tab2)) {
							if (item.getDevType() == 2) {
								// soutangji 
								cList.add(item);
							}
						} else if (mCurrentType.equals(FragmentOperation.tab4)) {
							if (item.getDevType() == 17) {
								// soutangji 
								cList.add(item);
							}
						}
						
					}
					LogUtils.i("ll1 cList:" + cList);
					if (bean.getOrderList() != null) {
						mDataListAdapter.addDataList(cList);
						mDataListAdapter.addShowDataList(getOrderBeanList(cList));
					}
				} else {
					Toast.makeText(getActivity(), "已经加载到最后", Toast.LENGTH_SHORT).show();
				}
				mAbPullToRefreshView.onFooterLoadFinish();
			}
		}, mCurrentDevType, lastOrderId, mStatus);
	}
	
	void loadData() {
		
		if (mCurrentType.equals(FragmentOperation.tab1)) {
			mCurrentDevType = 1;
		} else if (mCurrentType.equals(FragmentOperation.tab2)){
			mCurrentDevType = 2;
		} else {
			mCurrentDevType = 17;
		}
		String lastOrderId = "";
		if (mDataListAdapter.getDataList().size() != 0) {
			lastOrderId = mDataListAdapter.getDataList().get(mDataListAdapter.getDataList().size() -1).getOrderId() + "";
		}
		CloudManager.getInstance().requestUnTakenOrderList(new CloudInterface() {
			
			@Override
			public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
				LogUtils.i("ll1 mCurrentType:" + mCurrentType + " arg0=" + arg0);
				if (arg0 == CloudResponseStatus.Succ) {
					LogUtils.i("ll1 mCurrentType:" + mCurrentType);
					OrderBean bean = (OrderBean) arg1;
					List<InnerOrderBean> cList = new ArrayList<InnerOrderBean>();
					for (InnerOrderBean item : bean.getOrderList()) {
						if (mCurrentType.equals(FragmentOperation.tab1)) {
							if (item.getDevType() == 1) {
								// caofanji
								cList.add(item);
							}
						} else if (mCurrentType.equals(FragmentOperation.tab2)) {
							if (item.getDevType() == 2) {
								// soutangji 
								cList.add(item);
							}
						} else if (mCurrentType.equals(FragmentOperation.tab4)) {
							if (item.getDevType() == 17) {
								// soutangji 
								cList.add(item);
							}
						}
						
					}
					LogUtils.i("ll1 cList:" + cList);
					if (bean.getOrderList() != null) {
						mDataListAdapter.addDataList(cList);
						mDataListAdapter.addShowDataList(getOrderBeanList(cList));
					}
				} else {
					Toast.makeText(getActivity(), "已经加载到最后", Toast.LENGTH_SHORT).show();
				}
				mAbPullToRefreshView.onFooterLoadFinish();
			}
		}, mCurrentDevType, lastOrderId, mStatus);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		mCurrentType = getArguments().getString("text");
		initSound();
		View view;
		if (mCurrentType.equals(FragmentOperation.tab4)) {
			view = inflater
					.inflate(R.layout.fragment_order_cabinet, container, false);
		} else {
			view = inflater
					.inflate(R.layout.fragment_order, container, false);
		}
		TextView tv = (TextView)view.findViewById(R.id.tv4);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playSoundScan();
			}
		});
		mGridView = (GridView) view.findViewById(R.id.gridView);
		mAbPullToRefreshView = (AbPullToRefreshView)view.findViewById(R.id.refreshview);
		mDataListAdapter = new DataListAdapter<InnerOrderBean>(getActivity(), new DataListAdapter.ListAdapterInterface<InnerOrderBean>() {

			@Override
			public int getLayoutId() {

				if (mCurrentType.equals(FragmentOperation.tab4)) {
					return R.layout.item_fragment_order_cabinet;
				} else {
					return R.layout.item_fragment_order;
				}
			}

			@Override
			public void regesterListeners(final ViewHolder viewHolder, final int position) {
				// TODO Auto-generated method stub
				viewHolder.tvs[5].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CloudManager.getInstance().requestUnusedCabinet(new CloudInterface() {
							
							@Override
							public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
								LogUtils.i("ll1 responseBody:" + arg0);
								DoorBean DoorBean = (com.onekey.takeaway.bean.DoorBean) arg1;
								if (arg0 == CloudResponseStatus.Succ) {
//									LogUtils.i("ll1 responseBody:" + DoorBean.getDoorCode());
									Toast.makeText(getActivity(), "分配成功", Toast.LENGTH_SHORT).show();
									viewHolder.tvs[5].setText(Common.getDoorCodeCustom(DoorBean.getDoor().getDoorCode()));
								} else {
									Toast.makeText(getActivity(), DoorBean.getErrorInfo(), Toast.LENGTH_SHORT).show();
								}
							}
						}, mDataListAdapter.getItem(position).getOrderId());
					}
				});
				viewHolder.tvs[0].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CloudManager.getInstance().requestFoodStore(new CloudInterface() {
							
							@Override
							public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
								if (arg0 == CloudResponseStatus.Succ) {
									ResponseFoodStoreBean DoorBean = (com.onekey.takeaway.bean.ResponseFoodStoreBean) arg1;
									int total = 0;
									for (int i = 0; i < DoorBean.getStockList().size(); i++) {
										LogUtils.i("ll1 DoorBean.getStockList().get(i).getStock():" + DoorBean.getStockList().get(i).getStock());
										if (DoorBean.getStockList().get(i).getStock() == null) {
											continue;
										}
										total += Integer.parseInt(DoorBean.getStockList().get(i).getStock());
									}
									Toast.makeText(getActivity(), "剩余" + total + "个", Toast.LENGTH_SHORT).show();
								}
							}
						}, mDataListAdapter.getItem(position).getFoodId());						
					}
				});
				
				viewHolder.tvs[6].setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AbDialogUtil.showAlertDialog(getActivity(), "", "是否要取消订单? (下单时间" + mDataListAdapter.getItem(position).getGenTime() + ")", new AbDialogOnClickListener() {
							
							@Override
							public void onPositiveClick() {
								CloudManager.getInstance().requestCancelOrder(new CloudInterface() {
									
									@Override
									public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
										
										MsgBean info = (MsgBean) arg1;
										if (arg0 == CloudResponseStatus.Succ) {
											Toast.makeText(getActivity(), "取消成功", Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(getActivity(), info.getErrorInfo(), Toast.LENGTH_SHORT).show();
										}
									}
								}, mDataListAdapter.getItem(position).getOrderId() + "");				
							}
							
							@Override
							public void onNegativeClick() {
								AbDialogUtil.removeDialog(getActivity());
							}
						});
								
					}
				});
				

				viewHolder.tvs[7].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Toast.makeText(getActivity(), mDataListAdapter.getItem(position).getGenTime(), Toast.LENGTH_SHORT).show();
					}
					
				});
				
				
			}

			@Override
			public void initListViewItem(View convertView, ViewHolder holder,
					DataListAdapter<InnerOrderBean> adapter, int position) {
				// TODO Auto-generated method stub
				InnerOrderBean bean = adapter.getItem(position);
				holder.tvs[0].setText(bean.getFoodName());
				holder.tvs[1].setText(bean.getDevType() + "");
				String devID = bean.getDevId();
				if (devID != null) {
					devID = devID.substring(22);
				}
				holder.tvs[2].setText(devID);
				holder.tvs[3].setText(bean.getCustom());
				holder.tvs[4].setText(bean.getPay() + "");
				holder.tvs[5].setText(Common.getDoorCodeCustom(bean.getDoorId()) );
//				holder.tvs[6].setText(bean.getState());
				holder.tvs[6].setText("您的餐已备好 请扫码取餐");
				if (bean.getStateInt() == 2 || bean.getStateInt() == 5) {
					holder.tvs[6].setTextColor(Color.RED);
				} else {
					holder.tvs[6].setTextColor(Color.BLACK);
				}
				holder.tvs[7].setText(bean.getGenTime());
			}

			@Override
			public void initLayout(View convertView, ViewHolder holder) {
				// TODO Auto-generated method stub
				convertView.setBackgroundColor(getResources().getColor(R.color.title));
//				convertView.setBackgroundColor(getResources().getColor(android.R.color.white));
				holder.tvs = new TextView[9];
				holder.tvs[0] = (TextView)convertView.findViewById(R.id.tv1);
				holder.tvs[1] = (TextView)convertView.findViewById(R.id.tv2);
				holder.tvs[2] = (TextView)convertView.findViewById(R.id.tv3);
				holder.tvs[3] = (TextView)convertView.findViewById(R.id.tv4);
				holder.tvs[4] = (TextView)convertView.findViewById(R.id.tv5);
				holder.tvs[5] = (TextView)convertView.findViewById(R.id.tv6);
				holder.tvs[6] = (TextView)convertView.findViewById(R.id.tv7);
				holder.tvs[7] = (TextView)convertView.findViewById(R.id.tv41);
				
			}

			@Override
			public boolean isSameObject(InnerOrderBean t1, InnerOrderBean t2) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		mGridView.setAdapter(mDataListAdapter);
		mAbPullToRefreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView arg0) {
				loadData();
			}
		});
		mAbPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView arg0) {
				mCurrentPage = 1;
				CloudManager.getInstance().requestUnTakenOrderList(new CloudInterface() {
					
					@Override
					public void cloudCallback(CloudResponseStatus arg0, Object arg1) {
						if (arg0 == CloudResponseStatus.Succ) {
							OrderBean bean = (OrderBean) arg1;
							if (bean.getOrderList() != null) {
								if (!bean.getOrderList().isEmpty()) {
									if (mDataListAdapter.getDataShowList().isEmpty()) {

										if (mCurrentType.equals(FragmentOperation.tab4)) {
											playSoundScan();
										}
									} else {
										if (bean.getOrderList().get(0).getOrderId() != mDataListAdapter.getDataShowList().get(0).getOrderId()) {
											LogUtils.d("ll1 not equal");
											if (mCurrentType.equals(FragmentOperation.tab4)) {
												playSoundScan();
											}
										} else {
											LogUtils.d("ll1 equal");
										}
										
									}
									mDataListAdapter.setShowDataList(getOrderBeanList(bean.getOrderList()));
								} else {

									mDataListAdapter.setShowDataList(null);
								}
							}
						} 
						mAbPullToRefreshView.onHeaderRefreshFinish();
					}
				}, mCurrentDevType, "", mStatus);
			}
		});
		
//		mTv1.performClick();
		loadData();
//		initData();
		initTimer();
		return view;
	}
	
	List<InnerOrderBean> getOrderBeanList(List<InnerOrderBean> list) {
		List<InnerOrderBean> retList = new ArrayList<InnerOrderBean>();
		for (InnerOrderBean item : list) {
			if (item.getStateInt() != 7) {
				retList.add(item);
			}
		}
		return retList;
	}

	SoundPool soundPool = new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
	private boolean isFinishedLoad = false;
	int soundID;
	private void initSound() {
		soundID = soundPool.load(getActivity(), R.raw.please_scan, 1);
		soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				isFinishedLoad=true;
				
			}
		});
	}
	void playSound() {
		if (isFinishedLoad) {
//			soundPool.play(soundID, 0.6f, 0.6f, 0, 0, 1);
		}
		
		LogUtils.i("ll1 soundID=" + soundID + " isFinishedLoad=" + isFinishedLoad);
	}
	
	void playSoundScan() {
		if (isFinishedLoad) {
			soundPool.play(soundID, 0.9f, 0.9f, 0, 0, 1);
		}
		
		LogUtils.i("ll1 soundID=" + soundID + " isFinishedLoad=" + isFinishedLoad);
	}

    public void onDestroy() {
		super.onDestroy();
		mTimer.cancel();
		LogUtils.i("ll1 onDestroy");
	}
}
