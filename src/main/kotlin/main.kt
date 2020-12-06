import java.io.File

fun main(args: Array<String>) {
    val result = StringBuilder("part of 'R.dart';\n\n")

    result.append("// ignore_for_file: non_constant_identifier_names\n")
    result.append("class _DrawableReference {\n")
    result.append("  const _DrawableReference();\n\n")

    args.forEach { arg ->
        val file = File(arg)
        file.listFiles()?.forEach {
            if (it.isFile && fileExtension.contains(it.extension)) {
                var nameWithoutSuffix = it.nameWithoutExtension
                val suffix = it.name.split('.')[1]
                if (reserved.contains(nameWithoutSuffix)) {
                    nameWithoutSuffix = "${nameWithoutSuffix}_"
                }
                result.append("  /// ![preview](file://${it.absolutePath})\n")
                val folder = if (
                    suffix.endsWith("png", true)
                    || suffix.endsWith("jpg", true)
                    || suffix.endsWith("webp", true)
                    || suffix.endsWith("gif", true)
                )
                    "images"
                else
                    "fonts"
                result.append("  final ${nameWithoutSuffix.camel2Underscore()} = '$folder/${it.name}';\n\n")
            }
        }
    }

    result.append("}")

    val targetFile = File("lib/src/resource/drawables.dart")
    if (!targetFile.exists()) {
        targetFile.createNewFile()
    }
    targetFile.writeText(result.toString())

    print(result)
}

val reserved = listOf(
    "abstract", "dynamic", "implements", "show", "as", "else",
    "import", "static", "assert", "enum", "in", "super", "async",
    "export", "interface", "switch", "await", "extends", "is", "sync",
    "break", "external", "library", "this", "case", "factory", "mixin",
    "throw", "catch", "false", "new", "true", "class", "final", "null", "try",
    "const", "finally", "on", "typedef", "continue", "for", "operator", "var",
    "covariant", "Function", "part", "void", "default", "get", "rethrow",
    "while", "deferred", "hide", "return", "with", "do", "if", "set", "yield"
)

val fileExtension = listOf("png", "svg", "jpg", "webp")

/**
 * 下划线风格转为驼峰风格, [capitalized]表示转换后首字母是否大写
 */
fun String.underscore2Camel(capitalized: Boolean = true): String {
    val raw = trim { it == '_' } // 先去掉收尾的下划线
        .split("_")
        .joinToString("") { it.capitalize() }
    return if (capitalized) raw else raw.decapitalize()
}

/**
 * 下划线风格转为驼峰风格
 */
fun String.camel2Underscore(): String {
    if ("" == trim()) {
        return ""
    }
    val len = this.length
    val sb = StringBuilder(len)
    for (i in 0 until len) {
        val c = this[i]
        if (Character.isUpperCase(c)) {
            if (i != 0) sb.append("_")
            sb.append(Character.toLowerCase(c))
        } else {
            sb.append(c)
        }
    }
    return sb.toString().replace("-", "_").replace("(", "_").replace(")", "_")
}