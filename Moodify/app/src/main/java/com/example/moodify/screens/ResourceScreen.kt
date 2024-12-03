package com.example.moodify.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val phoneNumbers = listOf(
    "Call 8-1-1",
    "Call 9-8-8 for suicide crisis helpline",
    "1-800-263-2266 from 6 am to 2 am for youth",
    "1-800-361-5085 from 6 am to 12 am for parents",
    "514-684-6160 for crisis line available 24/7"
)

val websites = listOf(
    "'https://www.nami.org/': NAMI includes videos, blogs, articles, and resource connections. It shares the common signs of mental illness ranging from anxiety to ADHD to Schizophrenia and more.\n",
    "'https://psychcentral.com/': PsychCentral has blogs with practice mental health applications that are searchable by disorder. These blogs provide education for living with the disorder, helpful stories, and resources to find support.\n",
    "'https://kidsmentalhealthinfo.com/': Kids Mental Health Info can be a good place to start for questions about children’s mental health. This website provides parents with support and education for their child and resources to guide them through the therapeutic process.\n",
    "'https://www.calmsage.com/': Calm Sage has tons of information about self-care and ways to promote wellness into your everyday life. It also provides a space to explore ways to practice self-care while also allowing users to share their thoughts and feelings about numerous topics on mental health.\n"
)

val apps = listOf(
    "Headspace Mobile App: Think of Headspace as your lifelong guide to better mental health. We’re here for you whenever you need us, wherever you are, helping you get through tough times and find joy in every day.\n",
    "Happify Mobile App: Their proven techniques are developed by leading scientists and experts who've been studying evidence-based interventions in the fields of positive psychology, mindfulness, and cognitive behavioral therapy for decades.\n"
)

@Preview(showBackground = true)
@Composable
fun ResourceScreenPreview() {
    ResourceScreen()
}

@Composable
fun ResourceScreen(mod: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        //title of the page
        Text(
            text = "Resources",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        //Content of resource page
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(4.dp)
                .height(180.dp)
        ) {

            //contents of page
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box {
                    Column {
                        //phone numbers
                        Text(
                            "Phone numbers:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            textAlign = TextAlign.Start
                        )
                        LazyColumn(Modifier.padding(10.dp)) {
                            items(items = phoneNumbers) { number ->
                                Text(
                                    number,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(4.dp)
                .height(340.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box() {
                    Column {
                        //websites
                        Text(
                            "Websites:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            textAlign = TextAlign.Start
                        )
                        LazyColumn(Modifier.padding(10.dp)) {
                            items(items = websites) { site ->
                                Text(
                                    site,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(4.dp)
                .height(180.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Box {
                    Column {
                        //apps
                        Text(
                            "Apps:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp),
                            textAlign = TextAlign.Start
                        )
                        LazyColumn(Modifier.padding(10.dp)) {
                            items(items = apps) { app ->
                                Text(
                                    app,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}