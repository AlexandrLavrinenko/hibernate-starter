package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment extends AuditableEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    @Version
//    private Long version;

    @Column(nullable = false)
    private Integer amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

//    @PrePersist
//    public void prePersist() {  // метод назвать можно как угодно, главное тип VOID
//        setCreatedAt(Instant.now());
////        setCreatedBy(SecurityContext.getUser().getUserName());    // в реальных приложениях
//    }
//
//    @PreUpdate
//    public void preUpdate() {
//        setUpdatedAt(Instant.now());
////        setUpdatedBy(SecurityContext.getUser().getUserName());    // в реальных приложениях
//    }
}