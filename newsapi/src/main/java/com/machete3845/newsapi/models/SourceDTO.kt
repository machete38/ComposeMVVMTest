package com.machete3845.newsapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SourceDTO(
    @SerialName("id") val id: String?,
    @SerialName("name") val name: String
)
