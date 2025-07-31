package com.projectbytj.onebancrestaurant.api

import com.projectbytj.onebancrestaurant.screens.GetItemByFilterRequest
import com.projectbytj.onebancrestaurant.screens.GetItemByFilterResponse
import com.projectbytj.onebancrestaurant.screens.GetItemByIdRequest
import com.projectbytj.onebancrestaurant.screens.GetItemByIdResponse
import com.projectbytj.onebancrestaurant.screens.GetItemListRequest
import com.projectbytj.onebancrestaurant.screens.GetItemListResponse
import com.projectbytj.onebancrestaurant.screens.PaymentRequest
import com.projectbytj.onebancrestaurant.screens.PaymentResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface FoodService {
    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: get_item_list",
        "Content-Type: application/json"
    )
    @POST("emulator/interview/get_item_list")
    suspend fun getItemList(@Body body: GetItemListRequest): GetItemListResponse

    @POST("emulator/interview/get_item_by_id")
    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: get_item_by_id",
        "Content-Type: application/json"
    )
    suspend fun getItemById(@Body body: GetItemByIdRequest): GetItemByIdResponse

    @POST("emulator/interview/get_item_by_filter")
    @Headers(
        "X-Partner-API-Key: uonebancservceemultrS3cg8RaL30",
        "X-Forward-Proxy-Action: get_item_by_filter",
        "Content-Type: application/json"
    )
    suspend fun getItemsByFilter(@Body body: GetItemByFilterRequest): GetItemByFilterResponse

    @POST("make_payment")
    suspend fun makePayment(@Body request: PaymentRequest): PaymentResponse

}
