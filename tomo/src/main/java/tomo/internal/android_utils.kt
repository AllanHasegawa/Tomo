package tomo.internal

import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView

internal fun transformImageView(imageView: ImageView, transform: (Bitmap) -> Bitmap) {
    val w = imageView.width
    val h = imageView.height
    val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)

    imageView.draw(canvas)

    val newBitmap = transform(bitmap)
    imageView.setImageBitmap(newBitmap)
}
