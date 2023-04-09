package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyvs.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@SpringBootTest(properties = "spring.aop.proxy-target-class=false")     //JDK동적 프록시
@SpringBootTest(properties = "spring.aop.proxy-target-class=true")
@Import(ProxyDIAspect.class)
public class ProxyDITest {
    /** 먼저 JDK 동적 프록시로 실행해보자. 아마 MemberServiceImpl에서 주입이 안될 것.
     * JDK 동적 프록시는 인터페이스 타입에만 의존관계 주입이 가능하다.
     * */

    /**
     * CGLIB
     */

    //인터페이스로 주입받는 것.
    @Autowired
    MemberService memberService;


    //구체 타입으로 주입받는것.
    @Autowired
    MemberServiceImpl memberServiceImpl;


    @Test
    void go(){
        log.info("memberService class={}", memberService.getClass());
        log.info("memberServiceImpl class{}", memberServiceImpl.getClass());
        memberServiceImpl.hello("hello");
    }
}
