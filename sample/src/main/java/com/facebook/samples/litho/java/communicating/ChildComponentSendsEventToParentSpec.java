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

package com.facebook.samples.litho.java.communicating;

import android.graphics.Color;
import com.facebook.litho.Border;
import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.samples.litho.java.communicating.CommunicatingFromChildToParent.ComponentEventObserver;
import com.facebook.yoga.YogaEdge;

// start_demo
@LayoutSpec(events = {NotifyParentEvent.class})
class ChildComponentSendsEventToParentSpec {

  @OnCreateLayout
  static Component onCreateLayout(ComponentContext c) {
    return Column.create(c)
        .marginDip(YogaEdge.ALL, 30)
        .child(Text.create(c).text("ChildComponent").textSizeDip(20))
        .child(
            Text.create(c)
                .paddingDip(YogaEdge.ALL, 5)
                .border(
                    Border.create(c)
                        .color(YogaEdge.ALL, Color.BLACK)
                        .radiusDip(2f)
                        .widthDip(YogaEdge.ALL, 1)
                        .build())
                .text("Click to send event to parent!")
                .textSizeDip(15)
                .clickHandler(ChildComponentSendsEventToParent.onClickEvent(c)))
        .child(
            Text.create(c)
                .paddingDip(YogaEdge.ALL, 5)
                .border(
                    Border.create(c)
                        .color(YogaEdge.ALL, Color.BLACK)
                        .radiusDip(2f)
                        .widthDip(YogaEdge.ALL, 1)
                        .build())
                .text("Click to send event to Activity!")
                .textSizeDip(15)
                .clickHandler(ChildComponentSendsEventToParent.onSendEventToActivity(c)))
        .build();
  }

  @OnEvent(ClickEvent.class)
  static void onClickEvent(ComponentContext c) {
    ChildComponentSendsEventToParent.dispatchNotifyParentEvent(
        ChildComponentSendsEventToParent.getNotifyParentEventHandler(c));
  }

  @OnEvent(ClickEvent.class)
  static void onSendEventToActivity(ComponentContext c, @Prop ComponentEventObserver observer) {
    observer.onComponentClicked();
  }
}
// end_demo
