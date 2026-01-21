package com.danielvilha.wubba_lubba.repository

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Optional
import com.danielvilha.wubba_lubba.GetCharacterDetailQuery
import com.danielvilha.wubba_lubba.GetCharactersQuery
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apolloClient: ApolloClient
) {

    suspend fun getCharactersRaw(
        page: Int,
        name: String?,
        species: String?
    ): GetCharactersQuery.Characters? {
        val response = apolloClient.query(
            GetCharactersQuery(
                page = Optional.presentIfNotNull(page),
                name = Optional.presentIfNotNull(name),
                species = Optional.presentIfNotNull(species)
            )
        ).execute()

        return response.data?.characters
    }

    suspend fun getCharacters(name: String?, species: String?): List<GetCharactersQuery.Result?> {
        val response = apolloClient.query(
            GetCharactersQuery(
                name = Optional.presentIfNotNull(name),
                species = Optional.presentIfNotNull(species)
            )
        ).execute()

        return response.data?.characters?.results ?: emptyList()
    }

    suspend fun getCharacterDetail(id: String): GetCharacterDetailQuery.Character? {
        val response = apolloClient.query(GetCharacterDetailQuery(id)).execute()
        return response.data?.character
    }
}