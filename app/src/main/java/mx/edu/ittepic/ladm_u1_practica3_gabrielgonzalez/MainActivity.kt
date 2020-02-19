package mx.edu.ittepic.ladm_u1_practica3_gabrielgonzalez

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var vector : Array<Int> = Array(10,{0})
    var nombreArchivo = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                0)
        }else{
            mensaje("PERMISOS YA OTORGADOS")
        }

        asignar.setOnClickListener {
            if(valor.text.isEmpty() || posicion.text.isEmpty()){
                Toast.makeText(this, "ERROR TODOS LOS CAMPOS DEBEN TENER DATA", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(posicion.text.toString().toInt()>9){
                Toast.makeText(this,"NUMERO MAYOR DEL SOLICITADO", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var v = valor.text.toString().toInt()
            var p = posicion.text.toString().toInt()
            vector[p]=v
            Toast.makeText(this,"SE ASIGNO EL VALOR", Toast.LENGTH_SHORT).show()
            valor.setText("")
            posicion.setText("")
        }

        mostrar.setOnClickListener {
            var data = ""
            (0..9).forEach {
                data += "[ ${vector[it]}],"
            }
            AlertDialog.Builder(this).setTitle("VECTOR").setMessage(data)
                .setPositiveButton("ok"){d,i->}.show()
        }


        guardarsd.setOnClickListener {

            nombreArchivo = editText3.text.toString()
            guardarArchivoSD(nombreArchivo)
        }

        leersd.setOnClickListener {
            leerArchivoSD()
            mensaje("EXITO Se sustituyeron los datos de lectura, mostrar para comprobar")
        }

    }
    fun leerArchivoSD(){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {

            var rutaSD=Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,editText4.text.toString())
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()
            var vector2 = data.split("&")

            (0..9).forEach {
                vector[it] = vector2[it].toString().toInt()
            }
            flujoEntrada.close()


        }catch (e:IOException){
            mensaje(e.message.toString())
        }
    }
    fun guardarArchivoSD(nombreArchivo:String){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivos = File(rutaSD.absolutePath,nombreArchivo)
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivos))
            var data = ""

            (0..9).forEach {
                data += vector[it].toString()+"&"
            }
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO Se guardo correctamente")
        }catch (e:IOException){
            mensaje(e.message.toString())
        }
    }
    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado!=Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }
    fun mensaje(m:String){
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(m)
            .setPositiveButton("Ok"){d,i->}
            .show()
    }
}
