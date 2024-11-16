// MainPage.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moodify.R
import com.example.moodify.ui.theme.MoodifyTheme

@Composable
fun HomeScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    MoodifyTheme(darkTheme = isDarkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar with app name and theme toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Moodify",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Icon(
                    painter = painterResource(id = if (isDarkTheme) R.drawable.ic_sun else R.drawable.ic_moon),
                    contentDescription = "Toggle Theme",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { isDarkTheme = !isDarkTheme }
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(8.dp),
                    tint = Color.White
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main buttons
            val buttonTexts = listOf("Mood Tracker", "Diary", "Statistics", "Resources")
            buttonTexts.forEach { text ->
                Button(
                    onClick = { /* TODO: Navigate to respective screen */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(text = text, color = MaterialTheme.colorScheme.onPrimary)
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            // Bottom navigation bar
            BottomNavigationBar()
        }
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val navItems = listOf("Home", "Stats", "Diary")
        navItems.forEach { item ->
            Text(
                text = item,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.clickable { /* TODO: Handle navigation */ }
            )
        }
    }
}


@Preview
@Composable
fun SimpleComposablePreview() {
    HomeScreen()
}