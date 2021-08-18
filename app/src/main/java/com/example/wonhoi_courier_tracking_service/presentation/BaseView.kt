package com.example.wonhoi_courier_tracking_service.presentation

interface BaseView<PresenterT : BasePresenter> {

    val presenter : PresenterT

}