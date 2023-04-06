package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;


@Slf4j
public class AspectV5Order {

    /** 이제 AspectV5Order는 Aspect가 아니다.
     * 우리는 지금 트랜잭션 관련 부가기능을 먼저 적용되도록 하고 싶어서 순서를 바꿨다.
     * */

    @Aspect
    @Order(2)
    public static class LogAspect{
        /** 내부 클래스에 어드바이저를 집어 넣었다. */
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")      //hello.aop.order 패키지와 .. 그 하위 패키지를 지정하는 AspectJ표현식.  **포인트컷 부분**
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {

            log.info("[log] {}", joinPoint.getSignature());     //join point 시그니처 -> 반환타입과 해당 메서드의 위치 즉, 메서드의 정보가 찍힌다 보면 된다.
            return joinPoint.proceed();     //실제 타겟 호출.
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspect{
        //hello.aop.order 패키지와 하위 패키지 이면서. 동시에 클래스 이름 패턴이 *Service 인것. 여기선 OrderService
        //@Around("allOrder() && allService()")
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable{
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();        //실제 타겟을 호출 하고.
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature()); //정상적으로 끝났기 때문에 커밋을 하는 것.

                return result;
            } catch (Exception e) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            }finally{
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }

    /** 지금 순서는 doLog다음에 doTransaction 인데, 순서를 바꾸고 싶으면 ?????????? 위에 처럼 별도의 클래스로 분리해서 @Order(#) 으로 순서 정해라*/
}
