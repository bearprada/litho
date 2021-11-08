// (c) Facebook, Inc. and its affiliates. Confidential and proprietary.

package com.facebook.samples.litho.kotlin.collection

/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.MotionEvent
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.litho.*
import com.facebook.litho.annotations.*
import com.facebook.litho.annotations.State
import com.facebook.litho.core.padding
import com.facebook.litho.flexbox.flex
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.ListRecyclerConfiguration
import com.facebook.litho.sections.widget.RecyclerBinderConfiguration
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.facebook.litho.sections.widget.RecyclerCollectionEventsController
import com.facebook.litho.widget.Button
import com.facebook.litho.widget.LinearLayoutInfo
import com.facebook.litho.widget.LithoRecyclerView


data class Person(val name: String, val id: Int)

@LayoutSpec
object FriendsCollectionKComponentSpec {
    const val step = 10
    const val max = 10000
    val eventsController = RecyclerCollectionEventsController()

    @OnCreateInitialState
    fun onCreateInitialState(c: ComponentContext,
                             state: StateValue<List<Person>>,
                             currentIndex: StateValue<Int>,
                             textInputHandle: StateValue<Handle>,
                             keyboardHeightInPx: StateValue<Int>) {
        val start = max - (step * 4)
        state.set(createPersonList(start, max))
        currentIndex.set(start)
        textInputHandle.set(Handle())
        keyboardHeightInPx.set(0)
    }

    @OnUpdateState
    fun onUpdateKeyboardHeight(
            keyboardHeightInPx: StateValue<Int>, @Param newKeyboardHeightInPx: Int) {
        keyboardHeightInPx.set(newKeyboardHeightInPx)
    }

    @OnUpdateState
    fun updateNextPageState(state: StateValue<List<Person>>, currentIndex: StateValue<Int>, @Param newPersonList: List<Person>, @Param newIndex: Int) {
        currentIndex.set(newIndex)
        state.set(newPersonList)
    }

    @OnEvent(ClickEvent::class)
    fun onClick(c: ComponentContext, @State currentIndex: Int) {
        triggerNextPage(c, currentIndex)
        eventsController.requestScrollToPosition(0, true)

    }


    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext, @State state: List<Person>, @State textInputHandle: Handle, @State keyboardHeightInPx: Int): Component {
        return Column.create(c)
                .child(
                        Button.create(c).text("update data").clickHandler(FriendsCollectionKComponent.onClick(c))
                )
                .child(
                        RecyclerCollectionComponent.create(c)
                                .disablePTR(true)
                                .recyclerTouchEventHandler(FriendsCollectionKComponent.onRecyclerTouch(c))
                                .recyclerConfiguration(ListRecyclerConfiguration.create()
                                    .reverseLayout(true)
                                    .orientation(LinearLayoutManager.VERTICAL)
                                    .recyclerBinderConfiguration(
                                            RecyclerBinderConfiguration.create().useBackgroundChangeSets(true).build())
                                    .build())
                                .touchInterceptor { _, _ ->
                                    LithoRecyclerView.TouchInterceptor.Result.CALL_SUPER
                                }
                                .widthPercent(100f)
                                .kotlinStyle(Style.flex(grow = 1f).padding(top = 8.dp))
                                .itemAnimator(DefaultItemAnimator())
                                .eventsController(eventsController)
                                // .heightPx(screenHeightInPx - keyboardHeightInPx) // Prototype
                                .recyclerConfiguration(ListRecyclerConfiguration.create()
                                        .reverseLayout(true)
                                        .   linearLayoutInfoFactory { context, orientation, reverse ->
                                            LinearLayoutInfo(LinearLayoutManager(context, orientation, reverse)
                                                    .also { it.stackFromEnd = true }) }
                                        .build())
                                .section(FriendSectionComponent.create(SectionContext(c))
                                        .personList(state)
                                        .myViewportChangedEventHandler(FriendsCollectionKComponent.onViewportChanged(c)))
                )
                .build()
    }
    @OnEvent(MyViewportChangedEvent::class)
    fun onViewportChanged(c: ComponentContext,
                          @FromEvent firstVisibleIndex: Int,
                          @State currentIndex: Int) {
        if (firstVisibleIndex == 0) {
             triggerNextPage(c, currentIndex)
        }
    }

    @OnEvent(TouchEvent::class)
    fun onRecyclerTouch(
            c: ComponentContext?,
            @FromEvent motionEvent: MotionEvent?): Boolean {
        return false
    }

    private fun createPersonList(from: Int, end: Int) = (from until end).map { Person("text $it", it) }

    private fun triggerNextPage(c: ComponentContext, currentIndex: Int) {
        Thread {
            Thread.sleep(2000)
            val newIndex = currentIndex - step
            FriendsCollectionKComponent.updateNextPageState(c, createPersonList(newIndex, max), newIndex)
        }.start()
    }
}

