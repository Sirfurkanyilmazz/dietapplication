package com.dietapplication.view.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape

@Composable
fun AppNavHost(
    navController: NavHostController,
    isFirstLaunch: Boolean,
    isLoggedIn: Boolean
) {
    // --- start destination kararı ---
    val startDest = when {
        isFirstLaunch -> Root.Welcome.route
        isLoggedIn    -> Main.Graph.route
        else          -> Auth.SignIn.route
    }

    // --- shell: top bar + bottom bar + FAB(Tara) ---
    Scaffold(
        topBar = {
            AppTopBar(
                onOpenProfile = { navController.navigate(Root.Profile.route) },
                onOpenSettings = { navController.navigate(Root.Settings.route) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Log.Scan.route) },
                shape = CircleShape
            ) { Icon(Icons.Default.CameraAlt, contentDescription = "Tara") }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar {
                BottomNavItem(
                    label = "Ana",
                    icon = Icons.Default.Home,
                    route = Main.Home.route,
                    navController = navController
                )
                BottomNavItem(
                    label = "Günlük",
                    icon = Icons.Default.EditNote,
                    route = Main.Diary.route,   // <— Main.Log yerine Main.Diary
                    navController = navController
                )
                Spacer(Modifier.weight(1f))   // FAB için boşluk
                BottomNavItem(
                    label = "Planlar",
                    icon = Icons.Default.TableView,
                    route = Main.Plans.route,
                    navController = navController
                )
                BottomNavItem(
                    label = "İlerleme",
                    icon = Icons.Default.ShowChart,
                    route = Main.Progress.route,
                    navController = navController
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = startDest,
            modifier = Modifier.padding(padding)
        ) {

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
            composable(Root.Profile.route)  { ProfileScreen(onSettings = { navController.navigate(Root.Settings.route) }) }

            // -------- AUTH GRAPH --------
            navigation(startDestination = Auth.SignIn.route, route = "auth") {
                composable(Auth.SignIn.route) {
                    SignInScreen(
                        onSignInSuccess = {
                            // yeni kullanıcı ise setup'a
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
                composable(Setup.Reminders.route){
                    ReminderSetupScreen(
                        onFinish = { navController.navigate(Main.Graph.route) { popUpTo(0) } },
                        onBack   = { navController.popBackStack() }
                    )
                }
            }

            // -------- MAIN GRAPH (BottomNav) --------
            navigation(startDestination = Main.Home.route, route = Main.Graph.route) {

                // Ana
                composable(Main.Home.route) {
                    HomeScreen(
                        onQuickAdd = { mealType, date ->
                            navController.navigate(Log.AddMeal.create(mealType, date))
                        },
                        onGoSettings = { navController.navigate(Root.Settings.route) }
                    )
                }

                // Günlük
                composable(Main.Diary.route) {
                    LogScreen(
                        onSearch = { q -> navController.navigate(Log.Search.create(q)) },
                        onScan   = { navController.navigate(Log.Scan.route) },
                        onAddMeal= { mealType, date -> navController.navigate(Log.AddMeal.create(mealType, date)) }
                    )
                }

                // Planlar
                composable(Main.Plans.route) { PlansScreen(navController) }

                // İlerleme
                composable(Main.Progress.route) { ProgressScreen() }

                // LOG detayları (argümanlı)
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
}

/* ----------------- küçük yardımcılar ----------------- */

@Composable
private fun AppTopBar(
    onOpenProfile: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var open by remember { mutableStateOf(false) }
    SmallTopAppBar(
        title = { Text("Uygulama (DİYET)") },
        actions = {
            IconButton(onClick = { open = !open }) {
                // Foto yoksa default icon
                Icon(Icons.Default.Person, contentDescription = "Profil")
                // Eğer kullanıcı fotoğrafı varsa:
                // AsyncImage(model = photoUrl, contentDescription = "Profil",
                //   modifier = Modifier.size(28.dp).clip(CircleShape))
            }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                DropdownMenuItem(text = { Text("Profil") }, onClick = { open = false; onOpenProfile() })
                DropdownMenuItem(text = { Text("Ayarlar") }, onClick = { open = false; onOpenSettings() })
            }
        }
    )
}

@Composable
private fun RowScope.BottomNavItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    route: String,
    navController: NavHostController
) {
    val current = navController.currentBackStackEntryAsState().value?.destination?.route
    NavigationBarItem(
        selected = current == route,
        onClick = { navController.navigateSingleTop(route) },
        icon = { Icon(icon, null) },
        label = { Text(label) }
    )
}

private fun NavHostController.navigateSingleTop(route: String) = navigate(route) {
    popUpTo(graph.startDestinationId) { saveState = true }
    launchSingleTop = true
    restoreState = true
}
