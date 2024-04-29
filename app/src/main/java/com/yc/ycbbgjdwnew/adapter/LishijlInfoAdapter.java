package com.yc.ycbbgjdwnew.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yc.ycbbgjdwnew.R;
import com.yc.ycbbgjdwnew.entity.LsjlInfo;

import java.util.List;

/**
 * @Author ZJY
 * @Date 2024/4/10 13:46
 */
public class LishijlInfoAdapter extends BaseAdapter {
    private Context mContext;

    private List<LsjlInfo> mDatas;

    private LayoutInflater mInflater;


    public LishijlInfoAdapter(Context mContext, List<LsjlInfo> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        //Log.e("this.mContext==",this.mContext+"");
        mInflater = LayoutInflater.from(this.mContext);

    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LishijlInfoAdapter.ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.bb_ls_list2_info_item_layout, null);

            holder = new LishijlInfoAdapter.ViewHolder();

            holder.tvFj = convertView.findViewById(R.id.tvBbDyjlList2InfoItemFenjie);
            holder.tvBbzA = convertView.findViewById(R.id.tvBbDyjlList2InfoItemAbShice);
            holder.tvBbzB = convertView.findViewById(R.id.tvBbDyjlList2InfoItemBcShice);
            holder.tvBbzC = convertView.findViewById(R.id.tvBbDyjlList2InfoItemCaShice);
            holder.tvWcA = convertView.findViewById(R.id.tvBbDyjlList2InfoItemAbWucha);
            holder.tvWcB = convertView.findViewById(R.id.tvBbDyjlList2InfoItemBcWucha);
            holder.tvWcC = convertView.findViewById(R.id.tvBbDyjlList2InfoItemCaWucha);
            holder.tvBcBianbi = convertView.findViewById(R.id.tvBbDyjlList2InfoItemBiaocheng);
            //holder.tvWc = convertView.findViewById(R.id.tvBbDyjlList1InfoItemWucha);


            convertView.setTag(holder);

        } else {

            holder = (LishijlInfoAdapter.ViewHolder) convertView.getTag();
        }

        final LsjlInfo dataBean = mDatas.get(position);
        if (dataBean != null) {
            holder.tvFj.setText(dataBean.getFenjie()+"");
            holder.tvBbzA.setText(dataBean.getBbzA());
            holder.tvBbzB.setText(dataBean.getBbzB());
            holder.tvBbzC.setText(dataBean.getBbzC());
            holder.tvWcA.setText(dataBean.getWuchaA());
            holder.tvWcB.setText(dataBean.getWuchaB());
            holder.tvWcC.setText(dataBean.getWuchaC());
            holder.tvBcBianbi.setText(dataBean.getBiaocheng());

        }
        return convertView;
    }
    class ViewHolder {
        public TextView tvFj;
        public TextView tvBbzA;
        public TextView tvBbzB;
        public TextView tvBbzC;
        public TextView tvWcA;
        public TextView tvWcB;
        public TextView tvWcC;
        public TextView tvBcBianbi;

    }
}
