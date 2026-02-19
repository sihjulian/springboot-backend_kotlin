package com.example.demo.model

import jakarta.persistence.*
import java.time.Instant

// Represents a neighborhood community that groups users and notices.
@Entity
@Table(name = "communities")
class Community(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, length = 120)
    var name: String = "",

    @Column(nullable = false, length = 200)
    var address: String = "",

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    var updatedAt: Instant = Instant.now(),
) {
    @PrePersist
    fun onCreate() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
