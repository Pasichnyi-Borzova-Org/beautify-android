package com.opasichnyi.beautify.data.exception

class LoginFailedException(
    override val message: String = "Can't find account with such username or password"
) : Exception(message)