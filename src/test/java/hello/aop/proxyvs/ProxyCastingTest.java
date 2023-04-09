package hello.aop.proxyvs;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyCastingTest {

    @Test
    void jdkProxy() {
        MemberServiceImpl target = new MemberServiceImpl();      //구체클래스도 있고 인터페이스도 있다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(false);// JDK 동적 프록시

        //프록시를 인터페이스로 캐스팅하면 성공 -> 왜? JDK 동적 프록시는 인터페이스를 구현해서 만든거라 성공이지.
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //문제는 뭐냐! JDK로 만든 프록시는 MemberServiceImpl가 뭔지를 몰라. 프록시는 인터페이스를 구현해서 만든거야. 그래서 캐스팅이 실패한다.
        Assertions.assertThrows(ClassCastException.class, ()-> {MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;  });
        //MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;        // MemberServiceImpl 타입으로 캐스팅
    }

    @Test
    void cglibProxy() {
        MemberServiceImpl target = new MemberServiceImpl();      //구체클래스도 있고 인터페이스도 있다.
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);// CGLIB 프록시 생성

        //프록시를 인터페이스로 캐스팅하면 성공 왜냐? CGLIB 프록시는 MemberServiceImpl을 상속받아서 만든건데, MemberServiceImpl은  MemberService를 구현해서 만든것이다.  그래서 부모의 부모타입으로도 캐스팅이 가능.
        MemberService memberServiceProxy = (MemberService) proxyFactory.getProxy();

        //CGLIB 프록시를 구현 클래스로 캐스팅 시도해도 성공.
        MemberServiceImpl castingMemberService = (MemberServiceImpl) memberServiceProxy;        // MemberServiceImpl 타입으로 캐스팅
    }
}
