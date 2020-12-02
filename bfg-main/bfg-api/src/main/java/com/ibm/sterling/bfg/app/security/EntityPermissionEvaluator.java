package com.ibm.sterling.bfg.app.security;

import com.ibm.sterling.bfg.app.exception.entity.ChangeControlNotFoundException;
import com.ibm.sterling.bfg.app.exception.entity.EntityNotFoundException;
import com.ibm.sterling.bfg.app.model.entity.Entity;
import com.ibm.sterling.bfg.app.model.entity.ChangeControl;
import com.ibm.sterling.bfg.app.model.changecontrol.Operation;
import com.ibm.sterling.bfg.app.service.entity.ChangeControlService;
import com.ibm.sterling.bfg.app.service.entity.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
public class EntityPermissionEvaluator {

    @Autowired
    private EntityService entityService;

    @Autowired
    private ChangeControlService controlService;

    private BiFunction<String, String, String> permission = (entityOperation, entityService) ->
            new Formatter().format("SFG_UI_SCT_%s_ENTITY_%s",
                    entityOperation, entityService).toString();

    public boolean checkCreatePermission(Entity entity) {
        return isAllowed(permission.apply("CREATE", entity.getService()));
    }

    public boolean checkDeletePermission(int id) {
        Entity entityToDelete = entityService.findById(id).orElseThrow(EntityNotFoundException::new);
        return isAllowed(permission.apply("DELETE", entityToDelete.getService()));
    }

    public boolean checkEditPermission(int id) {
        Entity entityToEdit = entityService.findById(id).orElseThrow(EntityNotFoundException::new);
        return isAllowed(permission.apply("EDIT", entityToEdit.getService()));
    }

    public boolean checkEditPendingEntityChangePermission(String id, String serviceFromEntity) {
        ChangeControl changeControl = controlService.findById(id)
                .orElseThrow(ChangeControlNotFoundException::new);
        Operation operation = changeControl.getOperation();
        if (operation.equals(Operation.DELETE)) return false;
        String service = operation.equals(Operation.CREATE) ? serviceFromEntity : changeControl.getResultMeta2();
        return isAllowed(permission.apply(operation.getOperationPerm(), service));
    }

    public boolean checkDeletePendingEntityChangePermission(String id) {
        ChangeControl changeControl = controlService.findById(id)
                .orElseThrow(ChangeControlNotFoundException::new);
        String service = changeControl.getResultMeta2();
        return isAllowed(permission.apply(changeControl.getOperation().getOperationPerm(), service));
    }

    public boolean checkApprovePermission(Map<String, String> approve) {
        ChangeControl changeControl = controlService.findById(approve.get("changeID"))
                .orElseThrow(EntityNotFoundException::new);
        String service = changeControl.getResultMeta2();
        return isAllowed(permission.apply("APPROVE", service));
    }

    private boolean isAllowed(String permission) {
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<String> listOfPermissions = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return listOfPermissions.contains(permission);
    }

}
