package com.example.carsolution.data.auth.mapper

import com.example.carsolution.data.auth.dto.FirebaseUserDto
import com.example.carsolution.domain.model.AuthUser
import com.google.firebase.auth.FirebaseUser

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
