package com.vkasurinen.vknews.presentation.homescreen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.vkasurinen.vknews.domain.model.Article
import com.vkasurinen.vknews.util.Screen
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopNews(
    topHeadlines: List<Article>,
    navHostController: NavHostController,
    navigateToDetails: (NavHostController, Article, Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(topHeadlines) { article ->
                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(horizontal = 8.dp)
                        .clickable {
                            navigateToDetails(navHostController, article, true)
                        }
                ) {
                    CoilImage(url = article.urlToImage, contentDescription = "Name")
                    Spacer(modifier = Modifier.height(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            Text(
                                text = "Breaking",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = article.source.name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )

                            val parsedDate = ZonedDateTime.parse(article.publishedAt)
                            val formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("dd:MM:yyyy"))

                            Text(
                                text = formattedDate,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onBackground,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1
                            )



                        }

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

