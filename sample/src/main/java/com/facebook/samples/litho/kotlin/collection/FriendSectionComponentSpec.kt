package com.facebook.samples.litho.kotlin.collection

import com.facebook.litho.StateValue
import com.facebook.litho.annotations.*
import com.facebook.litho.sections.ChangesInfo
import com.facebook.litho.sections.Children
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.annotations.*
import com.facebook.litho.sections.common.DataDiffSection
import com.facebook.litho.sections.common.RenderEvent
import com.facebook.litho.sections.widget.RecyclerCollectionEventsController
import com.facebook.litho.widget.ComponentRenderInfo
import com.facebook.litho.widget.RenderInfo
import com.facebook.litho.widget.SmoothScrollAlignmentType
import com.facebook.samples.litho.kotlin.animations.expandableelement.ExpandableElementMe
import java.util.concurrent.atomic.AtomicInteger

@GroupSectionSpec(events = [
    MyViewportChangedEvent::class
])
object FriendSectionComponentSpec {
    @OnCreateChildren
    fun onCreateChildren(c: SectionContext, @Prop personList: List<Person>): Children =
            Children.create()
                    .child(
                            DataDiffSection.create<Person>(SectionContext(c))
                                    .data(personList)
                                    .renderEventHandler(FriendSectionComponent.onRender(c)))
                    .build()

    @OnEvent(RenderEvent::class)
    fun onRender(c: SectionContext, @FromEvent model: Person): RenderInfo =
            ComponentRenderInfo.create().component(
                    ExpandableElementMe(messageText = model.name, timestamp = model.name, seen = true)).build()


    @OnCreateInitialState
    fun onCreateInitialState(firstFullyVisibleIndexState: StateValue<AtomicInteger>, totalPersonCountState: StateValue<AtomicInteger>) {
        firstFullyVisibleIndexState.set(AtomicInteger(-1))
        totalPersonCountState.set(AtomicInteger(0))
    }

    @OnViewportChanged
    fun onViewportChanged(
            c: SectionContext,
            firstVisible: Int,
            lastVisible: Int,
            totalCount: Int,
            firstFullyVisible: Int,
            lastFullyVisible: Int,
            @State firstFullyVisibleIndexState: AtomicInteger,
    ) {
        println("FriendSectionComponentSpec onViewportChanged : firstVisible = $firstVisible, lastVisible = $lastVisible, totalCount = $totalCount, firstFullyVisible = $firstFullyVisible, lastFullyVisible = $lastFullyVisible")
        firstFullyVisibleIndexState.set(firstFullyVisible)
        FriendSectionComponent.getMyViewportChangedEventHandler(c)?.let {
            FriendSectionComponent.dispatchMyViewportChangedEvent(it, firstVisible, lastVisible, firstFullyVisible, lastFullyVisible, totalCount)
        }
    }

    @OnDataBound
    fun onDataBound(c: SectionContext,
                    @State firstFullyVisibleIndexState: AtomicInteger,
                    @Prop personList: List<Person>,
                    @State totalPersonCountState: AtomicInteger) {
        if (totalPersonCountState.getAndSet(personList.size) == 0) {
            FriendSectionComponent.requestFocus(c, 0)
        }
    }
}
