package org.msyu.logging;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("org.msyu.logging.*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {

    private TypeMirror preserveAnnotationTypeMirror;
	private Messager messager;
	private Elements eltUtils;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			return false;
		}

		messager = processingEnv.getMessager();
		eltUtils = processingEnv.getElementUtils();
		preserveAnnotationTypeMirror = eltUtils.getTypeElement(PreserveParameterNames.class.getName()).asType();

		for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(PreserveParameterNames.class)) {
			AnnotationMirror annotationMirror = getAnnotationMirror(annotatedElement);
			if (annotatedElement.getKind() == ElementKind.INTERFACE) {
				processPreserveAnnotation(annotatedElement);
			} else {
				messager.printMessage(
						Diagnostic.Kind.WARNING,
						"skipping non-interface annotated with " + PreserveParameterNames.class.getName(),
						annotatedElement,
						annotationMirror
				);
			}
		}
		return false;
	}

	private AnnotationMirror getAnnotationMirror(Element annotatedElement) {
		return annotatedElement.getAnnotationMirrors().stream()
				.filter(
						mirror -> processingEnv.getTypeUtils().isSameType(
								mirror.getAnnotationType(),
								preserveAnnotationTypeMirror
						)
				)
				.findAny()
				.get();
	}

	private void processPreserveAnnotation(Element annotatedElement) {
		messager.printMessage(Diagnostic.Kind.NOTE, "processing " + annotatedElement);
		TypeElement annotatedInterface = (TypeElement) annotatedElement;
		String className = eltUtils.getBinaryName(annotatedInterface) + PreserveParameterNames.PARAMETER_NAME_HOLDER_SUFFIX;

		try (Writer w = processingEnv.getFiler().createSourceFile(className, annotatedInterface).openWriter()) {
			PackageElement packageElement = eltUtils.getPackageOf(annotatedInterface);
			if (!packageElement.isUnnamed()) {
				w.write(String.format("package %s;\n", packageElement.getQualifiedName()));
			}
			w.write(String.format("public interface %s {\n", className.substring(className.lastIndexOf('.') + 1)));

			for (Element enclosedElement : annotatedInterface.getEnclosedElements()) {
				if (enclosedElement.getKind() != ElementKind.METHOD) {
					continue;
				}
				w.append("\tString[] ").append(enclosedElement.getSimpleName()).append(" = {");
				List<? extends VariableElement> parameters = ((ExecutableElement) enclosedElement).getParameters();
				for (int i = 0; i < parameters.size(); ++i) {
					if (i != 0) {
						w.append(", ");
					}
					w.append('"').append(parameters.get(i).getSimpleName()).append('"');
				}
				w.append("};\n");
			}
			w.write("}\n");
		} catch (IOException e) {
			messager.printMessage(
					Diagnostic.Kind.ERROR,
					"error while writing class " + className + ": " + e
			);
		}
	}

}
