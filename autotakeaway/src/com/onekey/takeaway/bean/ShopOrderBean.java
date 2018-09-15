package com.onekey.takeaway.bean;

import java.util.List;

public class ShopOrderBean extends MsgBean{
	
	List<InnerShopOrderBean> orderList;
	String totalPage;
	String payTotal;
	

	public List<InnerShopOrderBean> getOrderList() {
		return orderList;
	}


	public void setOrderList(List<InnerShopOrderBean> orderList) {
		this.orderList = orderList;
	}


	public String getTotalPage() {
		return totalPage;
	}


	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}


	public String getPayTotal() {
		return payTotal;
	}


	public void setPayTotal(String payTotal) {
		this.payTotal = payTotal;
	}


	@Override
	public String toString() {
		return "ShopOrderBean [orderList=" + orderList + ", totalPage="
				+ totalPage + ", payTotal=" + payTotal + "]";
	}


	public static class InnerShopOrderBean {
		String number;
		String custom;
		String pay;
		String food;
		int orderId; 
		String genTime;
		public int getOrderId() {
			return orderId;
		}
		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}
		public String getGenTime() {
			return genTime;
		}
		public void setGenTime(String genTime) {
			this.genTime = genTime;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getCustom() {
			return custom;
		}
		public void setCustom(String custom) {
			this.custom = custom;
		}
		public String getPay() {
			return pay;
		}
		public void setPay(String pay) {
			this.pay = pay;
		}
		public String getFood() {
			return food;
		}
		public void setFood(String food) {
			this.food = food;
		}
		@Override
		public String toString() {
			return "InnerShopOrderBean [number=" + number + ", custom="
					+ custom + ", pay=" + pay + ", food=" + food + ", orderId="
					+ orderId + ", genTime=" + genTime + "]";
		}
		
		
	}
}
