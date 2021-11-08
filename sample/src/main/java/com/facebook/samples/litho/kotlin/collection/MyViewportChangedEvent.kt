package com.facebook.samples.litho.kotlin.collection

import com.facebook.litho.annotations.Event

@Event
class MyViewportChangedEvent {
    @JvmField var firstVisibleIndex: Int = 0
    @JvmField var lastVisibleIndex: Int = 0
    @JvmField var firstFullyVisibleIndex: Int = 0
    @JvmField var lastFullyVisibleIndex: Int = 0
    @JvmField var listLength: Int = 0
}
