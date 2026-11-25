package com.molt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MoltApplication

fun main(args: Array<String>) {
    runApplication<MoltApplication>(*args)
}
