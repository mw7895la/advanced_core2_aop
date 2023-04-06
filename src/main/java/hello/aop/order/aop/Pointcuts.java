package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {
    }

    //클래스 이름 패턴이 *Service 인것.  비즈니스 로직을 실행할때 트랜잭션(DB)을 걸기 때문에.
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {
    }

    //allOrder와 allService 조합
    @Pointcut("allOrder() && allService()")
    public void orderAndService(){}
}
