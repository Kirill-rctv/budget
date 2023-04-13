package mobi.sevenwinds.app.budget

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mobi.sevenwinds.app.author.AuthorEntity
import mobi.sevenwinds.app.author.AuthorTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object BudgetService {
    suspend fun addRecord(body: BudgetRequest): BudgetResponse = withContext(Dispatchers.IO) {
        transaction {
            val entity = BudgetEntity.new {
                this.year = body.year
                this.month = body.month
                this.amount = body.amount
                this.type = body.type
                this.author = if (body.authorId != null) {
                    AuthorEntity.findById(EntityID(body.authorId, AuthorTable))
                } else {
                    null
                }
            }

            return@transaction entity.toResponse()
        }
    }

    suspend fun getYearStats(param: BudgetYearParam): BudgetYearStatsResponse = withContext(Dispatchers.IO) {
        transaction {

            val authorEntityID = AuthorEntity.find { AuthorTable.name eq param.name }.firstOrNull()

            val query = if (authorEntityID != null) {
                BudgetTable
                    .select { (BudgetTable.year eq param.year) or (BudgetTable.authorId eq authorEntityID.id) }
                    .limit(param.limit, param.offset)
            } else {
                BudgetTable
                    .select { BudgetTable.year eq param.year }
                    .limit(param.limit, param.offset)
            }

            val total = query.count()
            val data = BudgetEntity.wrapRows(query).map { it.toResponse() }

            val sumByType = data.groupBy { it.type.name }.mapValues { it.value.sumOf { v -> v.amount } }

            return@transaction BudgetYearStatsResponse(
                total = total,
                totalByType = sumByType,
                items = data
            )
        }
    }
}