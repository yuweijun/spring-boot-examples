# 应用启动时加入ApplicationListener的方式

1. 使用注解，@EventListener，如ListenerBean
2. new SpringApplicationBuilder(SpringBootApplicationListenerApplication.class).listeners(new ExampleListener())
3. 使用META-INF/spring.factories文件配置，自启动监听器

## 注意

有些事件实际上是在`ApplicationContext`创建前触发的，所以你不能在那些事件（处理类）中通过 @Bean 注册监听器，
只能通过`SpringApplication.addListeners(...)`或`SpringApplicationBuilder.listeners(...)`方法注册。
如果想让监听器自动注册，而不关心应用的创建方式，你可以在工程中添加一个`META-INF/spring.factories`文件，
并使用`org.springframework.context.ApplicationListener`作为key指向那些监听器，如下：

> org.springframework.context.ApplicationListener=com.example.project.MyListener
