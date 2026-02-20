package com.example.demo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import java.time.Instant

// Notice posted by a user in a community.
@Entity
@Table(name = "notices")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
class Notice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id", nullable = false)
    var community: Community? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_id", nullable = false)
    var authorUser: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    var category: NoticeCategory = NoticeCategory.OTHER,

    @Column(nullable = false, length = 140)
    var title: String = "",

    @Column(nullable = false, length = 1000)
    var description: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: NoticeStatus = NoticeStatus.OPEN,

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

// Business categories for notices.
enum class NoticeCategory {
    HELP,
    ALERT,
    PURCHASES,
    MEETING,
    LOST_PET,
    OTHER,
}

enum class NoticeStatus {
    OPEN,
    ATTENDED,
}
