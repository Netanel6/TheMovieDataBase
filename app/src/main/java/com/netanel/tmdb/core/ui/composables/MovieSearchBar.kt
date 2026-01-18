package com.netanel.tmdb.core.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchBar(
    query: String?,
    onQueryChange: (String) -> Unit,
    onSearchClicked: () -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query ?: "",
        onQueryChange = onQueryChange,
        onSearch = {
            onActiveChange(false)
            onSearchClicked()
        },
        active = active,
        onActiveChange = onActiveChange,
        placeholder = { Text("Search movies...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) { }
}
