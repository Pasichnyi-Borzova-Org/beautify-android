package com.opasichnyi.beautify.data.datasource.remote.service

import com.google.gson.JsonElement
import com.opasichnyi.beautify.data.entity.ApiCallResult
import com.opasichnyi.beautify.data.entity.DataAppointment
import com.opasichnyi.beautify.domain.entity.AppointmentCreationError
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AppointmentService {

    @GET("appointments/{username}")
    suspend fun getAppointmentsOfUser(@Path("username") username: String): Response<List<DataAppointment>>

    @POST("appointments/create")
    suspend fun createAppointment(@Body json: JsonElement): Response<ApiCallResult<DataAppointment, AppointmentCreationError>>

    @DELETE("appointments/delete/{id}")
    suspend fun deleteAppointment(@Path("id") id: Long): Response<String>

    // TODO("Check session to allow only master to complete appointment")
    @POST("appointments/complete/{id}")
    suspend fun completeAppointment(@Path("id") id: Long): Response<DataAppointment>

    @POST("appointments/rate/{id}/{rating}")
    suspend fun rateAppointment(
        @Path("id") id: Long,
        @Path("rating") rating: Int
    ): Response<DataAppointment>

    @PUT("appointments/update")
    suspend fun updateAppointment(@Body json: JsonElement): Response<ApiCallResult<DataAppointment, AppointmentCreationError>>
}