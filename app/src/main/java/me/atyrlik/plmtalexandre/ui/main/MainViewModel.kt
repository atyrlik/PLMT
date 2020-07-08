package me.atyrlik.plmtalexandre.ui.main

import androidx.lifecycle.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import me.atyrlik.plmtalexandre.entities.FlowMeasure
import me.atyrlik.plmtalexandre.repository.FlowRepository

class MainViewModel(
    private val repository: FlowRepository
) : ViewModel() {

    val measures: LiveData<List<FlowMeasure>> = repository.getMeasures().filterNotNull()
        .asLiveData(viewModelScope.coroutineContext)

    fun startRegisteringMeasure() {
        viewModelScope.launch {
            repository.registerFlowMeasures()
        }
    }

}