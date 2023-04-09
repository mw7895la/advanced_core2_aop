package hello.aop.exam;

import hello.aop.exam.aop.RetryAspect;
import hello.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import({TraceAspect.class, RetryAspect.class})
public class ExamTest {

    @Autowired
    ExamService examService;


    @Test
    void test() {
        log.info("examService ={}", examService.getClass());
        for(int i=0; i<5; i++){
            log.info("client request i={}", i);
            examService.request("data" + i);
        }
    }
    /**
     * 2023-04-09 10:47:01.035  INFO 17528 --- [    Test worker] hello.aop.exam.aop.RetryAspect           : [retry] String hello.aop.exam.ExamRepository.save(String) retry=@hello.aop.exam.annotation.Retry(value=4)
     * 2023-04-09 10:47:01.036  INFO 17528 --- [    Test worker] hello.aop.exam.aop.RetryAspect           : [retry] try count=1/4
     * 2023-04-09 10:47:01.036  INFO 17528 --- [    Test worker] hello.aop.exam.aop.RetryAspect           : [retry] try count=2/4
     *
     * 2번째 재시도에서 성공했다는 뜻이다.
     * */
}
