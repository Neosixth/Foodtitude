package com.neosixth.foodtitude

import android.widget.ListView

object UIUtils {

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     * @return true if the listView is successfully resized, false otherwise
     */
    fun setListViewHeightBasedOnItems(listView: ListView): Boolean {

        val listAdapter = listView.getAdapter()
        if (listAdapter != null) {

            val numberOfItems = listAdapter!!.getCount()

            // Get total height of all items.
            var totalItemsHeight = 0
            for (itemPos in 0 until numberOfItems) {
                val item = listAdapter!!.getView(itemPos, null, listView)
                item.measure(0, 0)
                totalItemsHeight += item.getMeasuredHeight()
            }

            // Get total height of all item dividers.
            val totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1)

            // Set list height.
            val params = listView.getLayoutParams()
            params.height = totalItemsHeight + totalDividersHeight
            listView.setLayoutParams(params)
            listView.requestLayout()

            return true

        } else {
            return false
        }

    }
}