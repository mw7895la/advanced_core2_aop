package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    //hello.aop.order 패키지와 하위패키지 전부다 뜻함.
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder(){}

    /** @Pointcut("execution(* hello.aop.order..*(..))") 이거를 이제 다른데서 쓰려면 아래처럼 넣고 (위 포인트컷 표현식을 아래 @Around에 넣어준 allOrder() 을 보자), 해당 표현식을 아래서도 쓸 수 있다.
     *  이 방식을 뭐라고 하냐면, pointcut signature 라고 한다.
     *
     *  메서드 이름 allOrder 와 파라미터 (지금은 없음) () 을 합쳐서 "메서드 시그니쳐" 라고 한다
     * */

    @Around("allOrder()")      //hello.aop.order 패키지와 .. 그 하위 패키지를 지정하는 AspectJ표현식.  **포인트컷 부분**
    public Object doLog(ProceedingJoinPoint joinPoint)throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());     //join point 시그니처 -> 반환타입과 해당 메서드의 위치 즉, 메서드의 정보가 찍힌다 보면 된다.
        return joinPoint.proceed();     //실제 타겟 호출.
    }

/*    @Around("allOrder()")
    public Object doLog2(ProceedingJoinPoint joinPoint)throws Throwable {
        log.info("[log] {}", joinPoint.getSignature());
        return joinPoint.proceed();
    }*/
}
