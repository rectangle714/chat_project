package com.chat_project.web.friends.enums

enum class FriendRequestStatus(val status: String) {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    companion object {
        fun from(status: String): FriendRequestStatus? {
            return values().find { it.status == status }
        }
    }

}