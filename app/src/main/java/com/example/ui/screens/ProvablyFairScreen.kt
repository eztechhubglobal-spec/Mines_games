package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRed
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianStroke
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AssistantViewModel

@Composable
fun ProvablyFairScreen(viewModel: AssistantViewModel) {
    val serverSeed by viewModel.serverSeedInput.collectAsState()
    val clientSeed by viewModel.clientSeedInput.collectAsState()
    val nonce by viewModel.nonceInput.collectAsState()
    val expectedHash by viewModel.expectedHashInput.collectAsState()
    val result by viewModel.verificationResult.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ObsidianBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = EmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "PROVABLY FAIR VERIFIER",
                        color = EmeraldPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Cryptographic SHA-256 & HMAC Validation",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Verification Input Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "VERIFY 1WIN ROUND FAIRNESS",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = serverSeed,
                        onValueChange = { viewModel.updateServerSeed(it) },
                        label = { Text("Server Seed (Revealed unhashed seed)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("server_seed_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = EmeraldPrimary,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = clientSeed,
                            onValueChange = { viewModel.updateClientSeed(it) },
                            label = { Text("Client Seed (User seed)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1.5f).testTag("client_seed_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = ObsidianStroke,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = EmeraldPrimary,
                                unfocusedLabelColor = TextMuted
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = nonce,
                            onValueChange = { viewModel.updateNonce(it) },
                            label = { Text("Nonce (#)", fontSize = 11.sp) },
                            modifier = Modifier.weight(1f).testTag("nonce_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = EmeraldPrimary,
                                unfocusedBorderColor = ObsidianStroke,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = EmeraldPrimary,
                                unfocusedLabelColor = TextMuted
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = expectedHash,
                        onValueChange = { viewModel.updateExpectedHash(it) },
                        label = { Text("Expected Round Hash (from game history)", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().testTag("expected_hash_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = ObsidianStroke,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = EmeraldPrimary,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { viewModel.verifyProvablyFair() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("verify_hash_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = ObsidianBackground, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Calculate & Verify Hash", color = ObsidianBackground, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Verification Result Output Card
        item {
            AnimatedVisibility(visible = result != null) {
                result?.let { res ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (res.matchesExpected) SafeGreen else if (expectedHash.isBlank()) GoldAccent else DangerRed
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (res.matchesExpected) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (res.matchesExpected) SafeGreen else GoldAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (res.matchesExpected) "VERIFIED MATCH" else "CALCULATED HASH",
                                    color = if (res.matchesExpected) SafeGreen else GoldAccent,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Calculated Cryptographic Hash:", color = TextMuted, fontSize = 10.sp)
                            Surface(
                                color = ObsidianBackground,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = res.generatedHash,
                                    color = EmeraldPrimary,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = res.explanation, color = TextWhite, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Honest AI & Nigerian Anti-Scam Advisory Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = ObsidianSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, ObsidianStroke)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scam Alert: The Truth About 'Predictor Bots'", color = GoldAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "In Nigeria, fraudulent Telegram channels sell '100% Mines Signal Bots' claiming to predict bomb locations. This is mathematically impossible:",
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    val bulletPoints = listOf(
                        "Provably Fair rounds are hashed before you play. The casino servers do not share the unhashed seed until the round completes.",
                        "Each game round is an independent Bernoulli trial. Past revealed tiles have zero memory influence on future rounds.",
                        "Real mathematical edge comes only from disciplined bankroll management (e.g. 1-2% stake sizing) and understanding cashout risk curves.",
                        "Our assistant provides honest combinatorics (P(safe), EV, Kelly fractions) without false promises."
                    )

                    bulletPoints.forEach { pt ->
                        Row(modifier = Modifier.padding(vertical = 3.dp)) {
                            Text("• ", color = EmeraldPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(pt, color = TextMuted, fontSize = 10.sp, lineHeight = 14.sp)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
