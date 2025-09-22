package com.dietapplication.view.navigation

sealed interface NavRoute { val route: String }

// --------- Root ----------
sealed class Root(override val route: String): NavRoute {
    data object Splash     : Root("splash")
    data object Welcome    : Root("welcome")
    data object Onboarding : Root("onboarding")
    data object Settings   : Root("settings")   // Profil/Ayarlar burada
    data object Profile    : Root("profile")
}

// --------- Auth ----------
sealed class Auth(override val route: String): NavRoute {
    data object SignIn : Auth("auth/signin")
    data object SignUp : Auth("auth/signup")
    data object Forgot : Auth("auth/forgot")
}

// --------- Setup ----------
sealed class Setup(override val route: String): NavRoute {
    data object Profile   : Setup("setup/profile")
    data object Activity  : Setup("setup/activity")
    data object Diet      : Setup("setup/diet")
    data object Pace      : Setup("setup/pace")
    data object Reminders : Setup("setup/reminders")
}

// --------- Main (Bottom bar + FAB) ----------
sealed class Main(override val route: String): NavRoute {
    data object Graph    : Main("main")
    data object Home     : Main("main/home")
    data object Diary    : Main("main/diary")
    data object Plans    : Main("main/plans")
    data object Progress : Main("main/progress")
    // Profil alt bards değil → Root.Profile/Settings üst menüden açılacak.
}

// --------- Log sub-graph (tara, yemek ekle, arama, tarif) ----------
sealed class Log(override val route: String): NavRoute {

    data object AddMeal : Log("log/addMeal?mealType={mealType}&date={date}") {
        fun create(mealType: String, date: String) =
            "log/addMeal?mealType=${mealType.encode()}&date=${date.encode()}"
    }

    data object Search : Log("log/search?query={query}") {
        fun create(query: String = "") = "log/search?query=${query.encode()}"
    }

    data object Scan : Log("log/scan") // FAB buraya gider

    data object Recipe : Log("log/recipe?recipeId={recipeId}") {
        fun create(recipeId: String) = "log/recipe?recipeId=${recipeId.encode()}"
    }
}

enum class BottomTab(val route: String, val label: String) {
    HOME(Main.Home.route, "Ana"),
    DIARY(Main.Diary.route, "Günlük"),
    PLANS(Main.Plans.route, "Planlar"),
    PROGRESS(Main.Progress.route, "İlerleme");
}

private fun String.encode(): String =
    java.net.URLEncoder.encode(this, Charsets.UTF_8.name())
