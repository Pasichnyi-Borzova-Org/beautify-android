package com.opasichnyi.beautify.data.exception

class MappingDataToDomainException(
    override val message: String = "Can't map data entity to domain",
) : Exception(message)