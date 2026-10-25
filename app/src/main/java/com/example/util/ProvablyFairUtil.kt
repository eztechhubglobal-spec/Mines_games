package com.example.util

import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object ProvablyFairUtil {

    fun sha256(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun hmacSha256(key: String, message: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA256")
        mac.init(secretKey)
        val hashBytes = mac.doFinal(message.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    data class VerificationResult(
        val combinedInput: String,
        val generatedHash: String,
        val matchesExpected: Boolean,
        val explanation: String
    )

    fun verifyRound(
        serverSeed: String,
        clientSeed: String,
        nonce: String,
        expectedHash: String
    ): VerificationResult {
        val combined = "$serverSeed:$clientSeed:$nonce"
        val calculated = if (clientSeed.isNotBlank()) {
            try {
                hmacSha256(serverSeed, "$clientSeed-$nonce")
            } catch (e: Exception) {
                sha256(combined)
            }
        } else {
            sha256(serverSeed)
        }

        val matches = if (expectedHash.isNotBlank()) {
            calculated.equals(expectedHash.trim(), ignoreCase = true)
        } else false

        val explanation = when {
            expectedHash.isBlank() -> "Generated reference hash from seeds and nonce. Compare this against your game round hash."
            matches -> "Verified Match! The round outcome was predetermined by this server seed before wagers were accepted."
            else -> "Hash Mismatch. Ensure the revealed unhashed server seed, client seed, and exact nonce match the game dialog."
        }

        return VerificationResult(
            combinedInput = combined,
            generatedHash = calculated,
            matchesExpected = matches,
            explanation = explanation
        )
    }
}
