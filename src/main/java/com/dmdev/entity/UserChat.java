package com.dmdev.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Builder
@Entity
@Table(name = "users_chat")
public class UserChat extends AuditableEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

//    private Instant createdAt;
//
//    private String createdBy;

    public void setUser(User user) {
        this.user = user;
        this.user.getUserChats().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getUserChats().add(this);
    }
}
