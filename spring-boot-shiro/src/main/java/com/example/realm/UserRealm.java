package com.example.realm;

import java.util.HashSet;
import java.util.Set;

import com.example.model.User;

import org.springframework.stereotype.Component;

/**
 * @author yuweijun 2017-06-04.
 */
@Component
public class UserRealm extends AbstractUserRealm {

    @Override
    public UserRolesAndPermissions doGetGroupAuthorizationInfo(User userInfo) {
        Set<String> userRoles = new HashSet<>();
        Set<String> userPermissions = new HashSet<>();
        return new UserRolesAndPermissions(userRoles, userPermissions);
    }

    @Override
    public UserRolesAndPermissions doGetRoleAuthorizationInfo(User userInfo) {
        Set<String> userRoles = new HashSet<>();
        Set<String> userPermissions = new HashSet<>();
        return new UserRolesAndPermissions(userRoles, userPermissions);
    }
}
