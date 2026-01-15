package com.example.collegeshedule.data.repository

import com.example.collegeshedule.data.api.ScheduleApi
import com.example.collegeshedule.data.dto.ScheduleByDateDto
class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(group: String): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = group,
            start = "2026-01-12",
            end = "2026-01-17"
        )
    }
}