package com.m3h3mm3dd.expensetracker.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLogs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @Lob
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ActorId")
    private User actor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}