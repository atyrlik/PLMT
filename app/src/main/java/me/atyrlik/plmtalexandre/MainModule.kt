package me.atyrlik.plmtalexandre

import androidx.room.Room
import com.plumelabs.lib.bluetooth.FlowBleClient
import me.atyrlik.plmtalexandre.repository.FlowRepository
import me.atyrlik.plmtalexandre.repository.FlowRepositoryImpl
import me.atyrlik.plmtalexandre.repository.local.MeasuresDatabase
import me.atyrlik.plmtalexandre.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// This could be divided in multiple modules in a bigger app.
val mainModule: Module = module {
    viewModel { MainViewModel(get()) }
    single<FlowRepository> { FlowRepositoryImpl(get(), get()) }
    single<FlowBleClient> { FlowBleClient.mockInstance() }
    single<MeasuresDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MeasuresDatabase::class.java, "measures_db"
        ).build()
    }
}