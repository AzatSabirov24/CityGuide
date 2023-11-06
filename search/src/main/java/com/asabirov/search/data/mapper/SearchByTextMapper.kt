package com.asabirov.search.data.mapper

import com.asabirov.search.data.remote.dto.search_by_text.ResultDto
import com.asabirov.search.data.remote.dto.search_by_text.SearchByTextDto
import com.asabirov.search.domain.model.search_by_text.SearchByTextResult
import com.asabirov.search.domain.model.search_by_text.SearchByTextModel

fun ResultDto.toResultModel(): SearchByTextResult {
    return SearchByTextResult(name = name)
}

fun SearchByTextDto.toSearchByTextModel(): SearchByTextModel {
    val results = this.results.map { it.toResultModel() }
    return SearchByTextModel(results = results)
}