package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Serializable entityId;

    private String entityName;

    private String entityContent;   // лучше использовать JSON в реальніх приложениях

    @Enumerated(EnumType.STRING)
    private Operation operation;

    public enum Operation {
    SAVE, UPDATE, DELETE, INSERT
    }
}
