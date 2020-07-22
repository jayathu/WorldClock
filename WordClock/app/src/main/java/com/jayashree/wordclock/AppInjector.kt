package com.jayashree.wordclock

import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {

    viewModel {
        DashBoardViewModel( get() )
        SearchViewModel( get() )
    }
}