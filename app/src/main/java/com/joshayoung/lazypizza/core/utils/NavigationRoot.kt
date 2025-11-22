package com.joshayoung.lazypizza.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.joshayoung.lazypizza.app.domain.models.AppWriteState
import com.joshayoung.lazypizza.auth.presentation.LoginScreenRoot
import com.joshayoung.lazypizza.cart.presentation.cart_list.CartListScreenRoot
import com.joshayoung.lazypizza.cart.presentation.checkout.CheckoutScreenRoot
import com.joshayoung.lazypizza.cart.presentation.confirmation.presentation.ConfirmationScreenRoot
import com.joshayoung.lazypizza.core.presentation.models.BottomNavItemUi
import com.joshayoung.lazypizza.core.ui.theme.CartIcon
import com.joshayoung.lazypizza.core.ui.theme.HistoryIcon
import com.joshayoung.lazypizza.core.ui.theme.MenuIcon
import com.joshayoung.lazypizza.menu.presentation.details.DetailsScreenRoot
import com.joshayoung.lazypizza.menu.presentation.home.HomeScreenRoot
import com.joshayoung.lazypizza.order.presentation.order_history.OrderHistoryScreenRoot
import kotlinx.coroutines.flow.Flow
import org.koin.core.parameter.parametersOf
import kotlin.collections.listOf

@Composable
fun NavigationRoot(
    authFlow: Flow<AppWriteState>,
    navController: NavHostController,
    cartItems: Int
) {
    var isLoggedIn by remember { mutableStateOf(false) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        backStackEntry
            ?.destination
            ?.route
            ?.substringAfterLast(".")

    ObserveAsEvents(authFlow) { authState ->
        isLoggedIn = authState.isLoggedIn

        if (isLoggedIn && currentRoute != Routes.Login.toString()) {
            navController.navigate(Routes.Menu) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }

    val bottomNavigationItems =
        listOf(
            BottomNavItemUi(
                label = "Menu",
                selected = currentRoute == Routes.Menu.toString(),
                clickAction = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                imageVector = MenuIcon
            ),
            BottomNavItemUi(
                label = "Cart",
                selected = currentRoute == Routes.Cart.toString(),
                clickAction = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                imageVector = CartIcon
            ),
            BottomNavItemUi(
                label = "History",
                selected = currentRoute == Routes.History.toString(),
                clickAction = {
                    navController.navigate(Routes.History) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                imageVector = HistoryIcon
            )
        )

    NavHost(
        navController = navController,
        startDestination = Routes.Menu
    ) {
        composable<Routes.Menu> {
            HomeScreenRoot(
                isLoggedIn = isLoggedIn,
                bottomNavItemUis = bottomNavigationItems,
                goToLoginScreen = {
                    navController.navigate(Routes.Login)
                },
                cartItems = cartItems,
                goToDetails = { id ->
                    navController.navigate(
                        Routes.Details.toString() + "?productId=$id"
                    )
                }
            )
        }

        composable(
            route = Routes.Details.toString() + "?productId={productId}",
            arguments =
                listOf(
                    navArgument(
                        name = "productId"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
        ) {
            DetailsScreenRoot(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToCart = {
                    navController.navigate(Routes.Cart) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.Cart> {
            CartListScreenRoot(
                bottomNavItemUis = bottomNavigationItems,
                cartItems = cartItems,
                checkout = {
                    navController.navigate(Routes.Checkout)
                },
                backToMenu = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.History> {
            OrderHistoryScreenRoot(
                isLoggedIn = isLoggedIn,
                cartItems = cartItems,
                bottomNavItemUis = bottomNavigationItems,
                goToLogin = {
                    navController.navigate(Routes.Login)
                },
                goToMenu = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.Login> {
            LoginScreenRoot(
                useAsGuest = {
                    navController.navigate(Routes.Menu) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.Checkout> {
            CheckoutScreenRoot(
                backToCart = {
                    navController.navigateUp()
                },
                navController = navController
            )
        }

        composable(
            route =
                Routes.Confirmation.toString() +
                    "?orderNumber={orderNumber}",
            arguments =
                listOf(
                    navArgument(
                        name = "orderNumber"
                    ) {
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
        ) {
            ConfirmationScreenRoot(
                backToMain = {
                    navController.navigate(Routes.Menu) {
                        parametersOf()
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
