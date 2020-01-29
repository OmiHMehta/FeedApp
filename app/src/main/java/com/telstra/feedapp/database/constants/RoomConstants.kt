package com.telstra.feedapp.database.constants

class RoomConstants {

    object RoomBuilder {
        const val DB_NAME: String = "test_room_database"
        const val DB_VERSION: Int = 1
    }

    object Tables {
        const val NewsFeed: String = "feed_data"
    }

    object ColumnName {
        const val ImageUrl: String = "image"
    }
}