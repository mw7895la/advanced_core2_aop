package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * application.properties.
 * spring.aop.proxy-target-class=true       - 인터페이스가 있든 없든 CGLIB
 * spring.aop.proxy-target-class=false      - 인터페이스가 있으면 JDK 없으면 CGLIB
 */
@Slf4j
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
@Import(ThisTargetTest.ThisTargetAspect.class)
public class ThisTargetTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {

        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    /** 스프링부트를 기본으로 쓰면 그냥 프록시들을 CGLIB로 만들어버린다.  그래서 옵션을 세팅 해야된다. application.properties*/

    @Aspect
    static class ThisTargetAspect {
        //this든 target이든 부모타입 허용
        @Around("this(hello.aop.member.MemberService)")
        public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberService)")
        public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-interface] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("this(hello.aop.member.MemberServiceImpl)")
        public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[this-Impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }

        @Around("target(hello.aop.member.MemberServiceImpl)")
        public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[target-Impl] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    /**
     *  spring.aop.proxy-target-class=false 옵션으로 했을 때
     *  this-impl의 로그가 찍히지 않았다   강의자료 29장에서 this는 MemberServiceImpl을 전혀 모른다. (JDK 동적 프록시)
     *
     *  spring.aop.proxy-target-class=true 옵션으로 했을 때
     *  로그는 전부다 찍혔다.
     * */
}
