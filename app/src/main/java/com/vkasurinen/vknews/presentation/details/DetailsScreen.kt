package com.vkasurinen.vknews.presentation.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.domain.model.Source
import com.vkasurinen.vknews.presentation.homescreen.components.CoilImage
import com.vkasurinen.vknews.presentation.details.components.DetailsTopBar
import com.vkasurinen.vknews.ui.theme.VKNewsTheme
import org.koin.androidx.compose.koinViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun DetailsScreenRoot(
    navController: NavHostController,
    viewModel: DetailsViewModel = koinViewModel(),
) {
    val articleUrl = navController.previousBackStackEntry?.savedStateHandle?.get<String>("articleUrl")
    val state = viewModel.detailsState.collectAsState().value

    LaunchedEffect(articleUrl) {
        articleUrl?.let { viewModel.getArticle(it) }
    }

    state.article?.let { article ->
        DetailsScreen(
            article = article,
            navHostController = navController,
            onEvent = viewModel::onEvent
        )
    }
}

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun DetailsScreen(
    article: Article,
    navHostController: NavHostController,
    onEvent: (DetailsUiEvent) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        DetailsTopBar(
            onBrowsingClick = {
                val url = article.url
                Log.d("DetailsScreen", "Attempting to open URL: $url")
                if (url.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                    } else {
                        Log.e("DetailsScreen", "No activity found to handle the intent for URL: $url")
                        // Fallback mechanism: Show a message to the user
                        Toast.makeText(context, "No application can handle this request. Please install a web browser.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e("DetailsScreen", "Invalid URL: $url")
                }
            },
            onShareClick = {
                Intent(Intent.ACTION_SEND).also {
                    it.putExtra(Intent.EXTRA_TEXT, article.url)
                    it.type = "text/plain"
                    if (it.resolveActivity(context.packageManager) != null) {
                        context.startActivity(it)
                    }
                }
            },
            onBookMarkClick = {
                onEvent(DetailsUiEvent.UpsertDeleteArticle(article))
            },
            onBackClick = { navHostController.navigateUp() }
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp
            )
        ) {

            item {
                CoilImage(
                    url = article.urlToImage,
                    contentDescription = article.title,
                    aspectRatio = 4f / 3f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(MaterialTheme.shapes.medium),

                )

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = article.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }

            item {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = article.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    //maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview() {
    VKNewsTheme(dynamicColor = false) {
        DetailsScreen(
            article = Article(
                author = "",
                title = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
                description = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
                content = "We use cookies and data to Deliver and maintain Google services Track outages and protect against spam, fraud, and abuse Measure audience engagement and site statistics to unde… [+1131 chars]",
                publishedAt = "2023-06-16T22:24:33Z",
                source = Source(id = "", name = "bbc"),
                url = "https://consent.google.com/ml?continue=https://news.google.com/rss/articles/CBMiaWh0dHBzOi8vY3J5cHRvc2F1cnVzLnRlY2gvY29pbmJhc2Utc2F5cy1hcHBsZS1ibG9ja2VkLWl0cy1sYXN0LWFwcC1yZWxlYXNlLW9uLW5mdHMtaW4td2FsbGV0LXJldXRlcnMtY29tL9IBAA?oc%3D5&gl=FR&hl=en-US&cm=2&pc=n&src=1",
                urlToImage = "https://media.wired.com/photos/6495d5e893ba5cd8bbdc95af/191:100/w_1280,c_limit/The-EU-Rules-Phone-Batteries-Must-Be-Replaceable-Gear-2BE6PRN.jpg"
            ),
            navHostController = NavHostController(LocalContext.current),
            onEvent = {}
        )
    }
}