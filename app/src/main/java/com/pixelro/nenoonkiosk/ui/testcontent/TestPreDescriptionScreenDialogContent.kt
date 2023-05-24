package com.pixelro.nenoonkiosk.ui.testcontent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.pixelro.nenoonkiosk.R
import com.pixelro.nenoonkiosk.data.StringProvider
import com.pixelro.nenoonkiosk.data.TestType

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TestPreDescriptionScreenDialogContent(
    selectedTestType: TestType
) {
    when(selectedTestType) {
        TestType.Presbyopia -> {
            GlideImage(
                modifier = Modifier
                    .width(700.dp)
                    .height(350.dp)
                    .padding(start = 20.dp, end = 20.dp),
                model = R.raw.img_start_presbyopia,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step1),
                body = StringProvider.getString(R.string.presbyopia_test_order_step1)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step2),
                body = StringProvider.getString(R.string.presbyopia_test_order_step2)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step3),
                body = StringProvider.getString(R.string.presbyopia_test_order_step3)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step4),
                body = StringProvider.getString(R.string.presbyopia_test_order_step4)
            )
        }
        TestType.ShortDistanceVisualAcuity -> {
            GlideImage(
                modifier = Modifier
                    .width(700.dp)
                    .height(350.dp)
                    .padding(start = 20.dp, end = 20.dp),
                model = R.raw.img_start_sight,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step1),
                body = StringProvider.getString(R.string.short_visual_acuity_test_order_step1)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step2),
                body = StringProvider.getString(R.string.short_visual_acuity_test_order_step2)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step3),
                body = StringProvider.getString(R.string.short_visual_acuity_test_order_step3)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step4),
                body = StringProvider.getString(R.string.short_visual_acuity_test_order_step4)
            )
        }
        TestType.LongDistanceVisualAcuity -> {
            GlideImage(
                modifier = Modifier
                    .width(700.dp)
                    .height(350.dp)
                    .padding(start = 20.dp, end = 20.dp),
                model = R.raw.img_start_sight,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step1),
                body = StringProvider.getString(R.string.long_visual_acuity_test_order_step1)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step2),
                body = StringProvider.getString(R.string.long_visual_acuity_test_order_step2)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step3),
                body = StringProvider.getString(R.string.long_visual_acuity_test_order_step3)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step4),
                body = StringProvider.getString(R.string.long_visual_acuity_test_order_step4)
            )
        }
        TestType.ChildrenVisualAcuity -> {
            GlideImage(
                modifier = Modifier
                    .width(700.dp)
                    .height(350.dp)
                    .padding(start = 20.dp, end = 20.dp),
                model = R.raw.img_start_sight,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step1),
                body = StringProvider.getString(R.string.children_visual_acuity_test_order_step1)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step2),
                body = StringProvider.getString(R.string.children_visual_acuity_test_order_step2)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step3),
                body = StringProvider.getString(R.string.children_visual_acuity_test_order_step3)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step4),
                body = StringProvider.getString(R.string.children_visual_acuity_test_order_step4)
            )
        }
        TestType.AmslerGrid -> {
            GlideImage(
                modifier = Modifier
                    .width(700.dp)
                    .height(350.dp)
                    .padding(start = 20.dp, end = 20.dp),
                model = R.raw.img_start_amsler_grid,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step1),
                body = StringProvider.getString(R.string.amsler_grid_test_order_step1)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step2),
                body = StringProvider.getString(R.string.amsler_grid_test_order_step2)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step3),
                body = StringProvider.getString(R.string.amsler_grid_test_order_step3)
            )
        }
        else -> {
            GlideImage(
                modifier = Modifier
                    .width(700.dp)
                    .height(350.dp)
                    .padding(start = 20.dp, end = 20.dp),
                model = R.raw.img_start_m_chart,
                contentScale = ContentScale.Fit,
                contentDescription = ""
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step1),
                body = StringProvider.getString(R.string.mchart_test_order_step1)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step2),
                body = StringProvider.getString(R.string.mchart_test_order_step2)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step3),
                body = StringProvider.getString(R.string.mchart_test_order_step3)
            )
            DialogDescriptionText(
                title = StringProvider.getString(R.string.test_dialog_step4),
                body = StringProvider.getString(R.string.mchart_test_order_step4)
            )
        }
    }
    LastDialogDescriptionText(
        title = StringProvider.getString(R.string.test_dialog_result),
        body = when(selectedTestType) {
            TestType.Presbyopia -> StringProvider.getString(R.string.presbyopia_test_order_result)
            TestType.ShortDistanceVisualAcuity -> StringProvider.getString(R.string.short_visual_acuity_test_order_result)
            TestType.LongDistanceVisualAcuity -> StringProvider.getString(R.string.long_visual_acuity_test_order_result)
            TestType.ChildrenVisualAcuity -> StringProvider.getString(R.string.children_visual_acuity_test_order_result)
            TestType.AmslerGrid -> StringProvider.getString(R.string.amsler_grid_test_order_result)
            else -> StringProvider.getString(R.string.mchart_test_order_result)
        }
    )
    Spacer(
        modifier = Modifier
            .height(16.dp)
    )
}

@Composable
fun DialogDescriptionText(
    title: String,
    body: String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 4.dp)
            .border(
                border = BorderStroke(2.dp, Color(0xff1d71e1)),
                shape = RectangleShape
            )
            .padding(4.dp),
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xff1d71e1)
                )
            ) {
                append(title + "\n")
            }
            append(body)
        },
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp
    )
}


@Composable
fun LastDialogDescriptionText(
    title: String,
    body: String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, bottom = 4.dp)
            .border(
                border = BorderStroke(2.dp, Color(0xff1d71e1)),
                shape = RectangleShape
            )
            .background(
                color = Color(0xff1d71e1)
            )
            .padding(4.dp),
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xfffff177)
                )
            ) {
                append(title + "\n")
            }
            withStyle(
                style = SpanStyle(
                    color = Color(0xffffffff)
                )
            ) {
                append(body)
            }
        },
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        textAlign = TextAlign.Center,
        lineHeight = 24.sp
    )
}