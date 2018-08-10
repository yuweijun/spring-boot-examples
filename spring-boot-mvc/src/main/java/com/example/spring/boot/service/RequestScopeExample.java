package com.example.spring.boot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Date;

/**
 * <pre>
 * 3.5.4.5 Scoped beans as dependencies
 * The Spring IoC container manages not only the instantiation of your objects (beans), but also the wiring up of collaborators (or dependencies). If you want to inject (for example) an HTTP request scoped bean into another bean, you must inject an AOP proxy in place of the scoped bean. That is, you need to inject a proxy object that exposes the same public interface as the scoped object but that can also retrieve the real, target object from the relevant scope (for example, an HTTP request) and delegate method calls onto the real object.
 *
 * [Note]	Note
 * You do not need to use the <aop:scoped-proxy/> in conjunction with beans that are scoped as singletons or prototypes. If you try to create a scoped proxy for a singleton bean, the BeanCreationException is raised.
 *
 * The configuration in the following example is only one line, but it is important to understand the “why” as well as the “how” behind it.
 *
 * <?xml version="1.0" encoding="UTF-8"?>
 * <beans xmlns="http://www.springframework.org/schema/beans"
 *        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *        xmlns:aop="http://www.springframework.org/schema/aop"
 *        xsi:schemaLocation="http://www.springframework.org/schema/beans
 *            http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
 *            http://www.springframework.org/schema/aop
 *            http://www.springframework.org/schema/aop/spring-aop-3.0.xsd">
 *
 *     <!-- an HTTP Session-scoped bean exposed as a proxy -->
 *     <bean id="userPreferences" class="com.foo.UserPreferences" scope="session">
 *
 *           <!-- this next element effects the proxying of the surrounding bean -->
 *           <aop:scoped-proxy/>
 *     </bean>
 *
 *     <!-- a singleton-scoped bean injected with a proxy to the above bean -->
 *     <bean id="userService" class="com.foo.SimpleUserService">
 *
 *         <!-- a reference to the proxied userPreferences bean -->
 *         <property name="userPreferences" ref="userPreferences"/>
 *
 *     </bean>
 * </beans>
 * To create such a proxy, you insert a child <aop:scoped-proxy/> element into a scoped bean definition. (If you choose class-based proxying, you also need the CGLIB library in your classpath. See the section called “Choosing the type of proxy to create” and Appendix C, XML Schema-based configuration.) Why do definitions of beans scoped at the request, session, globalSession and custom-scope levels require the <aop:scoped-proxy/> element ? Let's examine the following singleton bean definition and contrast it with what you need to define for the aforementioned scopes. (The following userPreferences bean definition as it stands is incomplete.)
 *
 * <bean id="userPreferences" class="com.foo.UserPreferences" scope="session"/>
 *
 * <bean id="userManager" class="com.foo.UserManager">
 *     <property name="userPreferences" ref="userPreferences"/>
 * </bean>
 * In the preceding example, the singleton bean userManager is injected with a reference to the HTTP Session-scoped bean userPreferences. The salient point here is that the userManager bean is a singleton: it will be instantiated exactly once per container, and its dependencies (in this case only one, the userPreferences bean) are also injected only once. This means that the userManager bean will only operate on the exact same userPreferences object, that is, the one that it was originally injected with.
 *
 * This is not the behavior you want when injecting a shorter-lived scoped bean into a longer-lived scoped bean, for example injecting an HTTP Session-scoped collaborating bean as a dependency into singleton bean. Rather, you need a single userManager object, and for the lifetime of an HTTP Session, you need a userPreferences object that is specific to said HTTP Session. Thus the container creates an object that exposes the exact same public interface as the UserPreferences class (ideally an object that is a UserPreferences instance) which can fetch the real UserPreferences object from the scoping mechanism (HTTP request, Session, etc.). The container injects this proxy object into the userManager bean, which is unaware that this UserPreferences reference is a proxy. In this example, when a UserManager instance invokes a method on the dependency-injected UserPreferences object, it actually is invoking a method on the proxy. The proxy then fetches the real UserPreferences object from (in this case) the HTTP Session, and delegates the method invocation onto the retrieved real UserPreferences object.
 *
 * Thus you need the following, correct and complete, configuration when injecting request-, session-, and globalSession-scoped beans into collaborating objects:
 *
 * <bean id="userPreferences" class="com.foo.UserPreferences" scope="session">
 *     <aop:scoped-proxy/>
 * </bean>
 *
 * <bean id="userManager" class="com.foo.UserManager">
 *     <property name="userPreferences" ref="userPreferences"/>
 * </bean>
 * Choosing the type of proxy to create
 * By default, when the Spring container creates a proxy for a bean that is marked up with the <aop:scoped-proxy/> element, a CGLIB-based class proxy is created. This means that you need to have the CGLIB library in the classpath of your application.
 *
 * Note: CGLIB proxies only intercept public method calls! Do not call non-public methods on such a proxy; they will not be delegated to the scoped target object.
 *
 * Alternatively, you can configure the Spring container to create standard JDK interface-based proxies for such scoped beans, by specifying false for the value of the proxy-target-class attribute of the <aop:scoped-proxy/> element. Using JDK interface-based proxies means that you do not need additional libraries in your application classpath to effect such proxying. However, it also means that the class of the scoped bean must implement at least one interface, and that all collaborators into which the scoped bean is injected must reference the bean through one of its interfaces.
 *
 * <!-- DefaultUserPreferences implements the UserPreferences interface -->
 * <bean id="userPreferences" class="com.foo.DefaultUserPreferences" scope="session">
 *     <aop:scoped-proxy proxy-target-class="false"/>
 * </bean>
 *
 * <bean id="userManager" class="com.foo.UserManager">
 *     <property name="userPreferences" ref="userPreferences"/>
 * </bean>
 * For more detailed information about choosing class-based or interface-based proxying, see Section 7.6, “Proxying mechanisms”.
 * </pre>
 *
 * @author yuweijun on 2018-08-10.
 */
@Service
@RequestScope
public class RequestScopeExample {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
