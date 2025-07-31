package com.projectbytj.onebancrestaurant.screens

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projectbytj.onebancrestaurant.api.RetrofitInstance
import kotlinx.coroutines.launch


// for cousines

data class GetItemListRequest(
    val page: Int,
    val count: Int
)

data class GetItemListResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val page: Int,
    val count: Int,
    val total_pages: Int,
    val total_items: Int,
    val cuisines: List<Cuisine>
)


data class Cuisine(
    val cuisine_id: String,
    val cuisine_name: String,
    val cuisine_image_url: String,
    val items: List<Dish>
)

data class Dish(
    val id: String,
    val name: String,
    val image_url: String,
    val price: String,
    val rating: String
)



// for items
data class GetItemByIdRequest(
    val item_id: Int
)

data class GetItemByIdResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val cuisine_id: String,
    val cuisine_name: String,
    val cuisine_image_url: String,
    val item_id: Int,
    val item_name: String,
    val item_price: Int,
    val item_rating: Double,
    val item_image_url: String
)


// for filter
data class GetItemByFilterRequest(
    val cuisine_type: List<String>? = null,
    val price_range: Int? = null,   // <-- Added price range filter (like 50, 100, 200)
    val min_rating: Double? = null
)
data class GetItemByFilterResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val items: List<Dish>
)

// Payment data class
data class PaymentRequest(
    val item_id: String,
    val quantity: Int,
    val amount: Int
)



data class PaymentResponse(
    val response_code: Int,
    val outcome_code: Int,
    val response_message: String,
    val transaction_id: String? = null
)



// viewmodel
class PageLink : ViewModel() {

    private val _cuisines = mutableStateOf<List<Cuisine>>(emptyList())
    val cuisines: State<List<Cuisine>> get() = _cuisines

    init {
        fetchCuisines()
    }

    private fun fetchCuisines() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getItemList(
                    GetItemListRequest(page = 1, count = 10)
                )
                _cuisines.value = response.cuisines

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    // to diaply each dish details by id
    private val _selectedItem = mutableStateOf<GetItemByIdResponse?>(null)
    val selectedItem: State<GetItemByIdResponse?> get() = _selectedItem

    fun fetchItemById(itemId: Int) {
        viewModelScope.launch {
            try {
                _selectedItem.value = null  // reset old data
                val response = RetrofitInstance.api.getItemById(GetItemByIdRequest(itemId))
                _selectedItem.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    // for filter to display the dishes

    private val _dishes = mutableStateOf<List<Dish>>(emptyList())
    val dishes: State<List<Dish>> get() = _dishes

    private val _filteredDishes = mutableStateOf<List<Dish>>(emptyList())
    val filteredDishes: State<List<Dish>> get() = _filteredDishes

    fun applyPriceFilter(maxPrice: Int, originalList: List<Dish>) {
        _filteredDishes.value = originalList.filter {
            it.price.toIntOrNull() ?: 0 <= maxPrice
        }
    }
    fun clearFilter() {
        _filteredDishes.value = emptyList()
    }
    fun fetchFilteredDishes(cuisineName: String, maxPrice: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getItemsByFilter(
                    GetItemByFilterRequest(listOf(cuisineName)) // API call (already valid)
                )

                // ✅ Safely convert price
                _filteredDishes.value = response.items.filter { dish ->
                    val price = dish.price.toIntOrNull() ?: Int.MAX_VALUE
                    price <= maxPrice
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _filteredDishes.value = emptyList()
            }
        }
    }



    // payment viewmodel
    private val _paymentResponse = mutableStateOf<PaymentResponse?>(null)
    val paymentResponse: State<PaymentResponse?> get() = _paymentResponse

    fun makePayment(itemId: String, quantity: Int, amount: Int) {
        viewModelScope.launch {
            try {
                // ✅ Log before API call
                Log.d("PAYMENT_API", "Sending payment request: itemId=$itemId, quantity=$quantity, amount=$amount")

                val response = RetrofitInstance.api.makePayment(
                    PaymentRequest(
                        item_id = itemId,
                        quantity = quantity,
                        amount = amount
                    )
                )
                _paymentResponse.value = response

                // ✅ Log after success
                Log.d("PAYMENT_API", "Payment Response: $response")

            } catch (e: Exception) {
                Log.e("PAYMENT_API", "Payment failed", e)  // logs full stacktrace
                _paymentResponse.value = PaymentResponse(
                    response_code = -1,
                    outcome_code = 0,
                    response_message = "Payment Failed"
                )
            }
        }
    }
}
