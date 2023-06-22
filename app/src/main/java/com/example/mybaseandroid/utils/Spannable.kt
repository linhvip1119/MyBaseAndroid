package com.example.mybaseandroid.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.example.mybaseandroid.R

class Spannable {
    fun TextView.setLinkSpanText(spanText: CharSequence) {
    movementMethod = LinkTouchMovementMethod()
    highlightColor = Color.TRANSPARENT
    text = spanText
}

fun SpannableStringBuilder.appendSpace(): SpannableStringBuilder = append(" ")
fun SpannableStringBuilder.appendThreeSpace(): SpannableStringBuilder = append("   ")

fun SpannableStringBuilder.append(texts: List<CharSequence>): SpannableStringBuilder {
    texts.forEach {
        append(it)
    }
    return this
}

fun SpannableStringBuilder.appendIcon(
    context: Context, @DrawableRes iconId: Int, @Px size: Int
): SpannableStringBuilder {
    this.append(" ")
    this.setSpan(
        context.icon(iconId, size), length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    return this
}

fun SpannableStringBuilder.withClickLinkSpanBlue(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.purple_500),
        pressTextColor = ContextCompat.getColor(context, R.color.purple_500),
        click = { click() }
    )
}

fun SpannableStringBuilder.withClickLinkSpanBlueLight(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.purple_500),
        pressTextColor = ContextCompat.getColor(context, R.color.purple_500),
        otherStyles = context.styleSpans { typeface(Typeface.BOLD) },
        click = { click() }
    )
}

fun SpannableStringBuilder.withClickLinkSpanAnnouncements(
    text: CharSequence, style: StyleSpans, textColor: Int, click: (() -> Unit)? = null
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = textColor,
        otherStyles = style,
        click = { click?.let { it1 -> it1() } }
    )
}

fun SpannableStringBuilder.withClickLinkDigitalToken(
    context: Context, text: CharSequence, click: () -> Unit
): SpannableStringBuilder {
    return withClickStateSpan(
        text = text,
        textColor = ContextCompat.getColor(context, R.color.purple_500),
        pressTextColor = ContextCompat.getColor(context, R.color.purple_500),
        otherStyles = context.styleSpans {
            underline()
            sansSerifMedium()
        },
        click = { click() }
    )
}

/*
* Only use with LinkTouchMovementMethod
* */
fun SpannableStringBuilder.withClickStateSpan(
    text: CharSequence,
    @ColorInt textColor: Int,
    @ColorInt pressTextColor: Int = 0,
    otherStyles: StyleSpans? = null,
    click: (View) -> Unit
): SpannableStringBuilder {
    val from = length
    append(text)

    val clickSpan = ClickableStateSpan(textColor, pressTextColor, click)
    setSpan(clickSpan, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    otherStyles?.forEach {
        setSpan(it, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}

fun SpannableStringBuilder.withClickSpan(
    text: CharSequence,
    styles: StyleSpans,
    click: (View) -> Unit
): SpannableStringBuilder {
    val from = length
    append(text)

    val clickSpan = OnlyClickableSpan(click)
    setSpan(clickSpan, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    styles.forEach {
        setSpan(it, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}

fun SpannableStringBuilder.withStyleSpan(
    text: CharSequence,
    style: StyleSpans
): SpannableStringBuilder {
    val from = length
    append(text)

    style.forEach {
        setSpan(it, from, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    return this
}

fun Context.styleSpans(options: StyleSpans.() -> Unit) = StyleSpans(this).apply(options)

class StyleSpans(private val context: Context) : Iterable<Any> {
    private val spans = mutableListOf<Any>()

    override fun iterator() = spans.iterator()

    fun size(@DimenRes id: Int) =
        spans.add(AbsoluteSizeSpan(context.resources.getDimensionPixelSize(id)))

    fun color(@ColorRes id: Int) =
        spans.add(ForegroundColorSpan(ContextCompat.getColor(context, id)))

    fun typeface(style: Int) = spans.add(StyleSpan(style))

    fun fontFamily(family: String) = spans.add(TypefaceSpan(family))
    fun sansSerifMedium() = fontFamily("sans-serif-medium")
    fun sansSerifRegular() = fontFamily("sans-serif-regular")

    fun textAppearance(@StyleRes appearanceId: Int) =
        spans.add(TextAppearanceSpan(context, appearanceId))

    fun backgroundColor(@ColorRes id: Int) =
        spans.add(BackgroundColorSpan(ContextCompat.getColor(context, id)))

    fun underline() = spans.add(UnderlineSpan())

    fun custom(span: ParcelableSpan) = spans.add(span)

    fun icon(@DrawableRes id: Int, size: Int): ImageSpan? {
        val drawable = ContextCompat.getDrawable(context, id)?.apply {
            setBounds(0, 0, size, size)
        }
        return ImageSpan(drawable ?: return null)
    }
}

fun Context.icon(@DrawableRes id: Int, size: Int): ImageSpan? {
    val drawable = ContextCompat.getDrawable(this, id)?.apply {
        setBounds(0, 0, size, size)
    }
    return ImageSpan(drawable ?: return null, ImageSpan.ALIGN_BASELINE)
}

fun String.setColorTextInString(context: Context, keyword: String): SpannableStringBuilder {
    val text = SpannableStringBuilder(this)
    val arrayText = this.split(keyword, ignoreCase = true)
    var count = 0
    for (i in 0 until arrayText.lastIndex) {
        count += arrayText.getOrNull(i)?.length ?: 0
        text.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.purple_500)),
            count, count + keyword.length,
            0
        )
        count += keyword.length
    }
    return text
}
}