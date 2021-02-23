package jpabook.jpashop_v2.config;


import java.util.concurrent.atomic.AtomicLong;
import jpabook.jpashop_v2.dto.Counter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HibernateInterceptor implements StatementInspector {

  private static ThreadLocal<Counter> queryCount = new ThreadLocal<>();

  void start() {
    queryCount.set(new Counter(new AtomicLong(0), System.currentTimeMillis()));
  }

  Counter getCount() {
    return queryCount.get();
  }

  void clear() {
    queryCount.remove();
  }

  @Override
  public String inspect(String sql) {
    log.info("sql = " + sql);
    Counter counter = queryCount.get();
    if (counter != null) {
      AtomicLong count = counter.getCount();
      count.addAndGet(1);
    }
    return sql;
  }

}