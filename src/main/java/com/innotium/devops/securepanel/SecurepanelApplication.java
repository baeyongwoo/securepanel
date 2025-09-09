package com.innotium.devops.securepanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//db 연결 관련 추후 삭제
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
        }
)

public class SecurepanelApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurepanelApplication.class, args);
        System.out.print("Hello");
	}

}
