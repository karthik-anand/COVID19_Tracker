package com.example.covid19tracker.data.api

import com.example.covid19tracker.data.model.Statewise

interface ApiServiceImpl : ApiService {
    override fun getData() : List<Statewise> {
        return 0
    }
}