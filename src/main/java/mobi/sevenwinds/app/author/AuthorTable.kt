package mobi.sevenwinds.app.author

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable

object AuthorTable : IntIdTable("author") {
    val name = varchar("name", 100)
    val datetimeOfCreate = datetime("datetime_of_create")
}

class AuthorEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AuthorEntity>(AuthorTable)

    var name by AuthorTable.name
    var datetimeOfCreate by AuthorTable.datetimeOfCreate

    fun toResponse(): AuthorResponse {
        return AuthorResponse(id.value, name, datetimeOfCreate)
    }
}