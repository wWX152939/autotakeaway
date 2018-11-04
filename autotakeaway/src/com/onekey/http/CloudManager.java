package com.onekey.http;

import java.util.ArrayList;
import java.util.List;

import mythware.common.LogUtils;
import mythware.http.CloudUpdateVersionServer.CloudInterface;
import mythware.http.CloudUpdateVersionServer.CloudResponseStatus;
import mythware.http.DataManagerInterface;
import mythware.http.NetworkAsyncTask;

import com.onekey.common.Common;
import com.onekey.common.DataUtils;
import com.onekey.takeaway.bean.CabinetBean;
import com.onekey.takeaway.bean.DeviceBean;
import com.onekey.takeaway.bean.DeviceFoodBean;
import com.onekey.takeaway.bean.DoorBean;
import com.onekey.takeaway.bean.FoodBean;
import com.onekey.takeaway.bean.LoginBean;
import com.onekey.takeaway.bean.ModifyCabinetBean;
import com.onekey.takeaway.bean.ModifyDevBean;
import com.onekey.takeaway.bean.ModifyDeviceFoodBean;
import com.onekey.takeaway.bean.ModifyDeviceFoodBean.InnerModifyDeviceFoodBean;
import com.onekey.takeaway.bean.ModifyFoodBean;
import com.onekey.takeaway.bean.MsgBean;
import com.onekey.takeaway.bean.OrderBean;
import com.onekey.takeaway.bean.RequestCancelOrderBean;
import com.onekey.takeaway.bean.RequestDevBean;
import com.onekey.takeaway.bean.RequestFoodBean;
import com.onekey.takeaway.bean.RequestFoodStoreBean;
import com.onekey.takeaway.bean.RequestOrderListBean;
import com.onekey.takeaway.bean.RequestShopOrderBean;
import com.onekey.takeaway.bean.RequestWithStoreIdBean;
import com.onekey.takeaway.bean.ResponseFoodStoreBean;
import com.onekey.takeaway.bean.ShopBean;
import com.onekey.takeaway.bean.ShopOrderBean;
import com.onekey.takeaway.bean.TokenBean;

public class CloudManager {
	public final static String URL_CLOUD = "http://140.143.161.135:8080/";
	private final String URL_CLOUD_PAD = "http://140.143.161.135:8080/auto-sell-web/pad";
	private final String URL_login = URL_CLOUD_PAD + "/login/login.do";
	private final String URL_request_info = URL_CLOUD_PAD + "/shop/orderList.do";
//	private final String URL_modify_food = URL_CLOUD_PAD + "/shop/modifyFood.do";
	private final String URL_modify_food = URL_CLOUD_PAD + "/slot/modifyManmadeStock.do";
	private final String URL_request_shop_devices = URL_CLOUD_PAD + "/dev/list.do";
	private final String URL_modify_device_status = URL_CLOUD_PAD + "/dev/modifystatus.do";
	private final String URL_request_order_list = URL_CLOUD_PAD + "/shop/requestOrdersByState.do";
	private final String URL_logout = URL_CLOUD_PAD + "/logout/logout.do";
	

	private final String URL_request_cabinet_info = URL_CLOUD_PAD + "/cabinet/detailInfo.do";
	private final String URL_request_machine_food_store = URL_CLOUD_PAD + "/shop/machineMadeStock.do";
	private final String URL_request_shop_food_store = URL_CLOUD_PAD + "/shop/manMadeStock.do";
	private final String URL_modify_machine_food_store = URL_CLOUD_PAD + "/slot/modifySlotStock.do";
	private final String URL_modify_cabinet_status = URL_CLOUD_PAD + "/door/modifyDoorStatus.do";
	private final String URL_find_unused_cabinet = URL_CLOUD_PAD + "/cabinet/unusedDoor.do";
	private final String URL_request_food_store = URL_CLOUD_PAD + "/store/stockFromFoodAndStore.do";
	private final String URL_request_cancel_order = URL_CLOUD_PAD + "/cancleOrder.do";
	
	private ShopBean mShopBean;
	
	public ShopBean getShopBean() {
		return mShopBean;
	}
	
	public void generateShopBean() {
		mShopBean = new ShopBean();
		mShopBean.setShopID("6fdf23bd-9172-4775-b5ac-8b5efd6c677f");
//		mShopBean.setShopID("1");
		mShopBean.setTitle("1号店");
		mShopBean.setInfo("自动化零售店");
		mShopBean.setToken("eddc225a-12c4-4911-936a-42c3e663bb64");
	}
	
	private static CloudManager mClassCloudManager;
	public static synchronized CloudManager getInstance() {
		if (mClassCloudManager == null) {
			mClassCloudManager = new CloudManager();
		}
		return mClassCloudManager;
	}
	
	public void login(final CloudInterface cloudInterface, final String account, final String password) {
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				ShopBean info = (ShopBean) DataUtils.getObject(ShopBean.class, responseBody);

				if (info.getErrorcode() == 0) {
					LogUtils.i("ll1 code=" + info.getErrorcode());
					mShopBean = info;
					Common.s_SettingPreferences.SetToken(info.getToken());
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					LogUtils.i("ll1 code=" + info.getErrorcode() + " Common.s_SettingPreferences.GetToken():" + Common.s_SettingPreferences.GetToken());
					if (info.getErrorcode() == 99) {
						logout(new CloudInterface() {
							
							@Override
							public void cloudCallback(CloudResponseStatus arg0, Object arg1) {

								LogUtils.i("ll1 arg0:" + arg0);
								if (arg0 == CloudResponseStatus.Succ) {
									login(cloudInterface, account, password);
								}
							}
						}, Common.s_SettingPreferences.GetToken());
					}
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		LoginBean loginBean = new LoginBean();
		loginBean.setAccount(account);
		loginBean.setPassword(password);
		String requestBody = DataUtils.beanToJson(loginBean);
		networkAsyncTask.setUrl(URL_login).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestBody).execute();
	}
	

	public void logout(final CloudInterface cloudInterface) {
		logout(cloudInterface, null);
	}
	
	public void logout(final CloudInterface cloudInterface, String token) {
		if (token == null) {
			if (mShopBean == null) {
				cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
				return;
			}
		} else {

			LogUtils.i("ll1 token:" + token);
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				MsgBean info = (MsgBean) DataUtils.getObject(MsgBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		String requestBody ;
		if (token == null) {
			requestBody = getToken();
		} else {
			TokenBean TokenBean = new TokenBean();
			TokenBean.setToken(token);
			requestBody = DataUtils.beanToJson(TokenBean);
		}
		networkAsyncTask.setUrl(URL_logout).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestBody).execute();
	}
	
	public String getToken() {
		if (mShopBean == null) {
			return null;
		}
		TokenBean bean = new TokenBean();
		bean.setToken(mShopBean.getToken());
		return DataUtils.beanToJson(bean);
	}

	public void requestDeviceFoodList(final CloudInterface cloudInterface) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				DeviceFoodBean info = (DeviceFoodBean) DataUtils.getObject(DeviceFoodBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestFoodBean bean = new RequestFoodBean();
		bean.setShopID(mShopBean.getShopID());
		String requestString = DataUtils.beanToJson(bean);
		networkAsyncTask.setUrl(URL_request_machine_food_store).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void requestFoodList(final CloudInterface cloudInterface) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				FoodBean info = (FoodBean) DataUtils.getObject(FoodBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestFoodBean bean = new RequestFoodBean();
		bean.setShopID(mShopBean.getShopID());
		String requestString = DataUtils.beanToJson(bean);
		networkAsyncTask.setUrl(URL_request_shop_food_store).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	/**
	 * 
	 * @param cloudInterface
	 * @param foodID
	 * @param type 1add 2delete 3modify
	 * @param total
	 */
	public void modifyDeviceFood(final CloudInterface cloudInterface, String slotID, int type, int total) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				MsgBean info = (MsgBean) DataUtils.getObject(MsgBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		ModifyDeviceFoodBean bean = new ModifyDeviceFoodBean();
		List<InnerModifyDeviceFoodBean> InnerbeanList = new ArrayList<InnerModifyDeviceFoodBean>();
		InnerModifyDeviceFoodBean Innerbean = new InnerModifyDeviceFoodBean();
		Innerbean.setSlotID(slotID);
		Innerbean.setTotal(total);
		Innerbean.setType(type);
		InnerbeanList.add(Innerbean);
		bean.setSlotList(InnerbeanList);
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_modify_machine_food_store).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void modifyFood(final CloudInterface cloudInterface, String foodID, int type, int total) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				FoodBean info = (FoodBean) DataUtils.getObject(FoodBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		ModifyFoodBean bean = new ModifyFoodBean();
		bean.setFoodId(foodID);
		bean.setTotal(total);
		bean.setType(type);
		bean.setStoreId(mShopBean.getShopID());
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_modify_food).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void requestCabinetList(final CloudInterface cloudInterface) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				CabinetBean info = (CabinetBean) DataUtils.getObject(CabinetBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestWithStoreIdBean bean = new RequestWithStoreIdBean();
		bean.setStoreId(mShopBean.getShopID());
		String requestString = DataUtils.beanToJson(bean);
		networkAsyncTask.setUrl(URL_request_cabinet_info).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void modifyCabinet(final CloudInterface cloudInterface, String doorId, boolean frontDoor, String status) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				MsgBean info = (MsgBean) DataUtils.getObject(MsgBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		ModifyCabinetBean bean = new ModifyCabinetBean();
		bean.setDoorId(doorId);
		bean.setStatus(status);
		bean.setDoorDirection(frontDoor ? "frontDoor" : "backDoor");
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_modify_cabinet_status).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	@Deprecated
	public void requestDevList(final CloudInterface cloudInterface) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				DeviceBean info = (DeviceBean) DataUtils.getObject(DeviceBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestDevBean bean = new RequestDevBean();
		bean.setToken(mShopBean.getToken());
		bean.setShopID(mShopBean.getShopID());
		String requestString = DataUtils.beanToJson(bean);
		networkAsyncTask.setUrl(URL_request_shop_devices).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	@Deprecated
	public void ModifyDevStatus(final CloudInterface cloudInterface, String deviceID, int status) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				DeviceBean info = (DeviceBean) DataUtils.getObject(DeviceBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		ModifyDevBean bean = new ModifyDevBean();
		bean.setToken(mShopBean.getToken());
		bean.setDeviceID(deviceID);
		bean.setStatus(status);
		String requestString = DataUtils.beanToJson(bean);
		networkAsyncTask.setUrl(URL_modify_device_status).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void requestShopOrderList(final CloudInterface cloudInterface, String orderId) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				ShopOrderBean info = (ShopOrderBean) DataUtils.getObject(ShopOrderBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestShopOrderBean bean = new RequestShopOrderBean();
		bean.setToken(mShopBean.getToken());
		bean.setShopID(mShopBean.getShopID());
//		bean.setShopID("1");
		bean.setOrderId(orderId);
		String requestString = DataUtils.beanToJson(bean);
		networkAsyncTask.setUrl(URL_request_info).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void requestUnusedCabinet(final CloudInterface cloudInterface, int orderId) {
		
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				DoorBean info = (DoorBean) DataUtils.getObject(DoorBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestWithStoreIdBean bean = new RequestWithStoreIdBean();
		bean.setStoreId(mShopBean.getShopID());
		bean.setOrderId(orderId + "");
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_find_unused_cabinet).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	public void requestFoodStore(final CloudInterface cloudInterface, String foodId) {
		
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				ResponseFoodStoreBean info = (ResponseFoodStoreBean) DataUtils.getObject(ResponseFoodStoreBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestFoodStoreBean bean = new RequestFoodStoreBean();
		bean.setStoreId(mShopBean.getShopID());
		bean.setFoodId(foodId);
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_request_food_store).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	
	public void requestOrderList(final CloudInterface cloudInterface, int devType, String orderId, String orderState) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
//				responseBody = responseBody.replace("0000000000000000000000", "_0000000000000000000000");
				OrderBean info = (OrderBean) DataUtils.getObject(OrderBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestOrderListBean bean = new RequestOrderListBean();
		bean.setToken(mShopBean.getToken());
		bean.setShopID(mShopBean.getShopID());
		bean.setDevType(devType);
		bean.setOrderId(orderId);
//		bean.setShopID("1");
		bean.setOrderState(orderState);
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_request_order_list).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
	
	public void requestCancelOrder(final CloudInterface cloudInterface, String orderId) {
		if (mShopBean == null) {
			cloudInterface.cloudCallback(CloudResponseStatus.Failed, null);
			return;
		}
		NetworkAsyncTask networkAsyncTask = new NetworkAsyncTask(new DataManagerInterface() {
			
			@Override
			public void getDataOnResult(String responseBody) {
				LogUtils.i("ll1 responseBody:" + responseBody);
				if (responseBody == null) {
					cloudInterface.cloudCallback(CloudResponseStatus.ErrorNetwork, null);
					return;
				}
				MsgBean info = (MsgBean) DataUtils.getObject(MsgBean.class, responseBody);
				if (info.getErrorcode() == 0) {
					cloudInterface.cloudCallback(CloudResponseStatus.Succ, info);
				} else {
					cloudInterface.cloudCallback(CloudResponseStatus.Failed, info);
				}
			}
		});
		RequestCancelOrderBean bean = new RequestCancelOrderBean();
		bean.setOrderID(orderId);
		String requestString = DataUtils.beanToJson(bean);
		LogUtils.i("ll1 requestString:" + requestString);
		networkAsyncTask.setUrl(URL_request_cancel_order).setRequestType(NetworkAsyncTask.TYPE_POST).setRequestBody(requestString).execute();
	}
}
