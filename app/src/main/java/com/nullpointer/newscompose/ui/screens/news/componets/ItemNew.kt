package com.nullpointer.newscompose.ui.screens.news.componets

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.nullpointer.newscompose.R
import com.nullpointer.newscompose.models.NewsDB
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
fun ItemNew(
    new: NewsDB,
    actionClick: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val painter = rememberImagePainter(new.urlImg) {
        crossfade(true)
        placeholder(R.drawable.ic_image)
        error(R.drawable.ic_hide_image)
    }
    Card(modifier = modifier.padding(4.dp),
        shape = RoundedCornerShape(5.dp),
        onClick = { actionClick(new.urlNew) }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)) {
                Image(
                    painter = painter,
                    contentDescription = stringResource(R.string.description_img_new),
                    contentScale = ContentScale.FillWidth,
                    modifier = when (painter.state) {
                        is ImagePainter.State.Success -> Modifier.fillMaxSize()
                        else -> Modifier.size(150.dp)
                    })
                if (painter.state is ImagePainter.State.Loading) {
                    CircularProgressIndicator()
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = new.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                fontWeight = FontWeight.W600)
            new.description?.let {
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = new.description,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 4,
                    style = MaterialTheme.typography.body1)
            }

        }
    }
}

@Composable
fun ItemNewShimmer() {
    Card(modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(5.dp)) {
        Column(modifier = Modifier
            .padding(10.dp)
            .shimmer()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray),
            )
            Spacer(modifier = Modifier.height(15.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.LightGray))

            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(Color.LightGray))

        }
    }

}