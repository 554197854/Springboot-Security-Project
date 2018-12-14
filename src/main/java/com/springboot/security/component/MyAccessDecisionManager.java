package com.springboot.security.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author N
 * @create 2018/12/14 -- 16:54
 */
@Component
public class MyAccessDecisionManager implements AccessDecisionManager {//自定义权限访问器
    @Override
    public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection)
            throws AccessDeniedException, InsufficientAuthenticationException {

        if (null == collection)
            return;
        for (ConfigAttribute co : collection) {
            //当前需要的权限
            String needRole = co.getAttribute();

            //本系统注册之后需要通过邮件激活，未激活的账户将无法访问
            if("ROLE_ACTIVE".equals(needRole)){
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("用户未激活");
                } else
                    return;
            }

            if("ROLE_LOGIN".equals(needRole)){
                if (authentication instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("用户未登录");
                } else
                    return;
            }

            // authority为用户所被赋予的权限, needRole 为访问相应的资源应该具有的权限。
            for (GrantedAuthority grantedAuthority : authentication
                    .getAuthorities()) {
                if (needRole.equals(grantedAuthority.getAuthority()))
                    return;
            }
        }
        throw new AccessDeniedException("权限不足!");


    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
    /***
     * AccessDecisionManager is the class in charge of deciding if a particular Authentication object is allowed or not allowed to access a particular resource.
     * In its main implementations, it delegates to AccessDecisionVoter objects, which basically compares the GrantedAuthorities in the Authentication object against the ConfigAttribute(s) required by the resource that is being accessed, deciding whether or not access should be granted.
     * They emit their vote to allow access or not. The AccessDecisionManager implementations take the output from the voters into consideration and apply a determined strategy on whether or not to grant access
     *
     * 上述为AccessDecisionManager的原文描述
     * 大意就是用这些重写方法来确定是否有一个访问权限资源的特定策略和一个访问对象
     *
     * **/
}
