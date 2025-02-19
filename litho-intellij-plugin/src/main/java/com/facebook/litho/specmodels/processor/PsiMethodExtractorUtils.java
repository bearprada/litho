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

package com.facebook.litho.specmodels.processor;

import static com.facebook.litho.specmodels.processor.MethodExtractorUtils.COMPONENTS_PACKAGE;

import com.facebook.litho.specmodels.model.MethodParamModel;
import com.facebook.litho.specmodels.model.MethodParamModelFactory;
import com.intellij.codeInsight.AnnotationUtil;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiNameValuePair;
import com.intellij.psi.PsiParameter;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeVariableName;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Extracts methods from the given input. */
public class PsiMethodExtractorUtils {

  static List<TypeVariableName> getTypeVariables(PsiMethod method) {
    return Arrays.stream(method.getTypeParameters())
        .map(PsiMethodExtractorUtils::toTypeVariableName)
        .collect(Collectors.toList());
  }

  private static TypeVariableName toTypeVariableName(PsiTypeParameter psiTypeParameter) {
    String typeName = psiTypeParameter.getName();
    TypeName[] typeBounds =
        Arrays.stream(psiTypeParameter.getBounds())
            .filter(PsiType.class::isInstance)
            .map(bound -> PsiTypeUtils.getTypeName((PsiType) bound))
            .toArray(TypeName[]::new);
    return TypeVariableName.get(typeName, typeBounds);
  }

  static List<MethodParamModel> getMethodParams(
      PsiMethod method,
      List<Class<? extends Annotation>> permittedAnnotations,
      List<Class<? extends Annotation>> permittedInterStageInputAnnotations,
      List<Class<? extends Annotation>> permittedPrepareInterStageInputAnnotations,
      List<Class<? extends Annotation>> delegateMethodAnnotationsThatSkipDiffModels) {

    final List<MethodParamModel> methodParamModels = new ArrayList<>();
    PsiParameter[] params = method.getParameterList().getParameters();

    for (final PsiParameter param : params) {
      methodParamModels.add(
          MethodParamModelFactory.create(
              PsiTypeUtils.generateTypeSpec(param.getType()),
              param.getName(),
              getLibraryAnnotations(param, permittedAnnotations),
              getExternalAnnotations(param),
              permittedInterStageInputAnnotations,
              permittedPrepareInterStageInputAnnotations,
              canCreateDiffModels(method, delegateMethodAnnotationsThatSkipDiffModels),
              param));
    }

    return methodParamModels;
  }

  private static boolean canCreateDiffModels(
      PsiMethod method,
      List<Class<? extends Annotation>> delegateMethodAnnotationsThatSkipDiffModels) {

    for (Class<? extends Annotation> delegate : delegateMethodAnnotationsThatSkipDiffModels) {
      if (PsiAnnotationProxyUtils.findAnnotationInHierarchy(method, delegate) != null) {
        return false;
      }
    }

    return true;
  }

  private static List<Annotation> getLibraryAnnotations(
      PsiParameter param, List<Class<? extends Annotation>> permittedAnnotations) {
    List<Annotation> paramAnnotations = new ArrayList<>();
    for (Class<? extends Annotation> possibleMethodParamAnnotation : permittedAnnotations) {
      final Annotation paramAnnotation =
          PsiAnnotationProxyUtils.findAnnotationInHierarchy(param, possibleMethodParamAnnotation);
      if (paramAnnotation != null) {
        paramAnnotations.add(paramAnnotation);
      }
    }

    return paramAnnotations;
  }

  private static List<AnnotationSpec> getExternalAnnotations(PsiParameter param) {
    PsiAnnotation[] annotationsOnParam = AnnotationUtil.getAllAnnotations(param, false, null);
    final List<AnnotationSpec> annotations = new ArrayList<>();

    for (PsiAnnotation annotationOnParam : annotationsOnParam) {
      if (annotationOnParam.getQualifiedName() == null
          || annotationOnParam.getQualifiedName().startsWith(COMPONENTS_PACKAGE)) {
        continue;
      }

      final AnnotationSpec.Builder annotationSpec =
          AnnotationSpec.builder(PsiTypeUtils.guessClassName(annotationOnParam.getQualifiedName()));

      PsiNameValuePair[] paramAttributes = annotationOnParam.getParameterList().getAttributes();
      for (PsiNameValuePair attribute : paramAttributes) {
        annotationSpec.addMember(attribute.getName(), attribute.getDetachedValue().getText());
      }

      annotations.add(annotationSpec.build());
    }

    return annotations;
  }
}
