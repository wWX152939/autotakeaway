package com.onekey.takeaway.bean;

import java.util.List;

public class FoodBean extends MsgBean{
	
	List<InnerFoodBean> stocks;
	

	public List<InnerFoodBean> getFoodlist() {
		return stocks;
	}


	public void setFoodlist(List<InnerFoodBean> foodlist) {
		this.stocks = foodlist;
	}


	@Override
	public String toString() {
		return "FoodBean [foodlist=" + stocks + "]";
	}


	public static class InnerFoodBean {
		String foodId;
		int stock;
		String foodName;
		public String getFoodId() {
			return foodId;
		}
		public void setFoodId(String foodId) {
			this.foodId = foodId;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		public String getFoodName() {
			return foodName;
		}
		public void setFoodName(String foodName) {
			this.foodName = foodName;
		}
		@Override
		public String toString() {
			return "InnerFoodBean [foodId=" + foodId + ", stock=" + stock
					+ ", foodName=" + foodName + "]";
		}
		
		
	}
}
