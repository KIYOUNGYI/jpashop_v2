package jpabook.jpashop_v2.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jpabook.jpashop_v2.dto.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestCountInterceptor extends HandlerInterceptorAdapter {

  private final HibernateInterceptor hibernateInterceptor;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    hibernateInterceptor.start();
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    Counter counter = hibernateInterceptor.getCount();
    long duration = System.currentTimeMillis() - counter.getTime();
    Long count = counter.getCount().get();
    log.info("time : {}, count : {} , url : {}", duration, count, request.getRequestURI());
    if (count >= 10) {
      log.error("한 request 에 쿼리가 10번 이상 날라갔습니다.  날라간 횟수 : {} ", count);
    }
    hibernateInterceptor.clear();
  }
}
