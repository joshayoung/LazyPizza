package com.joshayoung.lazypizza.search.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaRepository
import com.joshayoung.lazypizza.core.domain.LazyPizzaStorage
import com.joshayoung.lazypizza.core.domain.models.Product
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.search.data.models.Products
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val lazyPizzaRepository: LazyPizzaRepository,
    private val lazyPizzaStorage: LazyPizzaStorage
) : ViewModel() {
    private var _state = MutableStateFlow(HomeState())
    private var pizzas: List<Product> = emptyList()
    private var drinks: List<Product> = emptyList()
    private var iceCream: List<Product> = emptyList()

    val state =
        _state
            .onStart {
                loadData()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(1000L),
                HomeState()
            )

    init {
        _state.update {
            it.copy(
                token = lazyPizzaStorage.getJwt()
            )
        }
        _state.value.search
            .textAsFlow()
            .onEach { search ->
                searchList(search)
            }.launchIn(viewModelScope)
    }

    private fun searchList(search: CharSequence) {
        if (search.count() == 0) {
            val all =
                listOf(
                    Products(
                        name = "pizzas",
                        items = pizzas
                    ),
                    Products(
                        name = "drinks",
                        items = drinks
                    ),
                    Products(
                        name = "ice cream",
                        items = iceCream
                    )
                )
            _state.update {
                it.copy(
                    items = all
                )
            }
            return
        }

        val filteredPizzas = pizzas.filter { it.name.contains(search) }
        val filteredDrinks = drinks.filter { it.name.contains(search) }
        val filteredIceCream = iceCream.filter { it.name.contains(search) }

        val itemsFound = filteredPizzas + filteredDrinks + filteredIceCream

        if (itemsFound.count() < 1) {
            _state.update {
                it.copy(
                    items = emptyList(),
                    noItemsFound = true
                )
            }
            return
        }

        val all =
            listOf(
                Products(
                    name = "pizzas",
                    items = filteredPizzas
                ),
                Products(
                    name = "drinks",
                    items = filteredDrinks
                ),
                Products(
                    name = "ice cream",
                    items = filteredIceCream
                )
            )
        _state.update {
            it.copy(
                items = all,
                noItemsFound = false
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            pizzas = lazyPizzaRepository.getTableData(BuildConfig.PIZZA_COLLECTION_ID)
            drinks = lazyPizzaRepository.getTableData(BuildConfig.DRINK_COLLECTION_ID)
            iceCream = lazyPizzaRepository.getTableData(BuildConfig.ICE_CREAM_COLLECTION_ID)
            val drinkStart = pizzas.count() + 1
            val iceCreamStart = pizzas.count() + drinks.count() + 1
            val all =
                listOf(
                    Products(
                        name = "pizzas",
                        items = pizzas
                    ),
                    Products(
                        name = "drinks",
                        items = drinks
                    ),
                    Products(
                        name = "ice cream",
                        items = iceCream
                    )
                )

            _state.update {
                it.copy(
                    items = all,
                    pizzaScrollPosition = 0,
                    drinkScrollPosition = drinkStart,
                    iceCreamScrollPosition = iceCreamStart
                )
            }
        }
    }
}
