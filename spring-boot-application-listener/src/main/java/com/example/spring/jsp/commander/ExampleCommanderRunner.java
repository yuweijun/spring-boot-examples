package com.example.spring.jsp.commander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 如果需要在 SpringApplication 启动后执行一些特殊的代码，你可以实
 * 现 ApplicationRunner 或 CommandLineRunner 接口，这两个接口工作方式相
 * 同，都只提供单一的 run 方法，该方法仅在 SpringApplication.run(…) 完成
 * 之前调用。
 * CommandLineRunner 接口能够访问string数组类型的应用参数，
 * 而 ApplicationRunner 使用的是上面描述过的 ApplicationArguments 接口：
 * <p>
 * Created by yuweijun on 2017-12-08.
 */
@Component
public class ExampleCommanderRunner implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleCommanderRunner.class);

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info(getClass().getCanonicalName());
    }

}
