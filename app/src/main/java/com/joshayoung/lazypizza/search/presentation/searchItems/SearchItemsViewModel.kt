package com.joshayoung.lazypizza.search.presentation.searchItems

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.lazypizza.BuildConfig
import com.joshayoung.lazypizza.core.domain.LazyPizzaPreference
import com.joshayoung.lazypizza.core.presentation.utils.textAsFlow
import com.joshayoung.lazypizza.search.data.models.AllProducts
import com.joshayoung.lazypizza.search.data.models.Product
import com.joshayoung.lazypizza.search.domain.utils.LazyPizzaDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchItemsViewModel(
    private val lazyPizzaDatabase: LazyPizzaDatabase,
    private val lazyPizzaPreference: LazyPizzaPreference
) : ViewModel() {
    private var _state = MutableStateFlow(SearchItemsState())
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
                SearchItemsState()
            )

    init {
        _state.update {
            it.copy(
                token = lazyPizzaPreference.getJwt()
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
                    AllProducts(
                        name = "pizzas",
                        items = pizzas
                    ),
                    AllProducts(
                        name = "drinks",
                        items = drinks
                    ),
                    AllProducts(
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

        val all =
            listOf(
                AllProducts(
                    name = "pizzas",
                    items = filteredPizzas
                ),
                AllProducts(
                    name = "drinks",
                    items = filteredDrinks
                ),
                AllProducts(
                    name = "ice cream",
                    items = filteredIceCream
                )
            )
        _state.update {
            it.copy(
                items = all
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            pizzas = lazyPizzaDatabase.getTableData(BuildConfig.PIZZA_COLLECTION_ID)
            drinks = lazyPizzaDatabase.getTableData(BuildConfig.DRINK_COLLECTION_ID)
            iceCream = lazyPizzaDatabase.getTableData(BuildConfig.ICE_CREAM_COLLECTION_ID)
            val drinkStart = pizzas.count() + 1
            val iceCreamStart = pizzas.count() + drinks.count() + 1
            val all =
                listOf(
                    AllProducts(
                        name = "pizzas",
                        items = pizzas
                    ),
                    AllProducts(
                        name = "drinks",
                        items = drinks
                    ),
                    AllProducts(
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
