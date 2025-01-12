package com.vkasurinen.vknews.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.model.Source

@Composable
fun TopNews(articles: List<Article>, article: Article, navHostController: NavHostController) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = "Top News",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(articles) { article ->
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(article.urlToImage)
                        .size(Size.ORIGINAL)
                        .build()
                )

                val imageState = painter.state

                Column(
                    modifier = Modifier
                        .width(350.dp)
                        .padding(horizontal = 8.dp)
                ) {
                    if (imageState is AsyncImagePainter.State.Error) {
                        Box(
                            modifier = Modifier
                                .height(175.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(70.dp),
                                imageVector = Icons.Rounded.ImageNotSupported,
                                contentDescription = null
                            )
                        }
                    } else {
                        Image(
                            modifier = Modifier
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = painter,
                            contentDescription = "Name",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Breaking",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = article.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


class SampleArticleProvider : PreviewParameterProvider<Article> {
    override val values = sequenceOf(
        Article(
            author = "Author Name",
            content = "Sample content",
            description = "Sample description",
            publishedAt = "2025-01-11T15:42:53Z",
            source = Source(id = "sample-source", name = "Sample Source"),
            title = "Sample Title",
            url = "https://www.example.com",
            urlToImage = "https://www.example.com/sample.jpg"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewTopNews(
    @PreviewParameter(SampleArticleProvider::class) article: Article
) {
    val navController = rememberNavController()
    TopNews(articles = listOf(article), article = article, navHostController = navController)
}



