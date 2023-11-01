package com.arturlasok.feature_creator.model

sealed class ProjectMenuViewState {
    object Open : ProjectMenuViewState()
    object Close : ProjectMenuViewState()
    object Short : ProjectMenuViewState()
}
