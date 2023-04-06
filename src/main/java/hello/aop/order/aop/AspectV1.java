package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect     //이 어노테이션도 AspectJ가 제공하는 어노테이션
@Slf4j
public class AspectV1 {
    //aop 관련된 코드를 넣어보자ㅣ

    @Around("execution(* hello.aop.order..*(..))")      //hello.aop.order 패키지와 .. 그 하위 패키지를 지정하는 AspectJ표현식.  **포인트컷 부분**
    public Object doLog(ProceedingJoinPoint joinPoint)throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());     //join point 시그니처 -> 반환타입과 해당 메서드의 위치 즉, 메서드의 정보가 찍힌다 보면 된다.
        return joinPoint.proceed();     //실제 타겟 호출.
    }

    /**
     * Aspect Builder 에서  포인트컷과 어드바이스를 만들고 어드바이저도 내부적으로 자동으로 만든다.
     * 이렇게 어드바이저를 만들고 나서 스프링 빈으로 등록하자.*/

}
