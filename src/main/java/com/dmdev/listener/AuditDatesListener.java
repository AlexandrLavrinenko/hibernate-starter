package com.dmdev.listener;

import com.dmdev.entity.AuditableEntity;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

public class AuditDatesListener {
    @PrePersist
    public void prePersist(AuditableEntity<?> entity) {  // метод назвать можно как угодно, главное тип VOID
        entity.setCreatedAt(Instant.now());
//        entity.setCreatedBy(SecurityContext.getUser().getUserName());    // в реальных приложениях
    }

    @PreUpdate
    public void preUpdate(AuditableEntity<?> entity) {
        entity.setUpdatedAt(Instant.now());
//        entity.setUpdatedBy(SecurityContext.getUser().getUserName());    // в реальных приложениях
    }
}
