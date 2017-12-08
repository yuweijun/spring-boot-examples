package com.example.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Locale;

/**
 * <b>27.1.5 Static Content</b>
 * <p>
 * <p>
 * By default Spring Boot will serve static content from a directory called /static (or /public or /resources or
 * /META-INF/resources) in the classpath or from the root of the ServletContext. It uses the ResourceHttpRequestHandler
 * from Spring MVC so you can modify that behavior by adding your own WebMvcConfigurerAdapter and overriding the
 * addResourceHandlers method.
 * <p>
 * <pre>
 * <code>
 * &#64;Override
 * public void addResourceHandlers(ResourceHandlerRegistry registry) {
 * 	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
 * }
 * </code>
 * </pre>
 */
@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    private static final Logger logger = Logger.getLogger(MvcConfiguration.class);

    /**
     * 如果需要像传统web.xml那样配置404页面的话, 需要将下面这个Bean注入到spring中
     * <p>
     * // @Bean
     */
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            ErrorPage error401 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.jsp");
            ErrorPage error404 = new ErrorPage(HttpStatus.NOT_FOUND, "/404.jsp");
            ErrorPage error500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.jsp");
            container.addErrorPages(error401, error404, error500);
        });
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("addResourceHandlers");
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("jsp/login");
    }

    @Bean(name = "jspViewResolver")
    public ViewResolver jspViewResolver() {
        logger.info("jspViewResolver");
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Bean
    public MessageSource messageSource() {
        // 注册消息资源处理器，国际化
        logger.info("messageSource");
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        logger.info("localeChangeInterceptor");
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Bean
    public LocaleResolver localeResolver() {
        // 基于cookie的本地化资源处理器
        logger.info("localeResolver");
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.CHINA);
        return cookieLocaleResolver;
    }

    // @Bean(name = "localeResolver")
    // public LocaleResolver localeResolver() {
    // // 基于session的本地化资源处理器
    // logger.info("localeResolver");
    // SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
    // sessionLocaleResolver.setDefaultLocale(Locale.CHINA);
    // return sessionLocaleResolver;
    // }

    /**
     * http://wiki.jikexueyuan.com/project/spring-boot/spring-boot-interceptors.html
     * <p>
     * 为LocaleChangeInterceptor添加@Bean定义，这仅仅是定义了一个interceptor spring bean，但是Spring boot不会自动将它加入到调用链中。
     * <p>
     * 拦截器需要手动加入调用链。
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

    /**
     * Example of WebSecurityConfiguration:
     * <p>
     * <pre>
     * <code>
     * &#64;Override
     * protected void configure(HttpSecurity http) throws Exception {
     * http.authorizeRequests()
     * .antMatchers("/", "/public/**").permitAll()
     * .antMatchers("/users/**").hasAuthority("ADMIN")
     * .anyRequest().fullyAuthenticated()
     * .and()
     * .formLogin()
     * .loginPage("/login")
     * .failureUrl("/login?error")
     * .usernameParameter("email")
     * .permitAll()
     * .and()
     * .logout()
     * .logoutUrl("/logout")
     * .deleteCookies("remember-me")
     * .logoutSuccessUrl("/")
     * .permitAll()
     * .and()
     * .rememberMe();
     * }
     * </code>
     * </pre>
     * <p>
     * <pre>
     * <code>
     * protected void configure(HttpSecurity http) throws Exception {
     * http
     * .authorizeRequests()                                                                1
     * .antMatchers("/resources/**", "/signup", "/about").permitAll()                  2
     * .antMatchers("/admin/**").hasRole("ADMIN")                                      3
     * .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")            4
     * .anyRequest().authenticated()                                                   5
     * .and()
     * // ...
     * .formLogin();
     * }
     * 1. There are multiple children to the http.authorizeRequests() method each matcher is considered in the order they were declared.
     * 2. We specified multiple URL patterns that any user can access. Specifically, any user can access a request if the URL starts with "/resources/", equals "/signup", or equals "/about".
     * 3. Any URL that starts with "/admin/" will be resticted to users who have the role "ROLE_ADMIN". You will notice that since we are invoking the hasRole method we do not need to specify the "ROLE_" prefix.
     * 4. Any URL that starts with "/db/" requires the user to have both "ROLE_ADMIN" and "ROLE_DBA". You will notice that since we are using the hasRole expression we do not need to specify the "ROLE_" prefix.
     * 5. Any URL that has not already been matched on only requires that the user be authenticated
     * </code>
     * </pre>
     * <p>
     * Note that the matchers are considered in order. Therefore, the following is invalid because the first matcher
     * matches every request and will never get to the second mapping:
     * <p>
     * <pre>
     * <code>
     * http.authorizeRequests().antMatchers("/**").hasRole("USER").antMatchers("/admin/**").hasRole("ADMIN")
     * </code>
     * </pre>
     */
    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

        @Autowired
        private UserDetailsService userDetailsService;

        /**
         * Example: http://kielczewski.eu/2014/12/spring-boot-security-application/
         * <p>
         * IMPORTANT: One important remark here is that if the CSRF protection is turned on, the request to /logout
         * should be POST.
         * <p>
         * http://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf-caveats
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // 根目录和static目录下的表态资源谁的不需要用户登录
            // http.csrf().disable();
            http.csrf().ignoringAntMatchers("/sockjs/**")
                    .ignoringAntMatchers("/post/json/**")
                    .and().authorizeRequests().antMatchers("/", "/test", "/static/**", "/post/json/**").permitAll()
                    // Authority为资源设定访问权限，hasRole("ADMIN")就相当于是hasAuthority("ROLE_ADMIN")
                    .antMatchers("/users/**").hasAuthority("ADMIN")
                    // 对所有请求进行用户验证，authenticated()方法表示所有认证的用户就可以访问anyRequest()，所以hasAuthority("ADMIN")要放在authenticated()之前声明
                    // Note that the matchers are considered in order. Therefore, the following is invalid because the
                    // first matcher matches every request and will never get to the second mapping:
                    // http.authorizeRequests().antMatchers("/**").hasRole("USER").antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    // login settings.
                    .and().formLogin().loginPage("/login").defaultSuccessUrl("/settings/").failureUrl("/login?error")
                    .permitAll()
                    // logout settings.
                    .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout")
                    // logout handlers
                    .deleteCookies("remember-me").invalidateHttpSession(true).permitAll()
                    // remember me settings.
                    .and().rememberMe().tokenValiditySeconds(1209600);
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            /**
             * Populates the roles. This method is a shortcut for calling authorities(String), but automatically
             * prefixes each entry with "ROLE_". This means the following: builder.roles("USER","ADMIN"); is equivalent
             * to builder.authorities("ROLE_USER","ROLE_ADMIN");
             *
             * http://stackoverflow.com/questions/19525380/difference-between-role-and-grantedauthority-in-spring-
             * security
             *
             * But still: a role is just an authority with a special ROLE_ prefix. So in Spring security
             * 3 @PreAuthorize("hasRole('ROLE_XYZ')") is the same as @PreAuthorize("hasAuthority('ROLE_XYZ')")
             *
             * and in Spring security 4 @PreAuthorize("hasRole('XYZ')") is the same
             * as @PreAuthorize("hasAuthority('ROLE_XYZ')").
             *
             * 在spring security框架的 {@code UserDetails} 接口中 {@link UserDetails.getAuthorities()} 方法是控制访问权限的，每个用户有不同的
             * authorities，ROLE只是设置 authorities 的一个便捷方法。代码中统一使用 authority 会更清晰，减少混淆。
             */
            // auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and().withUser("admin")
            // .password("password").roles("USER", "ADMIN");

            auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        }
    }

}
