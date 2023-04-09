package hello.aop.exam;

import hello.aop.exam.annotation.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    @Trace      //이렇게 딱 붙이면 이제 부가기능이 적용 되는거다.
    public void request(String itemId) {
        examRepository.save(itemId);
    }
}
