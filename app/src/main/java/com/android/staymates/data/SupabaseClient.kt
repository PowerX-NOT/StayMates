package com.android.staymates.data

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    val instance: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = "https://tmhvktrezbunwyttbmqt.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRtaHZrdHJlemJ1bnd5dHRibXF0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI3MTIzOTAsImV4cCI6MjA4ODI4ODM5MH0.V-Y4oWZoLl025Hozr0iZnY3CBfMMqO0gEVzgRzjmwIs"
        ) {
            install(Postgrest)
            install(Auth)
        }
    }
}
