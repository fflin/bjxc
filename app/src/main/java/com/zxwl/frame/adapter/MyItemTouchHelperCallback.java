package com.zxwl.frame.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by Alessandro on 12/01/2016.
 */
public class MyItemTouchHelperCallback extends ItemTouchHelper.Callback {

    CallbackItemTouch callbackItemTouch; // interface

    public MyItemTouchHelperCallback(CallbackItemTouch callbackItemTouch) {
        this.callbackItemTouch = callbackItemTouch;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false; // swiped disabled
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.START | ItemTouchHelper.END; // movements drag
//        int makeFlag = makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags); // as parameter, action drag and flags drag
        return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG, dragFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public float getMoveThreshold(RecyclerView.ViewHolder viewHolder) {
        callbackItemTouch.itemTouchOnMove(viewHolder.getAdapterPosition(), viewHolder.getAdapterPosition()); // information to the interface
        Log.i("Main","getMoveThreshold");
        return super.getMoveThreshold(viewHolder);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        // swiped disabled
    }
}
