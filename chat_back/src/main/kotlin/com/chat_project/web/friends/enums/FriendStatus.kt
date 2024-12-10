package com.chat_project.web.friends.enums

enum class FriendStatus(val status: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    companion object {
        fun from(status: String): FriendStatus? {
            return values().find { it.status == status }
        }
    }

}