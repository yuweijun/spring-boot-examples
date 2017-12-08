/**
 * Copyright 2016 lyancoffee.com
 */
package com.example.spring.realm;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义 realm
 * <p>
 * Realm,用于身份信息权限信息的验证。开发时集成AuthorizingRealm，重写两个方法:
 * 1. doGetAuthenticationInfo(获取即将需要认真的信息)
 * 2. doGetAuthorizationInfo(获取通过认证后的权限信息)
 * <p>
 * HashedCredentialsMatcher，凭证匹配器，用于告诉Shiro在认证时通过什么方式(算法)来匹配密码。
 * 默认(storedCredentialsHexEncoded=false)Base64编码，可以修改为(storedCredentialsHexEncoded=true)Hex编码。
 */
public class ShiroCasRealm extends CasRealm {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroCasRealm.class);

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOGGER.info("doGetAuthorizationInfo begin, principals:{}", principals);
        return super.doGetAuthorizationInfo(principals);
    }

}
