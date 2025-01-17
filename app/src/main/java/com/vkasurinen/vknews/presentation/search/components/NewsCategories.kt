import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.model.Source
import com.vkasurinen.vknews.ui.theme.VKNewsTheme

@Composable
fun NewsCategories(
    articles: List<Article>,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val categories = listOf("All", "Author", "Published")

        categories.forEach { category ->
            val isSelected = selectedCategory == category
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
            val textColor = if (isSelected) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onSurfaceVariant

            Button(
                onClick = {
                    selectedCategory = category
                    onCategorySelected(category)
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor
                ),
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = category,
                    color = textColor
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NewsCategoriesPreview() {
    VKNewsTheme {
        NewsCategories(
            articles = listOf(
                Article(
                    source = Source(id = "1", name = "Source 1"),
                    author = "Author 1",
                    title = "Title 1",
                    description = "Description 1",
                    url = "https://example.com/1",
                    urlToImage = "https://example.com/image1.jpg",
                    publishedAt = "2025-01-13T12:30:17Z",
                    content = "Content 1"
                ),
                Article(
                    source = Source(id = "2", name = "Source 2"),
                    author = "Author 2",
                    title = "Title 2",
                    description = "Description 2",
                    url = "https://example.com/2",
                    urlToImage = "https://example.com/image2.jpg",
                    publishedAt = "2025-01-14T12:30:17Z",
                    content = "Content 2"
                )
            ),
            onCategorySelected =  {},
            modifier = Modifier,
        )
    }
}
