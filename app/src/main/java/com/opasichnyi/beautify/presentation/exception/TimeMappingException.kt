package com.opasichnyi.beautify.presentation.exception

class TimeMappingException(override val message: String = "Time has wrong format") :
    Exception(message)