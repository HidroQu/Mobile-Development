package com.capstone.hidroqu.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.hidroqu.nonui.data.MyPlantResponse
import com.capstone.hidroqu.ui.screen.home.ListAlarmHome

class HarvestNotificationViewModel : ViewModel() {
    private val _listAlarmHome = MutableLiveData<List<MyPlantResponse>>()
    val listAlarmHome: LiveData<List<MyPlantResponse>> = _listAlarmHome

    fun updateAlarmList(newAlarm: MyPlantResponse) {
        val currentList = _listAlarmHome.value.orEmpty().toMutableList()
        currentList.add(newAlarm)
        _listAlarmHome.value = currentList
    }
}
