package com.dietapplication.view.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.NavType

@Composable
fun AppNavHost(
    navController: NavHostController,
    isFirstLaunch: Boolean,
    isLoggedIn: Boolean
) {
    val startDest = when {
        isFirstLaunch -> Root.Welcome.route
        isLoggedIn    -> Main.Graph.route
        else          -> Auth.SignIn.route
    }

    NavHost(navController = navController, startDestination = startDest) {

        // -------- ROOT --------
        composable(Root.Welcome.route) {
            WelcomeScreen(
                onGetStarted = { navController.navigate(Root.Onboarding.route) },
                onHaveAccount = { navController.navigate(Auth.SignIn.route) }
            )
        }
        composable(Root.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Auth.SignUp.route) {
                        popUpTo(Root.Welcome.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Root.Settings.route) { SettingsScreen() }

        // -------- AUTH GRAPH --------
        navigation(startDestination = Auth.SignIn.route, route = "auth") {
            composable(Auth.SignIn.route) {
                SignInScreen(
                    onSignInSuccess = {
                        // yeni kullanıcıysa setup'a
                        navController.navigate(Setup.Profile.route) { popUpTo(0) }
                    },
                    onGoSignUp = { navController.navigate(Auth.SignUp.route) },
                    onForgot   = { navController.navigate(Auth.Forgot.route) }
                )
            }
            composable(Auth.SignUp.route) {
                SignUpScreen(
                    onSignUpSuccess = { navController.navigate(Setup.Profile.route) { popUpTo(0) } },
                    onGoSignIn = { navController.popBackStack() }
                )
            }
            composable(Auth.Forgot.route) { ForgotPasswordScreen(onDone = { navController.popBackStack() }) }
        }

        // -------- SETUP WIZARD --------
        navigation(startDestination = Setup.Profile.route, route = "setup") {
            composable(Setup.Profile.route)  { ProfileSetupScreen(onNext = { navController.navigate(Setup.Activity.route) }) }
            composable(Setup.Activity.route) { ActivitySetupScreen(onNext = { navController.navigate(Setup.Diet.route) }, onBack = { navController.popBackStack() }) }
            composable(Setup.Diet.route)     { DietSetupScreen(onNext = { navController.navigate(Setup.Pace.route) }, onBack = { navController.popBackStack() }) }
            composable(Setup.Pace.route)     { PaceSetupScreen(onNext = { navController.navigate(Setup.Reminders.route) }, onBack = { navController.popBackStack() }) }
            composable(Setup.Reminders.route){ ReminderSetupScreen(onFinish = {
                navController.navigate(Main.Graph.route) { popUpTo(0) }
            }, onBack = { navController.popBackStack() }) }
        }

        // -------- MAIN GRAPH (BottomNav) --------
        navigation(startDestination = Main.Home.route, route = Main.Graph.route) {
            composable(Main.Home.route)     { HomeScreen(
                onQuickAdd = { mealType, date -> navController.navigate(Log.AddMeal.create(mealType, date)) },
                onGoSettings = { navController.navigate(Root.Settings.route) }
            ) }
            composable(Main.Log.route)      { LogScreen(
                onSearch = { q -> navController.navigate(Log.Search.create(q)) },
                onScan   = { navController.navigate(Log.Scan.route) },
                onAddMeal= { mealType, date -> navController.navigate(Log.AddMeal.create(mealType, date)) }
            ) }
            composable(Main.Progress.route) { ProgressScreen() }
            composable(Main.Profile.route)  { ProfileScreen(onSettings = { navController.navigate(Root.Settings.route) }) }

            // LOG detayları (sadece bunlarda argüman var)
            composable(
                route = Log.AddMeal.route,
                arguments = listOf(
                    navArgument("mealType"){ type = NavType.StringType; defaultValue = "breakfast" },
                    navArgument("date"){ type = NavType.StringType; defaultValue = "" }
                )
            ) { AddMealScreen() }

            composable(
                route = Log.Search.route,
                arguments = listOf(navArgument("query"){ type = NavType.StringType; defaultValue = "" })
            ) { FoodSearchScreen() }

            composable(Log.Scan.route) { BarcodeScanScreen() }
            composable(
                route = Log.Recipe.route,
                arguments = listOf(navArgument("recipeId"){ type = NavType.StringType })
            ) { RecipeDetailScreen() }
        }
    }
}

/* -------------------- GEÇİCİ PLACEHOLDER EKRANLAR -------------------- */

@Composable
fun WelcomeScreen(onGetStarted: () -> Unit, onHaveAccount: () -> Unit) {
    Centered {
        Text("Welcome")
        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = onGetStarted) { Text("Başla") }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(onClick = onHaveAccount) { Text("Hesabım Var") }
        }
    }
}

@Composable fun OnboardingScreen(onFinish: () -> Unit) {
    Centered {
        Text("Onboarding")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onFinish) { Text("Devam Et") }
    }
}

@Composable fun SettingsScreen() { Centered { Text("Settings") } }

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit,
    onGoSignUp: () -> Unit,
    onForgot: () -> Unit
) {
    Centered {
        Text("Sign In")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onSignInSuccess) { Text("Giriş Yap") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onGoSignUp) { Text("Kayıt Ol") }
        TextButton(onClick = onForgot) { Text("Şifremi Unuttum") }
    }
}

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onGoSignIn: () -> Unit
) {
    Centered {
        Text("Sign Up")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onSignUpSuccess) { Text("Kaydı Tamamla") }
        TextButton(onClick = onGoSignIn) { Text("Girişe Dön") }
    }
}

@Composable fun ForgotPasswordScreen(onDone: () -> Unit) {
    Centered {
        Text("Forgot Password")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onDone) { Text("   Tamam") }
    }
}

@Composable fun ProfileSetupScreen(onNext: () -> Unit) {
    Centered {
        Text("Setup → Profile")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onNext) { Text("İleri") }
    }
}

@Composable fun ActivitySetupScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Centered {
        Text("Setup → Activity")
        Spacer(Modifier.height(12.dp))
        Row {
            OutlinedButton(onClick = onBack) { Text("Geri") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onNext) { Text("İleri") }
        }
    }
}

@Composable fun DietSetupScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Centered {
        Text("Setup → Diet")
        Spacer(Modifier.height(12.dp))
        Row {
            OutlinedButton(onClick = onBack) { Text("Geri") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onNext) { Text("İleri") }
        }
    }
}

@Composable fun PaceSetupScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Centered {
        Text("Setup → Pace")
        Spacer(Modifier.height(12.dp))
        Row {
            OutlinedButton(onClick = onBack) { Text("Geri") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onNext) { Text("İleri") }
        }
    }
}

@Composable fun ReminderSetupScreen(onFinish: () -> Unit, onBack: () -> Unit) {
    Centered {
        Text("Setup → Reminders")
        Spacer(Modifier.height(12.dp))
        Row {
            OutlinedButton(onClick = onBack) { Text("Geri") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onFinish) { Text("Bitir") }
        }
    }
}

@Composable
fun HomeScreen(
    onQuickAdd: (mealType: String, date: String) -> Unit,
    onGoSettings: () -> Unit
) {
    Centered {
        Text("Home")
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onQuickAdd("breakfast", "") }) { Text("Hızlı Ekle (Kahvaltı)") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onGoSettings) { Text("Ayarlar") }
    }
}

@Composable
fun LogScreen(
    onSearch: (String) -> Unit,
    onScan: () -> Unit,
    onAddMeal: (String, String) -> Unit
) {
    Centered {
        Text("Log")
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onSearch("") }) { Text("Ara") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = onScan) { Text("Barkod Tara") }
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = { onAddMeal("dinner", "") }) { Text("Öğün Ekle") }
    }
}

@Composable fun ProgressScreen() { Centered { Text("Progress") } }
@Composable fun FoodSearchScreen() { Centered { Text("Food Search") } }
@Composable fun AddMealScreen() { Centered { Text("Add Meal") } }
@Composable fun BarcodeScanScreen() { Centered { Text("Scan") } }
@Composable fun RecipeDetailScreen() { Centered { Text("Recipe Detail") } }

@Composable
fun ProfileScreen(onSettings: () -> Unit) {
    Centered {
        Text("Profile")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onSettings) { Text("Ayarlar") }
    }
}

/* Küçük yardımcı */
@Composable
private fun Centered(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        content = content
    )
}
