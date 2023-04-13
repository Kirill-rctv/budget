package mobi.sevenwinds.app.author

import com.papsign.ktor.openapigen.annotations.type.string.length.MaxLength
import com.papsign.ktor.openapigen.route.info
import com.papsign.ktor.openapigen.route.path.normal.NormalOpenAPIRoute
import com.papsign.ktor.openapigen.route.path.normal.post
import com.papsign.ktor.openapigen.route.response.respond
import com.papsign.ktor.openapigen.route.route
import org.joda.time.DateTime

fun NormalOpenAPIRoute.author() {
    route("/author") {
        route("/add").post<Unit, AuthorResponse, AuthorRequest>(info("Добавить запись")) { _, body ->
            respond(AuthorService.addRecord(body))
        }
    }
}

data class AuthorRequest(
    @MaxLength(100) val name: String
)

data class AuthorResponse(
    val id: Int,
    val name: String,
    val datetimeOfCreate: DateTime
)