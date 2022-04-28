package jp.chikaharu11.instant_drumpad_tr808

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class SequencerView (context : Context, attributeSet : AttributeSet): LinearLayout(context, attributeSet) {
    init {
        View.inflate(context, R.layout.sequencer_list, this)
    }
}