package co.koko.heartbeatmessages.ui.screens

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.koko.heartbeatmessages.ui.components.AppVersionText
import co.koko.heartbeatmessages.ui.components.SettingDescriptionNavigationItem
import co.koko.heartbeatmessages.ui.components.SettingNavigationItem
import androidx.core.net.toUri


@Composable
fun SettingScreen() {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    // PackageManager를 사용하여 versionName 가져오기
    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
    val versionName = packageInfo?.versionName ?: "알 수 없음"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 16.dp)
            .verticalScroll(scrollState)
    ) {
        SettingDescriptionNavigationItem(
            title = "앱 공유하기",
            description = "친구들에게 심쿵멘트 추천하기",
            onClick = {
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "설렘이 필요할 때, '심쿵멘트' 앱을 다운로드하고 연인에게 설렘을 선물하세요! Google Play 스토어에서 다운로드: https://play.google.com/store/apps/details?id=${context.packageName}")
                    type = "text/plain"
                }
                context.startActivity(Intent.createChooser(shareIntent, "앱 공유하기"))
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        SettingDescriptionNavigationItem(
            title = "리뷰 작성",
            description = "앱스토어에서 별점 남기기",
            onClick = {
                val uri = "market://details?id=${context.packageName}".toUri()
                val reviewIntent = Intent(Intent.ACTION_VIEW, uri)
                try {
                    context.startActivity(reviewIntent)
                } catch (e: Exception) {
                    val webUri =
                        "https://play.google.com/store/apps/details?id=${context.packageName}".toUri()
                    val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                    context.startActivity(webIntent)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingDescriptionNavigationItem(
            title = "패밀리 앱",
            description = "다른 유용한 앱들 둘러보기",
            onClick = {
                val url = "https://play.google.com/store/apps/developer?id=KOKO+COMPANY"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingNavigationItem(
            text = "이용약관",
            onClick = {
                val url = "https://www.notion.so/262931b917cf80d5bf49ee992f3cea48?source=copy_link"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        )
        Spacer(modifier = Modifier.weight(1f))

        AppVersionText(version = versionName)
    }
}