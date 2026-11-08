package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccommodationStatus
import com.example.data.model.VerificationStatus
import com.example.ui.theme.*

@Composable
fun AccommodationStatusBadge(
    status: AccommodationStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (status) {
        AccommodationStatus.HAS_APARTMENT -> Triple(
            FutaEmeraldLight,
            Color(0xFF065F46),
            "Has Apartment"
        )
        AccommodationStatus.LOOKING_FOR_APARTMENT -> Triple(
            FutaSkyLight,
            Color(0xFF0369A1),
            "Looking for Apartment"
        )
        AccommodationStatus.LOOKING_FOR_PARTNER -> Triple(
            FutaPurpleLight,
            Color(0xFF6D28D9),
            "Looking for Roommate"
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(textColor, shape = RoundedCornerShape(3.dp))
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}

@Composable
fun FutaVerifiedBadge(
    status: VerificationStatus,
    modifier: Modifier = Modifier
) {
    if (status == VerificationStatus.VERIFIED_FUTA_STUDENT) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(8.dp),
            color = FutaEmeraldLight
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified FUTA Student",
                    tint = FutaEmerald,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "FUTA Verified",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46)
                )
            }
        }
    }
}

@Composable
fun CompatibilityBadge(
    percentage: Int,
    modifier: Modifier = Modifier
) {
    val (badgeBg, badgeText) = when {
        percentage >= 85 -> Pair(Color(0xFFDCFCE7), Color(0xFF15803D))
        percentage >= 70 -> Pair(Color(0xFFFEF3C7), Color(0xFFB45309))
        else -> Pair(Color(0xFFE2E8F0), Color(0xFF475569))
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = badgeBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$percentage% Match",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = badgeText
            )
        }
    }
}

@Composable
fun FutaSafetyBanner(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFEF3C7),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Safety Alert",
                tint = Color(0xFFB45309),
                modifier = Modifier.size(20.dp)
            )
            Column {
                Text(
                    text = "FUTA Student Safety Notice",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF78350F)
                )
                Text(
                    text = "Never pay rent, inspection fees, or agent charges upfront without physically verifying the apartment with a fellow student. Report unverified agents immediately.",
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = Color(0xFF92400E)
                )
            }
        }
    }
}
