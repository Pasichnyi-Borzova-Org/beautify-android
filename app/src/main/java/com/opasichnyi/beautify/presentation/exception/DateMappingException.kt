package com.opasichnyi.beautify.presentation.exception

// TODO("Add localizations to exceptions")
class DateMappingException(override val message: String = "Date has wrong format") :
    Exception(message)