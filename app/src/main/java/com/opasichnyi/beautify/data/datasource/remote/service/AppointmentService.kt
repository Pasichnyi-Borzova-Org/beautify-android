package com.opasichnyi.beautify.data.datasource.remote.service

import com.google.gson.JsonElement
import com.opasichnyi.beautify.data.entity.DataAppointment
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AppointmentService {

    @GET("appointments/{username}")
    suspend fun getAppointmentsOfUser(@Path("username") username: String): Response<List<DataAppointment>>

    @POST("appointments/create")
    suspend fun createAppointment(@Body json: JsonElement): Response<DataAppointment>

    @DELETE("appointments/delete/{id}")
    suspend fun deleteAppointment(@Path("id") id: Long): Response<String>
}