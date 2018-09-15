package com.onekey.takeaway.ux;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressLint("HandlerLeak") 
public class DataListAdapter<T> extends BaseAdapter {
	
    private Context mContext;
    // 重要的接口，用于adapter向adapterview主体类传递数�?
    private ListAdapterInterface<T> mManager;
    // 保存�?有的对象
    private List<T> mDataAllList = new ArrayList<T>();
    // 保存要显示对�?
    private List<T> mDataShowList = new ArrayList<T>();
    // 保存选中的列表项索引
    private List<Integer> mSelectedPositionList = new ArrayList<Integer>();
    
    /**
     * 构�?�函�?
     * @param context
     * @param manager
     */
    public DataListAdapter(Context context, ListAdapterInterface<T> manager) {
    	this(context, manager, null);
    }
    
    /**
     * 构�?�函�?
     * 
     * @param context
     * @param manager
     */
    public DataListAdapter(Context context, ListAdapterInterface<T> manager, List<T> datalist) {
    	super();
        mContext = context;
        mManager = manager;
        if (datalist != null) {
        	mDataShowList = datalist;
        }
    }

    /**
     * 子线程�?�知�?线程刷新界面的处理函�?
     */
    @SuppressLint("HandlerLeak") 
    public Handler mHander = new Handler() {
        public void handleMessage(Message msg) {
            notifyDataSetChanged();
        }
    };   
    
    /**
     * 单独刷新指定行，�?要在UI线程调用
     * @param listview
     * @param position
     */
    @SuppressWarnings("rawtypes")
	public void updateItemView(AbsListView listview, int position) {
		int firstPos = listview.getFirstVisiblePosition();
		int lastPos = listview.getLastVisiblePosition();
		
		if (position >= firstPos && position <= lastPos) { // 可见才更新，不可见则在getView()时更�?
			// listview.getChildAt(i)获得的是当前可见的第i个item的view
			View convertView = listview.getChildAt(position - firstPos);
			if (mSelectedPositionList.contains((Integer) position)) {
				if (ConvertViewBgInterface.class.isInstance(mManager)) {
					convertView
							.setBackgroundResource(((ConvertViewBgInterface) mManager)
									.getSelectBg());
				}
			} else {
				if (ConvertViewBgInterface.class.isInstance(mManager)) {
					convertView
							.setBackgroundResource(((ConvertViewBgInterface) mManager)
									.getNormalBg());
				}
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();

			try {
				// 初始化列表项视图显示内容
				mManager.initListViewItem(convertView, holder, this, position);
				// AdapterView设置列表项的监听
				mManager.regesterListeners(holder, position);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
    }

    /**
     * 刷新界面,主线程直接刷�?,否�?�发送message
     */
    public void fresh() {
    	if (Looper.getMainLooper() == Looper.myLooper()) {
    		notifyDataSetChanged();
    	} else {
    		mHander.sendEmptyMessage(0);
    	}
    }

    /**
     * 清除数据列表�?有数�?
     */
    public void clearAll() {
        mDataAllList.clear();
        mDataShowList.clear();
        clearSelection();
        fresh();
    }

    /**
     * 设置数据列表
     * 
     * @param List
     */
    public void setDataList(List<T> list) {
        mDataAllList.clear();
        mDataShowList.clear();
        clearSelection();
        addDataList(list);
    }
    
    /**
     * 设置数据显示列表
     * @param list
     */
    public void setShowDataList(List<T> list) {
    	mDataShowList.clear();
    	if (list != null) {
    		mDataShowList.addAll(list);
    	}
    	clearSelection();
    	fresh();
    }
    
    /**
     * 添加数据列表，不带清除的添加
     * 
     * @param List
     */
    public void addShowDataList(List<T> list) {
        if (list != null) {
        	mDataShowList.addAll(list);
            fresh();
        }
    }
    
    /**
     * 添加数据，不带清除的添加
     * 
     * @param t
     */
    public void addShowData(T t) {
        if (t != null) {
        	mDataShowList.add(t);
            fresh();
        }
    }

    /**
     * 添加数据列表，不带清除的添加
     * 
     * @param List
     */
    public void addDataList(List<T> list) {
        if (list != null) {
            mDataAllList.addAll(list);
            fresh();
        }
    }

    /**
     * 添加数据到数据列�?
     * 
     * @param t
     */
    public void addDataToList(T t) {
        mDataAllList.add(t);
        mDataShowList.add(t);
        fresh();
    }

    /**
     * 删除指定的数据对�?
     * @param t
     */
    public void deleteData(T t) {
        for (int i = 0; i < mDataAllList.size(); i++) {
            if (mManager.isSameObject(mDataAllList.get(i), t)) {
                mDataAllList.remove(i);
                break;
            }
        }

        for (int i = 0; i < mDataShowList.size(); i++) {
            if (mManager.isSameObject(mDataShowList.get(i), t)) {
                mDataShowList.remove(i);
                break;
            }
        }

        fresh();
    }
    
    /**
     * 删除指定行数据对�?
     * @param t
     */
    public void deleteData(int mLine) {
    	mDataShowList.remove(mLine);
        fresh();
    }

    /**
     * 删除列表中的数据对象
     * 
     * @param list
     */
    public void deleteData(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            deleteData(t);
        }
        
        fresh();
    }

    /**
     * 更新数据对象
     * 
     * @param t
     */
    public void updateData(T t) {
        for (int i = 0; i < mDataAllList.size(); i++) {
            if (mManager.isSameObject(mDataAllList.get(i), t)) {
                mDataAllList.set(i, t);
                break;
            }
        }
        
        for (int i = 0; i < mDataShowList.size(); i++) {
            if (mManager.isSameObject(mDataShowList.get(i), t)) {
                mDataShowList.set(i, t);
                break;
            }
        }
        fresh();
    }
    
    /**
     * 更新数据对象
     * 
     * @param t
     */
    public void updateDataList(List<T> list) {
    	for (T t : list) {
    		for (int i = 0; i < mDataAllList.size(); i++) {
                if (mManager.isSameObject(mDataAllList.get(i), t)) {
                    mDataAllList.set(i, t);
                    break;
                }
            }
            
            for (int i = 0; i < mDataShowList.size(); i++) {
                if (mManager.isSameObject(mDataShowList.get(i), t)) {
                    mDataShowList.set(i, t);
                    break;
                }
            }
    	}
        
        fresh();
    }
    

    /**
     * 更新指定行数据对�?
     * 
     * @param t
     */
    public void updateData(int location, T t) {
    	mDataShowList.set(location, t);
        fresh();
    }
    
    /**
     * 获取�?有数据对象列�?
     * 
     * @return
     */
    public final List<T> getDataList() {
        return mDataAllList;
    }
    
    /**
     * 获取数据显示列表
     * @return
     */
    public final List<T> getDataShowList() {
    	return mDataShowList;
    }

    /**
     * 要显示的列表项数�?
     */
    @Override
    public int getCount() {
        return mDataShowList.size();
    }

    /**
     * 返回指定索引的数据对�?
     */
    @Override
    public T getItem(int position) {
        return mDataShowList.get(position);
    }

    /**
     * 获取指定的索�?
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    public boolean isDataEmpty() {
    	return mDataShowList == null || mDataShowList.isEmpty();
    }

    /**
     * 显示列表项视图的主体函数
     */
	@SuppressWarnings("rawtypes")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
		
    	// 用来设置Tag，以便循环利用已经隐藏的列表项视图对�?
        ViewHolder holder;

        if (convertView != null) {
        	// 循环利用隐藏的列表项
            holder = (ViewHolder) convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext)
            				.inflate(mManager.getLayoutId(), null);

            mManager.initLayout(convertView, holder);
            convertView.setTag(holder);
        }
        
        if (mSelectedPositionList.contains((Integer) position)) {
        	if (ConvertViewBgInterface.class.isInstance(mManager)) {
        		convertView.setBackgroundResource(((ConvertViewBgInterface)mManager).getSelectBg()); 
        	}
        } else {
        	if (ConvertViewBgInterface.class.isInstance(mManager)) {
        		convertView.setBackgroundResource(((ConvertViewBgInterface)mManager).getNormalBg()); 
        	}
        }

        // 初始化列表项视图显示内容
        mManager.initListViewItem(convertView, holder, this, position);
        
        // AdapterView设置列表项的监听
        mManager.regesterListeners(holder, position);
     
        return convertView;
    }
	
    /**
     * 单�??
     * 
     * @param position
     * @param isSeleced
     */
    public void setSelected(int position, boolean isSeleced) {
        mSelectedPositionList.clear();
        if (isSeleced) {
            mSelectedPositionList.add((Integer) position);
        }
        notifyDataSetChanged();
    }

    /**
     * 多�??
     * 
     * @param position
     */
    public void setPickSelected(int position) {
        boolean isSeleced = false;
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
        	/*原先是�?�中,则改为取�?*/
            if (position == mSelectedPositionList.get(i)) {
                mSelectedPositionList.remove(i);
                isSeleced = true;
            }
        }
        if (!isSeleced) {
            mSelectedPositionList.add((Integer) position);
        }
        notifyDataSetChanged();
    }
    
    /**
     * 判断选择列表中是否包含指定位�?
     * @param position
     * @return
     */
    public boolean isContainPosition(int position) {
    	for (Integer temp : mSelectedPositionList) {
    		if (position == temp) {
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * 获取已�?�择位置列表
     * 
     * @return
     */
    public final List<Integer> getSelectedList() {
        return mSelectedPositionList;
    }

    /**
     * 获取已�?�择的文件列�?
     * 
     * @return
     */
    public final List<T> getSelectedDatas() {
        List<T> selectedDatas = new ArrayList<T>();
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            selectedDatas.add(getItem(mSelectedPositionList.get(i)));
        }
        return selectedDatas;
    }

    /**
     * 选中�?有文�?
     */
    public void setSelectedAll() {
        mSelectedPositionList.clear();
        for (int i = 0; i < getCount(); i++) {
            mSelectedPositionList.add(i);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除�?有�?�择�?
     */
    public void clearSelection() {
        mSelectedPositionList.clear();
    }
    
    /**
     * 设置特定行�?�中
     * @param list
     */
    public void setSelectedList(List<Integer> list) {
    	mSelectedPositionList.clear();
    	mSelectedPositionList.addAll(list);
    	
    	notifyDataSetChanged();
    }
    
    public interface TickListAdapterInterface<T> extends ListAdapterInterface<T> {
    	int[] getEditTextNums();
    	boolean isDataValid();
    }
    
    public interface ConvertViewBgInterface<T> extends ListAdapterInterface<T> {
    	int getSelectBg();
    	int getNormalBg();
    }

    /**
     * 文件列表适配器接口，�?要在fragment中实�?
     * 
     * @author yuanlu
     * 
     */
    public interface ListAdapterInterface<T> {
    	// 获取列表布局资源ID
        int getLayoutId();

        // 注册组件的监听器
        void regesterListeners(ViewHolder viewHolder, final int position);
        
        // 初始化列表项视图
        void initListViewItem(View convertView, 
        						ViewHolder holder,
        						DataListAdapter<T> adapter,
        						int position);
        
        // 布局初始�?
        void initLayout(View convertView, ViewHolder holder);
        
        // 判断两个对象是否是同�?个对象，实际上要根据对象里面的ID来比较，而不是真正意义上的完全相同的对象
        boolean isSameObject(T t1, T t2);
    }

    public static class ViewHolder {
    	public View root;
    	public View[] vs;
        public TextView[] tvs;
        public ImageView[] ivs;
        public EditText[] ets;
        public Button[] bs;
        public CheckBox[] cbs;
        public AutoCompleteTextView[] actvs;
        public Spinner sp;
    }
}
