package hello.aop.pointcut;
import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
@Slf4j
//@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
public class AtTargetAtWithinTest {

    /**
     * @SpringBootTest를 사용하면서, 그 안에서 내부 클래스로 @Configuration을 사용하게 되면 스프링 부트의 다양한 설정들이 먹히지 않고,
     * @Configuration 안에서만 적용한 설정이 먹히게 됩니다.
     * 이 설정이 우선권을 가지고 가버리기 때문에 스프링 부트의 다른 설정이 적용되지 않습니다.
     * 쉽게 이야기해서 테스트에서는 이 설정만 사용하게 됩니다.
     *
     *  https://www.inflearn.com/course/lecture?courseSlug=%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8&unitId=94525&category=questionDetail&tab=community&q=430218
     *
     *
     * */



    @Autowired
    Child child;
    @Test
    void success() {
        log.info("child Proxy={}", child.getClass());
        child.childMethod(); //부모, 자식 모두 있는 메서드
        child.parentMethod(); //부모 클래스만 있는 메서드
    }
    @TestConfiguration
    static class Config {
        @Bean
        public Parent parent() {
            return new Parent();
        }
        @Bean
        public Child child() {
            return new Child();
        }
        @Bean
        public AtTargetAtWithinAspect atTargetAtWithinAspect() {
            return new AtTargetAtWithinAspect();
        }
    }
    static class Parent {
        public void parentMethod(){} //부모에만 있는 메서드
    }

    @ClassAop
    static class Child extends Parent {
        public void childMethod(){}
    }

    /** @target, @within은 타입(class,interface ..)에 있는 애노테이션으로 AOP 적용 여부를 판단한다.
     *
     *
     * */

    @Slf4j
    @Aspect
    static class AtTargetAtWithinAspect {
        //@target : 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
        //패키지는 테스트의 패키지 경로임. 그리고 어짜피 main 디렉토리도 똑같다 경로가..
        //스프링은 모든 곳에 프록시를 만들려고 시도하기 때문에  최대한 범위를 줄인다음에 @target을 써야한다.
        //@target(hello.aop.member.annotation.ClassAop) 이거는 객체 인스턴스가 만들어지고, 실제 런타임이 되어야 판단할 수 있다. 그럴라면 프록시가 있어야 한다.
        @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
        public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[@target] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        //@within : 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음.
        //적용 대상을 execution으로 줄이고 @within을 사용했다.
        @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
        public Object atWithin(ProceedingJoinPoint joinPoint)throws Throwable {
            log.info("[@within] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

}