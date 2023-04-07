package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class WithinTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);     //메소드 이름 hello에 파라미터 타입 String.class
    }

    /** Within -거의 사용하지는 않지만... */

    @Test
    void withinExact() {
        pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");   //MemberServiceImpl 타입 안에있는 메서드는 모두 OK
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();    //execution의 타입부분만 가져왔다 생각하면 된다.
    }
    @Test
    void withinStar() {
        pointcut.setExpression("within(hello.aop.member.*Service*)");       // 타입 앞뒤로 아무거나
        assertThat(pointcut.matches(helloMethod,
                MemberServiceImpl.class)).isTrue();
    }
    @Test
    void withinSubPackage() {
        pointcut.setExpression("within(hello.aop..*)");
        assertThat(pointcut.matches(helloMethod,
                MemberServiceImpl.class)).isTrue();
    }

    @Test
    @DisplayName("타겟의 타입에만 직접 적용, 인터페이스를 선정하면 안된다.")
    void withinSuperTypeFalse() {
        pointcut.setExpression("within(hello.aop.member.MemberService)");       //within은 정확하게 타입이 맞아야 한다.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }



    @Test
    @DisplayName("execution은 타입기반이라 인터페이스 선정 가능")
    void executionSuperTypeTrue() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");       //within은 정확하게 타입이 맞아야 한다.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

}
