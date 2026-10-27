import org.gradle.jvm.toolchain.JavaLanguageVersion

object Versions {
    const val APP_CODE = 33
    const val APP_NAME = "1.1.0"

    // BE-owned: bump for each findroid-be release; versionCode = APP_CODE * 100 + BE_REVISION.
    const val BE_REVISION = 3

    const val COMPILE_SDK = 37
    const val TARGET_SDK = 36
    const val MIN_SDK = 28
    const val BUILD_TOOLS = "37.0.0"

    // CE-owned: upstream moved to 17; the fork stays on 21 (local dev + CI use JDK 21).
    val JAVA = JavaLanguageVersion.of(21)
}