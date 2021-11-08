package com.facebook.samples.litho.kotlin.collection

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.litho.*
import com.facebook.litho.annotations.LayoutSpec
import com.facebook.litho.annotations.OnCreateInitialState
import com.facebook.litho.annotations.OnCreateLayout
import com.facebook.litho.annotations.State
import com.facebook.litho.core.padding
import com.facebook.litho.flexbox.flex
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.ListRecyclerConfiguration
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.facebook.litho.widget.Button
import com.facebook.litho.widget.LinearLayoutInfo
import com.facebook.litho.widget.TextInput

@LayoutSpec
object ComposerComponentSpec {

    @OnCreateInitialState
    fun onCreateInitialState(textInputHandle: StateValue<Handle>) {
        textInputHandle.set(Handle())
    }

    @OnCreateLayout
    fun onCreateLayout(c: ComponentContext, @State textInputHandle: Handle): Component {
        return Column.create(c)
                .child(TextInput.create(c).handle(textInputHandle))
                .build()
    }
}
