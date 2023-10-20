package me.staek.chapter06.item39;

import com.google.auto.service.AutoService;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @AutoService : META-INF > service 에 서비스로더를 등록해주는 Annotation
 */
@AutoService(Processor.class)
public class StaticCheckProcessor extends AbstractProcessor {
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Test.class.getName());
    }

    /**
     * Test.class를 작성한 모든 Element를 찾고, Method인지 검사후
     * static 이 아니라면 컴파일에러를 발생시킨다.
     *
     * @param annotations the annotation interfaces requested to be processed
     * @param roundEnv  environment for information about the current and prior round
     * @return
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Test.class);
        for(Element element : elements) {
            if (element.getKind() == ElementKind.METHOD) {
                if (element.getModifiers().contains(Modifier.STATIC) == false)
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR
                            , "please use only static method " + element.getSimpleName());
            }
        }
        return true;
    }
}
