package com.vp.list.viewmodel

import com.vp.list.model.ListItem
import com.vp.list.viewmodel.ListState.ERROR
import com.vp.list.viewmodel.ListState.IN_PROGRESS
import com.vp.list.viewmodel.ListState.LOADED
import java.util.Objects

class SearchResult private constructor(
  val items: List<ListItem>,
  val totalResult: Int,
  val listState: ListState
) {

  override fun equals(o: Any?): Boolean {
    if (this === o) return true
    if (o == null || javaClass != o.javaClass) return false
    val that = o as SearchResult
    return totalResult == that.totalResult && items == that.items && listState === that.listState
  }

  override fun hashCode() = Objects.hash(items, totalResult, listState)

  companion object {
    @JvmStatic fun error() = SearchResult(emptyList(), 0, ERROR)
    @JvmStatic fun success(items: List<ListItem>, totalResult: Int) = SearchResult(items, totalResult, LOADED)
    @JvmStatic fun inProgress() = SearchResult(emptyList(), 0, IN_PROGRESS)
  }
}