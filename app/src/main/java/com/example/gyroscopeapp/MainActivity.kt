package com.example.gyroscopeapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.atan2
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var tvAngle: TextView
    private lateinit var tvWarning: TextView

    private var currentAngle = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAngle = findViewById(R.id.tvAngle)
        tvWarning = findViewById(R.id.tvWarning)

        // Инициализация SensorManager и акселерометра
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Проверка наличия акселерометра
        if (accelerometer == null) {
            tvAngle.text = "Акселерометр не поддерживается"
            tvWarning.text = ""
        }
    }

    override fun onResume() {
        super.onResume()
        // Регистрация слушателя для акселерометра
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        // Отмена регистрации слушателя для акселерометра
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            // Получение значений ускорения по осям X, Y, Z
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Вычисление наклона устройства
            val gravity = sqrt(x * x + y * y + z * z)
            val pitch = atan2(x, sqrt(y * y + z * z))
            val roll = atan2(y, sqrt(x * x + z * z))

            // Преобразование углов в градусы
            val pitchDegrees = Math.toDegrees(pitch.toDouble()).toFloat()
            val rollDegrees = Math.toDegrees(roll.toDouble()).toFloat()

            // Обновление текста на экране
            tvAngle.text = "Наклон: ${pitchDegrees.toInt()} градусов"

            // Проверка наклона и вывод предупреждения
            if (Math.abs(pitchDegrees) > 15) {
                tvWarning.text = "Предупреждение: Наклон превышает 15 градусов!"
            } else {
                tvWarning.text = "Предупреждение: Нет"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Не требуется для этой задачи
    }
}