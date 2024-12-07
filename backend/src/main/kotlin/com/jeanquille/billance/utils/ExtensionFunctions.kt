package com.jeanquille.billance.utils

import com.jeanquille.billance.models.Account

fun Account.removeFriend(friend: Account) {
    this.friends.remove(friend)
}

fun Account.addFriend(friend: Account) {
    this.friends.add(friend)
}
