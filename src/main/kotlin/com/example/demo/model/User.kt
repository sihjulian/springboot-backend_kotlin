package com.example.demo.model

import jakarta.persistence.*
import java.time.Instant

// Community member identified by email and address (simple authentication).
@Entity
@Table(
    name = "users",
    uniqueConstraints = [UniqueConstraint(columnNames = ["email"])],
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    var community: Community? = null,

    @Column(nullable = false, length = 120)
    var name: String = "",

    @Column(nullable = false, length = 120)
    var email: String = "",

    @Column(nullable = false, length = 200)
    var address: String = "",

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),
) {
    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }
}
