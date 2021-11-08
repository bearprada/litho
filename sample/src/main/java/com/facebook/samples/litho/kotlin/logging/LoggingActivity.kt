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

package com.facebook.samples.litho.kotlin.logging

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import com.facebook.litho.ComponentContext
import com.facebook.litho.LithoView
import com.facebook.samples.litho.NavigatableDemoActivity
import com.facebook.samples.litho.kotlin.collection.ComposerComponent
import com.facebook.samples.litho.kotlin.collection.FriendsCollectionKComponent

class LoggingActivity : NavigatableDemoActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val c = ComponentContext(this)
    val view = FrameLayout(this).apply {  // Root
       layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
       addView(LinearLayout(context).apply {  // composer and message list
          layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
          orientation = LinearLayout.VERTICAL

          addView(LinearLayout(context).apply { // message list
                orientation = LinearLayout.VERTICAL
                setWeight(setHeight(this, MATCH_PARENT), 1f)
                addView(setHeight(LithoView.create(c, FriendsCollectionKComponent.create(c).build()), MATCH_PARENT)) // message list ui
                addView(View(context).apply { id = ViewCompat.generateViewId() }) // snackbar
          })
          addView(setWeight(setHeight(LithoView.create(c, ComposerComponent.create(c).build()), WRAP_CONTENT), 1f)) // composer ui
      })
    }
    setContentView(view)
  }
}
