package com.dietapplication.view.navigation

// NavRoutes.kt
sealed interface NavRoute { val route: String }

sealed class Root(override val route: String): NavRoute {
    data object Splash : Root("splash")
    data object Welcome : Root("welcome")
    data object Onboarding : Root("onboarding")
    data object Settings : Root("settings")
}

sealed class Auth(override val route: String): NavRoute {
    data object SignIn : Auth("auth/signin")
    data object SignUp : Auth("auth/signup")
    data object Forgot : Auth("auth/forgot")
}

sealed class Setup(override val route: String): NavRoute {
    data object Profile  : Setup("setup/profile")
    data object Activity : Setup("setup/activity")
    data object Diet     : Setup("setup/diet")
    data object Pace     : Setup("setup/pace")
    data object Reminders: Setup("setup/reminders")
}

sealed class Main(override val route: String): NavRoute {
    data object Graph    : Main("main")                 // nested graph root
    data object Home     : Main("main/home")
    data object Log      : Main("main/log")
    data object Progress : Main("main/progress")
    data object Profile  : Main("main/profile")
}

sealed class Log(override val route: String): NavRoute {
    data object AddMeal : Log("log/addMeal?mealType={mealType}&date={date}") {
        fun create(mealType: String, date: String) =
            "log/addMeal?mealType=$mealType&date=$date"
    }
    data object Search  : Log("log/search?query={query}") {
        fun create(query: String = "") = "log/search?query=$query"
    }
    data object Scan    : Log("log/scan")
    data object Recipe  : Log("log/recipe?recipeId={recipeId}") {
        fun create(recipeId: String) = "log/recipe?recipeId=$recipeId"
    }
}
