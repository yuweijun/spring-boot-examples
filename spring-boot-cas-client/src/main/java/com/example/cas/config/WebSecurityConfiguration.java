package com.example.cas.config;

import com.example.cas.service.CustomUserDetailsService;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfiguration.class);

    private static final String CAS_URL_LOGIN = "cas.service.login";
    private static final String CAS_URL_LOGOUT = "cas.service.logout";
    private static final String CAS_URL_PREFIX = "cas.url.prefix";
    private static final String CAS_SERVICE_URL = "app.service.security";
    private static final String APP_SERVICE_HOME = "app.service.home";

    @Inject
    private Environment env;

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties sp = new ServiceProperties();
        sp.setService(env.getRequiredProperty(CAS_SERVICE_URL));
        sp.setSendRenew(false);
        return sp;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {
        CasAuthenticationProvider casAuthenticationProvider = new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(customUserDetailsService());
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("an_id_for_this_auth_provider_only");
        return casAuthenticationProvider;
    }

    @Bean
    public AuthenticationUserDetailsService<CasAssertionAuthenticationToken> customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        String serverUrlPrefix = env.getRequiredProperty(CAS_URL_PREFIX);
        return new Cas20ServiceTicketValidator(serverUrlPrefix);
    }

    /**
     * 1. request URL: http://localhost:8282/
     * 2. redirect to: https://localhost:8443/cas/login?service=http://localhost:8282/login/cas
     * 3. redirect to: http://localhost:8282/login/cas?ticket=ST-223-c7DIBI4B9vwQKFbYsSVL-login.example.com
     * 4. redirect to:http://localhost:8282/
     *
     * @return
     * @throws Exception
     */
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        // casAuthenticationFilter.setFilterProcessesUrl("/j_spring_cas_security_check");
        // 设置这个地址，作为service的值传给cas server，cas认证好用户后，会重定向到这个地址，交由cas filter进行ticket验证
        casAuthenticationFilter.setFilterProcessesUrl("/login/cas");
        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint() {
        CasAuthenticationEntryPoint casAuthenticationEntryPoint = new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(env.getRequiredProperty(CAS_URL_LOGIN));
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    /**
     * spring-boot 1.3.3 和 1.4.2 不能打开这个Bean
     * <p>
     * https://groups.google.com/a/apereo.org/forum/#!topic/cas-user/5lmBMv8O3Ik
     * http://stackoverflow.com/questions/43118431/spring-boot-security-upgrade-breaks-cas
     * <p>
     * In SecurityConfiguration class one must remove @Bean annotations from SingleSignOutFilter.
     * This seemed to be the main issue and was pretty hard to track down.
     * <p>
     * replace app.service.security value from ../j_spring_cas_security_check to ../login/cas
     */
    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        String serverUrlPrefix = env.getRequiredProperty(CAS_URL_PREFIX);
        LOGGER.info("serverUrlPrefix is : {}", serverUrlPrefix);
        singleSignOutFilter.setCasServerUrlPrefix("");
        return singleSignOutFilter;
    }

    @Bean
    public LogoutFilter requestCasGlobalLogoutFilter() {
        String logoutUrl = env.getRequiredProperty(CAS_URL_LOGOUT);
        String appServiceName = env.getRequiredProperty(APP_SERVICE_HOME);
        LogoutFilter logoutFilter = new LogoutFilter(logoutUrl + "?service=" + appServiceName, new SecurityContextLogoutHandler());
        // logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"));
        logoutFilter.setLogoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        return logoutFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(casAuthenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CasAuthenticationFilter casAuthenticationFilter = casAuthenticationFilter();
        http.addFilter(casAuthenticationFilter);
        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint());
        http.addFilterBefore(singleSignOutFilter(), CasAuthenticationFilter.class)
                .addFilterBefore(requestCasGlobalLogoutFilter(), LogoutFilter.class);

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/", "/assets/**").permitAll()
                .anyRequest().authenticated();

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");
    }
}
