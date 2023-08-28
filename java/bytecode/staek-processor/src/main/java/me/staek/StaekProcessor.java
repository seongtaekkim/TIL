package me.staek;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.checkerframework.checker.signature.qual.ClassGetSimpleName;

import javax.annotation.processing.AbstractProcessor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;


// META-INF > services 에 추가하지 않고
// auto-service jar를 이용해서 쉽게 추가할 수 있다.
//@AutoService(Processor.class)
public class StaekProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ProcessorInterface.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ProcessorInterface.class);
        for(Element element : elements) {
            if (element.getKind() != ElementKind.INTERFACE) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "annotation can't be used on " + element.getSimpleName());
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "processing " + element.getSimpleName());
            }

            TypeElement typeElement = (TypeElement)element;
            ClassName className = ClassName.get(typeElement);

            MethodSpec method1 = MethodSpec.methodBuilder("method1")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", "call method1")
                    .build();

            TypeSpec interface1 = TypeSpec.classBuilder("ConcreteClass1")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(className)
                    .addMethod(method1)
                    .build();

            Filer filer = processingEnv.getFiler();
            try {
                JavaFile.builder(className.packageName(), interface1)
                        .build()
                        .writeTo(filer);
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Fatal Error " + e);
            }

        }

        return true;
    }
}
