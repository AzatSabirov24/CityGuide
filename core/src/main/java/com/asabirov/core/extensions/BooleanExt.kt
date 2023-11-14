package com.asabirov.core.extensions

fun Boolean?.isNotNullAndTrue() = this != null && this == true

fun Boolean?.isNotNullAndFalse() = this != null && this == false