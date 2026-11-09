package com.electrofire.playpkm.Domain

import java.text.Normalizer

fun String.normalizar(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace("\\p{Mn}+".toRegex(), "")
        .trim()
        .lowercase()

fun isAnswerCorrect(correctName: String, userAnswer: String): Boolean {
    val cleanCorrect = correctName.normalizar()
    val cleanUser = userAnswer.normalizar()

    if (cleanCorrect == cleanUser) return true

    // Permitir espacio o guion indistintamente (ej. "porygon z" == "porygon-z")
    val correctWithSpaces = cleanCorrect.replace("-", " ")
    val userWithSpaces = cleanUser.replace("-", " ")
    if (correctWithSpaces == userWithSpaces) return true

    // Verificar si es una variante de forma (ej. "toxtricity-amped" -> base es "toxtricity")
    val formSuffixes = listOf(
        "amped", "low-key", "male", "female", "midday", "midnight", "dusk",
        "incarnate", "therian", "green-plumage", "blue-plumage", "yellow-plumage",
        "white-plumage", "red-plumage", "galarian", "alolan", "hisuian", "paldean"
    )

    val parts = cleanCorrect.split("-")
    if (parts.size > 1 && formSuffixes.contains(parts.last())) {
        val baseName = parts.first()
        if (baseName == cleanUser) return true
    }

    // Excepciones de nombres compuestos donde la primera palabra sola no es un Pokémon válido
    val multiWordExceptions = listOf("tapu ", "mr ", "mime ", "codigo ")
    val isException = multiWordExceptions.any { correctWithSpaces.startsWith(it) }

    if (!isException && correctWithSpaces.startsWith("$userWithSpaces ")) {
        return true
    }

    return false
}
