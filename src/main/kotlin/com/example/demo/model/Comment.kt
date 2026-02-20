package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

// Comment left on a notice by a community member.
@Entity
@Table(name = "comments")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    var notice: Notice? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    var authorUser: User? = null,

    @Column(nullable = false, length = 600)
    var message: String = "",

    @Column(nullable = false)
    var createdAt: Instant = Instant.now(),
) {
    @PrePersist
    fun onCreate() {
        createdAt = Instant.now()
    }
}
