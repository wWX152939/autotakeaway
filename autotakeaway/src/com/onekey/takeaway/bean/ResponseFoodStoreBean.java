package com.onekey.takeaway.bean;

import java.util.List;



public class ResponseFoodStoreBean extends MsgBean{
	List<InnerRequestFoodStoreBean> stockList;
	

	@Override
	public String toString() {
		return "ResponseFoodStoreBean [stockList=" + stockList + "]";
	}


	public List<InnerRequestFoodStoreBean> getStockList() {
		return stockList;
	}


	public void setStockList(List<InnerRequestFoodStoreBean> stockList) {
		this.stockList = stockList;
	}


	public static class InnerRequestFoodStoreBean {
		String devCode;
		String stock;
		String slotCode;
		public String getDevCode() {
			return devCode;
		}
		public void setDevCode(String devCode) {
			this.devCode = devCode;
		}
		public String getStock() {
			return stock;
		}
		public void setStock(String stock) {
			this.stock = stock;
		}
		public String getSlotCode() {
			return slotCode;
		}
		public void setSlotCode(String slotCode) {
			this.slotCode = slotCode;
		}
		@Override
		public String toString() {
			return "InnerRequestFoodStoreBean [devCode=" + devCode + ", stock="
					+ stock + ", slotCode=" + slotCode + "]";
		}
		
	}
	

}
