package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Slf4j
@Aspect
public class AspectV6Advice {


    /*@Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            // @Before  는  joinpoint 이전까지만 작성할 수 있다.
            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
            Object result = joinPoint.proceed();        //실제 타겟을 호출 하고.

            // @AfterReturning은 joinpoint 이후에 작성
            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature()); //정상적으로 끝났기 때문에 커밋을 하는 것.

            return result;
        } catch (Exception e) {
            // @AfterThrowing 예외부분.
            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
            throw e;
        }finally{
            //@After finally 부분에만 작성할 수 있음.
            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
        }
    }*/

    /**
     * ProceedingJoinPoint는 @Around에서만 쓸 수 있다.
     * <p>
     * 어? 근데 아래 doBefore()대로 작성하면 joinPoint.proceed() 실행은 안하나? -> doBefore는 우리가 실행하는게 아니다. @Before가 끝나면 알아서 joinpoint.proceed()를 실행해준다.
     * <p>
     * doTransaction() 다 주석처리 하고  AspectV6Advice 빈으로 등록후에 실행해봐라.  doBefore()에서 로그 남기고 바로 타겟이 실행된다.
     * 2023-04-06 20:52:14.682  INFO 10452 --- [    Test worker] hello.aop.order.aop.AspectV6Advice       : [before] void hello.aop.order.OrderService.orderItem(String)
     * 2023-04-06 20:52:14.694  INFO 10452 --- [    Test worker] hello.aop.order.OrderService             : [orderService] 실행
     * 2023-04-06 20:52:14.694  INFO 10452 --- [    Test worker] hello.aop.order.OrderRepository          : [orderRepository] 실행
     * <p>
     * joinPoint.proceed() 실행 전까지만 뭔가 남기고 싶다
     *
     * @Around에서는 result값을 바꿔서 리턴할 수 있지만,
     * @AfterReturning에선 result값을 사용 및 조작할 수 있고(result.메소드), 리턴코드를 리턴하지 않기 때문에 result값을 바꿀 수 없다.
     */

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void deBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, String result) {
        /** result의 타입을 String으로 하면, 우리가 AOP를 orderService에 걸었는데 그곳의 반환타입이 void다 그래서 String으로 받을 수 없어서 테스트 실행시 [return] 로그가 찍히지 않는다.
         *  Object result로 하면 Object는 모든 타입을 다 받을 수 있다( void도 받을 수 있다 ) 즉 타입이 되는것들만 호출이 된다. */
        //returning의 "result"와 매칭돼서 부가기능 로직 수행후 리턴값이  들어온다.
        log.info("[return] {} return ={}", joinPoint.getSignature(), result);
    }

/*    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.allOrder()", returning = "result")
    public void doReturn2(JoinPoint joinPoint, String result) {
        *//** String으로 바꿔줬다. 그리고 allOrder()로  value를 변경해줬다. ( 레포지토리에도 적용하겠다는 말 )  레포지토리에서 반환된 ok값을 받는것을 확인할 수 있다.
         * 물론 Object로 해도 받는다. Object가 최 상위 클래스니까.  Integer result로 한다면 아예 여기는 호출이 안된다 ( 어드바이스가 적용되지 않는다 ).  String이 Integer로 못들어오지.*//*
        //returning의 "result"와 매칭돼서 부가기능 로직 수행후 리턴값이  들어온다.
        log.info("[return2] {} return ={}", joinPoint.getSignature(), result);
    }*/

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {         //여기 ex와 위의 ex가 이름이 맞아야 함.
        log.info("[ex] {} message={}", joinPoint.getSignature(),ex);
    }
    /** 여기 예외보다 상위 타입이면 역시 마찬가지로 호출 안된다.*/

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")       //finally 부분
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
