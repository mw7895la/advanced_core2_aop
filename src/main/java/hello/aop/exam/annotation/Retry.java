package hello.aop.exam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

    int value() default 3;      //이러면 int형 element를 가지고 value()에 기본값이 3이 들어가 있다.
    /** 반드시 default값을 정해줘야 한다.  정해주지 않으면 무한 request다. */
}
