package com.grensil.carinfo.data.auth.mapper

import com.google.firebase.auth.FirebaseUser
import com.grensil.carinfo.data.auth.dto.FirebaseUserDto
import com.grensil.carinfo.domain.model.AuthUser

fun FirebaseUserDto.toEntity(): AuthUser =
    AuthUser(
        uid = uid,
        phoneNumber = phoneNumber,
    )

fun FirebaseUser.toDto(): FirebaseUserDto =
    FirebaseUserDto(
        uid = uid,
        phoneNumber = phoneNumber,
        displayName = displayName,
    )
