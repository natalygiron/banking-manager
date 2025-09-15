package com.bootcamp.accountms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
  "spring.autoconfigure.exclude=" +
  "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
  "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class AccountMsApplicationTests {

  @Test
  void contextLoads() {
    // Context should start without DB for basic verification
  }
}
