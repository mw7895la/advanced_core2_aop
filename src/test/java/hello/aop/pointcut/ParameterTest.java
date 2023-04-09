package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService proxy={}", memberService.getClass());
        memberService.hello("helloA"); //파라미터를 넘기지? 이거를 어드바이스에 딱 잡아가지고 볼 수 있다.
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        //member 패키지에 있는 모든 곳에 다 aop를 적용한다.
        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        //어떻게 파라미터 값을 어드바이스에서 가로채서 쓰냐??
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object arg1 = joinPoint.getArgs()[0];       //파라미터가 배열로 넘어오기 때문에 꺼내면 된다.
            log.info("[logArgs1]{} , arg={}", joinPoint.getSignature(), arg1);
            return joinPoint.proceed();
        }

        //이 방법이 훨씬 깔끔하다.
        @Around("allMember() && args(arg,..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {

            log.info("[logArgs2]{} , arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        //@Before이기 때문에 반환할게 없다.
        @Before("allMember() && args(arg,..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3] arg={}", arg);
        }

        /**
         * this 와 target 는 대상을 직접 지정할 수 있다 ( 아래는 MemberService 에 관련된 곳에 AOP를 적용하겠다 )
         * 객체 인스턴스의 오브젝트가 넘어온다.
         */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[this] {}, obj={}", joinPoint.getSignature(),obj.getClass());        //this랑 this에서 넘어온 오브젝트가 뭐냐.
            //[this] String hello.aop.member.MemberServiceImpl.hello(String), obj=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$6aebe98e
            //프록시 객체다.
            //this는 스프링 컨테이너에 들어있는 애를 this로 인정하고.(즉 프록시)
        }

        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[target] {}, obj={}", joinPoint.getSignature(),obj.getClass());        //this랑 this에서 넘어온 오브젝트가 뭐냐.
            //[target] String hello.aop.member.MemberServiceImpl.hello(String), obj=class hello.aop.member.MemberServiceImpl
            // target은 프록시 말고 프록시가 호출하는 실제 대상을 target으로 한다.
        }

        //어노테이션 매칭
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target] {}, obj={}", joinPoint.getSignature(),annotation);
        }

        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within] {}, obj={}", joinPoint.getSignature(),annotation);

        }

        /** 우리가 MethodAop는 value를 넣어놨었다. 그래서 값을 꺼낼 수 있다.*/
        @Before("allMember() && @annotation(annotation)")
        public void atWithin(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation] {}, annotationValue={}", joinPoint.getSignature(),annotation.value());        //this랑 this에서 넘어온 오브젝트가 뭐냐.

        }
    }
}
