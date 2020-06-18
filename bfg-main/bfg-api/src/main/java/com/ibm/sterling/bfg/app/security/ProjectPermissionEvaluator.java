package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.exception.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.Entity;
import com.ibm.sterling.bfg.app.model.changeControl.ChangeControl;
import com.ibm.sterling.bfg.app.service.ChangeControlService;
import com.ibm.sterling.bfg.app.service.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProjectPermissionEvaluator implements PermissionEvaluator {
    @Autowired
    private EntityService entityService;
    @Autowired
    private ChangeControlService controlService;

    @Override
    public boolean hasPermission(Authentication authentication, Object object, Object operation) {
        return isAllowed(getPermission(object, (String) operation));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable id, String type, Object operation) {
        if ((authentication == null) || (type == null) || !(operation instanceof String)) {
            return false;
        }
        switch (type) {
            case "Entity":
                Entity entity = entityService.findById((Integer) id).orElseThrow(EntityNotFoundException::new);
                return isAllowed(getPermission(entity, (String) operation));
        }
        return false;
    }

    private String getPermission(Object object, String operation) {
        String service;
        if (object instanceof Entity) {
            Entity entity = (Entity) object;
            service = entity.getService();
        } else {
            Map<String, String> map = (Map) object;
            String changeId = (String) map.get("changeID");
            ChangeControl cc = controlService.findById(changeId).orElseThrow(EntityNotFoundException::new);
            service = cc.getResultMeta2();
        }
        String permission = "SFG_UI_SCT_" + operation + "_ENTITY_" + service;
        return permission;
    }

    private boolean isAllowed(String permission) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> listOfPermissions = authorities
                .stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        return listOfPermissions.contains(permission);
    }
}