package com.example.carsolution.data.repository

import com.example.carsolution.domain.model.Accident
import com.example.carsolution.domain.repository.AccidentRepository
import javax.inject.Inject

class FakeAccidentRepository @Inject constructor() : AccidentRepository {

    private val items = listOf(
        Accident("acc-1", "2024-12-01", "서울시 강남구 역삼동", "추돌사고"),
        Accident("acc-2", "2024-11-15", "경기도 성남시 분당구", "접촉사고"),
        Accident("acc-3", "2024-10-20", "서울시 송파구 잠실동", "단독사고"),
    )

    override suspend fun getAccidentList(): List<Accident> = items

    override suspend fun getAccidentById(id: String): Accident? =
        items.find { it.id == id }
}
