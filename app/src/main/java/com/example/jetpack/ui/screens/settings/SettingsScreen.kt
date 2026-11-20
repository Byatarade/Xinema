package com.example.jetpack.ui.screens.settings

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.R
import com.example.jetpack.core.common.LocaleHelper
import com.example.jetpack.ui.theme.DarkBackground
import com.example.jetpack.ui.theme.DarkPill
import com.example.jetpack.ui.theme.DarkSurface
import com.example.jetpack.ui.theme.DarkSurfaceVariant
import com.example.jetpack.ui.theme.LightText
import com.example.jetpack.ui.theme.MutedText
import com.example.jetpack.ui.theme.NetflixRed

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var currentLanguage by remember { mutableStateOf(LocaleHelper.getLanguage(context)) }
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = DarkBackground,
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBackground)
                .verticalScroll(scrollState)
        ) {
            // Header with Back Button and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(DarkSurfaceVariant.copy(alpha = 0.85f))
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LightText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = stringResource(R.string.settings_title),
                    color = LightText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Section: Language
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = NetflixRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.language),
                        color = LightText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.language_subtitle),
                    color = MutedText,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Option: English
                LanguageOptionCard(
                    flag = "🇬🇧",
                    title = stringResource(R.string.english),
                    subtitle = "English (US)",
                    isSelected = currentLanguage == LocaleHelper.LANGUAGE_EN,
                    onClick = {
                        if (currentLanguage != LocaleHelper.LANGUAGE_EN) {
                            currentLanguage = LocaleHelper.LANGUAGE_EN
                            LocaleHelper.setLanguage(context, LocaleHelper.LANGUAGE_EN)
                            (context as? Activity)?.recreate()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Option: Bahasa Indonesia
                LanguageOptionCard(
                    flag = "🇮🇩",
                    title = stringResource(R.string.indonesian),
                    subtitle = "Bahasa Indonesia (ID)",
                    isSelected = currentLanguage == LocaleHelper.LANGUAGE_IN,
                    onClick = {
                        if (currentLanguage != LocaleHelper.LANGUAGE_IN) {
                            currentLanguage = LocaleHelper.LANGUAGE_IN
                            LocaleHelper.setLanguage(context, LocaleHelper.LANGUAGE_IN)
                            (context as? Activity)?.recreate()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Section: About App
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = NetflixRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = stringResource(R.string.app_info),
                        color = LightText,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.about_app),
                                color = LightText,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .background(DarkPill, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "v1.0.0",
                                    color = NetflixRed,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(R.string.about_description),
                            color = MutedText,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun LanguageOptionCard(
    flag: String,
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) DarkSurfaceVariant else DarkSurface
        ),
        border = BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) NetflixRed else Color.White.copy(alpha = 0.08f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Flag Container
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.06f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = flag,
                        fontSize = 24.sp
                    )
                }

                Column {
                    Text(
                        text = title,
                        color = LightText,
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = MutedText,
                        fontSize = 12.sp
                    )
                }
            }

            // Radio / Check Indicator
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) NetflixRed else Color.Transparent
                    )
                    .then(
                        if (!isSelected) {
                            Modifier.background(
                                color = Color.Transparent,
                                shape = CircleShape
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = LightText,
                        modifier = Modifier.size(16.dp)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    )
                }
            }
        }
    }
}
