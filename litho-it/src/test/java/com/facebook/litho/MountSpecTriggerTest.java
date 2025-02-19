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

package com.facebook.litho;

import static com.facebook.litho.LifecycleStep.getSteps;
import static org.assertj.core.api.Assertions.assertThat;

import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
import com.facebook.litho.testing.LithoViewRule;
import com.facebook.litho.testing.testrunner.LithoTestRunner;
import com.facebook.litho.widget.MountSpecTriggerTester;
import com.facebook.litho.widget.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(LithoTestRunner.class)
public class MountSpecTriggerTest {

  public final @Rule LithoViewRule mLithoViewRule = new LithoViewRule();

  @Test
  public void mountSpec_setRootAndTriggerEvent_eventIsTriggered() {
    final ComponentContext componentContext = mLithoViewRule.getContext();
    final AtomicReference<Object> triggerObjectRef = new AtomicReference<>();
    final Handle triggerHandle = new Handle();
    final List<LifecycleStep.StepInfo> info = new ArrayList<>();
    final Component component =
        MountSpecTriggerTester.create(componentContext)
            .steps(info)
            .triggerObjectRef(triggerObjectRef)
            .handle(triggerHandle)
            .build();
    mLithoViewRule.setRoot(component);

    mLithoViewRule.attachToWindow().measure().layout();

    final Object bazObject = new Object();
    MountSpecTriggerTester.triggerTestEvent(
        mLithoViewRule.getComponentTree().getContext(), triggerHandle, bazObject);

    assertThat(getSteps(info))
        .describedAs("Should call @OnTrigger method")
        .containsExactly(LifecycleStep.ON_TRIGGER);

    assertThat(triggerObjectRef.get())
        .describedAs("Event object is correctly passed")
        .isEqualTo(bazObject);
  }

  @Test
  public void mountSpec_setRootAndTriggerEvent_eventIsTriggered_handle_used_in_child() {
    final ComponentContext componentContext = mLithoViewRule.getContext();
    final AtomicReference<Object> triggerObjectRef = new AtomicReference<>();
    final Handle triggerHandle = new Handle();
    final List<LifecycleStep.StepInfo> info = new ArrayList<>();
    final Component component =
        Column.create(componentContext)
            .child(Text.create(componentContext).text("Sample"))
            .child(
                MountSpecTriggerTester.create(componentContext)
                    .steps(info)
                    .triggerObjectRef(triggerObjectRef)
                    .handle(triggerHandle)
                    .build())
            .build();
    mLithoViewRule.setRoot(component);

    mLithoViewRule.attachToWindow().measure().layout();

    final Object bazObject = new Object();
    MountSpecTriggerTester.triggerTestEvent(
        mLithoViewRule.getComponentTree().getContext(), triggerHandle, bazObject);

    assertThat(getSteps(info))
        .describedAs("Should call @OnTrigger method")
        .containsExactly(LifecycleStep.ON_TRIGGER);

    assertThat(triggerObjectRef.get())
        .describedAs("Event object is correctly passed")
        .isEqualTo(bazObject);
  }

  @Test
  public void mountSpec_setRootAndTriggerEvent_eventIsTriggered_handle_used_in_nested_tree_root() {
    final ComponentContext componentContext = mLithoViewRule.getContext();
    final AtomicReference<Object> triggerObjectRef = new AtomicReference<>();
    final Handle triggerHandle = new Handle();
    final List<LifecycleStep.StepInfo> info = new ArrayList<>();
    final Component component =
        Column.create(componentContext)
            .child(Text.create(componentContext).text("Sample"))
            .child(
                RecyclerCollectionComponent.create(componentContext)
                    .heightPx(150)
                    .section(
                        SingleComponentSection.create(new SectionContext(componentContext))
                            .component(
                                MountSpecTriggerTester.create(componentContext)
                                    .steps(info)
                                    .triggerObjectRef(triggerObjectRef)
                                    .handle(triggerHandle)
                                    .build())))
            .build();
    mLithoViewRule.setRoot(component);

    mLithoViewRule.attachToWindow().measure().layout();

    final Object bazObject = new Object();
    MountSpecTriggerTester.triggerTestEvent(
        mLithoViewRule.getComponentTree().getContext(), triggerHandle, bazObject);

    assertThat(getSteps(info))
        .describedAs("Should call @OnTrigger method")
        .containsExactly(LifecycleStep.ON_TRIGGER);

    assertThat(triggerObjectRef.get())
        .describedAs("Event object is correctly passed")
        .isEqualTo(bazObject);
  }

  @Test
  public void
      mountSpec_setRootAndTriggerEvent_eventIsTriggered_handle_used_in_nested_tree_deeper_in_hierarchy() {
    final ComponentContext componentContext = mLithoViewRule.getContext();
    final AtomicReference<Object> triggerObjectRef = new AtomicReference<>();
    final Handle triggerHandle = new Handle();
    final List<LifecycleStep.StepInfo> info = new ArrayList<>();
    final Component component =
        Column.create(componentContext)
            .child(Text.create(componentContext).text("Sample"))
            .child(
                RecyclerCollectionComponent.create(componentContext)
                    .heightPx(150)
                    .section(
                        SingleComponentSection.create(new SectionContext(componentContext))
                            .component(
                                Column.create(componentContext)
                                    .child(Text.create(componentContext).text("Nested tree child"))
                                    .child(
                                        MountSpecTriggerTester.create(componentContext)
                                            .steps(info)
                                            .triggerObjectRef(triggerObjectRef)
                                            .handle(triggerHandle)
                                            .build()))))
            .build();
    mLithoViewRule.setRoot(component);

    mLithoViewRule.attachToWindow().measure().layout();

    final Object bazObject = new Object();
    MountSpecTriggerTester.triggerTestEvent(
        mLithoViewRule.getComponentTree().getContext(), triggerHandle, bazObject);

    assertThat(getSteps(info))
        .describedAs("Should call @OnTrigger method")
        .containsExactly(LifecycleStep.ON_TRIGGER);

    assertThat(triggerObjectRef.get())
        .describedAs("Event object is correctly passed")
        .isEqualTo(bazObject);
  }
}
