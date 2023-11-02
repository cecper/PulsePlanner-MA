package com.example.pulseplanner.util

class TextUtils {
    companion object {

        fun getSimilarity(fullStr: String, inputStr: String): Int {
            // Return 0 if either string is null or empty
            if (fullStr == null || inputStr == null || fullStr.isEmpty() || inputStr.isEmpty()) {
                return 0
            }

            // Convert the strings to lowercase for case-insensitivity
            var fullStr = fullStr.toLowerCase()
            var inputStr = inputStr.toLowerCase()

            // Initialize a 2D array to store the dynamic programming values
            val dp = Array(fullStr.length + 1) { IntArray(inputStr.length + 1) }

            // Initialize the first row and column to represent the distance from an empty string
            for (i in 0..fullStr.length) {
                dp[i][0] = i
            }
            for (j in 0..inputStr.length) {
                dp[0][j] = j
            }

            // Iterate through the 2D array and fill in the values using the Levenshtein distance algorithm
            for (i in 1..fullStr.length) {
                for (j in 1..inputStr.length) {
                    // If the characters at the current indices are the same, the distance is the same as the distance for the previous characters
                    if (fullStr[i - 1] == inputStr[j - 1]) {
                        dp[i][j] = dp[i - 1][j - 1]
                    } else {
                        // Otherwise, the distance is the minimum of the distance from deleting, inserting, or replacing a character
                        dp[i][j] = 1 + minOf(dp[i - 1][j], minOf(dp[i][j - 1], dp[i - 1][j - 1]))
                    }
                }
            }

            // The final value in the 2D array is the Levenshtein distance between the two strings
            val distance = dp[fullStr.length][inputStr.length]

            // Calculate the similarity score as 1 - (distance / max(fullStr.length, inputStr.length))
            val maxLength = maxOf(fullStr.length, inputStr.length)
            val similarity = (100 * (1.0 - distance.toDouble() / maxLength)).toInt()

            return similarity
        }
    }
}