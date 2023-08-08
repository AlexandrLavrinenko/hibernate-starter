package com.dmdev.listener;

import com.dmdev.entity.Audit;
import org.hibernate.event.spi.*;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        auditEntity(event, Audit.Operation.DELETE);

        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        auditEntity(event, Audit.Operation.INSERT);
        return false;
    }

    public void auditEntity(AbstractPreDatabaseOperationEvent event, // PreInsertEvent, PreDeleteEvent extends AbstractPreDatabaseOperationEvent
                            Audit.Operation operation) {
        // чтоб не зациклиться, не нужно производить аудит Audit.class'у
        if (event.getEntity().getClass() != Audit.class) {
            Audit audit = Audit.builder()
                    .entityId(event.getId())
                    .entityName(event.getEntityName())
                    .entityContent(event.getEntity().toString())
                    .operation(operation)
                    .build();
            event.getSession().save(audit);
        }
    }
}
