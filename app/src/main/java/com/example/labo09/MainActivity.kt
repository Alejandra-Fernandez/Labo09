package com.example.labo09

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.labo09.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val CAMARA_REQUEST_CODE: Int = 23
    private val PERMISO_CAMARA: Int = 99
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main)
        setContentView(binding.root)

        binding.btnTomarFoto.setOnClickListener {
            solicitarPermisos()
        }
    }

    private fun solicitarPermisos() {
        when{
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> {
                tomarFoto()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)->{
                mostrarMensaje("El permiso fue rechazado, habilitar en los ajustes")
            }
            else->{
                requestPermissions(arrayOf(Manifest.permission.CAMERA),PERMISO_CAMARA)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISO_CAMARA->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    tomarFoto()
                }
            }
            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    }

    private fun tomarFoto() {
        //mostrarMensaje("Tomar foto")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent,CAMARA_REQUEST_CODE)
    }

    fun mostrarMensaje(mensaje:String){
        Toast.makeText(applicationContext,mensaje, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CAMARA_REQUEST_CODE->{
                if(resultCode != Activity.RESULT_OK){
                    mostrarMensaje("No se tomo la foto")
                }
                else{
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.imageFoto.setImageBitmap(bitmap)
                }
            }
        }
    }
}