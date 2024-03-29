package com.seanlooong.exerciseandroid.modules.camera.viewModel

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import com.google.mlkit.vision.barcode.common.Barcode

/**
 * A ViewModel for encapsulating the data for a QR Code, including the encoded data, the bounding
 * box, and the touch behavior on the QR Code.
 *
 * As is, this class only handles displaying the QR Code data if it's a URL. Other data types
 * can be handled by adding more cases of Barcode.TYPE_URL in the init block.
 */
class QrCodeViewModel(bounding: Rect, content: String, valueType: Int) {
    var boundingRect: Rect = bounding
    var qrContent: String = ""
    var qrCodeTouchCallback = { _: View, _: MotionEvent -> false} //no-op

    init {
        when (valueType) {
            Barcode.TYPE_URL -> {
                qrContent = content
                qrCodeTouchCallback = { v: View, e: MotionEvent ->
                    if (e.action == MotionEvent.ACTION_DOWN && boundingRect.contains(e.x.toInt(), e.y.toInt())) {
                        val openBrowserIntent = Intent(Intent.ACTION_VIEW)
                        openBrowserIntent.data = Uri.parse(qrContent)
                        v.context.startActivity(openBrowserIntent)
                    }
                    true // return true from the callback to signify the event was handled
                }
            }
            // Add other QR Code types here to handle other types of data,
            // like Wifi credentials.
            else -> {
                qrContent = "Unsupported data type: $content"
            }
        }
    }
}