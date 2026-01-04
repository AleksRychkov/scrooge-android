package dev.aleksrychkov.scrooge.feature.category

fun interface SwapOrderIndexCategoryUseCase {
    suspend operator fun invoke(args: Args)

    class Args(val data: List<Arg>)
    class Arg(val categoryId: Long, val orderIndex: Int) {
        constructor(pair: Pair<Long, Int>) : this(pair.first, pair.second)
    }
}
