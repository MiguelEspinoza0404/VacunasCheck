package com.example.vacunacheck;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.vacunacheck.Helpers.AlarmReceiver;
import com.example.vacunacheck.Helpers.VacunaReminderReceiver;

import java.util.Calendar;

public class CalendarioActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TextView textViewVacunaPendiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        calendarView = findViewById(R.id.calendarViewVacunas);
        textViewVacunaPendiente = findViewById(R.id.textViewVacunaPendiente);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                if (!alarmManager.canScheduleExactAlarms()) {
                    Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                    startActivity(intent);
                }
            }

            if (fechaSeleccionada.equals("18/5/2025")) {
                textViewVacunaPendiente.setText("Vacuna COVID-19: 2da dosis pendiente");

                Calendar fechaRecordatorio = Calendar.getInstance();
//                fechaRecordatorio.set(year, month, dayOfMonth, 9, 0, 0);
                fechaRecordatorio.add(Calendar.SECOND, 10);
                programarRecordatorio(this, fechaRecordatorio, "COVID-19", "Recuerda tu 2da dosis de vacuna COVID-19");

                Toast.makeText(this, "Recordatorio programado para el " + fechaSeleccionada, Toast.LENGTH_SHORT).show();

            } else {
                textViewVacunaPendiente.setText("No hay vacunas pendientes para esta fecha.");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado. No se mostrarán recordatorios.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void programarRecordatorio(Context context, Calendar calendar, String titulo, String mensaje) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("titulo", titulo);
        intent.putExtra("mensaje", mensaje);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}