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

package com.facebook.litho.specmodels.generator;

import com.facebook.litho.annotations.Generated;
import com.facebook.litho.specmodels.internal.ImmutableList;
import com.facebook.litho.specmodels.model.ClassNames;
import com.facebook.litho.specmodels.model.PrepareInterStageInputParamModel;
import com.facebook.litho.specmodels.model.SpecModel;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import javax.lang.model.element.Modifier;

public class PrepareInterStagePropsContainerGenerator {
  static TypeSpec generate(SpecModel specModel) {
    final TypeSpec.Builder interStagePropsContainerBuilder =
        TypeSpec.classBuilder(getPrepareInterStagePropsContainerClassName(specModel))
            .addSuperinterface(ClassNames.PREPARE_INTER_STAGE_PROPS_CONTAINER)
            .addAnnotation(Generated.class)
            .addModifiers(Modifier.STATIC);

    final ImmutableList<PrepareInterStageInputParamModel> interStageInputs =
        specModel.getPrepareInterStageInputs();
    for (PrepareInterStageInputParamModel interStageInput : interStageInputs) {
      interStagePropsContainerBuilder.addField(
          FieldSpec.builder(interStageInput.getTypeName().box(), interStageInput.getName())
              .build());
    }

    return interStagePropsContainerBuilder.build();
  }

  static String getPrepareInterStagePropsContainerClassName(SpecModel specModel) {
    if (specModel.getPrepareInterStageInputs() == null
        || specModel.getPrepareInterStageInputs().isEmpty()) {
      return null;
    } else {
      return specModel.getComponentName() + "PrepareInterStagePropsContainer";
    }
  }
}
