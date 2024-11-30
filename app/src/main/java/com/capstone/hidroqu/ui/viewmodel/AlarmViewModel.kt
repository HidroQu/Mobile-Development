//package com.capstone.hidroqu.ui.viewmodel
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.capstone.hidroqu.nonui.MyPlantsRepository
//import com.capstone.hidroqu.nonui.api.HidroQuApiService
//import com.capstone.hidroqu.nonui.data.MyPlantResponse
//import com.capstone.hidroqu.nonui.data.MyPlantResponseWrapper
//import com.capstone.hidroqu.nonui.data.UserPreferences
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//import java.time.temporal.ChronoUnit
//
//class AlarmViewModel(
//    private val userPreferences: UserPreferences,
//    private val apiService: HidroQuApiService
//) : ViewModel() {
//    private val _upcomingHarvestPlants = MutableStateFlow(emptyList<MyPlantResponse>())
//    val upcomingHarvestPlants: StateFlow<List<MyPlantResponse>> = _upcomingHarvestPlants.asStateFlow()
//
//    private val _isLoading = MutableStateFlow(false)
//    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
//
//    private val myPlantsRepository by lazy { MyPlantsRepository(apiService) }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun fetchUpcomingHarvestPlants(token: String, daysThreshold: Int = 7) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                // Fetch the response from the repository
//                val myPlantsResponse = myPlantsRepository.getMyPlants(token)
//
//                // Safely access the data and filter the plants
//                val filteredPlants = myPlantsResponse.data.filter { myPlant: MyPlantResponse ->
//                    val plantingDate = parseDate(myPlant.planting_date)
//                    val harvestDate = plantingDate.plusDays(6) // Assuming 6 days to harvest
//                    val daysUntilHarvest = ChronoUnit.DAYS.between(LocalDate.now(), harvestDate)
//
//                    daysUntilHarvest in 0..daysThreshold
//                }.map { plant ->
//                    // Add description for alarm
//                    plant.copy(descAlarmHome = calculateHarvestDescription(plant))
//                }
//
//                _upcomingHarvestPlants.value = filteredPlants
//            } catch (e: Exception) {
//                // Handle error with logging or other error handling
//                e.printStackTrace()
//                _upcomingHarvestPlants.value = emptyList()
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun parseDate(dateString: String): LocalDate {
//        return LocalDate.parse(
//            dateString.substring(0, 10),
//            DateTimeFormatter.ofPattern("yyyy-MM-dd")
//        )
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun calculateHarvestDescription(plant: MyPlantResponse): String {
//        val plantingDate = parseDate(plant.planting_date)
//        val harvestDate = plantingDate.plusDays(6)
//        val daysUntilHarvest = ChronoUnit.DAYS.between(LocalDate.now(), harvestDate)
//
//        return when {
//            daysUntilHarvest == 0L -> "Panen hari ini!"
//            daysUntilHarvest == 1L -> "Panen besok!"
//            daysUntilHarvest in 2..7 -> "Panen dalam $daysUntilHarvest hari"
//            else -> ""
//        }
//    }
//}