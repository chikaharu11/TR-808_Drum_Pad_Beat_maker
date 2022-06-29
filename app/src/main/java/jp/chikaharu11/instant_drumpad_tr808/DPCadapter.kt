package jp.chikaharu11.instant_drumpad_tr808

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DPCadapter (val context: Context, private val dpcList: ArrayList<DPClist>) : BaseAdapter() {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_spinner_dropdown, null)
        val name = view.findViewById<TextView>(R.id.text)

        val dpc = dpcList[position]

        name.text = dpc.name

        if(position==1){
            // 背景色を変える
            convertView?.setBackgroundColor(Color.rgb(127, 127, 255));
        }
        if(position==3){
            // 背景色を変える
            convertView?.setBackgroundColor(Color.rgb(127, 127, 255));
        }
        if(position==5){
            // 背景色を変える
            convertView?.setBackgroundColor(Color.rgb(127, 127, 255));
        }
        if(position==7){
            // 背景色を変える
            convertView?.setBackgroundColor(Color.rgb(127, 127, 255));
        }

        return view
    }


    override fun getItem(position: Int): Any {
        return dpcList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dpcList.size
    }
}