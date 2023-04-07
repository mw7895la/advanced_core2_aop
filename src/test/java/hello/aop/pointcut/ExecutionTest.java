package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.*;


@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);     //메소드 이름 hello에 파라미터 타입 String.class


    }

    @Test
    void printMethod() {
        log.info("helloMethod={}", helloMethod);
        //호출의 결과는  public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        //execution(* ..package..Class.)  이게 호출의 결과와 매칭할 수 있다.
    }

    //모든 조건이 다 매칭되는 포인트 컷
    @Test
    void exactMatch() {
        //hello 메서드와 가장 정확하게 매칭되는 execution 표현식을 써볼것.
        //  public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");  //( )사이는 파라미터 , 파라미터부분의 앞 패키지명은 생략할 수 있다.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))  //지정한 pointcut 표현식이 매칭 되는지 확인 (true,false)로 반환. MemberServicImpl.class에 있는 메서드 정보(helloMethod)와 일치하는지
                .isTrue();
    }

    @Test
    void allMatch() {
        pointcut.setExpression("execution(* *(..))");    //반환타입 * -아무거나 반환 , 메서드이름 * , (..) 파라미터 - 파라미터의 타입과 개수가 상관없다는 뜻.
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();        //당연히 매치가 되겠지.
    }

    @Test
    void nameMatch() {
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isTrue();
    }

    @Test
    void nameMatchStar1() {
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isTrue();
    }

    @Test
    void nameMatchStar2() {
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isTrue();
    }

    @Test
    void nameMatchFalse() {
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isFalse();
    }

    //패키지명 까지 정확히 일치
    @Test
    void packageExactMatch1() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isTrue();
    }

    // 패키지 명은 다 적고 클래스명을 *로 메서드를 *로 .  MemberServiceImpl은 member 디렉터리 하위에 있다.
    @Test
    void packageExactMatch2() {
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isTrue();
    }

    //패키지 매치가 실패하는 케이스      , 우리가 hello.aop.*.*(..) 까지만 적어줬다 . 이러면 패키지는 딱 hello.aop와 맞아 떨어져야 하고 그 하위의 클래스와 메서드는 아무거나라는 뜻.
    // 그러면 hello.aop 까지만 적어주고 하위패키지 전부다 적용하려면 hello.aop..*.*(..) 점을 하나 더 추가하면 된다.
    @Test
    void packageExactFalse() {
        pointcut.setExpression("execution(* hello.aop.*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isFalse();
    }

    //hello.aop.member도 포함이 되고  hello.aop.member 의 하위 패키지들도 다 적용이 된다.
    @Test
    void packageMatchSubPackage1() {
        pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class))
                .isTrue();

    }

    @Test
    void packageMatchSubPackage2() {
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /**
     * 타입 매칭
     */
    @Test
    void typeExactMatch() {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");        //클래스명 이하 아무 메서드
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");        //이번엔 인터페이스를 지정했다 MemberService
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();        //검증은 MemberServiceImpl이다.
        //부모타입으로 포인트컷을 지정해도 테스트는 성공.
    }

    //MemberService에는 hello 메서드만 있다. 그런데 이 인터페이스를 구현한 MemberServiceImpl에는 hello말고도 internal이란 메서드가 있다.
    //internal은 매칭이 될까 안될까?
    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        //표현식에는 인터페이스를 적어줬다. 그러면 인터페이스와 매칭되는걸 적용할거야. MemberServiceImpl은 MemberService를 구현했지만, internal메서드는 인터페이스에서 선언하지 않았다.
        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);//메서드를 뽑는다.
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
        //부모 타입에 선언한 메서드까지만 True가 될 것이다. 이 테스트는 False가 맞다.
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

        Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);//메서드를 뽑는다.
        assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();

    }

    /**
     * 파라미터 매칭 - String 타입의 파라미터 허용
     */
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");       //접근제어자 생략, 반환타입 * , 선언타입 생략, 메서드 이름 * 아무거나. 파라미터 String
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    //왜냐면 hello 메서드는 파라미터가 있었다. , 포인트컷에는 파라미터가 없어서 실패.
    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    //정확히 하나의 파라미터를 허용하고, 대신에 모든 타입을 허용한다.
    // (Xxx)
    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");        //파라미터에 *는 개수는 1개지만, 모든 타입을 허용
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    //숫자와 무관하게 모든 파라미터, 모든 타입 허용
    //(), (Xxx), (Xxx, Xxx)
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }


    //String 타입으로 시작, 숫자와 무관하게 모든 파라미터, 모든 타입 허용
    //(String), (String, Xxx), (String, Xxx, Xxx)
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");        //첫번 째 파라미터는 String이고  .. -> 0 ~ N개 까지다. 실제 메서드의 파라미터가 1개면 파라미터 ..은 0으로 적용됨
        assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }



}
