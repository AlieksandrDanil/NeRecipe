package ru.netology.nerecipe.dto

object RecipesFilled {
    private var primId = 0L

    val empty = Recipe(
        id = 0,
        pos = 0,
        author = "",
        name = "",
        category = "Empty",
        content = "",
        likedByMe = false,
    )

    val recipesFilled = listOf(
        Recipe(
            id = primId,
            pos = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            name = "Рецепт - Супер кухня от Бидо",
            category = "Панаазиатская",
            content = "Освоение новой кухни — это не только открывающиеся возможности и перспективы, но и настоящий вызов самому себе. Приходится выходить из зоны комфорта и перестраивать привычный образ жизни: менять распорядок дня, искать время для занятий, быть готовым к возможным неудачам в начале пути. В блоге рассказали, как избежать стресса на курсах профпереподготовки → http://netolo.gy/fPD",
            likedByMe = false,
            likes = 999,
            shared = 1_099,
            viewed = 2_390_480,
            video = "With.error:https:www.youtube.com/watch?v=WhWc3b3KhnY"
        ),
        Recipe(
            id = primId,
            pos = 2,
            author = "Даниленко Сергей Викторович",
            name = "Рецепт - Классная кухня",
            category = "Средиземноморская",
            content = "\uD83D\uDE80 Средиземноморская кухня стартует новый поток бесплатного курса «Диджитал-старт: первый шаг к востребованной профессии» — за две недели вы попробуете себя в разных профессиях и определите, что подходит именно вам → http://netolo.gy/fQ",
            likedByMe = false,
            likes = 999,
            shared = 9_996,
            viewed = 2_390_480,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        ),
    )
}