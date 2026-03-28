package com.buildndeploy.herflow.presentation.export

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.view.View
import androidx.core.graphics.createBitmap
import androidx.core.view.drawToBitmap
import java.io.File
import java.io.FileOutputStream

data class ComposeExportFiles(
    val imageFile: File,
    val pdfFile: File
)

object ComposeExportUtils {

    fun exportComposeScreenAsImageAndPdf(
        context: Context,
        composeHostView: View,
        baseFileName: String = "herflow_export_${System.currentTimeMillis()}"
    ): ComposeExportFiles {
        val bitmap = captureComposeScreenAsBitmap(composeHostView)
        val image = exportBitmapAsPng(context, bitmap, "$baseFileName.png")
        val pdf = exportBitmapAsPdf(context, bitmap, "$baseFileName.pdf")
        return ComposeExportFiles(image, pdf)
    }

    /**
     * Captures any Compose host view (usually ComposeView/root view) as a Bitmap.
     */
    fun captureComposeScreenAsBitmap(composeHostView: View): Bitmap {
        if (composeHostView.width <= 0 || composeHostView.height <= 0) {
            error("Compose host view must be measured before exporting")
        }
        return composeHostView.drawToBitmap()
    }

    /**
     * Renders the provided view into an off-screen bitmap.
     * Useful when you need deterministic dimensions.
     */
    fun renderViewToBitmap(view: View, width: Int = view.width, height: Int = view.height): Bitmap {
        require(width > 0 && height > 0) { "width and height must be > 0" }
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return bitmap
    }

    /**
     * Exports bitmap as PNG in app-specific Pictures/HerFlowExports.
     */
    fun exportBitmapAsPng(
        context: Context,
        bitmap: Bitmap,
        fileName: String = "herflow_export_${System.currentTimeMillis()}.png"
    ): File {
        val outputDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "HerFlowExports"
        ).apply { mkdirs() }

        val outputFile = File(outputDir, fileName)
        FileOutputStream(outputFile).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }
        return outputFile
    }

    /**
     * Exports bitmap into single-page PDF via Android PdfDocument.
     */
    fun exportBitmapAsPdf(
        context: Context,
        bitmap: Bitmap,
        fileName: String = "herflow_export_${System.currentTimeMillis()}.pdf"
    ): File {
        val outputDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "HerFlowExports"
        ).apply { mkdirs() }

        val outputFile = File(outputDir, fileName)
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        FileOutputStream(outputFile).use { stream ->
            document.writeTo(stream)
        }
        document.close()
        return outputFile
    }
}
