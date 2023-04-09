package hello.aop.exam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
    /** Trace 어노테이션이 있으면 로그를 남긴다.*/
    /** 조건도 한번 걸어봐라.  실행 시간이 100ms를 넘기면 찍히도록 한다던가..*/
}
