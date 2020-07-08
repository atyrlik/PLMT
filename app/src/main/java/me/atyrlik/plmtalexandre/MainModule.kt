package me.atyrlik.plmtalexandre

import me.atyrlik.plmtalexandre.ui.main.MainViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// This could be divided in multiple modules in a bigger app.
val mainModule: Module = module {
    viewModel { MainViewModel() }
}