import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.multiple
import kotlinx.cli.required
import platform.posix.EOF
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.fputs

fun writeAllText(filePath:String, text:String) {
    val file = fopen(filePath, "w") ?:
    throw IllegalArgumentException("Cannot open output file $filePath")
    try {
        memScoped {
            if(fputs(text, file) == EOF) throw Error("File write error")
        }
    } finally {
        fclose(file)
    }
}

fun readAllText(filePath: String): String {
    val returnBuffer = StringBuilder()
    val file = fopen(filePath, "r") ?:
    throw IllegalArgumentException("Cannot open input file $filePath")

    try {
        memScoped {
            val readBufferLength = 64 * 1024
            val buffer = allocArray<ByteVar>(readBufferLength)
            while (true) {
                val line = fgets(buffer, readBufferLength, file)?.toKString() ?: break
                returnBuffer.append(line)
            }
        }
    } finally {
        fclose(file)
    }

    return returnBuffer.toString()
}



fun produce(result: List<Double>, format: Format, outputFileName: String?) {
    outputFileName.let {
        // Print to file.
        //...
    } ?: run {
        // Print to stdout.
        //...
    }
}

fun readFrom(inputFileName: String): String {
    return "readFrom returns"
}

fun calculate(inputData: String, eps: Double, debug: Boolean = false): List<Double> {
    return listOf(1.0, 2.0, 3.0)
}

enum class Format {
    HTML,
    CSV,
    PDF
}

fun main(args: Array<String>) {
    val parser = ArgParser("example")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input file").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
    val format by parser.option(ArgType.Choice<Format>(), shortName = "f",
        description = "Format for output file").default(Format.CSV).multiple()
    val stringFormat by parser.option(ArgType.Choice(listOf("html", "csv", "pdf"), { it }), shortName = "sf",
        description = "Format as string for output file").default("csv").multiple()
    val debug by parser.option(ArgType.Boolean, shortName = "d", description = "Turn on debug mode").default(false)
    val eps by parser.option(ArgType.Double, description = "Observational error").default(0.01)

    parser.parse(args)
    val inputData = readFrom(input)
    val result = calculate(inputData, eps, debug)
    format.forEach {
        produce(result, it, output)
    }
    println("Hello, Kotlin/Native!")
    val txt = "hello from the program\n1\n2\n3\n"
    writeAllText("out.txt", txt)
    val read = readAllText("out.txt")
    println("read from file: $read")
}