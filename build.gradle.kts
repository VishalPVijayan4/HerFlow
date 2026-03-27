plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false // This will now use 2.0.21
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
}
